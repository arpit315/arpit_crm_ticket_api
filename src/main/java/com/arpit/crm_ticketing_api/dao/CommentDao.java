package com.arpit.crm_ticketing_api.dao;

import com.arpit.crm_ticketing_api.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentDao {
    private final SessionFactory sessionFactory;

    public Comment save(Comment comment) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.persist(comment);
            tx.commit();
            return comment;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to save comment", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Comment findById(Long id) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Comment comment = session.get(Comment.class, id);
            tx.commit();
            return comment;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to fetch comment with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Comment> findAll() {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            List<Comment> comments = session.createQuery("from Comment", Comment.class).list();
            tx.commit();
            return comments;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to fetch comments", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public Comment update(Comment comment) {
        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Comment merged = (Comment) session.merge(comment);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to update comment with id: " + comment.getId(), e);
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
            Comment comment = session.get(Comment.class, id);
            if (comment != null) {
                session.remove(comment);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Failed to delete comment with id: " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
