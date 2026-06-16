package com.arpit.crm_ticketing_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank(message = "Message must not be blank")
    @Size(min = 1, max = 1000, message = "Message must be between 1 and 1000 characters")
    private String message;

    @NotNull(message = "Ticket id is required")
    private Long ticketId;

    @NotNull(message = "Agent id is required")
    private Long agentId;
}
