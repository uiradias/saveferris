package com.ferrishibernatesupport.saveferris;

import com.ferrishibernatesupport.saveferris.criteria.domain.Expressions;
import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.dao.PersonRepository;
import com.ferrishibernatesupport.saveferris.model.Person;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
import static org.junit.Assert.assertThat;

/**
 * Created by uiradias on 23/04/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/test-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FindWithExpressionsTest {

    private Select select;

    private Person ferris;

    private Person cameron;

    private Person sloane;

    private List selectResult;

    private List<Person> persons;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_filter_by_and_expression(){
        given_persons_persisted();
        when_select_person_by_name_and_age();
        then_ferris_must_appear();
    }

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_filter_by_or_expressions(){
        given_persons_persisted();
        when_select_person_by_name_or_age();
        then_ferris_and_cameron_must_appear();
    }

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_filter_by_and_or_expressions(){
        given_persons_persisted();
        when_select_person_by_name_and_age_or_gender();
        then_ferris_and_sloane_must_appear();
    }

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_filter_and_project_by_and_or_expressions(){
        given_persons_persisted();
        when_select_and_project_person_by_name_and_age_or_gender();
        then_ferris_and_sloane_must_appear_only_with_name_and_age_filled();
    }

    private void given_ferris() {
        ferris = new Person();
        ferris.setName("Ferris Bueller");
        ferris.setAge(19);
        ferris.setGender(Person.Gender.MALE);
        ferris.setEmail("ferrisbueller@gmail.com");
    }

    private void given_cameron() {
        cameron = new Person();
        cameron.setName("Cameron Frye");
        cameron.setAge(21);
        cameron.setGender(Person.Gender.MALE);
        cameron.setEmail("cameron.ferrari@gmail.com");
    }


    private void given_sloane() {
        sloane = new Person();
        sloane.setName("Sloane Peterson");
        sloane.setAge(17);
        sloane.setGender(Person.Gender.FEMALE);
        sloane.setEmail("hotsloane@gmail.com");
    }

    private void given_persons_persisted() {
        given_ferris();
        given_cameron();
        given_sloane();

        personRepository.save(ferris);
        personRepository.save(cameron);
        personRepository.save(sloane);
        personRepository.getSession().flush();
    }

    private void when_select_person_by_name_and_age() {
        this.select = new Select().where(Expressions.and(new Filter("name", "Ferris", MatchMode.ANYWHERE), new Filter("age", 19)));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }


    private void when_select_person_by_name_or_age() {
        this.select = new Select().where(Expressions.or(Filter.like("name","Ferris",MatchMode.ANYWHERE), Filter.eq("age",21)));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void when_select_person_by_name_and_age_or_gender() {
        this.select = new Select().where(Expressions.or(Expressions.and(new Filter("name", "Ferris", MatchMode.ANYWHERE), new Filter("age", 19)), new Filter("gender", Person.Gender.FEMALE)));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void when_select_and_project_person_by_name_and_age_or_gender() {
        this.select = new Select("name","age").
                where(Expressions.or(Expressions.and(
                                                    Filter.like("name", "Ferris", MatchMode.ANYWHERE),
                                                    Filter.eq("age", 19)
                                                    ),
                               Filter.eq("gender", Person.Gender.FEMALE))
                ).orderBy(Order.asc("name"));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void then_ferris_must_appear() {
        assertThat(persons, hasItems(ferris));
        assertThat(persons, not(hasItems(cameron)));
    }


    private void then_ferris_and_cameron_must_appear() {
        assertThat(persons, hasItems(ferris, cameron));
    }


    private void then_ferris_and_sloane_must_appear() {
        assertThat(persons, hasItems(ferris, sloane));
    }

    private void then_ferris_and_sloane_must_appear_only_with_name_and_age_filled() {
        Person ferris = new Person();
        ferris.setName("Ferris Bueller");
        ferris.setAge(19);

        Person sloane = new Person();
        sloane.setName("Sloane Peterson");
        sloane.setAge(17);

        assertThat(persons.size(), equalTo(2));
        assertThat(persons.get(0), equalTo(ferris));
        assertThat(persons.get(1), equalTo(sloane));
    }
}