package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketDao {
    private final SessionFactory sessionFactory;

    public Ticket save(Ticket ticket) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.persist(ticket);
            tx.commit();
            return ticket;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to save ticket", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Ticket findById(Long id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Ticket ticket = session.get(Ticket.class, id);
            tx.commit();
            return ticket;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to fetch ticket with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Ticket> findAll() {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            List<Ticket> tickets = session.createQuery("from Ticket", Ticket.class).list();
            tx.commit();
            return tickets;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to fetch tickets", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Ticket update(Ticket ticket) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Ticket merged = (Ticket) session.merge(ticket);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to update ticket with id: " + ticket.getId(), e);
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
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket != null) {
                session.remove(ticket);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to delete ticket with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
