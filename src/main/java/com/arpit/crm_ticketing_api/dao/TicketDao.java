package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Ticket;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TicketDao extends GenericDaoImpl<Ticket, Long> {
    public TicketDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
