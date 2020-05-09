package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.Files;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

public class FilesDao extends AbstractDAO<Files> {
    public FilesDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Files create(final Files files) {
        return persist(files);
    }

    public void update(final Files files) {
        currentSession().saveOrUpdate(files);
    }

    public Files find(final long id) {
        return currentSession().load(Files.class, id);
    }

    public void delete(final long id) {
        final Files files = currentSession().load(Files.class, id);
        currentSession().delete(files);
    }
}
