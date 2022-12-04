package com.prixbanque.transferservice.controller;

import com.prixbanque.transferservice.dto.TransferRequest;
import com.prixbanque.transferservice.dto.TransferResponse;
import com.prixbanque.transferservice.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransfer(@RequestBody TransferRequest transferRequest) {
        transferService.createTransfer(transferRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TransferResponse getTransfer(@RequestParam(name = "key") UUID confirmationKey, @RequestParam(name = "email") String recipientsEmail) {
        return transferService.getTransfer(confirmationKey, recipientsEmail);
    }
}
