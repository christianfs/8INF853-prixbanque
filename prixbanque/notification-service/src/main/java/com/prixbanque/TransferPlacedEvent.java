package com.prixbanque;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferPlacedEvent {
    private String id;
    private String sendersAccountNumber;
    private String recipientsEmail;
    private UUID confirmationKey;
    private BigDecimal value;
}