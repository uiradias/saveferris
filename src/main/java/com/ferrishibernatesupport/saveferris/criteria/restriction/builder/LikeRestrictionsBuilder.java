package com.ferrishibernatesupport.saveferris.criteria.restriction.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * Created by uiradias on 16/04/14.
 */
public class LikeRestrictionsBuilder {

    private String attribute;

    public LikeRestrictionsBuilder(String attribute){
        this.attribute = attribute;
    }

    public Criterion like(String value){
        return like(value, null);
    }

    public Criterion like(String value, MatchMode matchMode){
        if(matchMode==null){
            matchMode = MatchMode.ANYWHERE;
        }
        return Restrictions.ilike(this.attribute, value, matchMode);
    }

}
