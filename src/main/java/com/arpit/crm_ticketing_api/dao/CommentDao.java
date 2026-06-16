package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Comment;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends GenericDaoImpl<Comment, Long> {
    public CommentDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
