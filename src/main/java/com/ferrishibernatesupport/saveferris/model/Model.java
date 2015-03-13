/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.model;

import java.io.Serializable;

/**
 * @author uiradias.
 *         Created 04/04/2014.
 */
public interface Model extends Cloneable, Serializable{

	Integer getId();

	void setId(Integer id);
	
}
