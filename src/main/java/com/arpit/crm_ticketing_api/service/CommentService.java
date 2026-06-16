package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dao.CommentDao;
import com.arpit.crm_ticketing_api.dao.TicketDao;
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

    private static final Logger log =
            LoggerFactory.getLogger(CommentService.class);

    private final CommentDao commentDao;
    private final TicketDao ticketDao;
    private final AgentDao agentDao;

    public Comment create(Comment comment) {

        if (comment == null) {
            throw new IllegalArgumentException(
                    "Comment must not be null"
            );
        }

        if (comment.getTicket() == null ||
                comment.getTicket().getId() == null) {

            throw new IllegalArgumentException(
                    "Ticket id is required"
            );
        }

        if (comment.getAgent() == null ||
                comment.getAgent().getId() == null) {

            throw new IllegalArgumentException(
                    "Agent id is required"
            );
        }

        Ticket ticket =
                ticketDao.findById(
                        comment.getTicket().getId()
                );

        if (ticket == null) {
            throw new ResourceNotFoundException(
                    "Ticket not found with id: "
                            + comment.getTicket().getId()
            );
        }

        Agent agent =
                agentDao.findById(
                        comment.getAgent().getId()
                );

        if (agent == null) {
            throw new ResourceNotFoundException(
                    "Agent not found with id: "
                            + comment.getAgent().getId()
            );
        }

        comment.setTicket(ticket);
        comment.setAgent(agent);

        return commentDao.save(comment);
    }

    public List<Comment> findAll() {

        return commentDao.findAll();
    }

    public Comment findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Comment id must be positive"
            );
        }

        Comment comment =
                commentDao.findById(id);

        if (comment == null) {
            throw new ResourceNotFoundException(
                    "Comment not found with id: " + id
            );
        }

        return comment;
    }
}