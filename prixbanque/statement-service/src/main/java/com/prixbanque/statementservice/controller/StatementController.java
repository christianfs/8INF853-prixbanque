package com.prixbanque.statementservice.controller;

import com.prixbanque.statementservice.dto.StatementRequest;
import com.prixbanque.statementservice.dto.StatementResponse;
import com.prixbanque.statementservice.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/statement")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean createStatement(@RequestBody StatementRequest statementRequest) {
        return statementService.createStatement(statementRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StatementResponse> getAllStatements(){
        return statementService.getAllStatements();
    }


    @GetMapping(path = "/accountNumber/{accountNumber}/startDate/{startDate}/endDate/{endDate}")
    @ResponseStatus(HttpStatus.OK)
    public List<StatementResponse> getStatementsByAccountNumberAndDates(@PathVariable String accountNumber, @PathVariable String startDate, @PathVariable String endDate) throws ParseException {
        return statementService.getStatementsByAccountNumberAndDates(accountNumber, startDate, endDate);
    }
}
