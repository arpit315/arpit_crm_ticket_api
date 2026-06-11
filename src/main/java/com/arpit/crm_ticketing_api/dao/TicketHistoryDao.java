package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.TicketHistory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketHistoryDao {

    private final SessionFactory sessionFactory;

    public TicketHistory save(TicketHistory history) {

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            session.persist(history);

            tx.commit();

            return history;

        } catch (Exception e) {

            if (tx != null) {
                tx.rollback();
            }

            throw new RuntimeException(
                    "Failed to save ticket history",
                    e
            );

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }
}