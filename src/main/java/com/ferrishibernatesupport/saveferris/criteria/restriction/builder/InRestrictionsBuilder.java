package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by uiradias on 06/09/14.
 */
public class InRestrictionsBuilder {

    public String attribute;

    public InRestrictionsBuilder(String attribute) {
        this.attribute = attribute;
    }

    public Criterion in(Object[] value){
        return Restrictions.in(this.attribute, value);
    }
}
