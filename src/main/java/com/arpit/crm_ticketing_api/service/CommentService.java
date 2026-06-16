package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dao.CommentDao;
import com.arpit.crm_ticketing_api.dao.TicketDao;
import com.arpit.crm_ticketing_api.dto.CommentRequest;
import com.arpit.crm_ticketing_api.dto.CommentResponse;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.entity.Comment;
import com.arpit.crm_ticketing_api.entity.Ticket;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentDao commentDao;
    private final TicketDao ticketDao;
    private final AgentDao agentDao;

    public CommentResponse create(CommentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Comment request must not be null");
        }
        if (request.getTicketId() == null || request.getTicketId() <= 0) {
            throw new IllegalArgumentException("Ticket id must be a positive number");
        }
        if (request.getAgentId() == null || request.getAgentId() <= 0) {
            throw new IllegalArgumentException("Agent id must be a positive number");
        }

        try {
            log.info("Creating comment for ticketId={} by agentId={}", request.getTicketId(), request.getAgentId());
            Ticket ticket = ticketDao.findById(request.getTicketId());
            if (ticket == null) {
                log.warn("Comment creation failed because ticket {} was not found", request.getTicketId());
                throw new ResourceNotFoundException("Ticket not found with id: " + request.getTicketId());
            }
            Agent agent = agentDao.findById(request.getAgentId());
            if (agent == null) {
                log.warn("Comment creation failed because agent {} was not found", request.getAgentId());
                throw new ResourceNotFoundException("Agent not found with id: " + request.getAgentId());
            }
            Comment comment = new Comment();
            comment.setMessage(request.getMessage());
            comment.setTicket(ticket);
            comment.setAgent(agent);
            return toResponse(commentDao.save(comment));
        } catch (ResourceNotFoundException e) {
            log.error("Comment creation failed for ticketId={} and agentId={}", request.getTicketId(), request.getAgentId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to create comment", e);
            throw e;
        }
    }

    public List<CommentResponse> findAll() {
        try {
            log.info("Fetching all comments");
            return commentDao.findAll().stream().map(this::toResponse).toList();
        } catch (Exception e) {
            log.error("Failed to fetch all comments", e);
            throw e;
        }
    }

    public CommentResponse findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Comment id must be a positive number");
        }

        try {
            Comment comment = commentDao.findById(id);
            if (comment == null) {
                throw new ResourceNotFoundException("Comment not found with id: " + id);
            }
            log.info("Fetched comment with id={}", id);
            return toResponse(comment);
        } catch (ResourceNotFoundException e) {
            log.error("Comment not found with id={}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch comment with id={}", id, e);
            throw e;
        }
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getMessage(), comment.getCreatedAt(),
                comment.getTicket().getId(), comment.getAgent().getId());
    }
}
