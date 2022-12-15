package com.prixbanque;

import com.prixbanque.dto.NotificationType;
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
    private UUID transferId;
    private BigDecimal amount;
    private NotificationType type;
}