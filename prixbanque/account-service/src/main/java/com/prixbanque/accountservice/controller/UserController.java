package com.prixbanque.accountservice.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.prixbanque.accountservice.entity.User;
import org.springframework.ui.Model;
import org.slf4j.Logger;
@RestController
@RequestMapping("/register")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/register")
    public String register(@ModelAttribute User user, Model model){
        model.addAttribute("user",user);
        return "register";
    }

    @PostMapping("/register")
    public void save(User user){
        log.info(">> User : {}", user.toString());
    }
}
