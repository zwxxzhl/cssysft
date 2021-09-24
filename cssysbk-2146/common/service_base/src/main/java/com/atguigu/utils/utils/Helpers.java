package com.atguigu.utils.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

public class Helpers {

    /**
     * 判断字符串为空
     */
    public static boolean isBlank(String str) {
        return StringUtils.isEmpty(StringUtils.trimWhitespace(str));
    }

    /**
     * 判断字符串不为空
     */
    public static boolean isNotBlank(String str) {
        return !StringUtils.isEmpty(StringUtils.trimWhitespace(str));
    }

    /**
     * 获取 Mybatis 查询对象
     */
    public static <T> QueryWrapper<T> getQueryWrapper(Class<T> t) {
        return new QueryWrapper<>();
    }

}
