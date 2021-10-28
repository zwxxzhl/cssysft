package com.atguigu.aclservice.util;

import com.atguigu.aclservice.config.gson.GsonTypeAdapter;
import com.atguigu.aclservice.enums.ExpParamsEnum;
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
import java.util.*;

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
     * 下划线 转 驼峰
     * user_name  ---->  userName
     * userName   --->  userName
     */
    public static String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                buffer.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 驼峰 转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append("_").append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 通用 QueryWrapper 查询参数初始化
     */
    public static <E> QueryWrapper<E> queryWrapperParams(QueryWrapper<E> wrapper, Map<String, Object> params) {
        Set<String> keys = params.keySet();
        keys.forEach(k -> {
            if (ExpParamsEnum.ASC.getCode().equals(k)) {
                wrapper.orderByAsc(!StringUtils.isEmpty(params.get(k)), ((String) params.get(k)).split(","));
            } else if (ExpParamsEnum.DESC.getCode().equals(k)) {
                wrapper.orderByDesc(!StringUtils.isEmpty(params.get(k)), ((String) params.get(k)).split(","));
            } else {
                String[] s = k.split(ExpParamsEnum.JOIN.getCode());
                if (ExpParamsEnum.BETWEEN.getCode().equals(s[0])) {
                    String[] s1 = s[1].split(ExpParamsEnum.MID_JOIN.getCode());
                    if (ExpParamsEnum.PRE.getCode().equals(s1[0])) {
                        String sufKey = ExpParamsEnum.BETWEEN.getCode() + ExpParamsEnum.JOIN.getCode() + ExpParamsEnum.SUF.getCode() + ExpParamsEnum.MID_JOIN.getCode() + s1[1];
                        wrapper.between(!StringUtils.isEmpty(params.get(k)) && !StringUtils.isEmpty(params.get(sufKey)), toUnderlineCase(s1[1]), params.get(k), params.get(sufKey));
                    }
                } else if (ExpParamsEnum.NOT_BETWEEN.getCode().equals(s[0])) {
                    String[] s1 = s[1].split(ExpParamsEnum.MID_JOIN.getCode());
                    if (ExpParamsEnum.PRE.getCode().equals(s1[0])) {
                        String sufKey = ExpParamsEnum.NOT_BETWEEN.getCode() + ExpParamsEnum.JOIN.getCode() + ExpParamsEnum.SUF.getCode() + ExpParamsEnum.MID_JOIN.getCode() + s1[1];
                        wrapper.between(!StringUtils.isEmpty(params.get(k)) && !StringUtils.isEmpty(params.get(sufKey)), toUnderlineCase(s1[1]), params.get(k), params.get(sufKey));
                    }
                } else if (ExpParamsEnum.EQ.getCode().equals(s[0])) {
                    wrapper.eq(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.NE.getCode().equals(s[0])) {
                    wrapper.ne(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.GT.getCode().equals(s[0])) {
                    wrapper.gt(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.GE.getCode().equals(s[0])) {
                    wrapper.ge(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.LT.getCode().equals(s[0])) {
                    wrapper.lt(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.LE.getCode().equals(s[0])) {
                    wrapper.le(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.LIKE.getCode().equals(s[0])) {
                    wrapper.like(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.NOT_LIKE.getCode().equals(s[0])) {
                    wrapper.notLike(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.LIKE_LEFT.getCode().equals(s[0])) {
                    wrapper.likeLeft(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.LIKE_RIGHT.getCode().equals(s[0])) {
                    wrapper.likeRight(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), params.get(k));
                } else if (ExpParamsEnum.OR_SAME.getCode().equals(s[0])) {
                    // 示例：qrSame__pre_code / qrSame__suf_code
                    String[] s1 = s[1].split(ExpParamsEnum.MID_JOIN.getCode());
                    if (ExpParamsEnum.PRE.getCode().equals(s1[0])) {
                        String sufKey = ExpParamsEnum.OR_SAME.getCode() + ExpParamsEnum.JOIN.getCode() + ExpParamsEnum.SUF.getCode() + ExpParamsEnum.MID_JOIN.getCode() + s1[1];

                        wrapper.and(!StringUtils.isEmpty(params.get(k)) && !StringUtils.isEmpty(params.get(sufKey)), (wp) -> wp
                                .eq(toUnderlineCase(s1[1]), params.get(k))
                                .or()
                                .eq(toUnderlineCase(s1[1]), params.get(sufKey)));
                    }
                } else if (ExpParamsEnum.OR_DIFF.getCode().equals(s[0])) {
                    // 示例：qrDiff__code#name_code / qrDiff__code#name_name
                    String[] s1 = s[1].split(ExpParamsEnum.MID_JOIN.getCode());
                    String[] ks = s1[0].split(ExpParamsEnum.OR_JOIN.getCode());
                    String otherKey = ExpParamsEnum.OR_DIFF.getCode() + ExpParamsEnum.JOIN.getCode() + s1[0] + ExpParamsEnum.MID_JOIN.getCode() + ks[1];

                    wrapper.and(!StringUtils.isEmpty(params.get(k)) && !StringUtils.isEmpty(params.get(otherKey)), (wp) -> wp
                            .eq(toUnderlineCase(ks[0]), params.get(k))
                            .or()
                            .eq(toUnderlineCase(ks[1]), params.get(otherKey)));
                } else if (ExpParamsEnum.IN.getCode().equals(s[0])) {
                    wrapper.in(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), Arrays.asList(((String) params.get(k)).split(",")));
                } else if (ExpParamsEnum.NOT_IN.getCode().equals(s[0])) {
                    wrapper.notIn(!StringUtils.isEmpty(params.get(k)), toUnderlineCase(s[1]), Arrays.asList(((String) params.get(k)).split(",")));
                }
            }
        });
        return wrapper;
    }

}
