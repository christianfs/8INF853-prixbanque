package com.prixbanque.transferservice.service;

import com.prixbanque.transferservice.dto.TransferRequest;
import com.prixbanque.transferservice.dto.TransferResponse;
import com.prixbanque.transferservice.model.Transfer;
import com.prixbanque.transferservice.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;

    public void createTransfer(TransferRequest transferRequest) {
        Transfer transfer = Transfer.builder()
                .recipientsEmail(transferRequest.getRecipientsEmail())
                .sendersAccountNumber(transferRequest.getSendersAccountNumber())
                .value(transferRequest.getValue())
                .confirmationKey(UUID.randomUUID())
                .transferCompleted(false)
                .build();

        transferRepository.save(transfer);
        log.info("Transfer {} is created", transfer.getId());
    }

    public TransferResponse getTransfer(UUID confirmationKey, String recipientsEmail) {
        Optional<Transfer> transfer = transferRepository.findByConfirmationKeyAndRecipientsEmail(confirmationKey, recipientsEmail);

        if(transfer.isEmpty()) {
            return new TransferResponse();
        }

        return  mapToTransferResponse(transfer.get());
    }

    private TransferResponse mapToTransferResponse(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .sendersAccountNumber(transfer.getSendersAccountNumber())
                .recipientsEmail(transfer.getRecipientsEmail())
                .transferCompleted(transfer.getTransferCompleted())
                .value(transfer.getValue())
                .build();
    }
}
