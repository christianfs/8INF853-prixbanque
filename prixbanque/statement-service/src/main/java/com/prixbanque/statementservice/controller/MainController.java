package com.prixbanque.statementservice.controller;


import com.prixbanque.statementservice.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.prixbanque.statementservice.repository.UserRepository;

import com.prixbanque.statementservice.model.User;
import com.prixbanque.statementservice.model.Balance;

@Controller // This means that this class is a Controller
@RequestMapping(path="/api") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    @PostMapping(path="/add") // Map ONLY POST Requests
    public @ResponseBody String addNewUser (@RequestParam String name
            , @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
    }

    public @ResponseBody String addNewAmount (@RequestParam int userId
            , @RequestParam int amount) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Balance b = new Balance();
        b.setUser_Id(userId);
        b.setAmount(userId);
        balanceRepository.save(b);
        return "Saved";
    }

    @GetMapping(path="/account")
    public @ResponseBody Iterable<Balance> getAllBalances() {
        // This returns a JSON or XML with the users
       // return userRepository.findAll();
        return balanceRepository.findAll();
    }
}