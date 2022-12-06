package com.prixbanque.transferservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Document(value="bankTransfer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Transfer {
    @Id
    private String id;
    private String accountNumber;
    private String recipientsEmail;
    private UUID confirmationKey;
    private BigDecimal value;
    private Boolean transferCompleted;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant lastModifiedDate;
}
