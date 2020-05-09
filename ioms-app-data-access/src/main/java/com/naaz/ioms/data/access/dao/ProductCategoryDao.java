package com.naaz.ioms.data.access.dao;

import com.naaz.ioms.data.access.entities.Product;
import com.naaz.ioms.data.access.entities.ProductCategory;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductCategoryDao extends AbstractDAO<ProductCategory> {
    public ProductCategoryDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ProductCategory create(final ProductCategory productCategory)
    {
        return persist(productCategory);
    }

    public void update(final ProductCategory productCategory)
    {
        currentSession().saveOrUpdate(productCategory);
    }

    public ProductCategory find(final long id)
    {
        return currentSession().load(ProductCategory.class, id);
    }

    public void delete(final long id) {
        final ProductCategory productCategory = currentSession().load(ProductCategory.class, id);
        currentSession().delete(productCategory);
    }

    public List<ProductCategory> findAll() {
        final CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<ProductCategory> criteria = builder.createQuery(ProductCategory.class);
        final Root<ProductCategory> root = criteria.from(ProductCategory.class);
        criteria.select(root);
        final List<ProductCategory> productCategory = currentSession().createQuery(criteria).getResultList();
        return productCategory;
    }

    public List<ProductCategory> fetchAllProductCategoryForStatus(Boolean status) {
        final CriteriaBuilder criteriaBuilder = currentSession().getCriteriaBuilder();
        final CriteriaQuery<ProductCategory> criteriaQuery = criteriaBuilder.createQuery(ProductCategory.class);
        final Root<ProductCategory> root = criteriaQuery.from(ProductCategory.class);
        final List<Predicate> predicateList = new ArrayList<Predicate>();
        final Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), status);
        predicateList.add(statusPredicate);
        criteriaQuery.where(predicateList.toArray(new Predicate[]{}));
        final List<ProductCategory> productCategory = currentSession().createQuery(criteriaQuery.select(root)).getResultList();
        if(productCategory.isEmpty()) {
            return Collections.emptyList();
        }

        return productCategory;
    }
}
