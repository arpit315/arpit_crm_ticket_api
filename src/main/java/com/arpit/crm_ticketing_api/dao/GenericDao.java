package com.arpit.crm_ticketing_api.dao;

import java.util.List;

public interface GenericDao<T, ID> {
    T save(T entity);

    T findById(ID id);

    List<T> findAll();

    T update(T entity);

    void delete(ID id);
}
