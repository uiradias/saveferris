/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.dao;

import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.model.BaseModel;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Uira Dias.
 *         Created 27/03/2014.
 */
public interface GenericRepository<T extends BaseModel> {

	void save(T model);
	
	void update(T model);

    void delete(Serializable id);

    T findByID(Serializable id);

    T findUnique(Select select);

    List<T> findBy(Select select);
	
	Class<?> getBaseClass();

    Session getSession();
}
