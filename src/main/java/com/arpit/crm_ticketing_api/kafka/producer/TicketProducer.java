package com.arpit.crm_ticketing_api.kafka.producer;

import com.arpit.crm_ticketing_api.kafka.event.TicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketProducer {

    private final KafkaTemplate<String, TicketEvent> kafkaTemplate;

    public void publish(TicketEvent event) {
        kafkaTemplate.send("ticket-topic", event);
    }
}