package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.cache.RedisCacheService;
import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dao.TicketDao;
import com.arpit.crm_ticketing_api.dto.TicketEvent;
import com.arpit.crm_ticketing_api.dto.TicketRequest;
import com.arpit.crm_ticketing_api.dto.TicketResponse;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.entity.Ticket;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import com.arpit.crm_ticketing_api.kafka.producer.TicketProducer;
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
    private final TicketProducer ticketProducer;
    private final Cache<Long, Object> caffeineCache;
    private final RedisCacheService redisCacheService;

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

            Ticket savedTicket = ticketDao.save(ticket);
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
            TicketResponse response = toResponse(savedTicket);

            try {
                redisCacheService.save(
                        "ticket:" + response.getId(),
                        response
                );
            } catch (Exception e) {
                log.warn("Failed to save ticket in Redis");
            }

            return response;



        } catch (Exception e) {
            log.error("Failed to create ticket", e);
            throw e;
        }
    }

    public List<TicketResponse> findAll() {

        try {

            @SuppressWarnings("unchecked")
            List<TicketResponse> cachedTickets =
                    (List<TicketResponse>) caffeineCache.getIfPresent(-1L);

            if (cachedTickets != null) {

                log.info("Returning tickets from Caffeine cache");

                return cachedTickets;
            }

            log.info("Cache miss. Fetching tickets from DB");

            List<TicketResponse> tickets =
                    ticketDao.findAll()
                            .stream()
                            .map(this::toResponse)
                            .toList();

            caffeineCache.put(-1L, tickets);

            return tickets;

        } catch (Exception e) {

            log.error("Failed to fetch all tickets", e);

            throw e;
        }
    }
    public TicketResponse findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Ticket id must be a positive number");
        }

        String key = "ticket:" + id;

        try {

            try {

                Object cached = redisCacheService.get(key);

                if (cached != null) {

                    log.info("Returning ticket from Redis cache. id={}", id);

                    return (TicketResponse) cached;
                }

            } catch (Exception e) {

                log.warn("Redis unavailable. Falling back to DB. id={}", id);
            }

            Ticket ticket = ticketDao.findById(id);

            if (ticket == null) {
                throw new ResourceNotFoundException(
                        "Ticket not found with id: " + id
                );
            }

            TicketResponse response = toResponse(ticket);

            try {
                redisCacheService.save(key, response);
            } catch (Exception e) {
                log.warn("Failed to cache ticket in Redis. id={}", id);
            }

            return response;

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

            Ticket updatedTicket = ticketDao.update(existing);
            caffeineCache.invalidate(-1L);

            ticketProducer.publish(
                    TicketEvent.builder()
                            .ticketId(updatedTicket.getId())
                            .action("UPDATE")
                            .title(updatedTicket.getTitle())
                            .status(updatedTicket.getStatus().name())
                            .priority(updatedTicket.getPriority().name())
                            .assignedAgentId(
                                    updatedTicket.getAssignedAgent() != null
                                            ? updatedTicket.getAssignedAgent().getId()
                                            : null
                            )
                            .build()
            );

            TicketResponse response = toResponse(updatedTicket);

            try {
                redisCacheService.save(
                        "ticket:" + response.getId(),
                        response
                );
            } catch (Exception e) {
                log.warn("Failed to update Redis cache");
            }

            return response;

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
            try {
                redisCacheService.delete(
                        "ticket:" + id
                );
            } catch (Exception e) {
                log.warn("Failed to delete Redis cache");
            }
            caffeineCache.invalidate(-1L);
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
