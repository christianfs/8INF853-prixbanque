package com.prixbanque.accountservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String account;
}
