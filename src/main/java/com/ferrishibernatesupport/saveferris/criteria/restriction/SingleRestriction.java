/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.criteria.restriction;

import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.restriction.builder.SingleRestrictionsBuilder;
import org.hibernate.Criteria;
import org.springframework.stereotype.Service;

/**
 * @author Uira Dias.
 *         Created 04/04/2014.
 */
@Service
public class SingleRestriction {

	public void add(Criteria criteria, Filter filter){
		criteria.add(new SingleRestrictionsBuilder(filter).build());
	}
	
}
