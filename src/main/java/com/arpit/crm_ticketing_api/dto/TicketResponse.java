package com.arpit.crm_ticketing_api.dto;

import com.arpit.crm_ticketing_api.enums.Priority;
import com.arpit.crm_ticketing_api.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long assignedAgentId;
}
