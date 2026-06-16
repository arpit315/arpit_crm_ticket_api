package com.arpit.crm_ticketing_api.kafka.consumer;

import com.arpit.crm_ticketing_api.dao.TicketHistoryDao;
import com.arpit.crm_ticketing_api.kafka.event.TicketEvent;
import com.arpit.crm_ticketing_api.entity.TicketHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketConsumer {

    private final TicketHistoryDao ticketHistoryDao;

    @KafkaListener(
            topics = "ticket-topic",
            groupId = "ticket-history-group"
    )
    public void consume(TicketEvent event) {

        log.info(
                "Received Event -> TicketId={}, Action={}, Status={}",
                event.getTicketId(),
                event.getAction(),
                event.getStatus()
        );

        TicketHistory history = new TicketHistory();

        history.setTicketId(event.getTicketId());
        history.setAction(event.getAction());
        history.setTitle(event.getTitle());
        history.setStatus(event.getStatus());
        history.setPriority(event.getPriority());
        history.setAssignedAgentId(event.getAssignedAgentId());

        ticketHistoryDao.save(history);

        log.info(
                "History record saved for TicketId={}",
                event.getTicketId()
        );
    }
}