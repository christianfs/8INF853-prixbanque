package com.prixbanque.transferservice.service;

import com.prixbanque.transferservice.dto.AccountResponse;
import com.prixbanque.transferservice.dto.TransferRequest;
import com.prixbanque.transferservice.dto.TransferResponse;
import com.prixbanque.transferservice.event.NotificationPlacedEvent;
import com.prixbanque.transferservice.dto.NotificationType;
import com.prixbanque.transferservice.model.Transfer;
import com.prixbanque.transferservice.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final KafkaTemplate<String, NotificationPlacedEvent> kafkaTemplate;
    private final WebClient.Builder webClientBuilder;

    public Boolean createTransfer(TransferRequest transferRequest) {
        Transfer transfer = Transfer.builder()
                .recipientsEmail(transferRequest.getRecipientsEmail())
                .accountNumber(transferRequest.getAccountNumber())
                .amount(transferRequest.getAmount())
                .transferId(UUID.randomUUID())
                .transferCompleted(false)
                .build();

        AccountResponse accountResponse = webClientBuilder.build().get()
                .uri("http://account-service/api/account",
                        uriBuilder -> uriBuilder.path("/{accountNumber}")
                        .build(transfer.getAccountNumber()))
                .retrieve()
                .bodyToMono(AccountResponse.class)
                .block();

        if(accountResponse != null && accountResponse.getBalance().compareTo(transfer.getAmount()) >= 0) {
            transferRepository.save(transfer);
            kafkaTemplate.send("notificationTopic",
                    new NotificationPlacedEvent(
                            transfer.getId(),
                            "",
                            transfer.getAccountNumber(),
                            transfer.getRecipientsEmail(),
                            transfer.getTransferId(),
                            transfer.getAmount(),
                            NotificationType.TRANSFER)
            );
            log.info("Account Transfer {} is created", transfer.getAccountNumber());
            return true;
        }
        return false;
    }

    public TransferResponse commitTransfer(UUID transferId) {
        Optional<Transfer> optionalTransfer = transferRepository.findByTransferId(transferId);

        if(optionalTransfer.isEmpty()) {
            return null;
        }

        Transfer transfer = optionalTransfer.get();

        if(transfer.getTransferCompleted()) {
            return null;
        }

        transfer.setTransferCompleted(true);
        transferRepository.save(transfer);
        return mapToTransferResponse(transfer);
    }

    public List<TransferResponse> getTransfersByAccountNumber(String accountNumber) {
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
                .amount(transfer.getAmount())
                .createdDate(transfer.getCreatedDate())
                .lastModifiedDate(transfer.getLastModifiedDate())
                .build();
    }
}
