package com.prixbanque.coreservice.controller;

import com.prixbanque.coreservice.dto.BalanceAccountResponse;
import com.prixbanque.coreservice.service.BalanceAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/core/balance-account")
@RequiredArgsConstructor
public class BalanceAccountController {

    private final BalanceAccountService balanceAccountService;

    @PostMapping(path = "/{accountNumber}")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBalanceAccount(@PathVariable String accountNumber){
        return balanceAccountService.createBalanceAccount(accountNumber);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BalanceAccountResponse> getAllBalanceAccounts(){
        return balanceAccountService.getAllBalanceAccounts();
    }

    @GetMapping(path = "/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public BalanceAccountResponse getBalanceAccount(@PathVariable String accountNumber){
        return balanceAccountService.getBalanceAccount(accountNumber);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllBalanceAccounts() {
        balanceAccountService.deleteAllBalanceAccounts();
    }
}
