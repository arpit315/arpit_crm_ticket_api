package com.arpit.crm_ticketing_api.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@RequiredArgsConstructor
public abstract class GenericDaoImpl<T, ID> implements GenericDao<T, ID> {

    protected final SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    protected Class<T> entityClass() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            return (Class<T>) parameterizedType.getActualTypeArguments()[0];
        }
        throw new IllegalStateException("Unable to resolve entity type for generic DAO");
    }

    protected Session openSession() {
        return sessionFactory.openSession();
    }

    protected Transaction beginTransaction(Session session) {
        return session.beginTransaction();
    }

    @Override
    public T save(T entity) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = beginTransaction(session);
            session.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to save " + entityClass().getSimpleName().toLowerCase(), e);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public T findById(ID id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = beginTransaction(session);
            T entity = session.get(entityClass(), id);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to fetch " + entityClass().getSimpleName().toLowerCase() + " with id: " + id, e);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<T> findAll() {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = beginTransaction(session);
            List<T> entities = session.createQuery("from " + entityClass().getSimpleName(), entityClass()).list();
            tx.commit();
            return entities;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to fetch " + entityClass().getSimpleName().toLowerCase() + " list", e);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public T update(T entity) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = beginTransaction(session);
            T merged = (T) session.merge(entity);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to update " + entityClass().getSimpleName().toLowerCase(), e);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void delete(ID id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = openSession();
            tx = beginTransaction(session);
            T entity = session.get(entityClass(), id);
            if (entity != null) {
                session.remove(entity);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to delete " + entityClass().getSimpleName().toLowerCase() + " with id: " + id, e);
        } finally {
            if (session != null) session.close();
        }
    }
}
