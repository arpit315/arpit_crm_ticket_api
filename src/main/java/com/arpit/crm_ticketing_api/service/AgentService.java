package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dto.AgentRequest;
import com.arpit.crm_ticketing_api.dto.AgentResponse;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {
    private static final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final AgentDao agentDao;

    @Transactional
    public AgentResponse create(AgentRequest request) {
        log.info("Creating agent with email={}", request.getEmail());
        Agent agent = new Agent();
        agent.setName(request.getName());
        agent.setEmail(request.getEmail());
        agent.setDepartment(request.getDepartment());
        return toResponse(agentDao.save(agent));
    }

    @Transactional(readOnly = true)
    public List<AgentResponse> findAll() {
        log.info("Fetching all agents");
        return agentDao.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AgentResponse findById(Long id) {
        Agent agent = agentDao.findById(id);
        if (agent == null) {
            throw new ResourceNotFoundException("Agent not found with id: " + id);
        }
        log.info("Fetched agent with id={}", id);
        return toResponse(agent);
    }

    @Transactional
    public AgentResponse update(Long id, AgentRequest request) {
        Agent existing = agentDao.findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Agent not found with id: " + id);
        }
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setDepartment(request.getDepartment());
        log.info("Updating agent with id={}", id);
        return toResponse(agentDao.update(existing));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting agent with id={}", id);
        agentDao.delete(id);
    }

    private AgentResponse toResponse(Agent agent) {
        return new AgentResponse(agent.getId(), agent.getName(), agent.getEmail(), agent.getDepartment());
    }
}
