package com.arpit.crm_ticketing_api.dto;

import com.arpit.crm_ticketing_api.enums.Priority;
import com.arpit.crm_ticketing_api.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {
    @NotBlank
    @Size(min = 3, max = 150)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Priority priority;

    @NotNull
    private TicketStatus status;

    private Long assignedAgentId;
}
