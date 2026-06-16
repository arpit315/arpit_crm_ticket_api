package com.arpit.crm_ticketing_api.kafka.event;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketEvent {

    private Long ticketId;
    private String action;
    private String title;
    private String status;
    private String priority;
    private Long assignedAgentId;
}