/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * @author Uira Dias.
 *         Created 04/04/2014.
 */
@MappedSuperclass
public class BaseModel implements Model{

	/** */
	private static final long serialVersionUID = -2276858973821639226L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTITY_SEQ")
	protected Integer id;
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	protected BaseModel clone() throws CloneNotSupportedException {
		return (BaseModel) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseModel other = (BaseModel) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
	
}
