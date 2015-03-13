package com.ferrishibernatesupport.saveferris.httpsearchapi.converter;

import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.httpsearchapi.RequestCriteria;

/**
 * Created by uiradias on 06/09/14.
 */
public interface RequestCriteriaToSelect {

    void append(Select select, RequestCriteria requestCriteria);

}
