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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentDao commentDao;
    private final TicketDao ticketDao;
    private final AgentDao agentDao;

    @Transactional
    public CommentResponse create(CommentRequest request) {
        log.info("Creating comment for ticketId={} by agentId={}", request.getTicketId(), request.getAgentId());
        Ticket ticket = ticketDao.findById(request.getTicketId());
        if (ticket == null) {
            throw new ResourceNotFoundException("Ticket not found with id: " + request.getTicketId());
        }
        Agent agent = agentDao.findById(request.getAgentId());
        if (agent == null) {
            throw new ResourceNotFoundException("Agent not found with id: " + request.getAgentId());
        }
        Comment comment = new Comment();
        comment.setMessage(request.getMessage());
        comment.setTicket(ticket);
        comment.setAgent(agent);
        return toResponse(commentDao.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findAll() {
        log.info("Fetching all comments");
        return commentDao.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CommentResponse findById(Long id) {
        Comment comment = commentDao.findById(id);
        if (comment == null) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        log.info("Fetched comment with id={}", id);
        return toResponse(comment);
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getMessage(), comment.getCreatedAt(),
                comment.getTicket().getId(), comment.getAgent().getId());
    }
}
