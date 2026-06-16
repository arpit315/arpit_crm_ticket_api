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
    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Status is required")
    private TicketStatus status;

    private Long assignedAgentId;
}
