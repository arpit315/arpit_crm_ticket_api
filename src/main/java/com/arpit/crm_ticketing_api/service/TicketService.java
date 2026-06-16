package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.cache.RedisCacheService;
import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dao.TicketDao;
import com.arpit.crm_ticketing_api.dto.TicketEvent;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.entity.Ticket;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import com.arpit.crm_ticketing_api.kafka.producer.TicketProducer;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final Logger log =
            LoggerFactory.getLogger(TicketService.class);

    private final TicketDao ticketDao;
    private final AgentDao agentDao;
    private final TicketProducer ticketProducer;
    private final Cache<Long, Object> caffeineCache;
    private final RedisCacheService redisCacheService;

    public Ticket create(Ticket ticket) {

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket must not be null"
            );
        }

        try {

            if (ticket.getAssignedAgent() != null) {

                Agent agent =
                        agentDao.findById(
                                ticket.getAssignedAgent().getId()
                        );

                if (agent == null) {
                    throw new ResourceNotFoundException(
                            "Agent not found with id: "
                                    + ticket.getAssignedAgent().getId()
                    );
                }

                ticket.setAssignedAgent(agent);
            }

            Ticket savedTicket =
                    ticketDao.save(ticket);

            caffeineCache.invalidate(-1L);

            ticketProducer.publish(
                    TicketEvent.builder()
                            .ticketId(savedTicket.getId())
                            .action("CREATE")
                            .title(savedTicket.getTitle())
                            .status(savedTicket.getStatus().name())
                            .priority(savedTicket.getPriority().name())
                            .assignedAgentId(
                                    savedTicket.getAssignedAgent() != null
                                            ? savedTicket.getAssignedAgent().getId()
                                            : null
                            )
                            .build()
            );

            redisCacheService.save(
                    "ticket:" + savedTicket.getId(),
                    savedTicket
            );

            return savedTicket;

        } catch (Exception e) {

            log.error("Failed to create ticket", e);

            throw e;
        }
    }

    public List<Ticket> findAll() {

        try {

            @SuppressWarnings("unchecked")
            List<Ticket> cachedTickets =
                    (List<Ticket>) caffeineCache.getIfPresent(-1L);

            if (cachedTickets != null) {
                return cachedTickets;
            }

            List<Ticket> tickets =
                    ticketDao.findAll();

            caffeineCache.put(-1L, tickets);

            return tickets;

        } catch (Exception e) {

            log.error("Failed to fetch tickets", e);

            throw e;
        }
    }

    public Ticket findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Ticket id must be positive"
            );
        }

        try {

            Object cached =
                    redisCacheService.get(
                            "ticket:" + id
                    );

            if (cached != null) {
                return (Ticket) cached;
            }

            Ticket ticket =
                    ticketDao.findById(id);

            if (ticket == null) {
                throw new ResourceNotFoundException(
                        "Ticket not found with id: " + id
                );
            }

            redisCacheService.save(
                    "ticket:" + id,
                    ticket
            );

            return ticket;

        } catch (Exception e) {

            log.error(
                    "Failed to fetch ticket with id={}",
                    id,
                    e
            );

            throw e;
        }
    }

    public Ticket update(Long id, Ticket ticket) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Ticket id must be positive"
            );
        }

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "Ticket must not be null"
            );
        }

        Ticket existing =
                ticketDao.findById(id);

        if (existing == null) {
            throw new ResourceNotFoundException(
                    "Ticket not found with id: " + id
            );
        }

        existing.setTitle(ticket.getTitle());
        existing.setDescription(ticket.getDescription());
        existing.setPriority(ticket.getPriority());
        existing.setStatus(ticket.getStatus());

        if (ticket.getAssignedAgent() != null) {

            Agent agent =
                    agentDao.findById(
                            ticket.getAssignedAgent().getId()
                    );

            if (agent == null) {
                throw new ResourceNotFoundException(
                        "Agent not found with id: "
                                + ticket.getAssignedAgent().getId()
                );
            }

            existing.setAssignedAgent(agent);
        }

        Ticket updatedTicket =
                ticketDao.update(existing);

        caffeineCache.invalidate(-1L);

        redisCacheService.save(
                "ticket:" + updatedTicket.getId(),
                updatedTicket
        );

        return updatedTicket;
    }

    public void delete(Long id) {

        ticketDao.delete(id);

        redisCacheService.delete(
                "ticket:" + id
        );

        caffeineCache.invalidate(-1L);
    }

}
