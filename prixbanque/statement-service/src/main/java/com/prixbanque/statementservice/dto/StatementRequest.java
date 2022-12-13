package com.prixbanque.statementservice.dto;

import com.prixbanque.statementservice.model.TransactionType;
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
    private String recepientsAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
}
