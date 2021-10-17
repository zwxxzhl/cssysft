package com.atguigu.aclservice.util;

import com.atguigu.aclservice.config.gson.GsonTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

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

}
