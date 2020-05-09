package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.Product;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDao extends AbstractDAO<Product> {
    public ProductDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Product create(final Product product)
    {
        return persist(product);
    }

    public void update(final Product product)
    {
        currentSession().saveOrUpdate(product);
    }

    public Product find(final long id)
    {
        return currentSession().load(Product.class, id);
    }

    public void delete(final long id) {
        final Product product = currentSession().load(Product.class, id);
        currentSession().delete(product);
    }

    public List<Product> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
        final Root<Product> root = criteria.from(Product.class);
        criteria.select(root);
        final List<Product> product = currentSession().createQuery(criteria).getResultList();
        return product;
    }

    public List<Product> fetchAllActiveProduct() {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        final Root<Product> root = criteriaQuery.from(Product.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), true);
        predicateList.add(statusPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<Product> product = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(product.isEmpty()) {
            return Collections.emptyList();
        }

        return product;
    }
}
