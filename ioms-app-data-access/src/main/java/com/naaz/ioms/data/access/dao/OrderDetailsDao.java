package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.OrdersDetails;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailsDao  extends AbstractDAO<OrdersDetails> {
    public OrderDetailsDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public OrdersDetails create(final OrdersDetails ordersDetails)
    {
        return persist(ordersDetails);
    }

    public void update(final OrdersDetails ordersDetails)
    {
        currentSession().saveOrUpdate(ordersDetails);
    }

    public OrdersDetails find(final long id)
    {
        return currentSession().load(OrdersDetails.class, id);
    }

    public void delete(final long id) {
        final OrdersDetails ordersDetails = currentSession().load(OrdersDetails.class, id);
        currentSession().delete(ordersDetails);
    }

    public List<OrdersDetails> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<OrdersDetails> criteria = builder.createQuery(OrdersDetails.class);
        final Root<OrdersDetails> root = criteria.from(OrdersDetails.class);
        criteria.select(root);
        final List<OrdersDetails> ordersDetails = currentSession().createQuery(criteria).getResultList();
        return ordersDetails;
    }

    public List<OrdersDetails> fetchOrderDetailsForOrder(final long orderId) {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<OrdersDetails> criteriaQuery = criteriaBuilder.createQuery(OrdersDetails.class);
        final Root<OrdersDetails> root = criteriaQuery.from(OrdersDetails.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate orderPredicate = criteriaBuilder.equal(root.get("orderId"), orderId);
        predicateList.add(orderPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<OrdersDetails> ordersDetails = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(ordersDetails.isEmpty()) {
            return Collections.emptyList();
        }

        return ordersDetails;
    }
}
