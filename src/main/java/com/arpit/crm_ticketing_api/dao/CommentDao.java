package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentDao {
    private final SessionFactory sessionFactory;

    public Comment save(Comment comment) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(comment);
        return comment;
    }

    public Comment findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Comment.class, id);
    }

    public List<Comment> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Comment", Comment.class).list();
    }
}
