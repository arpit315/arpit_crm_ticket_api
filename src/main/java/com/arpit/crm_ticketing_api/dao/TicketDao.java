package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketDao {
    private final SessionFactory sessionFactory;

    public Ticket save(Ticket ticket) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(ticket);
        return ticket;
    }

    public Ticket findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Ticket.class, id);
    }

    public List<Ticket> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Ticket", Ticket.class).list();
    }

    public Ticket update(Ticket ticket) {
        Session session = sessionFactory.getCurrentSession();
        return (Ticket) session.merge(ticket);
    }

    public void delete(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Ticket ticket = session.get(Ticket.class, id);
        if (ticket != null) {
            session.remove(ticket);
        }
    }
}
