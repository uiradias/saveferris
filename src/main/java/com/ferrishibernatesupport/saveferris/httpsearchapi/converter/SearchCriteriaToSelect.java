package com.ferrishibernatesupport.saveferris.httpsearchapi.converter;

import com.ferrishibernatesupport.saveferris.criteria.domain.Select;
import com.ferrishibernatesupport.saveferris.httpsearchapi.SearchCriteria;

/**
 * Created by uiradias on 06/09/14.
 */
public interface SearchCriteriaToSelect {

    void append(Select select, SearchCriteria searchCriteria, Class<?> clazz);

}
