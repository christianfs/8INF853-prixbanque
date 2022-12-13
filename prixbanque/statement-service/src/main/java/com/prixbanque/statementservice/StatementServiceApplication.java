package com.prixbanque.statementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableMongoAuditing
@EnableEurekaClient
public class StatementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatementServiceApplication.class, args);
    }
}
