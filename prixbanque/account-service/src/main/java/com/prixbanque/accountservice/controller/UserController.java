package com.prixbanque.accountservice.controller;

import com.prixbanque.accountservice.dto.UserRequest;
import com.prixbanque.accountservice.dto.UserResponse;
import com.prixbanque.accountservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody UserRequest userRequest){
        userService.createAccount(userRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers(){
       return userService.getAllUsers();
    }
}
