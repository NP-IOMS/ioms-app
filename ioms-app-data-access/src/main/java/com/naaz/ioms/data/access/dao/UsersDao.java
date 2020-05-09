package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.UserRole;
import com.naaz.ioms.data.access.entities.Users;
import io.dropwizard.hibernate.AbstractDAO;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersDao  extends AbstractDAO<Users> {
    public UsersDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Users create(final Users users)
    {
        return persist(users);
    }

    public void update(final Users users)
    {
        currentSession().saveOrUpdate(users);
    }

    public Users find(final long id)
    {
        return currentSession().load(Users.class, id);
    }

    public void delete(final long id) {
        final Users users = currentSession().load(Users.class, id);
        currentSession().delete(users);
    }

    public List<Users> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Users> criteria = builder.createQuery(Users.class);
        final Root<Users> root = criteria.from(Users.class);
        criteria.select(root);
        final List<Users> users = currentSession().createQuery(criteria).getResultList();
        return users;
    }

    public List<Users> findAllUserByRole(final long userRoleId) {
        final UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Users> criteriaQuery = criteriaBuilder.createQuery(Users.class);
        final Root<Users> root = criteriaQuery.from(Users.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate userRolePredicate = criteriaBuilder.equal(root.get("userRole"), userRole);
        predicateList.add(userRolePredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<Users> users = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(users.isEmpty()) {
            return Collections.emptyList();
        }

        return users;
    }

    public List<Users> findActiveUser(final Users user) {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Users> criteriaQuery = criteriaBuilder.createQuery(Users.class);
        final Root<Users> root = criteriaQuery.from(Users.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate userNamePredicate = criteriaBuilder.equal(root.get("userName"), user.getUserName());
        final Predicate userPassPredicate = criteriaBuilder.equal(root.get("userPass"), user.getUserPass());
        predicateList.add(userNamePredicate);
        predicateList.add(userPassPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<Users> users = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(users.isEmpty()) {
            return Collections.emptyList();
        }

        return users;
    }
}
