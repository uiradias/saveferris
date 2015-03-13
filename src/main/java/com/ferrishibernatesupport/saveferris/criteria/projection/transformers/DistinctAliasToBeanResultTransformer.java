package com.ferrishibernatesupport.saveferris.criteria.projection.transformers;

import com.ferrishibernatesupport.saveferris.criteria.projection.mapping.EntityTupleMapping;
import com.ferrishibernatesupport.saveferris.reflection.ResultSetEntityCache;
import org.hibernate.transform.ResultTransformer;

import java.util.*;

/**
 * Result transformer that should be used to convert into beans
 * a result set of hibernate criteria using projections.
 *
 * This result transformer is able to map association and group the
 * result in order to resturn unique results in one-to-one, many-to-one
 * and one-to-many associations.
 *
 * It optimizes the transformation by keeping an internal cache that
 * avoids multiple parsing of the same instance of object.
 *
 * If you uses the projection in an hibernate criteria, hibernate will
 * not be able to parse the associations between classes. This transformer
 * should be used to parse projections created in the following way:
 *
 * @see EntityTupleMapping
 * @see EntityKeyCache
 *
 * @author Andre Campos andreloc@gmail.com, andre@sagaranatech.com
 */
public class DistinctAliasToBeanResultTransformer implements ResultTransformer {

    private static final long serialVersionUID = 8705706398275156725L;
    private static final int ROOT_LEVEL = 0;

    private Class<?> clazz;

    private boolean initialized;

    private EntityTupleMapping root;

    /**
     * Group the sorted list of root entities
     */
    private ArrayList<Object> rootEntities;


    private Map<String, ResultSetEntityCache> cacheMap;

    public DistinctAliasToBeanResultTransformer(final Class<?> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        if(!initialized) {
            initialize(aliases);
            rootEntities = new ArrayList<Object>();
            cacheMap = new HashMap<String, ResultSetEntityCache>();
        }
        return processAssociation(root, tuple);
    }

    @Override
    @SuppressWarnings("rawtypes") // removing warning because interface from hibernate does not use generics
    public List transformList(List collection) {
        if(rootEntities == null) {
            return Collections.EMPTY_LIST;
        }
        return rootEntities;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object processAssociation(EntityTupleMapping association, Object[] tuple) {
        Object instance = null;
        try {

            instance = getCachedOrFillNewEntity(association, tuple);
            if(instance == null) {
                return null; // no information set
            }

            for (EntityTupleMapping subAssociation : association.getAssociations()) {
                Object result = processAssociation(subAssociation, tuple);
                if(result == null) continue;
                if(subAssociation.isManyRelationship()) {
                    List list = getPreviousOrCreateNewList(instance,
                            subAssociation);
                    if(!list.contains(result)) {
                        // TODO Andre: Esse metodo esta em O(n2), pois esta verificando se o item
                        // ja existe antes de adicionalo a listagem.
                        // Pode ser utilizado um SET auxiliar para jogar essa complexidade para O(log(n))
                        list.add(result);
                    }
                } else {
                    subAssociation.join.set(instance, result);
                }
            }

        } catch (InstantiationException e) {
            //TODO Log something here...
        } catch (IllegalAccessException e) {
            //TODO Log something here...
        }

        return instance;
    }

    @SuppressWarnings("rawtypes")
    private List getPreviousOrCreateNewList(Object instance,
                                            EntityTupleMapping subAssociation) throws IllegalAccessException {
        List list = (List) subAssociation.join.get(instance);

        if(list == null) {
            list = new ArrayList();
            subAssociation.join.set(instance, list);
        }
        return list;
    }

    private Object getCachedOrFillNewEntity(EntityTupleMapping association,
                                            Object[] tuple) throws InstantiationException,
            IllegalAccessException {
        Object instance;
        if(association.isEmpty(tuple)) {
            return null;
        }

        ResultSetEntityCache entityCache = cacheMap.get(association.getRegexPrefix());
        if(entityCache == null) {
            entityCache = ResultSetEntityCache.newInstance(association);
            cacheMap.put(association.getRegexPrefix(), entityCache);
        }

        instance = entityCache.getCached(tuple);
        if(instance == null) {
            instance = association.getAssocType().newInstance();
            for (EntityTupleMapping.TupleFieldAcessor fieldAcessor : association.getFields()) {
                fieldAcessor.set(instance, tuple);
            }
            if(association == root) {
                rootEntities.add(instance); // new root entity created
            }
            entityCache.cache(instance, tuple);
        }

        return instance;
    }


    private void initialize(String[] aliases) {
        replaceUnderlines(aliases);
        root = new EntityTupleMapping(clazz);
        root.load(aliases);
        initialized = true;
    }

    /**
     * Relation aliases could use underline _ when
     * @param aliases
     */
    private void replaceUnderlines(String[] aliases) {
        for (int i = 0; i < aliases.length; i++) {
            aliases[i] = aliases[i].replace("_", ".");
        }
    }

}
