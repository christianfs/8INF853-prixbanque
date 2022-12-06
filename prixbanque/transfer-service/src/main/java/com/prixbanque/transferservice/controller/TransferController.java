package com.prixbanque.transferservice.controller;

import com.prixbanque.transferservice.dto.TransferRequest;
import com.prixbanque.transferservice.dto.TransferResponse;
import com.prixbanque.transferservice.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping(path = "/api/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransfer(@RequestBody TransferRequest transferRequest) {
        transferService.createTransfer(transferRequest);
    }

    @PostMapping(path = "/api/transfer/commit")
    @ResponseStatus(HttpStatus.OK)
    public Boolean commitTransfer(@RequestParam(name = "confirmationKey") UUID confirmationKey, @RequestParam(name = "recipientsEmail") String recipientsEmail) {
        return transferService.commitTransfer(confirmationKey, recipientsEmail);
    }

    @GetMapping(path = "/api/transfer/account/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransferResponse> getAllTransfersByAccountNumber(@PathVariable String accountNumber) {
        return transferService.getAllTransfersByAccountNumber(accountNumber);
    }
}
