package com.ferrishibernatesupport.saveferris.reflection;

import com.ferrishibernatesupport.saveferris.exception.SearchCriteriaSyntaxException;
import com.ferrishibernatesupport.saveferris.exception.UnsupportedDateFormatException;
import com.ferrishibernatesupport.saveferris.httpsearchapi.DateFormatDiscover;
import org.hibernate.LazyInitializationException;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by uiradias on 03/09/14.
 */
public class ReflectionUtil {

    private static final Map<Class, List<Field>> fieldsCache = new HashMap<Class, List<Field>>();

    public static boolean isEntity(Class<?> clazz) {
        boolean result = false;
        for (int i = 0; i < clazz.getAnnotations().length; i++) {
            Annotation annotation = clazz.getAnnotations()[i];
            Class<?> type = annotation.annotationType();
            if (type == Entity.class) {
                return true;
            }
        }
        return result;
    }

    public static Object executeGetterOfField(Object entity, Field sourceIdField) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Method sourceGetId =  entity.getClass().getMethod(ReflectionUtil.getGetter(sourceIdField));
        return sourceGetId.invoke(entity);
    }

    public static String getGetter(Field field) {
        String fieldName = field.getName();
        String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return methodName;
    }

    public static String getGetter(String property) {
        return "get" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    public static String getSetter(String property) {
        return "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
    }

    public static boolean isEmbedded(Class<?> clazz) {
        boolean result = false;
        for (int i = 0; i < clazz.getAnnotations().length; i++) {
            Annotation annotation = clazz.getAnnotations()[i];
            Class<?> type = annotation.annotationType();
            if (type == Embeddable.class) {
                return true;
            }
        }
        return result;
    }

    public static List<Field> getNoTransientFields(Class<?> clazz) {
        final List<Field> fieldsCollection = new ArrayList<Field>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            boolean isTransient = false;
            Field field = clazz.getDeclaredFields()[i];
            Annotation[] annotations = field.getAnnotations();
            for (int j = 0; j < annotations.length; j++) {
                if (annotations[j].annotationType() == Transient.class)
                    isTransient = true;
            }
            if (!isTransient) {
                fieldsCollection.add(field);
            }
        }

        return fieldsCollection;
    }

    public static Object invoke(Object entity, String property) {
        final Class<?> clazz = entity.getClass();

        Object invocation = null;
        try {
            Method method = clazz.getMethod(ReflectionUtil.getGetter(property));
            invocation = method.invoke(entity);
        } catch (LazyInitializationException e) {
            throw new LazyInitializationException("");
        } catch (Exception e) {
            //TODO Log something here...
        }

        return invocation;
    }

    public static boolean isEmbedded(Class<?> clazz, String attributePath) {
        try {
            String embeddedCandidateName;
            if (attributePath.contains(".")) {
                embeddedCandidateName = attributePath.substring(0, attributePath.indexOf("."));
                Field fieldCandidate = clazz.getDeclaredField(embeddedCandidateName);
                Class<?> clazzCandidate = fieldCandidate.getType(); // getGetter(fieldCandidate).getClass();
                int length = embeddedCandidateName.length();
                String associationPathCandidate = attributePath.substring(length + 1);
                return isEmbedded(clazzCandidate, associationPathCandidate);
            } else {
                embeddedCandidateName = attributePath;
            }

            final Field field = clazz.getDeclaredField(embeddedCandidateName);
            for (int i = 0; i < field.getAnnotations().length; i++) {
                Annotation annotation = field.getAnnotations()[i];
                if (annotation.annotationType() == EmbeddedId.class) {
                    return true;
                }
            }
        } catch (Exception e) {
            //TODO Log something here...
            return false;
        }
        return false;
    }

    public static boolean isCollection(Class<?> clazz, String attributePath) {
        try {
            Field field = getField(clazz, attributePath);
            if (field.getType().isAssignableFrom(List.class)) {
                return true;
            }
        } catch (SecurityException e) {
            //TODO Log something here...
        } catch (NoSuchFieldException e) {
            //TODO Log something here...
        }

        return false;
    }

    public static Field getIdField(Class clazz) {
        try {
            final List<Field> declaredFields = getDeclaredFields(clazz);

            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Id.class)){
                    return field;
                }
            }
        } catch (SecurityException e) {
            //TODO Log something here...
        }

        return null;
    }

    public static Field getField(Class clazz, String attribute) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(attribute);
        } catch (NoSuchFieldException e){
            final Class superClass = clazz.getSuperclass();
            if(superClass != null){
                return getField(superClass, attribute);
            } else{
                throw e;
            }
        }
    }

    public static List<Field> getDeclaredFields(Class clazz) {
        if(fieldsCache.containsKey(clazz)){
            return fieldsCache.get(clazz);
        } else {
            final List<Field> fields = new ArrayList<Field>();
            Collections.addAll(fields, clazz.getDeclaredFields());

            Class superClass = clazz.getSuperclass();
            while(superClass != null){
                Collections.addAll(fields, superClass.getDeclaredFields());
                superClass = superClass.getSuperclass();
            }

            fieldsCache.put(clazz, fields);
            return fields;
        }
    }

    public static boolean isOneToMany(Field fieldSource) {
        if(fieldSource.getAnnotation(OneToMany.class) != null){
            return true;
        }

        return false;
    }

    public static boolean isOneToManyWithOrphanRemoval(Field fieldSource) {
        if(fieldSource.getAnnotation(OneToMany.class) != null && fieldSource.getAnnotation(OneToMany.class).orphanRemoval()){
            return true;
        }

        return false;
    }

    public static boolean isOneToManyWithoutOrphanRemoval(Field fieldSource) {
        if(fieldSource.getAnnotation(OneToMany.class) != null && !fieldSource.getAnnotation(OneToMany.class).orphanRemoval()){
            return true;
        }

        return false;
    }

    public static boolean isManyToOne(Field fieldSource) {
        if(fieldSource.getAnnotation(ManyToOne.class) != null){
            return true;
        }

        return false;
    }

    public static Class<?> getGenericType(Field field)
    {
        Class<?> clazz = Object.class;
        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType)
        {
            Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();

            if (types.length > 0)
            {
                Type type = types[0];

                if (type instanceof TypeVariable) {
                    clazz = (Class<?>) ((TypeVariable<?>) type).getGenericDeclaration();
                } else {
                    clazz = (Class<?>) type;
                }
            }
        }
        return clazz;
    }

    public static String getBooleanVariationGetter(Field field) {
        String fieldName = field.getName();
        String methodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return methodName;
    }


    public static Object getObjectWithType(Class<?> clazz, String attr, Object value) throws SearchCriteriaSyntaxException {
        try {
            return castDynamicClass(returnObjectType(clazz, attr, value), value.toString());
        } catch (IllegalArgumentException e) {
            throw new SearchCriteriaSyntaxException("IllegalArgumentException");
        } catch (SecurityException e) {
            throw new SearchCriteriaSyntaxException("SecurityException");
        } catch (InstantiationException e) {
            throw new SearchCriteriaSyntaxException("The constructor to this was not found");
        } catch (IllegalAccessException e) {
            throw new SearchCriteriaSyntaxException("IllegalAccessException");
        } catch (InvocationTargetException e) {
            throw new SearchCriteriaSyntaxException("IllegalAccessException");
        } catch (NoSuchMethodException e) {
            throw new SearchCriteriaSyntaxException("The field "+ attr + " does not belong to the model " + clazz.getName());
        } catch (NoSuchFieldException e) {
            throw new SearchCriteriaSyntaxException("The field "+ attr + " does not belong to the model " + clazz.getName());
        }
    }

    public static Class<? extends Object> returnObjectType(Class<? extends Object> model, String attr, Object value) throws SecurityException, NoSuchFieldException, SearchCriteriaSyntaxException {
        Class<?> parentClass = model;
        String[] pathArray = attr.split("[.]");
        for (int i = 0; i < pathArray.length; i++) {
            if(pathArray.length==1){
                return getClassBy(pathArray[0], model);
            }

            parentClass = getClassBy(pathArray[i], parentClass);
        }

        return parentClass;
    }

    private static Class<? extends Object> getClassBy(String attr, Class<? extends Object> parentClass) throws SecurityException, NoSuchFieldException, SearchCriteriaSyntaxException{
        Field field = getFieldFromClassAndInheritance(attr, parentClass);
        if(field != null){
            if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType listType = (ParameterizedType) field
                        .getGenericType();
                return (Class<?>) listType.getActualTypeArguments()[0];
            } else {
                return field.getType();
            }
        }
        throw new SearchCriteriaSyntaxException();
    }


    private static Field getFieldFromClassAndInheritance(String attr, Class<?> clazz) throws SecurityException, SearchCriteriaSyntaxException {
        Field field;
        try {
            field = clazz.getDeclaredField(attr);
        } catch (NoSuchFieldException e) {
            if(clazz.getSuperclass() != null){
                return getFieldFromClassAndInheritance(attr, clazz.getSuperclass());
            }
            throw new SearchCriteriaSyntaxException();
        }
        return field;
    }

    private static Date convertObjectToDate(Object value) {
        String dateFormat = DateFormatDiscover.discoverDateFormatForString((String) value);
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse((String) value);
        } catch (ParseException e) {
            throw new UnsupportedDateFormatException();
        }
    }

    public static Object castDynamicClass(Class<?> clazz, String value) throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        if (clazz == Date.class) {
            return convertObjectToDate(value);
        }
        if(clazz.isEnum()){
            return Enum.valueOf((Class<Enum>)clazz, value);
        }
        if (clazz == Character.class) {
            return convertObjectToCharacter(value);
        }
        Constructor<?> cons = (Constructor<?>) clazz.getConstructor(new Class<?>[] { String.class });
        return (Object) cons.newInstance(new Object[] { value });
    }

    private static Character convertObjectToCharacter(String value) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        Constructor<Character> constructor = Character.class.getConstructor(char.class);
        return (Character) constructor.newInstance(value.charAt(0));
    }
}
