package com.prixbanque.coreservice.service;

import com.prixbanque.coreservice.dto.DepositWithdrawRequest;
import com.prixbanque.coreservice.dto.FundTransferRequest;
import com.prixbanque.coreservice.dto.StatementRequest;
import com.prixbanque.coreservice.dto.TransactionType;
import com.prixbanque.coreservice.model.BalanceAccount;
import com.prixbanque.coreservice.repository.BalanceAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final BalanceAccountRepository balanceAccountRepository;
    private final WebClient.Builder webClientBuilder;

    @Transactional
    public boolean fundTransfer(FundTransferRequest fundTransferRequest) {

        UUID transactionId = UUID.randomUUID();
        boolean madeWithdraw = internalWithdraw(
                new DepositWithdrawRequest(fundTransferRequest.getAccountNumber(), fundTransferRequest.getAmount()),
                transactionId
        );

        if(!madeWithdraw) {
            return false;
        }

        internalDeposit(
                new DepositWithdrawRequest(fundTransferRequest.getRecipientAccountNumber(), fundTransferRequest.getAmount()),
                transactionId
        );

        log.info("Transfer of {} successfully made from account {} to account {}",
                fundTransferRequest.getAmount(),
                fundTransferRequest.getAccountNumber(),
                fundTransferRequest.getRecipientAccountNumber());
        return true;
    }

    public boolean deposit(DepositWithdrawRequest depositRequest) {
        return internalDeposit(depositRequest, UUID.randomUUID());
    }

    public boolean withdraw(DepositWithdrawRequest withdrawRequest) {
        return internalWithdraw(withdrawRequest, UUID.randomUUID());
    }

    private boolean internalDeposit(DepositWithdrawRequest depositRequest, UUID transactionId) {

        Optional<BalanceAccount> senderBalance = balanceAccountRepository.findByAccountNumber(depositRequest.getAccountNumber());
        if(senderBalance.isEmpty()) {
            log.info("Account {} not found.", depositRequest.getAccountNumber());
            return false;
        }

        senderBalance.get().setBalance(senderBalance.get().getBalance().add(depositRequest.getAmount()));
        balanceAccountRepository.save(senderBalance.get());
        log.info("Deposit of {} made successfully to the account {}", depositRequest.getAmount(), depositRequest.getAccountNumber());
        saveInStatementService(depositRequest.getAccountNumber(), depositRequest.getAmount(), TransactionType.DEPOSIT, transactionId);
        return true;
    }

    private boolean internalWithdraw(DepositWithdrawRequest withdrawRequest, UUID transactionId) {

        Optional<BalanceAccount> senderBalance = balanceAccountRepository.findByAccountNumber(withdrawRequest.getAccountNumber());
        if(senderBalance.isEmpty()) {
            log.info("Account {} not found.", withdrawRequest.getAccountNumber());
            return false;
        }

        if(senderBalance.get().getBalance().compareTo(withdrawRequest.getAmount()) < 0) {
            log.info("Insufficient account balance. Balance of {}.", senderBalance.get().getBalance());
            return false;
        }

        senderBalance.get().setBalance(senderBalance.get().getBalance().subtract(withdrawRequest.getAmount()));
        balanceAccountRepository.save(senderBalance.get());
        log.info("Withdrawal of {} made successfully from the account {}", withdrawRequest.getAmount(), withdrawRequest.getAccountNumber());
        saveInStatementService(withdrawRequest.getAccountNumber(), withdrawRequest.getAmount(), TransactionType.WITHDRAW, transactionId);
        return true;
    }

    private Boolean saveInStatementService(String accountNumber,
                                           BigDecimal amount,
                                           TransactionType transactionType,
                                           UUID transactionId) {
        return webClientBuilder.build().post()
                .uri("http://statement-service/api/statement")
                .bodyValue(new StatementRequest(
                                accountNumber,
                                amount,
                                transactionType,
                                transactionId
                        )
                )
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
}
