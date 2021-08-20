package com.zwx.doorbk.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    public static final String ITEMS = "items";
    public static final String TOTAL = "total";
    public static final String DESC = "desc";

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    private Result() {}

    public static Result ok() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(20000);
        result.setMessage("成功");
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(20001);
        result.setMessage("失败");
        return result;
    }

    public Result success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result code(Integer code){
        this.setCode(code);
        return this;
    }

    public Result data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.setData(mapper.readValue(mapper.writeValueAsString(object), new TypeReference<Map<String, Object>>() {}));
        } catch (JsonProcessingException e) {
            this.setData(new HashMap<>(1));
        }
        return this;
    }
}

