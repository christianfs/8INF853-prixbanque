package com.prixbanque.accountservice.controller;

import com.prixbanque.accountservice.dto.AccountRequest;
import com.prixbanque.accountservice.dto.AccountResponse;
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
    public String createAccount(@RequestBody AccountRequest accountRequest){
        return accountService.createAccount(accountRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    @GetMapping(path = "/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccount(@PathVariable String accountNumber){
        return accountService.getAccountByAccountNumber(accountNumber);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllAccounts() {
        accountService.deleteAllAccounts();
    }
}
