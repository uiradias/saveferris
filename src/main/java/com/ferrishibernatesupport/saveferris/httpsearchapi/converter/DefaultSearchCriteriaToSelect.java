package com.ferrishibernatesupport.saveferris.httpsearchapi.converter;

import com.ferrishibernatesupport.saveferris.criteria.domain.Expressions;
import com.ferrishibernatesupport.saveferris.criteria.domain.Filter;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.exception.SearchCriteriaSyntaxException;
import com.ferrishibernatesupport.saveferris.httpsearchapi.SearchCriteria;
import com.ferrishibernatesupport.saveferris.httpsearchapi.converter.builder.SingleSearchCriteriaOperationBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* Created by uiradias on 06/09/14.
*/
@Service
public class DefaultSearchCriteriaToSelect implements SearchCriteriaToSelect {

    @Override
    public void append(Select select, SearchCriteria searchCriteria, Class<?> clazz) {
        addCriteria(select, searchCriteria, clazz);
    }

    private void addCriteria(Select select, SearchCriteria searchCriteria, Class<?> clazz) {
        if(searchCriteria.getOr()!=null){
            select.where(createOrOperation(clazz, searchCriteria.getOr()));
        }else if(searchCriteria.getAnd()!=null){
            select.where(createAndOperation(clazz, searchCriteria.getAnd()));
        }
    }

    private Expressions createAndOperation(Class<?> clazz, List<SearchCriteria> and) {
        List<Filter> filters = new ArrayList<>();
        Expressions orOperation = null;
        for (SearchCriteria monetCriteria : and) {
            if(monetCriteria.getOr()!=null){
                orOperation = createOrOperation(clazz, monetCriteria.getOr());
            }else{
                filters.add(addSingleCriteria(clazz, monetCriteria));
            }
        }

        if(orOperation!=null){
            return Expressions.and(orOperation, filters);
        }else{
            return Expressions.and(filters);
        }
    }

    private Filter addSingleCriteria(Class<?> clazz, SearchCriteria monetCriteria) throws SearchCriteriaSyntaxException {
        return new SingleSearchCriteriaOperationBuilder(clazz)
                .withCriteria(monetCriteria).build();
    }

    private Expressions createOrOperation(Class<?> clazz, List<SearchCriteria> or) {
        List<Filter> filters = new ArrayList<>();
        Expressions andOperation = null;
        for (SearchCriteria monetCriteria : or) {
            if(monetCriteria.getAnd()!=null){
                andOperation = createAndOperation(clazz, monetCriteria.getAnd());
            }else{
                filters.add(addSingleCriteria(clazz, monetCriteria));
            }
        }

        if(andOperation!=null){
            return Expressions.or(andOperation, filters);
        }else{
            return Expressions.or(filters);
        }
    }

}
