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
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean createTransfer(@RequestBody TransferRequest transferRequest) {
        return transferService.createTransfer(transferRequest);
    }

    @PutMapping(path = "/commit")
    @ResponseStatus(HttpStatus.OK)
    public TransferResponse commitTransfer(@RequestParam(name = "transferId") UUID transferId) {
        return transferService.commitTransfer(transferId);
    }

    @GetMapping(path = "/account/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransferResponse> getTransfersByAccountNumber(@PathVariable String accountNumber) {
        return transferService.getTransfersByAccountNumber(accountNumber);
    }
}
