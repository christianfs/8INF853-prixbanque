package com.prixbanque.accountservice.service;

import com.prixbanque.accountservice.dto.UserRequest;
import com.prixbanque.accountservice.dto.UserResponse;
import com.prixbanque.accountservice.model.User;
import com.prixbanque.accountservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j //pour les {}
public class UserService {
    private final UserRepository userRepository;

    public void createAccount(UserRequest userRequest){
        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .address(userRequest.getAddress())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .account(userRequest.getAccount())
                .password(userRequest.getPassword())
                .build();

        userRepository.save(user);
        log.info("User {} is saved", user.getUserId());
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .account(user.getAccount())
                .password(user.getPassword())
                .build();
    }

}
