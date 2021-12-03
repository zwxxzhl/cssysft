package com.atguigu.aclservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonCusUtil {

    /**
     * Map 转 Bean
     * @param map Map
     * @param clazz 实体类
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(map), clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象List 转 Map数组List
     * @param list 对象数据
     * @param dateFormat 日期格式字符串
     */
    public static <T> List<Map<String,Object>> toListMap(List<T> list, String dateFormat) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(list), new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 对象List 转 Map数组List
     * @param list 对象数据
     */
    public static <T> List<Map<String,Object>> toListMap(List<T> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(list), new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
