package com.prixbanque.coreservice.repository;

import com.prixbanque.coreservice.model.BalanceAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BalanceAccountRepository extends MongoRepository<BalanceAccount, String> {
    Optional<BalanceAccount> findByAccountNumber(String accountNumber);
}
