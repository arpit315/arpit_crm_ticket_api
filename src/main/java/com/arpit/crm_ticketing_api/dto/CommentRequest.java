package com.arpit.crm_ticketing_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank
    @Size(min = 1, max = 1000)
    private String message;

    @NotNull
    private Long ticketId;

    @NotNull
    private Long agentId;
}
