package com.arpit.crm_ticketing_api.service;

import com.arpit.crm_ticketing_api.cache.LruCache;
import com.arpit.crm_ticketing_api.dao.AgentDao;
import com.arpit.crm_ticketing_api.entity.Agent;
import com.arpit.crm_ticketing_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {

    private static final Logger log =
            LoggerFactory.getLogger(AgentService.class);

    private final LruCache<Long, Agent> lruCache;
    private final AgentDao agentDao;

    public Agent create(Agent agent) {

        if (agent == null) {
            throw new IllegalArgumentException(
                    "Agent must not be null"
            );
        }

        return agentDao.save(agent);
    }

    public List<Agent> findAll() {

        return agentDao.findAll();
    }

    public Agent findById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Agent id must be positive"
            );
        }

        if (lruCache.containsKey(id)) {
            return lruCache.get(id);
        }

        Agent agent =
                agentDao.findById(id);

        if (agent == null) {
            throw new ResourceNotFoundException(
                    "Agent not found with id: " + id
            );
        }

        lruCache.put(id, agent);

        return agent;
    }

    public Agent update(Long id, Agent agent) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Agent id must be positive"
            );
        }

        if (agent == null) {
            throw new IllegalArgumentException(
                    "Agent must not be null"
            );
        }

        Agent existing =
                agentDao.findById(id);

        if (existing == null) {
            throw new ResourceNotFoundException(
                    "Agent not found with id: " + id
            );
        }

        existing.setName(agent.getName());
        existing.setEmail(agent.getEmail());
        existing.setDepartment(agent.getDepartment());

        Agent updatedAgent =
                agentDao.update(existing);

        lruCache.put(id, updatedAgent);

        return updatedAgent;
    }

    public void delete(Long id) {

        agentDao.delete(id);

        lruCache.remove(id);
    }
}