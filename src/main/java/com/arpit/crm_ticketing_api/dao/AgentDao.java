package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Agent;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AgentDao {
    private final SessionFactory sessionFactory;

    public Agent save(Agent agent) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(agent);
        return agent;
    }

    public Agent findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Agent.class, id);
    }

    public List<Agent> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Agent", Agent.class).list();
    }

    public Agent update(Agent agent) {
        Session session = sessionFactory.getCurrentSession();
        return (Agent) session.merge(agent);
    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Agent agent = session.get(Agent.class, id);
        if (agent != null) {
            session.remove(agent);
        }
    }
}
