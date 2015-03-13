package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by uiradias on 07/09/14.
 */
public class GreaterRestrictionsBuilder {

    public String attribute;

    public GreaterRestrictionsBuilder(String attribute) {
        this.attribute = attribute;
    }

    public Criterion gt(Object value){
        return Restrictions.gt(this.attribute, value);
    }

    public Criterion gte(Object value){
        return Restrictions.ge(this.attribute, value);
    }
}
