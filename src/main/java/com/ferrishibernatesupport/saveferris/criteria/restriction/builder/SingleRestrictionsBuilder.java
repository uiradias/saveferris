/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import org.hibernate.criterion.Criterion;

/**
 * @author Uira Dias
 *         Created 04/04/2014.
 */
public class SingleRestrictionsBuilder {

	Filter filter;

	public SingleRestrictionsBuilder(Filter filter) {
		super();
		this.filter = filter;
	}
	
	public Criterion build(){
		Criterion criterion = null;
        Object value = this.filter.getValue();

        if(withoutDefinedConditions()){
            if(value instanceof String){
                criterion = new LikeRestrictionsBuilder(this.filter.getAttribute()).like((String) this.filter.getValue(), this.filter.getMatchMode());
            }else if(value instanceof Number || value instanceof Enum){
                criterion = new EqualsRestrictionsBuilder(this.filter.getAttribute()).eq(this.filter.getValue());
            }else if(value instanceof Object[]){
                criterion = new InRestrictionsBuilder(this.filter.getAttribute()).in((Object[]) this.filter.getValue());
            }
        }else{
            if(nullCondition()){
                criterion = createNullCondition();
            }else if(lessCondition()){
                criterion = createLessCondition();
            }else if(greaterCondition()){
                criterion = createGreaterCondition();
            }else if(notEqualsCondition()){
                criterion = createNotEqualsCondition();
            }
        }


		return criterion;
	}

    private Criterion createNotEqualsCondition() {
        return new EqualsRestrictionsBuilder(this.filter.getAttribute()).neq(this.filter.getValue());
    }

    private boolean notEqualsCondition() {
        return filter.getCondition().equals(Filter.Condition.NOT_EQUALS);
    }

    private Criterion createGreaterCondition() {
        if(filter.getCondition().equals(Filter.Condition.GREATER)){
            return new GreaterRestrictionsBuilder(this.filter.getAttribute()).gt(this.filter.getValue());
        }else{
            return new GreaterRestrictionsBuilder(this.filter.getAttribute()).gte(this.filter.getValue());
        }
    }

    private boolean greaterCondition() {
        return filter.getCondition().equals(Filter.Condition.GREATER) || filter.getCondition().equals(Filter.Condition.GREATER_OR_EQUALS);
    }

    private Criterion createLessCondition() {
        if(filter.getCondition().equals(Filter.Condition.LESS)){
            return new LessRestrictionsBuilder(this.filter.getAttribute()).lt(this.filter.getValue());
        }else{
            return new LessRestrictionsBuilder(this.filter.getAttribute()).lte(this.filter.getValue());
        }
    }

    private boolean lessCondition() {
        return filter.getCondition().equals(Filter.Condition.LESS) || filter.getCondition().equals(Filter.Condition.LESS_OR_EQUALS);
    }

    private Criterion createNullCondition() {
        if(filter.getCondition().equals(Filter.Condition.NULL)){
            return new NullRestrictionsBuilder(this.filter.getAttribute()).isNull();
        }else{
            return new NullRestrictionsBuilder(this.filter.getAttribute()).isNotNull();
        }
    }

    private boolean nullCondition() {
        return filter.getCondition().equals(Filter.Condition.NULL) || filter.getCondition().equals(Filter.Condition.NOT_NULL);
    }

    private boolean withoutDefinedConditions() {
        return filter.getCondition() == null;
    }
}
