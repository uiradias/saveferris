package com.ferrishibernatesupport.saveferris.criteria.domain;

import com.ferrishibernatesupport.saveferris.criteria.restriction.builder.SingleRestrictionsBuilder;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by uiradias on 23/04/14.
 */
public class Expressions {

    private List<String> internalPaths = new ArrayList<>();

    private Conjunction conjunction;

    private Disjunction disjunction;

    public static Expressions and(Filter... filters){
        return Expressions.and(Arrays.asList(filters));
    }

    public static Expressions and(List<Filter> filters){
        Expressions expressions = new Expressions();
        Conjunction conjunction = Restrictions.conjunction();

        for (Filter filter : filters){
            conjunction.add(new SingleRestrictionsBuilder(filter).build());
            expressions.internalPaths.add(filter.getAttribute());
        }

        expressions.setConjunction(conjunction);
        return expressions;
    }

    public static Expressions and(Expressions disjunction, List<Filter> filters){
        Expressions expressions = and(filters);
        expressions.getConjunction().add(disjunction.getDisjunction());
        return expressions;
    }

    public static Expressions and(Expressions disjunction, Filter... filters){
        return and(disjunction, Arrays.asList(filters));
    }

    public static Expressions or(Filter... filters){
        return Expressions.or(Arrays.asList(filters));
    }

    public static Expressions or(List<Filter> filters){
        Expressions expressions = new Expressions();
        Disjunction disjunction = Restrictions.disjunction();

        for (Filter filter : filters){
            disjunction.add(new SingleRestrictionsBuilder(filter).build());
            expressions.internalPaths.add(filter.getAttribute());
        }

        expressions.setDisjunction(disjunction);
        return expressions;
    }

    public static Expressions or(Expressions conjunction, List<Filter> filters){
        Expressions expressions = or(filters);
        expressions.getDisjunction().add(conjunction.getConjunction());
        return expressions;
    }

    public static Expressions or(Expressions conjunction, Filter... filters){
        return or(conjunction, Arrays.asList(filters));
    }

    public Conjunction getConjunction() {
        return conjunction;
    }

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;
    }

    public Disjunction getDisjunction() {
        return disjunction;
    }

    public void setDisjunction(Disjunction disjunction) {
        this.disjunction = disjunction;
    }

    public List<String> getInternalPaths() {
        return internalPaths;
    }
}
