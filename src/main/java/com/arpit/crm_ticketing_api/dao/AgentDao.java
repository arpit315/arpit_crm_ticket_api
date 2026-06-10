package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Agent;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AgentDao {
    private final SessionFactory sessionFactory;

    public Agent save(Agent agent) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.persist(agent);
            tx.commit();
            return agent;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to save agent", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Agent findById(Long id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Agent agent = session.get(Agent.class, id);
            tx.commit();
            return agent;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to fetch agent with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Agent> findAll() {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            List<Agent> agents = session.createQuery("from Agent", Agent.class).list();
            tx.commit();
            return agents;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to fetch agents", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Agent update(Agent agent) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Agent merged = (Agent) session.merge(agent);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to update agent with id: " + agent.getId(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void delete(Long id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Agent agent = session.get(Agent.class, id);
            if (agent != null) {
                session.remove(agent);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to delete agent with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
