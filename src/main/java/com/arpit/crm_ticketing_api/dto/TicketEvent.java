package com.arpit.crm_ticketing_api.dto;

import lombok.*;

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