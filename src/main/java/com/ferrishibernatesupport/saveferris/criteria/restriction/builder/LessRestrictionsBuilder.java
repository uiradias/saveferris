package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by uiradias on 07/09/14.
 */
public class LessRestrictionsBuilder {

    public String attribute;

    public LessRestrictionsBuilder(String attribute) {
        this.attribute = attribute;
    }

    public Criterion lt(Object value){
        return Restrictions.lt(this.attribute, value);
    }

    public Criterion lte(Object value){
        return Restrictions.le(this.attribute, value);
    }
}
