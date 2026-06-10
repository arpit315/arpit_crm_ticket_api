package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dao.TicketDao;
import com.arpit.crm_ticketing_api.dto.TicketRequest;
import com.arpit.crm_ticketing_api.dto.TicketResponse;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.entity.Ticket;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketDao ticketDao;
    private final AgentDao agentDao;

    public TicketResponse create(TicketRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Ticket request must not be null");
        }

        try {
            log.info("Creating ticket with title={}", request.getTitle());
            Ticket ticket = new Ticket();
            ticket.setTitle(request.getTitle());
            ticket.setDescription(request.getDescription());
            ticket.setPriority(request.getPriority());
            ticket.setStatus(request.getStatus());

            if (request.getAssignedAgentId() != null) {
                Agent agent = agentDao.findById(request.getAssignedAgentId());
                if (agent == null) {
                    throw new ResourceNotFoundException("Agent not found with id: " + request.getAssignedAgentId());
                }
                ticket.setAssignedAgent(agent);
            }

            return toResponse(ticketDao.save(ticket));
        } catch (Exception e) {
            log.error("Failed to create ticket", e);
            throw e;
        }
    }

    public List<TicketResponse> findAll() {
        try {
            log.info("Fetching all tickets");
            return ticketDao.findAll().stream().map(this::toResponse).toList();
        } catch (Exception e) {
            log.error("Failed to fetch all tickets", e);
            throw e;
        }
    }

    public TicketResponse findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ticket id must be a positive number");
        }

        try {
            Ticket ticket = ticketDao.findById(id);
            if (ticket == null) {
                throw new ResourceNotFoundException("Ticket not found with id: " + id);
            }
            log.info("Fetched ticket with id={}", id);
            return toResponse(ticket);
        } catch (ResourceNotFoundException e) {
            log.error("Ticket not found with id={}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch ticket with id={}", id, e);
            throw e;
        }
    }

    public TicketResponse update(Long id, TicketRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ticket id must be a positive number");
        }
        if (request == null) {
            throw new IllegalArgumentException("Ticket request must not be null");
        }

        try {
            Ticket existing = ticketDao.findById(id);
            if (existing == null) {
                throw new ResourceNotFoundException("Ticket not found with id: " + id);
            }
            existing.setTitle(request.getTitle());
            existing.setDescription(request.getDescription());
            existing.setPriority(request.getPriority());
            existing.setStatus(request.getStatus());

            if (request.getAssignedAgentId() != null) {
                Agent agent = agentDao.findById(request.getAssignedAgentId());
                if (agent == null) {
                    throw new ResourceNotFoundException("Agent not found with id: " + request.getAssignedAgentId());
                }
                existing.setAssignedAgent(agent);
            }

            log.info("Updating ticket with id={}", id);
            return toResponse(ticketDao.update(existing));
        } catch (ResourceNotFoundException e) {
            log.error("Ticket update failed for id={}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to update ticket with id={}", id, e);
            throw e;
        }
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ticket id must be a positive number");
        }

        try {
            log.info("Deleting ticket with id={}", id);
            ticketDao.delete(id);
        } catch (Exception e) {
            log.error("Failed to delete ticket with id={}", id, e);
            throw e;
        }
    }

    private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getPriority(),
                ticket.getStatus(), ticket.getCreatedAt(), ticket.getUpdatedAt(),
                ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getId() : null);
    }
}
