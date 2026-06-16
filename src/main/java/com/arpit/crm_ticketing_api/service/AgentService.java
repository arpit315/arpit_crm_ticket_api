package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.dto.AgentRequest;
import com.arpit.crm_ticketing_api.dto.AgentResponse;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import com.arpit.crm_ticketing_api.cache.LruCache;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {
    private static final Logger log = LoggerFactory.getLogger(AgentService.class);
    private final LruCache<Long, AgentResponse> lruCache;
    private final AgentDao agentDao;

    public AgentResponse create(AgentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Agent request must not be null");
        }

        try {
            log.info("Creating agent with email={}", request.getEmail());
            Agent agent = new Agent();
            agent.setName(request.getName());
            agent.setEmail(request.getEmail());
            agent.setDepartment(request.getDepartment());
            return toResponse(agentDao.save(agent));

        } catch (Exception e) {
            log.error("Failed to create agent", e);
            throw e;
        }
    }

    public List<AgentResponse> findAll() {
        try {
            log.info("Fetching all agents");
            return agentDao.findAll().stream().map(this::toResponse).toList();
        } catch (Exception e) {
            log.error("Failed to fetch all agents", e);
            throw e;
        }
    }

    public AgentResponse findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Agent id must be a positive number");
        }

        try {

            if (lruCache.containsKey(id)) {

                log.info("Returning agent from LRU cache. id={}", id);

                return lruCache.get(id);
            }

            log.info("LRU cache miss. Fetching agent from DB. id={}", id);

            Agent agent = agentDao.findById(id);

            if (agent == null) {
                log.warn("Agent not found with id={}", id);
                throw new ResourceNotFoundException("Agent not found with id: " + id);
            }

            AgentResponse response = toResponse(agent);

            lruCache.put(id, response);

            return response;

        } catch (ResourceNotFoundException e) {

            log.error("Agent not found with id={}", id, e);

            throw e;

        } catch (Exception e) {

            log.error("Failed to fetch agent with id={}", id, e);

            throw e;
        }
    }

    public AgentResponse update(Long id, AgentRequest request) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Agent id must be a positive number");
        }
        if (request == null) {
            throw new IllegalArgumentException("Agent request must not be null");
        }

        try {
            Agent existing = agentDao.findById(id);
            if (existing == null) {
                log.warn("Agent update skipped because id={} was not found", id);
                throw new ResourceNotFoundException("Agent not found with id: " + id);
            }
            existing.setName(request.getName());
            existing.setEmail(request.getEmail());
            existing.setDepartment(request.getDepartment());
            log.info("Updating agent with id={}", id);

            AgentResponse response =
                    toResponse(agentDao.update(existing));

            lruCache.put(id, response);

            return response;
        } catch (ResourceNotFoundException e) {
            log.error("Agent update failed for id={}", id, e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to update agent with id={}", id, e);
            throw e;
        }
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Agent id must be a positive number");
        }

        try {
            log.info("Deleting agent with id={}", id);
            agentDao.delete(id);
            lruCache.remove(id);
        } catch (Exception e) {
            log.error("Failed to delete agent with id={}", id, e);
            throw e;
        }
    }

    private AgentResponse toResponse(Agent agent) {
        return new AgentResponse(agent.getId(), agent.getName(), agent.getEmail(), agent.getDepartment());
    }
}
