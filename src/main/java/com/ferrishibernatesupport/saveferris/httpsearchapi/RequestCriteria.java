package com.ferrishibernatesupport.saveferris.httpsearchapi;

import java.util.List;

/**
 * Created by uiradias on 06/09/14.
 */
public class RequestCriteria {

    private List<String> projections;
    private Integer 	firstResult;
    private Integer 	maxResults;

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public List<String> getProjections() {
        return projections;
    }

    public void setProjections(List<String> projections) {
        this.projections = projections;
    }
}
