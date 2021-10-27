package com.atguigu.aclservice.util;

import com.atguigu.aclservice.config.gson.GsonTypeAdapter;
import com.atguigu.utils.utils.Helpers;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AuxProUtil {

    /**
     *  Gson 构建
     */
    public static Gson gsonBuilder(Type type) {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(type, new GsonTypeAdapter())
                .create();
    }

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
     * 获取对象属性值
     */
    public static Object getValue(Object o, String propertyName) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(o.getClass(), propertyName);
        assert pd != null;
        return pd.getReadMethod().invoke(o);
    }

    /**
     * 设置对象属性值
     */
    public static void setValue(Object o, String propertyName, Object value) throws InvocationTargetException, IllegalAccessException {
        PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(o.getClass(), propertyName);
        assert pd != null;
        pd.getWriteMethod().invoke(o, value);
    }

    /**
     * 获取登陆用户
     */
    public static <S extends IService<U>, U> U getLoginUser(S userService) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<U> wrapperU = new QueryWrapper<>();
        wrapperU.eq("username", username);

        List<U> list = userService.list(wrapperU);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 新增绑定user
     */
    public static <U> void bindEntityCreate(Object o, U user) {
        try {
            AuxProUtil.setValue(o, "gmtCreateUser", AuxProUtil.getValue(user, "id"));
            AuxProUtil.setValue(o, "gmtUpdateUser", AuxProUtil.getValue(user, "id"));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("新增绑定user出错");
        }
    }

    /**
     * 更新绑定user
     */
    public static <U> void bindEntityUpdate(Object o, U user) {
        try {
            AuxProUtil.setValue(o, "gmtUpdateUser", AuxProUtil.getValue(user, "id"));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("更新绑定user出错");
        }
    }

    /**
     * 清除线程
     */
    public static void removeThread(ThreadLocal<?> ...tls) {
        for (ThreadLocal<?> tl : tls) {
            tl.remove();
        }
    }

    /**
     * 通用 QueryWrapper 查询参数初始化
     */
    public static <E> QueryWrapper<E> initQueryWrapper(E entity, List<String> exclude, Consumer<QueryWrapper<E>> consumer) {
        QueryWrapper<E> wrapper = new QueryWrapper<>();
        try {
            List<String> entityProps = getEntityProps(entity.getClass(), Collections.emptyList());
            for (String prop : entityProps) {
                if (!exclude.contains(prop)) {
                    Object value = getValue(entity, prop);
                    if (Optional.ofNullable(value).isPresent() && !StringUtils.isEmpty(value)) {
                        wrapper.eq(prop, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("查询参数包装出错");
        }
        consumer.accept(wrapper);
        return wrapper;
    }

}
