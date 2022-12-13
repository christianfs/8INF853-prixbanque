package com.prixbanque.statementservice.service;

import com.prixbanque.statementservice.dto.StatementRequest;
import com.prixbanque.statementservice.dto.StatementResponse;
import com.prixbanque.statementservice.model.Statement;
import com.prixbanque.statementservice.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;
    public void createStatement(StatementRequest statementRequest) {
        Statement statement = Statement.builder()
                .accountNumber(statementRequest.getAccountNumber())
                .amount(statementRequest.getAmount())
                .recepientsAccountNumber(statementRequest.getRecepientsAccountNumber())
                .transactionType(statementRequest.getTransactionType())
                .build();

        statementRepository.save(statement);
    }

    public List<StatementResponse> getStatementsByAccountNumberAndDates(String accountNumber, String startDate, String endDate) throws ParseException {

        if(endDate.equals(null) || endDate.isEmpty()) {
            endDate = startDate;
        }

        SimpleDateFormat formatterStart = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterEnd = new SimpleDateFormat("yyyy-MM-dd");
        Instant start = formatterStart.parse(startDate).toInstant().atZone(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant();
        Instant end = formatterEnd.parse(endDate).toInstant().atZone(ZoneOffset.UTC).withHour(23).withMinute(59).withSecond(59).withNano(999999999).toInstant();

        Optional<List<Statement>> optionalStatements = statementRepository.findByCreatedDateBetween(start, end);

        if(optionalStatements.get().isEmpty()) {
            return List.of();
        }

        return optionalStatements.get().stream()
                .map(statement -> mapToStatementResponse(statement))
                .collect(Collectors.toList());
    }

    public List<StatementResponse> getAllStatements() {
        List<Statement> statements = statementRepository.findAll();
        return statements.stream().map(this::mapToStatementResponse).collect(Collectors.toList());
    }

    private StatementResponse mapToStatementResponse(Statement statement) {
        return StatementResponse.builder()
                .accountNumber(statement.getAccountNumber())
                .recepientsAccountNumber(statement.getRecepientsAccountNumber())
                .amount(statement.getAmount())
                .transactionType(statement.getTransactionType())
                .createdDate(statement.getCreatedDate())
                .build();
    }
}
