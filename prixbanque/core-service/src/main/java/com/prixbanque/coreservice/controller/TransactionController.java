package com.prixbanque.coreservice.controller;

import com.prixbanque.coreservice.dto.DepositWithdrawRequest;
import com.prixbanque.coreservice.dto.FundTransferRequest;
import com.prixbanque.coreservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/core/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping(path = "/deposit")
    @ResponseStatus(HttpStatus.OK)
    public boolean deposit(@RequestBody DepositWithdrawRequest depositRequest){
        return transactionService.deposit(depositRequest);
    }

    @PutMapping(path = "/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public boolean withdraw(@RequestBody DepositWithdrawRequest withdrawRequest){
        return transactionService.withdraw(withdrawRequest);
    }

    @PutMapping(path = "/fund-transfer")
    @ResponseStatus(HttpStatus.OK)
    public boolean fundTransfer(@RequestBody FundTransferRequest transferRequest){
        return transactionService.fundTransfer(transferRequest);
    }
}
