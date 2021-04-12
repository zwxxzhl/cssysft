package com.atguigu.utils.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class R {

    public static final String ITEMS = "items";
    public static final String TOTAL = "total";
    public static final String DESC = "desc";

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    /**
     * 构造方法私有
     */
    private R() {}

    public static R ok() {
        R r = new R();
        r.setSuccess(true);
        r.setCode(20000);
        r.setMessage("成功");
        return r;
    }

    public static R error() {
        R r = new R();
        r.setSuccess(false);
        r.setCode(20001);
        r.setMessage("失败");
        return r;
    }

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.setData(mapper.readValue(mapper.writeValueAsString(object), new TypeReference<Map<String, Object>>() {}));
        } catch (JsonProcessingException e) {
            this.setData(new HashMap<>(1));
        }
        return this;
    }
}
