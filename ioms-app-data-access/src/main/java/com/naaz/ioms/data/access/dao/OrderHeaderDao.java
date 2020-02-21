package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.OrdersHeader;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHeaderDao extends AbstractDAO<OrdersHeader> {
    public OrderHeaderDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public OrdersHeader create(final OrdersHeader ordersHeader)
    {
        return persist(ordersHeader);
    }

    public void update(final OrdersHeader ordersHeader)
    {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaUpdate<OrdersHeader> criteria = builder.createCriteriaUpdate(OrdersHeader.class);
        final Root<OrdersHeader> root = criteria.from(OrdersHeader.class);
        criteria.set(root.get("status"), ordersHeader.getStatus());
        criteria.set(root.get("approvedOn"), ordersHeader.getApprovedOn());
        criteria.set(root.get("dispatchedOn"), ordersHeader.getDispatchedOn());
        criteria.where(builder.equal(root.get("id"), ordersHeader.getId()));
        currentSession().createQuery(criteria).executeUpdate();
    }

    public OrdersHeader find(final long id)
    {
        return currentSession().load(OrdersHeader.class, id);
    }

    public void delete(final long id) {
        final OrdersHeader ordersHeader = currentSession().load(OrdersHeader.class, id);
        currentSession().delete(ordersHeader);
    }

    public List<OrdersHeader> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<OrdersHeader> criteria = builder.createQuery(OrdersHeader.class);
        final Root<OrdersHeader> root = criteria.from(OrdersHeader.class);
        criteria.select(root);
        final List<OrdersHeader> ordersHeader = currentSession().createQuery(criteria).getResultList();
        return ordersHeader;
    }

    public List<OrdersHeader> fetchOrdersForSalesman(Long userId) {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<OrdersHeader> criteriaQuery = criteriaBuilder.createQuery(OrdersHeader.class);
        final Root<OrdersHeader> root = criteriaQuery.from(OrdersHeader.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();

        final Predicate userPredicate = criteriaBuilder.equal(root.get("created_by"), userId);
        predicateList.add(userPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<OrdersHeader> ordersHeader = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(ordersHeader.isEmpty()) {
            return Collections.emptyList();
        }

        return ordersHeader;
    }

    public List<OrdersHeader> fetchOrdersForAStatus(String orderStatus) {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<OrdersHeader> criteriaQuery = criteriaBuilder.createQuery(OrdersHeader.class);
        final Root<OrdersHeader> root = criteriaQuery.from(OrdersHeader.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), orderStatus);
        predicateList.add(statusPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<OrdersHeader> ordersHeader = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(ordersHeader.isEmpty()) {
            return Collections.emptyList();
        }

        return ordersHeader;
    }
}
