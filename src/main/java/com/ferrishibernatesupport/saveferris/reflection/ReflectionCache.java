package com.ferrishibernatesupport.saveferris.reflection;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * Used to cache field information and avoid wasting time with iteration through
 * field reflection.
 *
 * Example:
 *
 * ReflectionCache.get(Location.class)
 *
 * @author Andre Campos andreloc@gmail.com, andre@sagaranatech.com
 */
public class ReflectionCache {

    private final Class<?> 			clazz;
    private List<Field> objectFields;
    private List<Field> 			primitiveFields;
    private Map<String, Field> objectFieldsByName;
    private Map<String, Class<?>> 	fieldTypeByName;
    private Map<String, Field> 		primitiveFieldsByName;
    private Field 					idField;
    private List<Field>				natualIdFields;

    /**
     * ReflectionUtil cached
     */
    private static Map<Class<?>, ReflectionCache> reflectionInfos = new HashMap<Class<?>,ReflectionCache>();

    /**
     * More optimization stuff :-).
     * See: double-checked lock
     */
    private static Object LOCK = new Object();

    /**
     * Retrieve the cached or a new instance of reflection informations that
     * informations
     * @param clazz
     * @return
     */
    public static ReflectionCache get(final Class<?> clazz) {
        ReflectionCache info = reflectionInfos.get(clazz);
        if(info == null) {
            synchronized(LOCK) {
                if(info == null) {
                    info = new ReflectionCache(clazz);
                    reflectionInfos.put(clazz, info);
                }
            }
        }
        return info;
    }

    /**
     * Private to avoid someone to create the object and
     * dont use the cache advantage.
     * @param clazz
     */
    private ReflectionCache(Class<?> clazz) {
        super();
        this.clazz = clazz;
        constructReflectionCache();
    }

    /**
     * Get the fields containing primitives or primitive wrappers like Integer
     * and so on.
     *
     * @return
     */
    public List<Field> getPrimitiveFields() {
        return primitiveFields;
    }

    /**
     * Object fields are fields with entity referencies.
     *
     * @return
     */
    public List<Field> getObjectFields() {
        return objectFields;
    }

    /**
     * Iterate over the class fields and instantiate the appropriate list of
     * objects and basic fields
     */
    private void constructReflectionCache() {
        List<Field> objectFields    = new ArrayList<Field>();
        objectFieldsByName 		    = new HashMap<String, Field>();

        List<Field> primitiveFields = new ArrayList<Field>();
        primitiveFieldsByName 		= new HashMap<String, Field>();
        fieldTypeByName 			= new HashMap<String, Class<?>>();
        List<Field> natualIdFields	= new ArrayList<Field>();

        loadFieldsInHierachy(objectFields, primitiveFields, natualIdFields);
        this.primitiveFields = Collections.unmodifiableList(primitiveFields);
        this.objectFields = Collections.unmodifiableList(objectFields);
        this.natualIdFields	  = Collections.unmodifiableList(natualIdFields);

    }

    private void loadFieldsInHierachy(List<Field> objectFields,
                                      List<Field> primitiveFields, List<Field> natualIdFields) {
        Class<?> parentClass = clazz;
        while(parentClass != null) {
            Field[] fields = parentClass.getDeclaredFields();
            fillObjectAndPrimitiveFields(objectFields, primitiveFields, natualIdFields, fields);
            parentClass = parentClass.getSuperclass();
        }
    }

    private void fillObjectAndPrimitiveFields(
            List<Field> objectFields,
            List<Field> primitiveFields,
            List<Field> natualIdFields,
            Field[] fields) {

        for (Field field : fields) {
            int modifiers = field.getModifiers();

            if (Modifier.isTransient(modifiers)) {
                continue;
            }

            if (Modifier.isStatic(modifiers)) {
                continue;
            }

            if(isTransient(field)){
                continue;
            }

            if (isPrimitive(field)) {
                primitiveFields.add(field);
                primitiveFieldsByName.put(field.getName(), field);
                if( idField == null && isId(field) ) {
                    idField = field;
                }
                if( isNatualId(field) ) {
                    natualIdFields.add(field);
                }
            } else {
                objectFields.add(field);
                objectFieldsByName.put(field.getName(), field);
            }
            cacheFieldType(field);
            field.setAccessible(true);
        }
    }

    private boolean isTransient(Field field) {
        return field.isAnnotationPresent(Transient.class);
    }

    /**
     * Create the local cache of the field type
     * @param field
     */
    private void cacheFieldType(Field field)
    {
        String name = field.getName();
        Class<?> type = field.getType();
        Class<?> mappedType = type;

        if(type.isAssignableFrom(List.class)) {
            mappedType = ReflectionUtil.getGenericType(field);
        }
        else if(type.isArray()) {
            mappedType = type.getComponentType();
        }
        fieldTypeByName.put(name, mappedType);
    }

    private boolean isPrimitive(final Field field) {
        final Class<?> fieldType = field.getType();
        return fieldType.isPrimitive()
                || !(fieldType.isAssignableFrom(List.class)
                || fieldType.isArray()
                || fieldType.isAnnotationPresent(Entity.class)
                || field.isAnnotationPresent(ManyToOne.class)
                || field.isAnnotationPresent(OneToOne.class)
                || field.isAnnotationPresent(OneToMany.class)
        );
    }


    private boolean isNatualId(Field field) {
        return field.isAnnotationPresent(NaturalId.class);
    }

    private boolean isId(final Field field) {
        return field.isAnnotationPresent(Id.class);
    }

    public Field getIdField() {
        return idField;
    }

    public List<Field> getNaturalId() {
        return natualIdFields;
    }

    public Field getObjectField(String fieldName) {
        return objectFieldsByName.get(fieldName);
    }

    public Field getPrimitiveField(String fieldName) {
        return primitiveFieldsByName.get(fieldName);
    }

    public boolean isObject(String objectProperty) {
        return (null != objectFieldsByName.get(objectProperty));
    }

    public Class<?> getFieldType(String field) {
        return fieldTypeByName.get(field);
    }

    public Class<?> getCachedClass() {
        return clazz;
    }

    public boolean isNatualId(String field) {
        Field primitive = getPrimitiveField(field);
        if(primitive == null) return false;
        return isNatualId(primitive);
    }
}
