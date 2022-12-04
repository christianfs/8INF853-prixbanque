package com.prixbanque.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    private String id;
    private String sendersAccountNumber;
    private String recipientsEmail;
    private BigDecimal value;
    private Boolean transferCompleted;
}
