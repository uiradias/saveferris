package com.ferrishibernatesupport.saveferris.criteria.projection.mapping;

import com.ferrishibernatesupport.saveferris.reflection.ReflectionCache;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by uiradias on 06/09/14.
 */
@Component
public class WildCardToAttributeMapping {

    public Collection<String> convert(String wildCard, Class<?> entity) {
        String[] parameters = wildCard.split("\\.");
        Class<?> wildCardEntity = entity;
        StringBuilder alias = new StringBuilder();
        for (String parameter : parameters) {
            if(parameter.contains("*")) {
                return getFiltersForMatchingFields(alias.toString(), parameter, wildCardEntity);
            } else {
                wildCardEntity = ReflectionCache.get(wildCardEntity).getFieldType(parameter);
                if(wildCardEntity == null) {
                    break;
                }
                alias
                        .append(parameter)
                        .append(".");
            }
        }
        return Collections.emptyList();
    }

    private Collection<String> getFiltersForMatchingFields(
            String alias, String expression, Class<?> entity) {
        List<String> filters = new ArrayList<>();
        ReflectionCache cache = ReflectionCache.get(entity);
        expression = expression.replace("*", ".*");

        for (Field field : cache.getPrimitiveFields()) {
            String fieldName = field.getName();
            if(fieldName.matches(expression)) {
                filters.add(alias+fieldName);
            }
        }
        return filters;
    }

}
