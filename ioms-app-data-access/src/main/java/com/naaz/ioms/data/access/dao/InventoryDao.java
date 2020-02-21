package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.Inventory;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryDao extends AbstractDAO<Inventory> {
    public InventoryDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Inventory create(final Inventory inventory)
    {
        return persist(inventory);
    }

    public void update(final Inventory inventory)
    {
        currentSession().saveOrUpdate(inventory);
    }

    public Inventory find(final long id)
    {
        return currentSession().load(Inventory.class, id);
    }

    public void delete(final long id) {
        final Inventory inventory = currentSession().load(Inventory.class, id);
        currentSession().delete(inventory);
    }

    public List<Inventory> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Inventory> criteria = builder.createQuery(Inventory.class);
        final Root<Inventory> root = criteria.from(Inventory.class);
        criteria.select(root);
        final List<Inventory> inventory = currentSession().createQuery(criteria).getResultList();
        return inventory;
    }

    public List<Inventory> fetchAllActiveInventory() {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Inventory> criteriaQuery = criteriaBuilder.createQuery(Inventory.class);
        final Root<Inventory> root = criteriaQuery.from(Inventory.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), true);
        predicateList.add(statusPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<Inventory> inventory = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(inventory.isEmpty()) {
            return Collections.emptyList();
        }

        return inventory;
    }
}
