/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.model;

import javax.persistence.*;
import java.util.List;


/**
 *
 * @author Uira Dias.
 *         Created 27/03/2014.
 */
@Entity(name="Person")
@SequenceGenerator(name = "ENTITY_SEQ", sequenceName = "PERSON_SEQ")
public class Person extends BaseModel{

    public enum Gender{
        MALE, FEMALE;
    }

	/** */
	private static final long serialVersionUID = -6842245225252744991L;
	
	@Column
	private String name;

    @Column
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "securitysocialid")
    private SecuritySocial securitySocial;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Address> addresses;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public SecuritySocial getSecuritySocial() {
        return securitySocial;
    }

    public void setSecuritySocial(SecuritySocial securitySocial) {
        this.securitySocial = securitySocial;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
