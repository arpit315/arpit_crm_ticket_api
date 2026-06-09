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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketDao ticketDao;
    private final AgentDao agentDao;

    @Transactional
    public TicketResponse create(TicketRequest request) {
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
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findAll() {
        log.info("Fetching all tickets");
        return ticketDao.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse findById(Long id) {
        Ticket ticket = ticketDao.findById(id);
        if (ticket == null) {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }
        log.info("Fetched ticket with id={}", id);
        return toResponse(ticket);
    }

    @Transactional
    public TicketResponse update(Long id, TicketRequest request) {
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
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting ticket with id={}", id);
        ticketDao.delete(id);
    }

    private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(ticket.getId(), ticket.getTitle(), ticket.getDescription(), ticket.getPriority(),
                ticket.getStatus(), ticket.getCreatedAt(), ticket.getUpdatedAt(),
                ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getId() : null);
    }
}
