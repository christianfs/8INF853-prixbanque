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
    public Boolean createStatement(StatementRequest statementRequest) {
        Statement statement = Statement.builder()
                .accountNumber(statementRequest.getAccountNumber())
                .amount(statementRequest.getAmount())
                .recipientsAccountNumber(statementRequest.getRecipientsAccountNumber())
                .transactionType(statementRequest.getTransactionType())
                .transferId(statementRequest.getTransferId())
                .build();

        statementRepository.save(statement);
        log.info("Statement {} was created successfully.", statement.getId());
        return true;
    }

    public List<StatementResponse> getStatementsByAccountNumberAndDates(String accountNumber, String startDate, String endDate) throws ParseException {

        if(endDate == null || endDate.isEmpty()) {
            endDate = startDate;
        }

        SimpleDateFormat formatterStart = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatterEnd = new SimpleDateFormat("yyyy-MM-dd");
        Instant start = formatterStart.parse(startDate).toInstant().atZone(ZoneOffset.UTC).withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant();
        Instant end = formatterEnd.parse(endDate).toInstant().atZone(ZoneOffset.UTC).withHour(23).withMinute(59).withSecond(59).withNano(999999999).toInstant();

        Optional<List<Statement>> optionalStatements = statementRepository.findByAccountNumberAndCreatedDateBetween(accountNumber, start, end);

        if(optionalStatements.get().isEmpty()) {
            return List.of();
        }

        return optionalStatements.get().stream()
                .map(this::mapToStatementResponse)
                .collect(Collectors.toList());
    }

    public List<StatementResponse> getAllStatements() {
        List<Statement> statements = statementRepository.findAll();
        return statements.stream().map(this::mapToStatementResponse).collect(Collectors.toList());
    }

    private StatementResponse mapToStatementResponse(Statement statement) {
        return StatementResponse.builder()
                .accountNumber(statement.getAccountNumber())
                .recipientsAccountNumber(statement.getRecipientsAccountNumber())
                .amount(statement.getAmount())
                .transactionType(statement.getTransactionType())
                .transferId(statement.getTransferId())
                .createdDate(statement.getCreatedDate())
                .build();
    }
}
