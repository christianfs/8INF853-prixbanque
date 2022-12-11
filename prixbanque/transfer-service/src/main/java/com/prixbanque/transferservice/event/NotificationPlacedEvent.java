package com.prixbanque.transferservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPlacedEvent {
    private String id;
    private String fullName;
    private String accountNumber;
    private String recipientsEmail;
    private UUID confirmationKey;
    private BigDecimal value;
    private String type;
}
