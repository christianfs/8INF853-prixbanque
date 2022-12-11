package com.prixbanque;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(NotificationPlacedEvent notificationPlacedEvent) {
        // send out an email notification
        log.info("Id {}, name {}, account {}, email {}, confirmationKey {}, value {}",
                notificationPlacedEvent.getId(),
                notificationPlacedEvent.getFullName(),
                notificationPlacedEvent.getAccountNumber(),
                notificationPlacedEvent.getRecipientsEmail(),
                notificationPlacedEvent.getConfirmationKey(),
                notificationPlacedEvent.getValue());
    }
}
