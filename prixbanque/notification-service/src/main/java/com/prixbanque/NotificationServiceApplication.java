package com.prixbanque;

import com.prixbanque.dto.NotificationType;
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
        if(notificationPlacedEvent.getType().equals(NotificationType.TRANSFER)) {
            log.info("http://localhost:8181/api/account/transfer/" + notificationPlacedEvent.getTransferId());
        } else {
            log.info("New account created " + notificationPlacedEvent.toString());
        }
    }
}
