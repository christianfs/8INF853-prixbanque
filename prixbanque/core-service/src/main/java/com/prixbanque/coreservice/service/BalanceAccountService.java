package com.prixbanque.coreservice.service;

import com.prixbanque.coreservice.dto.BalanceAccountResponse;
import com.prixbanque.coreservice.model.BalanceAccount;
import com.prixbanque.coreservice.repository.BalanceAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceAccountService {

    private final BalanceAccountRepository balanceAccountRepository;

    public List<BalanceAccountResponse> getAllBalanceAccounts() {
        List<BalanceAccount> balanceAccounts = balanceAccountRepository.findAll();
        return balanceAccounts.stream().map(this::mapToBalanceAccountResponse).collect(Collectors.toList());
    }

    public BalanceAccountResponse getBalanceAccount(String accountNumber) {
        Optional<BalanceAccount> balanceAccount = balanceAccountRepository.findByAccountNumber(accountNumber);

        if(balanceAccount.isEmpty()) {
            return new BalanceAccountResponse();
        }

        return mapToBalanceAccountResponse(balanceAccount.get());
    }

    public String createBalanceAccount(String accountNumber) {
        BalanceAccount balanceAccount = BalanceAccount.builder()
                .balance(new BigDecimal(0))
                .accountNumber(accountNumber)
                .build();
        balanceAccountRepository.save(balanceAccount);
        String message = "Account balance for " + accountNumber + " is saved.";
        log.info(message);
        return message;
    }

    private BalanceAccountResponse mapToBalanceAccountResponse(BalanceAccount balanceAccount) {
        return BalanceAccountResponse.builder()
                .accountNumber(balanceAccount.getAccountNumber())
                .balance(balanceAccount.getBalance())
                .build();
    }

    public void deleteAllBalanceAccounts() {
        balanceAccountRepository.deleteAll();
    }
}
