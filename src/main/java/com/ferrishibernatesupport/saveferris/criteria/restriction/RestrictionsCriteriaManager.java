package com.ferrishibernatesupport.saveferris.criteria.restriction;

import com.ferrishibernatesupport.saveferris.criteria.alias.builder.LeftJoinAlias;
import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by uiradias on 23/04/14.
 */
@Service
public class RestrictionsCriteriaManager {

    @Autowired
    private  SingleRestriction singleRestriction;

    @Autowired
    private LeftJoinAlias leftJoinAlias;

    public void addCriteria(Criteria criteria, Select select){
        List<Filter> filters = select.getFilters();

        createJoins(criteria, select);

        if(hasConjunctionExpression(select)){
            addConjunction(criteria, select);
        }if(hasDisjunctionExpression(select)){
            addDisjunction(criteria, select);
        }else{
            addSingleRestrictions(criteria, filters);
        }

        addOrder(criteria, select);

    }

    private void createJoins(Criteria criteria, Select select) {
        List<String> internalPaths = select.getInternalPaths();
        for(String internalPath : internalPaths){
            leftJoinAlias.add(criteria, internalPath);
        }
    }

    private void addOrder(Criteria criteria, Select select) {
        List<Order> orders = select.getOrders();
        if(orders !=null && !orders.isEmpty()){
            for(Order order:orders){
                criteria.addOrder(order);
            }
        }
    }

    private void addDisjunction(Criteria criteria, Select select) {
        criteria.add(select.getDisjunction());
    }

    private void addConjunction(Criteria criteria, Select select) {
        criteria.add(select.getConjunction());
    }

    private void addSingleRestrictions(Criteria criteria, List<Filter> filters) {
        for (Filter filter : filters) {
            singleRestriction.add(criteria, filter);
        }
    }

    private boolean hasConjunctionExpression(Select select) {
        return select.hasConjunction();
    }

    private boolean hasDisjunctionExpression(Select select) {
        return select.hasDisjunction();
    }
}
