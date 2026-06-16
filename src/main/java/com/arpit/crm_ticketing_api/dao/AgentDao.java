package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Agent;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class AgentDao extends GenericDaoImpl<Agent, Long> {
    public AgentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
