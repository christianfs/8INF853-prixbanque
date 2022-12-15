package com.prixbanque.statementservice.model;

import com.prixbanque.statementservice.dto.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Document(value="statementBalance")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Statement {
    @Id
    private String id;
    private String accountNumber;
    private String recipientsAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private UUID transferId;
    @CreatedDate
    private Instant createdDate;
}
