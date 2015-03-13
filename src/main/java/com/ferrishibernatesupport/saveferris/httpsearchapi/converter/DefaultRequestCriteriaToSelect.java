package com.ferrishibernatesupport.saveferris.httpsearchapi.converter;

import com.ferrishibernatesupport.saveferris.criteria.domain.Projections;
import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.httpsearchapi.RequestCriteria;
import org.springframework.stereotype.Component;

/**
 * Created by uiradias on 06/09/14.
 */
@Component
public class DefaultRequestCriteriaToSelect implements RequestCriteriaToSelect{

    @Override
    public void append(Select select, RequestCriteria requestCriteria) {
        appendProjections(select, requestCriteria);
    }

    private void appendProjections(Select select, RequestCriteria requestCriteria) {
        Projections projections = new Projections();
        projections.add(requestCriteria.getProjections());
        select.project(projections);
    }

}
