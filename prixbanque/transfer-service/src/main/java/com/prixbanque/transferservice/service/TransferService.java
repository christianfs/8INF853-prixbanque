package com.prixbanque.transferservice.service;

import com.prixbanque.transferservice.dto.TransferRequest;
import com.prixbanque.transferservice.dto.TransferResponse;
import com.prixbanque.transferservice.event.TransferPlacedEvent;
import com.prixbanque.transferservice.model.Transfer;
import com.prixbanque.transferservice.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final KafkaTemplate<String, TransferPlacedEvent> kafkaTemplate;

    public void createTransfer(TransferRequest transferRequest) {
        Transfer transfer = Transfer.builder()
                .recipientsEmail(transferRequest.getRecipientsEmail())
                .accountNumber(transferRequest.getAccountNumber())
                .value(transferRequest.getValue())
                .confirmationKey(UUID.randomUUID())
                .transferCompleted(false)
                .build();

        transferRepository.save(transfer);
        kafkaTemplate.send("notificationTopic",
                new TransferPlacedEvent(
                        transfer.getId(),
                        transfer.getAccountNumber(),
                        transfer.getRecipientsEmail(),
                        transfer.getConfirmationKey(),
                        transfer.getValue())
        );
        log.info("Transfer {} is created", transfer.getId());
    }

    public Boolean commitTransfer(UUID confirmationKey, String recipientsEmail) {
        Optional<Transfer> optionalTransfer = transferRepository.findByConfirmationKeyAndRecipientsEmail(confirmationKey, recipientsEmail);

        if(optionalTransfer.isEmpty()) {
            return false;
        }

        Transfer transfer = optionalTransfer.get();

        if(transfer.getTransferCompleted()) {
            return false;
        }

        transfer.setTransferCompleted(true);
        transferRepository.save(transfer);
        return true;
    }

    public List<TransferResponse> getAllTransfersByAccountNumber(String accountNumber) {
        Optional<List<Transfer>> optionalTransfers = transferRepository.findByAccountNumber(accountNumber);

        if(optionalTransfers.get().isEmpty()) {
            return List.of();
        }

        return optionalTransfers.get().stream()
                .map(transfer -> mapToTransferResponse(transfer))
                .collect(Collectors.toList());
    }

    private TransferResponse mapToTransferResponse(Transfer transfer) {
        return TransferResponse.builder()
                .id(transfer.getId())
                .accountNumber(transfer.getAccountNumber())
                .recipientsEmail(transfer.getRecipientsEmail())
                .transferCompleted(transfer.getTransferCompleted())
                .value(transfer.getValue())
                .createdDate(transfer.getCreatedDate())
                .lastModifiedDate(transfer.getLastModifiedDate())
                .build();
    }
}
