package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.IterableUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

/**
 * 参数添加器
 * <br>Created by Soybeany on 2019/5/29.
 */
public class ParamAppender {
    private Map<String, String> mMap = new HashMap<>();

    // //////////////////////////////////设置区//////////////////////////////////

    /**
     * 使用键值对
     */
    public ParamAppender add(String key, String value) {
        if (null != key && null != value) {
            mMap.put(key, value);
        }
        return this;
    }

    /**
     * 使用映射中的值
     */
    public ParamAppender add(Map<String, String> map) {
        if (null != map) {
            IterableUtils.forEach(map.entrySet(), (entry, flag) -> add(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    /**
     * 使用对象的public字段及值
     */
    public ParamAppender add(IParamProvider provider) {
        IterableUtils.forEach(Arrays.asList(provider.getClass().getFields()),
                (field, flag) -> mMap.put(field.getName(), field.get(provider).toString()));
        return this;
    }

    // //////////////////////////////////输出区//////////////////////////////////

    public String toUrl(String url) {
        return URLParser.mergeUrl(url, mMap);
    }

    public Map<String, String> toMap() {
        return new HashMap<>(mMap);
    }

    public FormBody.Builder toNewFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        toFormBody(builder);
        return builder;
    }

    public void toFormBody(FormBody.Builder builder) {
        for (Map.Entry<String, String> entry : mMap.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
    }

    // //////////////////////////////////继承区//////////////////////////////////

    protected Map<String, String> getParams() {
        return mMap;
    }

    // //////////////////////////////////接口区//////////////////////////////////

    /**
     * 标识符接口
     */
    public interface IParamProvider {
    }

}
