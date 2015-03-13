package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * Created by uiradias on 07/09/14.
 */
public class NullRestrictionsBuilder {

    public String attribute;

    public NullRestrictionsBuilder(String attribute) {
        this.attribute = attribute;
    }

    public Criterion isNull(){
        return Restrictions.isNull(this.attribute);
    }

    public Criterion isNotNull(){
        return Restrictions.isNotNull(this.attribute);
    }
}
