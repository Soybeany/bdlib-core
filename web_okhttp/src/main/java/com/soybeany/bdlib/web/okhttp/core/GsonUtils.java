package com.soybeany.bdlib.web.okhttp.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * <br>Created by Soybeany on 2019/7/9.
 */
public class GsonUtils {

    public static final Gson DEFAULT_GSON = new Gson();

    private GsonUtils() {
    }

    /**
     * 转Dto（普通型）
     */
    public static <T> T toDto(String json, Class<T> aClass) {
        return toDto(json, aClass, DEFAULT_GSON);
    }

    /**
     * 转Dto（普通型）
     */
    public static <T> T toDto(String json, Class<T> aClass, Gson gson) {
        return gson.fromJson(json, aClass);
    }

    /**
     * 转Dto（带泛型的）
     *
     * @param typeToken 参照 new TypeToken&lt;List&lt;User&gt;&gt;(){}
     */
    public static <T> T toDto(String json, TypeToken<T> typeToken) {
        return toDto(json, typeToken, DEFAULT_GSON);
    }

    /**
     * 转Dto（带泛型的）
     *
     * @param typeToken 参照 new TypeToken&lt;List&lt;User&gt;&gt;(){}
     */
    public static <T> T toDto(String json, TypeToken<T> typeToken, Gson gson) {
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     * 转Json（普通型）
     */
    public static String toJson(Object object) {
        return toJson(object, DEFAULT_GSON);
    }

    /**
     * 转Json（普通型）
     */
    public static String toJson(Object object, Gson gson) {
        return gson.toJson(object);
    }

    /**
     * 获得自定义命名的gson对象<br>key:自定义的key，value:原key
     */
    public static Gson getFieldNamingGson(final Map<String, String> nameMap) {
        return new GsonBuilder().setFieldNamingStrategy(f -> {
            String originalName = nameMap.get(f.getName());
            if (null != originalName) {
                return originalName;
            }
            return f.getName();
        }).create();
    }

}
