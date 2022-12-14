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
    public void createAccount(AccountRequest accountRequest){
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

        log.info("Account {} is saved", account.getAccountNumber());
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
                .mapToObj(i -> Integer.toString(i))
                .collect(Collectors.joining());
    }

    public Boolean deposit(TransactionRequest transactionRequest) {
        Account account = getAccount(transactionRequest.getAccountNumber());

        account.setBalance(account.getBalance().add(transactionRequest.getAmount()));
        accountRepository.save(account);
        saveInStatementService(
                account.getAccountNumber(),
                null,
                transactionRequest.getAmount(),
                TransactionType.DEPOSIT
        );
        return true;
    }

    public Boolean withdraw(TransactionRequest transactionRequest) {
        Account account = getAccount(transactionRequest.getAccountNumber());
        if (account.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            return false;
        }

        account.setBalance(account.getBalance().subtract(transactionRequest.getAmount()));
        accountRepository.save(account);
        saveInStatementService(
                account.getAccountNumber(),
                null,
                transactionRequest.getAmount(),
                TransactionType.WITHDRAW
        );
        return true;
    }

    @Transactional
    public Boolean transfer(TransferRequest transferRequest) {
        Optional<Customer> recipientCustomer = customerRepository.findByEmail(transferRequest.getRecipientsEmail());
        if(recipientCustomer.isEmpty()) {
            return false;
        }

        Optional<Account> account = accountRepository.findByAccountNumber(transferRequest.getAccountNumber());
        if(account.isEmpty()) {
            return false;
        }

        if(withdraw(new TransactionRequest(account.get().getAccountNumber(), transferRequest.getAmount()))) {
             if(deposit(new TransactionRequest(recipientCustomer.get().getAccount().getAccountNumber(), transferRequest.getAmount()))) {
                 saveInStatementService(
                         account.get().getAccountNumber(),
                         recipientCustomer.get().getAccount().getAccountNumber(),
                         transferRequest.getAmount(),
                         TransactionType.TRANSFER
                 );
                 return true;
             }
        }
        return false;
    }

    private Boolean saveInStatementService(String accountNumber, String recepientsAccountNumber, BigDecimal amount, TransactionType transactionType) {
        return webClientBuilder.build().put()
                .uri("http://statement-service/api/statement")
                .bodyValue(new StatementRequest(
                        accountNumber,
                        recepientsAccountNumber,
                        amount,
                        transactionType)
                )
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
