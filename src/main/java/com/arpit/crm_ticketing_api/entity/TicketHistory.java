package com.arpit.crm_ticketing_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "ticket_history")
@Getter
@Setter
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String priority;

    @Column(name = "assigned_agent_id")
    private Long assignedAgentId;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @PrePersist
    protected void onCreate() {
        eventTime = Instant.now();
    }
}