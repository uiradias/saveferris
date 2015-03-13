package com.ferrishibernatesupport.saveferris.criteria.projection;

import com.ferrishibernatesupport.saveferris.criteria.domain.Projections;
import com.ferrishibernatesupport.saveferris.criteria.projection.mapping.WildCardToAttributeMapping;
import com.ferrishibernatesupport.saveferris.criteria.projection.transformers.DistinctAliasToBeanResultTransformer;
import com.ferrishibernatesupport.saveferris.reflection.ReflectionUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by uiradias on 23/04/14.
 */
@Service
public class ProjectionsCriteriaList {

    @Autowired
    private WildCardToAttributeMapping wildCardToAttributeMapping;

    public void project(Criteria criteria, Projections projections, Class clazz) {
        if (projections != null) {
            ProjectionList projectionList = org.hibernate.criterion.Projections.projectionList();

            convertWildCardIntoFilter(projections.getAttributes(), clazz);

            for (String attribute : projections.getAttributes()) {
                if(attribute.contains(".")){
                    String associationPath = attribute.substring(0, attribute.lastIndexOf("."));
                    String alias = ReflectionUtil.isEmbedded(clazz, associationPath) ? associationPath : associationPath.replace(".", "_");
                    String attr = attribute.substring(attribute.lastIndexOf(".") + 1);
                    String propertyName = alias + "." + attr;
                    projectionList.add(new CustomPropertyAliasProjection(propertyName, attribute));
                }else{
                    projectionList.add(new CustomPropertyAliasProjection(attribute, attribute));
                }
            }

            criteria.setProjection(projectionList);
            setTransformer(criteria, clazz);
        }
    }

    private void setTransformer(Criteria criteria, Class clazz) {
        criteria.setResultTransformer(new DistinctAliasToBeanResultTransformer(clazz));
    }

    private void convertWildCardIntoFilter(List<String> projections, Class clazz) {
        List<String> newProjections = new ArrayList<String>();
        for (Iterator<String> iterator = projections.iterator(); iterator.hasNext();) {
            String projection = (String) iterator.next();
            if (projection.contains("*")) {
                iterator.remove();
                newProjections.addAll(
                        wildCardToAttributeMapping.convert(projection, clazz));
            }
        }
        projections.addAll(newProjections);
    }
}
