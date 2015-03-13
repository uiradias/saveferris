package com.ferrishibernatesupport.saveferris.httpsearchapi.converter.builder;

import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.exception.SearchCriteriaSyntaxException;
import com.ferrishibernatesupport.saveferris.httpsearchapi.SearchCriteria;
import com.ferrishibernatesupport.saveferris.reflection.ReflectionUtil;
import org.hibernate.criterion.MatchMode;

import java.util.List;

/**
* Created by uiradias on 06/09/14.
*/
public class SingleSearchCriteriaOperationBuilder {

    private final Class<?> criteriaClass;
    private SearchCriteria criteria;

    public SingleSearchCriteriaOperationBuilder(Class<?> criteriaClass) {
        this.criteriaClass = criteriaClass;
    }

    public SingleSearchCriteriaOperationBuilder withCriteria(SearchCriteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public Class<?> getCriteriaClass() {
        return criteriaClass;
    }

    public SearchCriteria getCriteria() {
        return criteria;
    }

    public Filter build() throws SearchCriteriaSyntaxException {
        Filter filter = null;

        if(this.criteria.getEq()!=null && this.criteria.getMatchMode()==null){
            return createEqFilter();
        }else if(this.criteria.getEq()!=null && this.criteria.getMatchMode()!=null){
            return createEqWithMactchModeFilter();
        }else if(this.criteria.getGt()!=null){
            return createGtFilter();
        }else if(this.criteria.getGte()!=null){
            return createGteFilter();
        }else if(this.criteria.getLt()!=null){
            return createLtFilter();
        }else if(this.criteria.getLte()!=null){
            return createLteFilter();
        }else if(this.criteria.getIsNull()!=null){
            return createIsNullFilter();
        }else if(this.criteria.getIn()!=null){
            return createInFilter();
        }else if(this.criteria.getNeq()!=null) {
            return createNotEqualsFilter();
        }

        return filter;
    }

    private Filter createInFilter() throws SearchCriteriaSyntaxException {
        List<?> inArrayList = ((List<?>)this.criteria.getIn());
        Object[] inObjects = new Object[inArrayList.size()];
        for (int i = 0; i < inArrayList.size(); i++) {
            inObjects[i] = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), inArrayList.get(i));
        }
        Filter inFilter = new Filter(this.criteria.getAttr(), inObjects);
        return inFilter;
    }

    private Filter createIsNullFilter() {
        if((Boolean) this.criteria.getIsNull()){
            return Filter.isNull(this.criteria.getAttr());
        }
        else {
            return Filter.isNotNull(this.criteria.getAttr());
        }
    }

    private Filter createLteFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getLte());
        Filter lte = Filter.le(this.criteria.getAttr(), objectValue);
        return lte;
    }

    private Filter createLtFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getLt());
        Filter lt = Filter.lt(this.criteria.getAttr(), objectValue);
        return lt;
    }

    private Filter createGtFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getGt());
        Filter gt = Filter.gt(this.criteria.getAttr(), objectValue);
        return gt;
    }

    private Filter createGteFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getGte());
        Filter gte = Filter.gte(this.criteria.getAttr(), objectValue);
        return gte;
    }

    private Filter createEqWithMactchModeFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getEq());
        Filter eq;

        switch (this.criteria.getMatchMode()) {
            case EXACT:
                eq = Filter.like(this.criteria.getAttr(), (String) objectValue, MatchMode.EXACT);
                break;

            case ANYWHERE:
                eq = Filter.like(this.criteria.getAttr(), (String) objectValue, MatchMode.ANYWHERE);
                break;

            case START:
                eq = Filter.like(this.criteria.getAttr(), (String) objectValue, MatchMode.START);
                break;

            case END:
                eq = Filter.like(this.criteria.getAttr(), (String) objectValue, MatchMode.END);
                break;

            default:
                eq = createEqFilter();
                break;
        }

        return eq;
    }

    private Filter createEqFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getEq());
        Filter eq = Filter.eq(this.criteria.getAttr(), objectValue);
        return eq;
    }

    private Filter createNotEqualsFilter() throws SearchCriteriaSyntaxException {
        Object objectValue = ReflectionUtil.getObjectWithType(criteriaClass, this.criteria.getAttr(), this.criteria.getNeq());
        Filter neq = Filter.neq(this.criteria.getAttr(), objectValue);
        return neq;
    }

}
