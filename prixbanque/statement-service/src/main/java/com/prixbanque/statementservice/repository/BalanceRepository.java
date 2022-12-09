package com.prixbanque.statementservice.repository;

import org.springframework.data.repository.CrudRepository;

import com.prixbanque.statementservice.model.Balance;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface BalanceRepository extends CrudRepository<Balance, Integer> {

}