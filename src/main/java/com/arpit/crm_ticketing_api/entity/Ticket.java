package com.arpit.crm_ticketing_api.entity;

import com.arpit.crm_ticketing_api.enums.Priority;
import com.arpit.crm_ticketing_api.enums.TicketStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(
        name = "tickets",
        indexes = {
                @Index(name = "idx_ticket_status", columnList = "status"),
                @Index(name = "idx_ticket_priority", columnList = "priority")
        }
)
@Getter
@Setter
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "updated_at")
    private Instant updatedAt;
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 150,
            message = "Title must be between 3 and 150 characters")
    @Column(nullable = false)
    private String title;

    @Size(max = 1000,
            message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_agent_id")
    private Agent assignedAgent;

    public Long getAssignedAgentId() {
        return assignedAgent != null
                ? assignedAgent.getId()
                : null;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}