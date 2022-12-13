package com.prixbanque.statementservice.repository;

import com.prixbanque.statementservice.model.Statement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface StatementRepository extends MongoRepository<Statement, String> {
    @Query(value = "{ 'createdDate' : {$gte : ?0, $lte: ?1 }}")
    Optional<List<Statement>> findByCreatedDateBetween(Instant startDate, Instant endDate);
}
