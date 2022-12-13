package com.prixbanque.transferservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prixbanque.transferservice.dto.TransferRequest;
import com.prixbanque.transferservice.model.Transfer;
import com.prixbanque.transferservice.repository.TransferRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class TransferServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TransferRepository transferRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void shouldCreateTransfer() throws Exception {
        TransferRequest transferRequest = getTransferRequest();
        String transferRequestString = objectMapper.writeValueAsString(transferRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(2, transferRepository.findAll().size());
    }

    @Test
    public void shouldGetTransfer() throws Exception {
        Transfer transfer = getTransfer();
        transferRepository.save(transfer);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/transfer")
                        .param("key", transfer.getConfirmationKey().toString())
                        .param("email", transfer.getRecipientsEmail()))
                .andExpect(status().isOk());
    }

    private static Transfer getTransfer() {
        return Transfer.builder()
                .value(BigDecimal.valueOf(3000))
                .recipientsEmail("recipient@email.com")
                .accountNumber("12345-67")
                .transferCompleted(false)
                .confirmationKey(UUID.randomUUID())
                .build();
    }

    private TransferRequest getTransferRequest() {
        return TransferRequest.builder()
                .accountNumber("67453-21")
                .recipientsEmail("recipient2@email.com")
                .amount(BigDecimal.valueOf(50000))
                .build();
    }
}
