package com.atguigu.aclservice.util;

import org.springframework.beans.BeanUtils;
import org.springframework.data.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ReflectCusUtil {

    /**
     * 获取类注解
     */
    public static <T, A extends Annotation> A getClassAnnotation(Class<T> clazzT, Class<A> clazzA) {
        if (clazzT.isAnnotationPresent(clazzA)) {
            return clazzT.getAnnotation(clazzA);
        }
        return null;
    }

    /**
     * 获取类属性注解
     */
    public static <T, A extends Annotation> A getPropAnnotation(Class<T> clazzT, Class<A> clazzA) {
        Field[] fields = clazzT.getDeclaredFields();
        for(Field field: fields){
            if(field.isAnnotationPresent(clazzA)){
                return field.getAnnotation(clazzA);
            }
        }
        return null;
    }

    /**
     * 获取对象属性列表
     */
    public static <T> List<String> getEntityProps(Class<T> clazz, List<String> exclude) {
        List<String> columns = new ArrayList<>();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        if (propertyDescriptors.length > 0) {
            for (PropertyDescriptor prop : propertyDescriptors) {
                if (!"class".equals(prop.getName())) {
                    if (!exclude.contains(prop.getName())) {
                        columns.add(prop.getName());
                    }
                }
            }
        }
        return columns;
    }

    /**
     * 通过 ReflectionUtils 获取对象属性值
     */
    public static Object getValue(Object o, String name) throws IllegalAccessException {
        Field requiredField = ReflectionUtils.findRequiredField(o.getClass(), name);
        requiredField.setAccessible(true);
        return requiredField.get(o);
    }

    /**
     * 通过 ReflectionUtils 设置对象属性值
     */
    public static void setValue(Object o, String name, Object value) throws IllegalAccessException {
        Field requiredField = ReflectionUtils.findRequiredField(o.getClass(), name);
        requiredField.setAccessible(true);
        requiredField.set(o, value);
    }

    /**
     * 通过 BeanUtils 获取对象属性值
     */
    public static Object getObjectValue(Object o, String propertyName) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(o.getClass(), propertyName);
        assert pd != null;
        return pd.getReadMethod().invoke(o);
    }

    /**
     * 通过 BeanUtils 设置对象属性值
     */
    public static void setObjectValue(Object o, String propertyName, Object value) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(o.getClass(), propertyName);
        assert pd != null;
        pd.getWriteMethod().invoke(o, value);
    }


}
