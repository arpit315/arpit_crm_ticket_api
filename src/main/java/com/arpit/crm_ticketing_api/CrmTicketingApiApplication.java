package com.arpit.crm_ticketing_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class CrmTicketingApiApplication {
    public static void main(String[] args) {

        SpringApplication.run(CrmTicketingApiApplication.class, args);
    }
}
