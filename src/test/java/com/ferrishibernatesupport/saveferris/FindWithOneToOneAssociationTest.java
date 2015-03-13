package com.ferrishibernatesupport.saveferris;

import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.dao.PersonRepository;
import com.ferrishibernatesupport.saveferris.model.Person;
import com.ferrishibernatesupport.saveferris.model.SecuritySocial;
import org.hibernate.criterion.MatchMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by uiradias on 31/08/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/test-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class FindWithOneToOneAssociationTest {

    private Select select;

    private Person ferris;

    private Person cameron;

    private Person sloane;

    private SecuritySocial ferrisSecuritySocial;

    private List selectResult;

    private List<Person> persons;

    @Autowired
    private PersonRepository personRepository;

    @Test
    @Rollback(true)
    @Transactional
    public void ir_should_find_person_by_social_security_number(){
        given_persons_persisted();
        when_select_person_by_social_security_number();
        then_ferris_must_appear();
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


    private void when_select_person_by_social_security_number() {
        this.select = new Select().add(Filter.like("securitySocial.number", "023-987", MatchMode.ANYWHERE));
        this.selectResult = personRepository.findBy(this.select);
        this.persons = (List<Person>) selectResult;
    }

    private void then_ferris_must_appear() {
        assertThat(persons, hasItems(ferris));
        assertThat(persons, not(hasItems(cameron)));
    }

}
