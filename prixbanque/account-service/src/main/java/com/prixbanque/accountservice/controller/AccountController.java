package com.prixbanque.accountservice.controller;

import com.prixbanque.accountservice.dto.AccountRequest;
import com.prixbanque.accountservice.dto.AccountResponse;
import com.prixbanque.accountservice.dto.TransactionRequest;
import com.prixbanque.accountservice.dto.TransferRequest;
import com.prixbanque.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountRequest accountRequest){
        accountService.createAccount(accountRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @PutMapping(path = "/deposit")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deposit(@RequestBody TransactionRequest transactionRequest){
        return accountService.deposit(transactionRequest);
    }

    @PutMapping(path = "/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public Boolean withdraw(@RequestBody TransactionRequest transactionRequest){
        return accountService.withdraw(transactionRequest);
    }

    @PutMapping(path = "/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Boolean transfer(@RequestBody TransferRequest transferRequest){
        return accountService.transfer(transferRequest);
    }
}