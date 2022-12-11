package com.prixbanque.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String accountNumber;
    private BigDecimal balance;
}