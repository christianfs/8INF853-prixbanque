package com.prixbanque.accountservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPlacedEvent {
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String accountNumber;
    private String email;
}
