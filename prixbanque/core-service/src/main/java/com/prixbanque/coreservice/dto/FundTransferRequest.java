package com.prixbanque.coreservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundTransferRequest {
    private String accountNumber;
    private String recipientAccountNumber;
    private BigDecimal amount;
}
