package com.prixbanque.accountservice.repository;

import com.prixbanque.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByEmail(String email);
}
