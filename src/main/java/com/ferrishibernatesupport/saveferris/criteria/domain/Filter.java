/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.criteria.domain;


import org.hibernate.criterion.MatchMode;

/**
 * @author Uira Dias.
 *         Created 04/04/2014.
 */
public class Filter {

	private String attribute;
	
	private Object value;
	
	private Condition condition;

    private MatchMode matchMode;
	
	public static enum Condition {
		EQUALS, NOT_EQUALS,
		GREATER, GREATER_OR_EQUALS, LESS, LESS_OR_EQUALS, 
		NULL, NOT_NULL, 
		BETWEEN, OR, AND, IN, NOT_IN, 
		EQ_PROPERTY, LESS_THAN_PROPERTY, LESS_OR_EQUALS_THAN_PROPERTY,
		GREATER_THAN_PROPERTY, GREATER_OR_EQUALS_THAN_PROPERTY;
	}
	
	/**
	 * @param attribute
	 * @param value
	 * @param matchMode
	 */
	public Filter(String attribute, Object value, MatchMode matchMode) {
		this.attribute = attribute;
		this.value = value;
		this.matchMode = matchMode;
	}

    public Filter(String attribute, Condition condition) {
        this.attribute = attribute;
        this.condition = condition;
    }

    public Filter(String attribute, Object value, Condition condition) {
        this.attribute = attribute;
        this.value = value;
        this.condition = condition;
    }

    public Filter(String attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    public static Filter like(String attribute, String value, MatchMode matchMode){
        return new Filter(attribute, value, matchMode);
    }

    public static Filter eq(String attribute, Object value){
        return new Filter(attribute, value);
    }

    public static Filter neq(String attribute, Object value){
        return new Filter(attribute, value, Condition.NOT_EQUALS);
    }

    public static Filter in(String attribute, Object[] value){
        return new Filter(attribute, value);
    }

    public static Filter isNull(String attribute){
        return new Filter(attribute, Condition.NULL);
    }

    public static Filter isNotNull(String attribute){
        return new Filter(attribute, Condition.NOT_NULL);
    }

    public static Filter le(String attribute, Object value){
        return new Filter(attribute, value, Condition.LESS_OR_EQUALS);
    }

    public static Filter lt(String attribute, Object value){
        return new Filter(attribute, value, Condition.LESS);
    }

    public static Filter gt(String attribute, Object value){
        return new Filter(attribute, value, Condition.GREATER);
    }

    public static Filter gte(String attribute, Object value){
        return new Filter(attribute, value, Condition.GREATER_OR_EQUALS);
    }

	public Condition getCondition() {
		return this.condition;
	}

	public String getAttribute() {
		return this.attribute;
	}

	public Object getValue() {
		return this.value;
	}

    public MatchMode getMatchMode() {
        return matchMode;
    }
}
