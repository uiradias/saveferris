/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.dao.impl;

import com.ferrishibernatesupport.saveferris.dao.PersonRepository;
import com.ferrishibernatesupport.saveferris.model.Person;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Uira Dias.
 *         Created 27/03/2014.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class DefaultPersonRepository extends DefaultRepository<Person> implements PersonRepository {
    @Override
    public Class<?> getBaseClass() {
        return Person.class;
    }
}