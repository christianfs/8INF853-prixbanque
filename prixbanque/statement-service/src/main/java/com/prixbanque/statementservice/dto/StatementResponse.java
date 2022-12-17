package com.prixbanque.statementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementResponse {
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private UUID transactionId;
    private Instant createdDate;
}
