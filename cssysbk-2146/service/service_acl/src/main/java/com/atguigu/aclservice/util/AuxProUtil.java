package com.atguigu.aclservice.util;

import com.atguigu.aclservice.config.gson.GsonTypeAdapter;
import com.atguigu.aclservice.enums.ExpParamsEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

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
     * 清除线程
     */
    public static void removeThread(ThreadLocal<?> ...tls) {
        for (ThreadLocal<?> tl : tls) {
            tl.remove();
        }
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
            ReflectCusUtil.setValue(o, "gmtCreateUser", ReflectCusUtil.getValue(user, "id"));
            ReflectCusUtil.setValue(o, "gmtUpdateUser", ReflectCusUtil.getValue(user, "id"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("新增绑定user出错");
        }
    }

    /**
     * 更新绑定user
     */
    public static <U> void bindEntityUpdate(Object o, U user) {
        try {
            ReflectCusUtil.setValue(o, "gmtUpdateUser", ReflectCusUtil.getValue(user, "id"));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("更新绑定user出错");
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
     *  json 参数绑定 QueryWrapper
     */
    public static <E> QueryWrapper<E> jsonParamsWrapper(QueryWrapper<E> wrapper, Map<String, Object> jsonParams) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> list = new ArrayList<>();
        jsonParams.forEach((k, v) -> {
            Map<String, Object> km = null;
            try {
                km = objectMapper.readValue(jsonParams.get(k).toString(), new TypeReference<Map<String,Object>>(){});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (Optional.ofNullable(km).isPresent()) {
                list.add(km);
            }
        });

        for (Map<String, Object> m : list) {
            if (!StringUtils.isEmpty(m.get(ExpParamsEnum.EXP.getCode()))
                    && !StringUtils.isEmpty(m.get(ExpParamsEnum.PROP.getCode()))) {

                if (ExpParamsEnum.IN.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.in(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()),
                            Arrays.asList(m.get(ExpParamsEnum.VAL.getCode()).toString().split(",")));
                } else if (ExpParamsEnum.NOT_IN.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.notIn(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()),
                            Arrays.asList(m.get(ExpParamsEnum.VAL.getCode()).toString().split(",")));
                } else if (ExpParamsEnum.EQ.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.eq(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.NE.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.ne(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.GT.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.gt(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.GE.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.ge(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.LT.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.lt(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.LE.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.le(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.LIKE.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.like(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.LIKE_LEFT.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.likeLeft(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.LIKE_RIGHT.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.likeRight(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.NOT_LIKE.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.notLike(!StringUtils.isEmpty(m.get(ExpParamsEnum.VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PROP.getCode()).toString()), m.get(ExpParamsEnum.VAL.getCode()));
                } else if (ExpParamsEnum.ASC.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    if (!StringUtils.isEmpty(m.get(ExpParamsEnum.PROP.getCode()))) {
                        String[] props = m.get(ExpParamsEnum.PROP.getCode()).toString().split(",");
                        String[] columns = new String[props.length];
                        for (int i = 0; i < props.length; i++) {
                            columns[i] = toUnderlineCase(props[i]);
                        }
                        wrapper.orderByAsc(columns);
                    }
                } else if (ExpParamsEnum.DESC.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    if (!StringUtils.isEmpty(m.get(ExpParamsEnum.PROP.getCode()))) {
                        String[] props = m.get(ExpParamsEnum.PROP.getCode()).toString().split(",");
                        String[] columns = new String[props.length];
                        for (int i = 0; i < props.length; i++) {
                            columns[i] = toUnderlineCase(props[i]);
                        }
                        wrapper.orderByDesc(columns);
                    }
                }
            } else if (!StringUtils.isEmpty(m.get(ExpParamsEnum.EXP.getCode()))
                    && !StringUtils.isEmpty(m.get(ExpParamsEnum.PRE_PROP.getCode()))
                    && !StringUtils.isEmpty(m.get(ExpParamsEnum.SUF_PROP.getCode()))) {

                if (ExpParamsEnum.BETWEEN.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.between(!StringUtils.isEmpty(m.get(ExpParamsEnum.PRE_VAL.getCode()))
                                    && !StringUtils.isEmpty(m.get(ExpParamsEnum.SUF_VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PRE_PROP.getCode()).toString()),
                            m.get(ExpParamsEnum.PRE_VAL.getCode()), m.get(ExpParamsEnum.SUF_VAL.getCode()));
                } else if (ExpParamsEnum.NOT_BETWEEN.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.notBetween(!StringUtils.isEmpty(m.get(ExpParamsEnum.PRE_VAL.getCode()))
                                    && !StringUtils.isEmpty(m.get(ExpParamsEnum.SUF_VAL.getCode())),
                            toUnderlineCase(m.get(ExpParamsEnum.PRE_PROP.getCode()).toString()),
                            m.get(ExpParamsEnum.PRE_VAL.getCode()), m.get(ExpParamsEnum.SUF_VAL.getCode()));
                } else if (ExpParamsEnum.OR.getCode().equals(m.get(ExpParamsEnum.EXP.getCode()))) {
                    wrapper.and((wp) -> wp
                            .eq(toUnderlineCase(m.get(ExpParamsEnum.PRE_PROP.getCode()).toString()), m.get(ExpParamsEnum.PRE_VAL.getCode()))
                            .or()
                            .eq(toUnderlineCase(m.get(ExpParamsEnum.SUF_PROP.getCode()).toString()), m.get(ExpParamsEnum.SUF_VAL.getCode())));

                }
            }
        }

        return wrapper;
    }

}
