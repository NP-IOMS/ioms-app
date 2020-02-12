package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.UserRole;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserRoleDao  extends AbstractDAO<UserRole> {
    public UserRoleDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public UserRole create(final UserRole userRole)
    {
        return persist(userRole);
    }

    public void update(final UserRole userRole)
    {
        currentSession().saveOrUpdate(userRole);
    }

    public UserRole find(final long id)
    {
        return currentSession().load(UserRole.class, id);
    }

    public void delete(final long id)
    {
        final UserRole userRole = currentSession().load(UserRole.class, id);
        currentSession().delete(userRole);
    }

    public List<UserRole> findAll()
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<UserRole> criteria = builder.createQuery(UserRole.class);
        final Root<UserRole> root = criteria.from(UserRole.class);
        criteria.select(root);
        final List<UserRole> userRoles = currentSession().createQuery(criteria).getResultList();
        return userRoles;
    }
}
