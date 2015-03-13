package com.ferrishibernatesupport.saveferris.criteria.alias.builder;

import org.hibernate.Criteria;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Created by uiradias on 31/08/14.
 */
@Service
public class LeftJoinAlias {

    public void add(Criteria criteria, String attribute) {
        String[] aliasArray = attribute.split("\\.");
        for (int i = 0; i < aliasArray.length - 1; i++) {
            String alias = getAlias(aliasArray, i);
            createAliasIfNecessary(criteria, alias);
        }
    }

    private void createAliasIfNecessary(Criteria criteria, String alias) {
        if(!aliasAlreadyExists(criteria, alias)){
            criteria.createAlias(alias, alias, JoinType.LEFT_OUTER_JOIN);
        }
    }

    private String getAlias(String[] aliasArray, int i) {
        StringBuffer proj = new StringBuffer();
        for (int y = 0; y <= i; y++) {
            if (y != i) {
                proj.append(aliasArray[y]);
                proj.append(".");
            } else {
                proj.append(aliasArray[y]);
            }
        }
        return proj.toString();
    }

    protected Boolean aliasAlreadyExists(Criteria criteria, String alias){
        if (alias != null && criteria instanceof CriteriaImpl){
            CriteriaImpl c = (CriteriaImpl) criteria;
            Iterator subcriterias = c.iterateSubcriteria();

            while (subcriterias.hasNext()){
                Criteria subCriteria = (Criteria) subcriterias.next();

                if (alias.equals(subCriteria.getAlias())) {
                    return true;
                }
            }
        }
        return false;
    }
}
