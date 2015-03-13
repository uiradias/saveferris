/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.dao.impl;

import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.criteria.projection.ProjectionsCriteriaList;
import com.ferrishibernatesupport.saveferris.criteria.restriction.RestrictionsCriteriaManager;
import com.ferrishibernatesupport.saveferris.dao.GenericRepository;
import com.ferrishibernatesupport.saveferris.exception.NotUniqueResultException;
import com.ferrishibernatesupport.saveferris.model.BaseModel;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Uira Dias. 
 * 		Created 27/03/2014.
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public abstract class DefaultRepository<T extends BaseModel> implements GenericRepository<T> {

    @Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private RestrictionsCriteriaManager restrictionsCriteriaManager;

    @Autowired
    private ProjectionsCriteriaList projectionsCriteriaList;
	
	@Override
	public void save(T model) {
		this.getSession().save(model);
	}

	@Override
	public void update(T model) {
		this.getSession().update(model);
	}

    @Override
    public void delete(Serializable id) {
        T baseModel = findByID(id);
        this.getSession().delete(baseModel);
    }

    @SuppressWarnings("unchecked")
    @Override
	public T findByID(Serializable id) {
		return (T) this.getSession().get(getBaseClass(), id);
	}

    @Override
    public T findUnique(Select select) {
        List<T> list = findBy(select);

        if (list == null || list.isEmpty()) {
            return null;
        }

        if (list.size() > 1) {
            throw new NotUniqueResultException("Not unique result for select: " + select);
        }

        return list.get(0);

    }
	
	@SuppressWarnings("unchecked")
    @Override
	public List<T> findBy(Select select) {
		Criteria criteria = createCriteria();
        restrictionsCriteriaManager.addCriteria(criteria, select);
        projectionsCriteriaList.project(criteria, select.getProjections(), getBaseClass());
		return criteria.list();
	}

	private Criteria createCriteria() {
		return getSession().createCriteria(getBaseClass());
	}

    @Override
	public Session getSession() {
        Session currentSession = this.sessionFactory.getCurrentSession();
        currentSession.setFlushMode(FlushMode.COMMIT);
        return currentSession;
	}
	
}