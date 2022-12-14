package com.prixbanque.statementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementResponse {
    private String accountNumber;
    private String recipientsAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Instant createdDate;
}
