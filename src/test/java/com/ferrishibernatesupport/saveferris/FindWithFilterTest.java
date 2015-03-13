/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris;

import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.domain.Projections;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.dao.PersonRepository;
import com.ferrishibernatesupport.saveferris.model.BaseModel;
import com.ferrishibernatesupport.saveferris.model.Person;
import org.hibernate.criterion.MatchMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 *
 * @author Uira Dias.
 *         Created 27/03/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/test-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FindWithFilterTest {

	private Person ferris;
	
	private Person cameron;
	
	private Select select;
	
	private List selectResult;
	
	private List<Person> persons;
	
	@Autowired
	private PersonRepository personRepository;
	
	@Test
    @Rollback(true)
    @Transactional
	public void it_should_save_ferris(){
		given_ferris();
		when_try_to_save_him();
		then_ferris_should_be_saved();
	}
	
	@Test
    @Rollback(true)
    @Transactional
	public void it_should_update_ferris(){
		given_ferris();
		when_try_to_change_his_name();
		then_ferris_should_have_its_name_changed();
	}
	
	@Test
    @Rollback(true)
    @Transactional
	public void it_should_filter_by_name(){
		given_persons_persisted();
		when_try_to_find_ferris_by_name();
		then_ferris_must_appear();
	}

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_filter_by_name_and_age(){
        given_persons_persisted();
        when_try_to_find_ferris_by_name_and_age();
        then_ferris_must_appear();
    }

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_filter_by_name_and_project_age_and_name(){
        given_persons_persisted();
        when_try_to_find_ferris_by_name_and_project_by_name_and_age();
        then_ferris_must_appear_just_with_name_and_age();
    }

	private void given_persons_persisted() {
		given_ferris();
		given_cameron();

		personRepository.save(ferris);
		personRepository.save(cameron);
        personRepository.getSession().flush();
	}

	private void given_ferris() {
		ferris = new Person();
		ferris.setName("Ferris Bueller");
        ferris.setAge(19);
        ferris.setEmail("ferrisbueller@gmail.com");
	}

	private void given_cameron() {
		cameron = new Person();
		cameron.setName("Cameron");
        cameron.setAge(21);
        cameron.setEmail("cameron.ferrari@gmail.com");
	}

	private void when_try_to_save_him() {
		personRepository.save(ferris);
	}
	
	private void when_try_to_change_his_name() {
		personRepository.save(ferris);
		
		ferris.setName("Ferris Buller Da Silva Dias");
		personRepository.update(ferris);
	}
	
	private void when_try_to_find_ferris_by_name() {
		this.select = new Select().add(new Filter("name", "Ferris", MatchMode.ANYWHERE));
		this.selectResult = personRepository.findBy(this.select);
		this.persons = (List<Person>) selectResult;
	}

    private void when_try_to_find_ferris_by_name_and_age() {
        this.select = new Select().add(new Filter("name", "Ferris", MatchMode.ANYWHERE)).add(new Filter("age", 19));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void when_try_to_find_ferris_by_name_and_project_by_name_and_age() {
        this.select = new Select().project(Projections.add("name","age")).add(new Filter("name", "Ferris", MatchMode.ANYWHERE)).add(new Filter("age", 19));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

	private void then_ferris_should_be_saved(){
		BaseModel persistedFerris = personRepository.findByID(ferris.getId());
		assertEquals(persistedFerris, ferris);
	}
	
	private void then_ferris_should_have_its_name_changed() {
		Person persistedFerris = (Person) personRepository.findByID(ferris.getId());
		assertEquals(persistedFerris.getName(), ferris.getName());
	}
	
	private void then_ferris_must_appear() {
		assertThat(persons, hasItems(ferris));
		assertThat(persons, not(hasItems(cameron)));
	}

    private void then_ferris_must_appear_just_with_name_and_age() {
        assertThat(persons.get(0).getName(), equalTo(ferris.getName()));
        assertThat(persons.get(0).getAge(), equalTo(ferris.getAge()));
        assertNull(persons.get(0).getEmail());
    }

}
