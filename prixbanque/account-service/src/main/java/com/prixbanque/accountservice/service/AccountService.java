package com.prixbanque.accountservice.service;

import com.prixbanque.accountservice.dto.*;
import com.prixbanque.accountservice.event.NotificationPlacedEvent;
import com.prixbanque.accountservice.model.Account;
import com.prixbanque.accountservice.model.Customer;
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
                .customer(customer)
                .build();

        accountRepository.save(account);

        try {
            webClientBuilder.build().post()
                    .uri("http://core-service/api/core/balance-account",
                            uriBuilder -> uriBuilder.path("/{accountNumber}")
                                    .build(account.getAccountNumber()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception ex) {
            log.error(ex.toString());
            throw ex;
        }

        kafkaTemplate.send("notificationTopic",
                new NotificationPlacedEvent(
                        account.getId().toString(),
                        account.getCustomer().getFirstName(),
                        account.getCustomer().getLastName(),
                        account.getCustomer().getPhone(),
                        account.getAccountNumber(),
                        account.getCustomer().getEmail()
                )
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
                .build();
    }

    private String generateAccountNumber(Integer size, Integer min, Integer max) {
        return new Random().ints(size, min, max + 1)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }

    public void deleteAllAccounts() {
        accountRepository.deleteAll();
    }
}
