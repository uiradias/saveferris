package com.ferrishibernatesupport.saveferris.httpsearchapi;

import java.util.List;

/**
 * Created by uiradias on 06/09/14.
 */
public class SearchCriteria {

    private String attr;

    private List<SearchCriteria> or;

    private List<SearchCriteria> and;

    private List<SearchCriteria> criteria;

    private Object gt;

    private Object gte;

    private Object lt;

    private Object lte;

    private Object eq;

    private Object isNull;

    private Object in;

    private Object neq;

    private List<CriteriaOrder> sort;

    private SearchCriteriaMatchMode matchMode;

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }


    public Object getGt() {
        return gt;
    }

    public void setGt(Object gt) {
        this.gt = gt;
    }

    public Object getLt() {
        return lt;
    }

    public void setLt(Object lt) {
        this.lt = lt;
    }

    public Object getEq() {
        return eq;
    }

    public void setEq(Object eq) {
        this.eq = eq;
    }

    public Object getGte() {
        return gte;
    }

    public void setGte(Object gte) {
        this.gte = gte;
    }

    public Object getLte() {
        return lte;
    }

    public void setLte(Object lte) {
        this.lte = lte;
    }

    public Object getIsNull() {
        return isNull;
    }

    public void setIsNull(Object isNull) {
        this.isNull = isNull;
    }

    public Object getIn() {
        return in;
    }

    public void setIn(Object in) {
        this.in = in;
    }

    public List<CriteriaOrder> getSort() {
        return sort;
    }

    public void setSort(List<CriteriaOrder> sort) {
        this.sort = sort;
    }

    public Object getNeq() {
        return neq;
    }

    public void setNeq(Object neq) {
        this.neq = neq;
    }

    public List<SearchCriteria> getOr() {
        return or;
    }

    public void setOr(List<SearchCriteria> or) {
        this.or = or;
    }

    public List<SearchCriteria> getAnd() {
        return and;
    }

    public void setAnd(List<SearchCriteria> and) {
        this.and = and;
    }

    public List<SearchCriteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<SearchCriteria> criteria) {
        this.criteria = criteria;
    }

    public SearchCriteriaMatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(SearchCriteriaMatchMode matchMode) {
        this.matchMode = matchMode;
    }
}
