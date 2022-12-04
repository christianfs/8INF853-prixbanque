package com.prixbanque.transferservice.repository;

import com.prixbanque.transferservice.model.Transfer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepository extends MongoRepository<Transfer, String> {
    Optional<Transfer> findByConfirmationKeyAndRecipientsEmail(UUID confirmationKey, String recipientsEmail);
}
