package com.ferrishibernatesupport.saveferris.criteria.projection.mapping;

import com.ferrishibernatesupport.saveferris.reflection.ReflectionCache;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by uiradias on 03/09/14.
 */
public class EntityTupleMapping {
    private static final String VARIABLE_REGEX = "(\\w*)$";
    private static final String ASSOCIATION_REGEX = "(\\w*)\\..*";

    private String regexPrefix;
    private ReflectionCache cache;
    public Field join;
    private List<TupleFieldAcessor> fields = new ArrayList<TupleFieldAcessor>();

    private List<EntityTupleMapping> associationMapping  = new ArrayList<EntityTupleMapping>();

    private boolean isManyRelationship;

    public EntityTupleMapping(Class<?> clazz) {
        this(null,null,clazz,false);
    }

    public ReflectionCache getCache() {
        return cache;
    }

    public Field getJoin() {
        return join;
    }

    public String getRegexPrefix() {
        return regexPrefix;
    }

    private EntityTupleMapping(EntityTupleMapping parent, Field field, Class<?> clazz,
                               boolean isManyRelationship) {
        join = field;
        regexPrefix = mountPrefix(parent);
        cache = ReflectionCache.get(clazz);
        this.isManyRelationship = isManyRelationship;
    }

    public boolean isManyRelationship() {
        return isManyRelationship;
    }

    /**
     * Load the list of aliases and check the associations and fields
     * of this class
     * @param aliases
     */
    public void load(String[] aliases) {
        Pattern fieldPattern = Pattern.compile(regexPrefix + VARIABLE_REGEX);
        Pattern assocPattern = Pattern.compile(regexPrefix + ASSOCIATION_REGEX);
        Matcher matcher;
        String alias;
        for (int i = 0; i < aliases.length; i++) {
            alias = aliases[i];
            matcher = fieldPattern.matcher(alias);
            addField(matcher, i);
            matcher = assocPattern.matcher(alias);
            addAssociation(aliases, matcher);
        }
    }

    public Class<?> getAssocType() {
        return cache.getCachedClass();
    }

    /**
     * Check if the tuple has any information
     * @param tuple
     * @return
     */
    public boolean isEmpty(Object[] tuple) {
        // if the tuple contains a value
        for (TupleFieldAcessor field : fields) {
            if(tuple[field.tupleIdx] != null) {
                return false;
            }
        }

        // if the subassociations contains any value
        for(EntityTupleMapping assoc : associationMapping) {
            if(!assoc.isEmpty(tuple)) {
                return false;
            }
        }

        // no property will be set in this entity. So it is empty
        return true;
    }

    private void addAssociation(String[] aliases, Matcher matcher) {
        if(matcher.find()) {
            String fieldName = matcher.group(1);
            Class<?> type = cache.getFieldType(fieldName);
            Field field = cache.getObjectField(fieldName);

            if(type == null || field == null) {
                // TODO Log something here
            } else {

                if(isAlreadyMapped(field)) {
                    return;
                }

                EntityTupleMapping assoc = new EntityTupleMapping(this,
                        field,type, (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class)));

                assoc.load(aliases); // load the aliases for the subclass
                associationMapping.add(assoc);
            }
        }
    }

    private boolean isAlreadyMapped(Field field) {
        for (EntityTupleMapping subAssoc : associationMapping) {
            if(subAssoc.join == field) return true; // already created
        }
        return false;
    }

    private void addField(Matcher matcher, int idx) {
        if(matcher.find()) {
            String fieldName = matcher.group(1);

            Field field = cache.getPrimitiveField(fieldName);
            if(field == null) {
                // TODO log something here
            } else {
                fields.add(new TupleFieldAcessor(field, idx));
            }
        }
    }

    @Override
    public String toString() {
        return join.getName();
    }


    private String mountPrefix(EntityTupleMapping parent) {
        if(parent == null) return "^";
        else return parent.regexPrefix + join.getName() + "\\.";
    }

    public List<TupleFieldAcessor> getFields() {
        return fields;
    }

    public void setFields(List<TupleFieldAcessor> fields) {
        this.fields = fields;
    }

    public List<EntityTupleMapping> getAssociations() {
        return associationMapping;
    }

    public void setAssocs(List<EntityTupleMapping> assocs) {
        this.associationMapping = assocs;
    }


    /**
     *
     * @author andrecampos
     */
    public static class TupleFieldAcessor {

        private int tupleIdx;
        private Field field;

        public TupleFieldAcessor(Field field, int tupleIdx) {
            this.field = field;
            this.tupleIdx = tupleIdx;
        }

        public void set(Object instance, Object tuple[])
                throws IllegalArgumentException, IllegalAccessException {
            field.set(instance, tuple[tupleIdx]);
        }

        public Object get(Object tuple[]) {
            return tuple[tupleIdx];
        }

        @Override
        public String toString() {
            return "["+tupleIdx+":"+field.getName()+"]";
        }

        public int getTupleIdx() {
            return tupleIdx;
        }

        public Field getField() {
            return field;
        }
    }

}
