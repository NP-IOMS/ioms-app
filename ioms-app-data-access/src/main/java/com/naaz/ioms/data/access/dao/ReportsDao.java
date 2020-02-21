package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.OrdersHeader;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

public class ReportsDao extends AbstractDAO<OrdersHeader> {
    public ReportsDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<OrdersHeader> getOrderForADurationAndStatus(Date startDate, Date endDate, String orderStatus) {

        final List<OrdersHeader> ordersHeader =  currentSession()
                .createQuery("FROM OrdersHeader AS c WHERE status=:status AND c.dispatchedOn BETWEEN :stDate AND :edDate")
                .setParameter("status", orderStatus)
                .setParameter("stDate", startDate)
                .setParameter("edDate", endDate)
                .list();

        return ordersHeader;
    }
}
