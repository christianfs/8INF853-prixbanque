package com.prixbanque.accountservice.service;

import com.prixbanque.accountservice.dto.*;
import com.prixbanque.accountservice.event.NotificationPlacedEvent;
import com.prixbanque.accountservice.model.Account;
import com.prixbanque.accountservice.model.Customer;
import com.prixbanque.accountservice.model.NotificationType;
import com.prixbanque.accountservice.repository.AccountRepository;
import com.prixbanque.accountservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, NotificationPlacedEvent> kafkaTemplate;
    private final WebClient.Builder webClientBuilder;

    @Transactional
    public String createAccount(AccountRequest accountRequest){
        Customer customer = Customer.builder()
                .firstName(accountRequest.getFirstName())
                .lastName(accountRequest.getLastName())
                .address(accountRequest.getAddress())
                .email(accountRequest.getEmail())
                .phone(accountRequest.getPhone())
                .build();

        customerRepository.save(customer);

        Account account = Account.builder()
                .accountNumber(generateAccountNumber(8, 0, 10))
                .password(accountRequest.getPassword())
                .balance(new BigDecimal(0))
                .customer(customer)
                .build();

        accountRepository.save(account);

        kafkaTemplate.send("notificationTopic",
                new NotificationPlacedEvent(
                        account.getId().toString(),
                        account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),
                        account.getAccountNumber(),
                        account.getCustomer().getEmail(),
                        null,
                        null,
                        NotificationType.ACCOUNT)
        );

        String message = "Account " + account.getAccountNumber() + " is saved.";
        log.info(message);
        return message;
    }

    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if(optionalAccount.isEmpty()) {
            return null;
        }
        return mapToAccountResponse(optionalAccount.get());
    }

    private Account getAccount(String accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findByAccountNumber(accountNumber);
        if(optionalAccount.isEmpty()) {
            return null;
        }

        return optionalAccount.get();
    }

    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(this::mapToAccountResponse).collect(Collectors.toList());
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .firstName(account.getCustomer().getFirstName())
                .lastName(account.getCustomer().getLastName())
                .address(account.getCustomer().getAddress())
                .email(account.getCustomer().getEmail())
                .phone(account.getCustomer().getPhone())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .build();
    }

    private String generateAccountNumber(Integer size, Integer min, Integer max) {
        return new Random().ints(size, min, max + 1)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }

    public Boolean deposit(TransactionRequest transactionRequest) {
        Account account = getAccount(transactionRequest.getAccountNumber());

        if (account == null) {
            log.info("Unable to make deposit, account {} not found.", transactionRequest.getAccountNumber());
            return false;
        }

        account.setBalance(account.getBalance().add(transactionRequest.getAmount()));
        accountRepository.save(account);
        saveInStatementService(
                account.getAccountNumber(),
                null,
                transactionRequest.getAmount(),
                TransactionType.DEPOSIT,
                transactionRequest.getTransferId()
        );
        log.info("Deposit made successfully to the account {}", account.getAccountNumber());
        return true;
    }

    public Boolean withdraw(TransactionRequest transactionRequest) {
        Account account = getAccount(transactionRequest.getAccountNumber());
        if (account == null) {
            log.info("Account {} does not exist.", transactionRequest.getAccountNumber());
            return false;
        }

        if (account.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            log.info("There are not enough funds to withdraw from the account {}. Your current balance is {}", account.getAccountNumber(), account.getBalance());
            return false;
        }

        account.setBalance(account.getBalance().subtract(transactionRequest.getAmount()));
        accountRepository.save(account);
        saveInStatementService(
                account.getAccountNumber(),
                null,
                transactionRequest.getAmount(),
                TransactionType.WITHDRAW,
                transactionRequest.getTransferId()
        );
        log.info("Withdrawal made successfully from the account {}", account.getAccountNumber());
        return true;
    }

    @Transactional
    public Boolean transfer(UUID transferId) {
        TransferResponse transferResponse = webClientBuilder.build().put()
                .uri("http://transfer-service/api/transfer/commit",
                        uriBuilder -> uriBuilder.queryParam("transferId", transferId).build())
                .retrieve()
                .bodyToMono(TransferResponse.class)
                .block();
        if(transferResponse == null) {
            log.info("No data found to perform the transfer. Transfer id {}", transferId);
            return false;
        }

        Optional<Customer> recipientCustomer = customerRepository.findByEmail(transferResponse.getRecipientsEmail());
        if(recipientCustomer.isEmpty()) {
            log.info("Recipient account not found for email {}", transferResponse.getRecipientsEmail());
            return false;
        }

        Optional<Account> account = accountRepository.findByAccountNumber(transferResponse.getAccountNumber());
        if(account.isEmpty()) {
            log.info("Source account not found. Account number {}", transferResponse.getAccountNumber());
            return false;
        }

        if(Boolean.TRUE.equals(withdraw(new TransactionRequest(account.get().getAccountNumber(), transferResponse.getAmount(), transferResponse.getTransferId()))) &&
                Boolean.TRUE.equals(deposit(new TransactionRequest(recipientCustomer.get().getAccount().getAccountNumber(), transferResponse.getAmount(), transferResponse.getTransferId())))) {
             saveInStatementService(
                     account.get().getAccountNumber(),
                     recipientCustomer.get().getAccount().getAccountNumber(),
                     transferResponse.getAmount(),
                     TransactionType.TRANSFER,
                     transferResponse.getTransferId()
             );
            log.info("Transfer of {} successfully made from account {} to account {}", transferResponse.getAmount(), account.get().getAccountNumber(), recipientCustomer.get().getAccount().getAccountNumber());
            return true;
        }
        log.info("The transfer of {} failed from account {} to account {}. Please contact the sender or your bank.", transferResponse.getAmount(), account.get().getAccountNumber(), recipientCustomer.get().getAccount().getAccountNumber());
        return false;
    }

    private Boolean saveInStatementService(String accountNumber,
                                           String recepientsAccountNumber,
                                           BigDecimal amount,
                                           TransactionType transactionType,
                                           UUID transferId) {
        return webClientBuilder.build().post()
                .uri("http://statement-service/api/statement")
                .bodyValue(new StatementRequest(
                        accountNumber,
                        recepientsAccountNumber,
                        amount,
                        transactionType,
                        transferId
                        )
                )
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
