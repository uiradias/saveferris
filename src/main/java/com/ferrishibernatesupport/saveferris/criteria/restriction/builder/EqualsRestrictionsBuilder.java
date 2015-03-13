package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by uiradias on 23/04/14.
 */
public class EqualsRestrictionsBuilder {

    public String attribute;

    public EqualsRestrictionsBuilder(String attribute) {
        this.attribute = attribute;
    }

    public Criterion eq(Object value){
        return Restrictions.eq(this.attribute, value);
    }

    public Criterion neq(Object value){
        return Restrictions.ne(this.attribute, value);
    }
}
