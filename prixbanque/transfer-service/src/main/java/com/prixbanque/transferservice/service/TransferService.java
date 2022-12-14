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

    public void createTransfer(TransferRequest transferRequest) {
        Transfer transfer = Transfer.builder()
                .recipientsEmail(transferRequest.getRecipientsEmail())
                .accountNumber(transferRequest.getAccountNumber())
                .value(transferRequest.getAmount())
                .confirmationKey(UUID.randomUUID())
                .transferCompleted(false)
                .build();

        AccountResponse result = webClientBuilder.build().get()
                .uri("http://account-service/api/account",
                        uriBuilder -> uriBuilder.path("/{accountNumber}")
                        .build(transfer.getAccountNumber()))
                .retrieve()
                .bodyToMono(AccountResponse.class)
                .block();

        if(result != null) {
            transferRepository.save(transfer);
            kafkaTemplate.send("notificationTopic",
                    new NotificationPlacedEvent(
                            transfer.getId(),
                            "",
                            transfer.getAccountNumber(),
                            transfer.getRecipientsEmail(),
                            transfer.getConfirmationKey(),
                            transfer.getValue(),
                            NotificationType.TRANSFER)
            );
            log.info("Account Transfer {} is created", transfer.getAccountNumber());
        }
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

        Boolean result = webClientBuilder.build().put()
                .uri("http://account-service/api/account/transfer")
                .bodyValue(new TransferRequest(
                        transfer.getAccountNumber(),
                        transfer.getRecipientsEmail(),
                        transfer.getValue())
                )
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(result) {
            transfer.setTransferCompleted(true);
            transferRepository.save(transfer);
            return true;
        }
        return false;
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
                .amount(transfer.getValue())
                .createdDate(transfer.getCreatedDate())
                .lastModifiedDate(transfer.getLastModifiedDate())
                .build();
    }
}
