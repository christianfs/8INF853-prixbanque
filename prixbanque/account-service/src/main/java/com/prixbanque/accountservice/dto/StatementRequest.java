package com.prixbanque.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementRequest {
    private String accountNumber;
    private String recipientsAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
}
