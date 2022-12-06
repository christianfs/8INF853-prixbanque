package com.prixbanque.transferservice.dto;

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
public class TransferResponse {
    private String id;
    private String accountNumber;
    private String recipientsEmail;
    private BigDecimal value;
    private Boolean transferCompleted;
    private Instant createdDate;
    private Instant lastModifiedDate;
}
