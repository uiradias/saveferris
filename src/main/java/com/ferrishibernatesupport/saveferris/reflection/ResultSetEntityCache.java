package com.ferrishibernatesupport.saveferris.reflection;

import com.ferrishibernatesupport.saveferris.criteria.projection.mapping.EntityTupleMapping;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by uiradias on 03/09/14.
 */
public final class ResultSetEntityCache {

    /**
     * Key field indexes
     */
    private int[] keyFields;

    /**
     * Cache
     */
    private Map<Key, Object> objectCacheById;

    /**
     * Create the result set cache using the requested field
     * @param keyFields
     */
    private ResultSetEntityCache(int[] keyFields) {
        super();
        this.keyFields = keyFields;
        objectCacheById = new HashMap<Key, Object>();
    }

    /**
     * Get the cached object based in the key fields
     * and the tuple
     * @param tuple
     * @return
     */
    public Object getCached(Object tuple[]) {
        return objectCacheById.get(new Key(tuple));
    }

    /**
     * Caches a new entity using the tuple and key fields
     * @param object
     * @param tuple
     */
    public void cache(Object object,Object tuple[]) {
        objectCacheById.put(new Key(tuple), object);
    }


    /**
     *  Key fields are used to mount Key object
     *  that is used to map the cached entities
     */
    public static ResultSetEntityCache newInstance(EntityTupleMapping association) {
        ReflectionCache cache = association.getCache();
        List<EntityTupleMapping.TupleFieldAcessor> fields = association.getFields();

        ResultSetEntityCache instance =
                newInstanceById(cache.getIdField(),fields);

        if(instance != null) {
            return instance;
        }

        // try by naturalIds
        instance = fillKeyByNaturalIds(cache, fields);
        if(instance != null) {
            return instance;
        }

        return fillNewInstanceUsingAllAvailableFields(fields);
    }

    private static ResultSetEntityCache fillNewInstanceUsingAllAvailableFields(List<EntityTupleMapping.TupleFieldAcessor> fields) {
        int size = fields.size();
        int[] keyFields = new int[size];
        for (int i = 0; i < size; i++) {
            keyFields[i] = fields.get(i).getTupleIdx();
        }
        return new ResultSetEntityCache(keyFields);
    }

    private static ResultSetEntityCache newInstanceById(final Field id, List<EntityTupleMapping.TupleFieldAcessor> fields) {

        // try by ID
        for (EntityTupleMapping.TupleFieldAcessor field: fields) {
            if(field.getField().equals(id)) {
                return new ResultSetEntityCache(
                        new int[]{field.getTupleIdx()});
            }
        }
        return null;
    }

    private static ResultSetEntityCache fillKeyByNaturalIds(ReflectionCache cache, List<EntityTupleMapping.TupleFieldAcessor> fields) {
        final List<Field> natualIdFiedls = cache.getNaturalId();
        int natualFieldsSize = natualIdFiedls.size();
        int[] keyFields = new int[natualFieldsSize];
        int idx = 0;
        for (EntityTupleMapping.TupleFieldAcessor fieldAcessor : fields) {
            if(cache.isNatualId(fieldAcessor.getField().getName())) {
                keyFields[idx++] = fieldAcessor.getTupleIdx();
                if(idx == natualFieldsSize) {
                    return new ResultSetEntityCache(keyFields);
                }
            }
        }
        return null;
    }


    /**
     * Unique key based in the key indexes
     *
     * @author andrecampos
     */
    private class Key {

        private Object[] values;

        public Key(Object[] tuple) {
            values = new Object[keyFields.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = tuple[keyFields[i]];
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(values);
        }

        @Override
        public boolean equals(Object obj) {
            if(values.length == 0) {
                return false; // cannot compare
            }
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (!Arrays.equals(values, other.values))
                return false;
            return true;
        }
    }
}
