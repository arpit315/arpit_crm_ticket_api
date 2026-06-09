package com.arpit.crm_ticketing_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private Long ticketId;
    private Long agentId;
}
