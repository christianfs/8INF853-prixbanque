package com.prixbanque.statementservice.repository;

import com.prixbanque.statementservice.model.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface StatementRepository extends MongoRepository<Statement, String> {
    @Query(value = "{$or:[{'accountNumber': ?0}, {'recipientsAccountNumber': ?0}], 'createdDate' : {$gte : ?1, $lte: ?2 }}")
    Optional<List<Statement>> findByAccountNumberAndCreatedDateBetween(String accountNumber, Instant startDate, Instant endDate);
}
