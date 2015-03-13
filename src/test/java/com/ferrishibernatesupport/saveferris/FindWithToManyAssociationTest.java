package com.ferrishibernatesupport.saveferris;

import com.ferrishibernatesupport.saveferris.criteria.domain.Expressions;
import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.dao.PersonRepository;
import com.ferrishibernatesupport.saveferris.model.Address;
import com.ferrishibernatesupport.saveferris.model.Person;
import com.ferrishibernatesupport.saveferris.model.SecuritySocial;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by uiradias on 03/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/test-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FindWithToManyAssociationTest {

    private Select select;

    private Person ferris;

    private Person cameron;

    private Person sloane;

    private SecuritySocial ferrisSecuritySocial;

    private Address address;

    private List<Address> addresses = new ArrayList<>();

    private List selectResult;

    private List<Person> persons;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_find_person_by_address(){
        given_persons_persisted();
        when_select_person_by_address_description();
        then_ferris_must_appear();
    }

    @Test
    @Rollback(true)
    @Transactional
    public void it_should_find_person_by_address_projecting_id_name_and_address(){
        given_persons_persisted();
        when_select_and_project_person_by_id_name_and_address();
        then_ferris_and_sloane_must_appear_only_with_name_and_age_filled();
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

    private void given_ferris() {
        ferris = new Person();
        ferris.setName("Ferris Bueller");
        ferris.setAge(19);
        ferris.setGender(Person.Gender.MALE);
        ferris.setEmail("ferrisbueller@gmail.com");

        ferrisSecuritySocial = new SecuritySocial();
        ferrisSecuritySocial.setNumber("023-987-233");
        ferrisSecuritySocial.setExpedition(new Date());

        ferris.setSecuritySocial(ferrisSecuritySocial);

        address = new Address();
        address.setType(Address.Type.AVENUE);
        address.setDescription("Test");
        address.setNumber(1234);
        address.setZipCode("60170-040");
        address.setPerson(ferris);
        addresses.add(address);

        ferris.setAddresses(addresses);
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

    private void when_select_person_by_address_description() {
        this.select = new Select().add(Filter.like("addresses.description", "Test", MatchMode.ANYWHERE));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void when_select_and_project_person_by_id_name_and_address() {
        this.select = new Select("id", "name","addresses.description").
                where(Expressions.and(
                                Filter.like("addresses.description", "Test", MatchMode.ANYWHERE),
                                Filter.eq("age", 19)
                        )
                ).orderBy(Order.asc("name"));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void then_ferris_must_appear() {
        assertThat(persons, hasItems(ferris));
        assertThat(persons, not(hasItems(cameron)));
    }

    private void then_ferris_and_sloane_must_appear_only_with_name_and_age_filled() {
        assertThat(persons.size(), equalTo(1));
        assertThat(persons.get(0).getName(), equalTo("Ferris Bueller"));
        assertThat(persons.get(0).getAddresses().size(), equalTo(1));
    }

}
