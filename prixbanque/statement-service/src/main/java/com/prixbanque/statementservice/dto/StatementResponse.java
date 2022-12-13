package com.prixbanque.statementservice.dto;

import com.prixbanque.statementservice.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementResponse {
    private String accountNumber;
    private String recepientsAccountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Instant createdDate;
}
