/*
 * Copyright 2013 GreenMile LLC.  All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * GreenMile LLC ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with GreenMile LLC.
 */
package com.ferrishibernatesupport.saveferris.criteria.domain;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Uira Dias.
 *         Created 04/04/2014.
 */
public class Select {

	List<Filter> filters = new ArrayList<Filter>();

	Integer firstResult;
	
	Integer maxResult;
	
	List<Order> orders = new ArrayList<Order>();

    Projections projections;

    Conjunction conjunction;

    Disjunction disjunction;

    private List<String> internalPaths = new ArrayList<>();

    public Select(){
        super();
    }

    public Select(List<String> projections){
        createProjections(projections);
    }

    public Select(String... projections){
        createProjections(Arrays.asList(projections));
    }

    private void createProjections(List<String> projections) {
        Projections innerProjections = new Projections();
        for (String attribute : projections) {
            innerProjections.add(attribute);
            internalPaths.add(attribute);
        }
        this.projections = innerProjections;
    }

    public Select where(Expressions expressions){
        if(expressions.getConjunction()!=null){
            this.conjunction = expressions.getConjunction();
        }else if(expressions.getDisjunction()!=null){
            this.disjunction = expressions.getDisjunction();
        }
        this.internalPaths.addAll(expressions.getInternalPaths());
        return this;
    }

    public Select project(Projections projections){
        this.projections = projections;
        this.internalPaths.addAll(projections.getAttributes());
        return this;
    }

	public Select orderBy(Order... orders){
        for(Order order:orders){
            this.orders.add(order);
        }
		return this;
	}
	
	public Select add(Filter filter){
		this.filters.add(filter);
        this.internalPaths.add(filter.getAttribute());
		return this;
	}

    public Select add(List<Filter> filters){
        if(filters!=null){
            this.filters.addAll(filters);
            for(Filter filter : filters){
                this.internalPaths.add(filter.getAttribute());
            }
        }
        return this;
    }
	
	public Select firstResult(Integer firstResult){
		this.firstResult = firstResult;
		return this;
	}

	public List<Filter> getFilters() {
		return this.filters;
	}

    public Projections getProjections() {
        return projections;
    }

    public Conjunction getConjunction() {
        return conjunction;
    }

    public Disjunction getDisjunction() {
        return disjunction;
    }

    public boolean hasConjunction(){
        return this.conjunction!=null;
    }

    public boolean hasDisjunction(){
        return this.disjunction!=null;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<String> getInternalPaths() {
        return internalPaths;
    }

    @Override
    public String toString() {
        return "Select{" +
                "filters=" + filters +
                ", firstResult=" + firstResult +
                ", maxResult=" + maxResult +
                ", orders=" + orders +
                ", projections=" + projections +
                ", conjunction=" + conjunction +
                ", disjunction=" + disjunction +
                '}';
    }
}
