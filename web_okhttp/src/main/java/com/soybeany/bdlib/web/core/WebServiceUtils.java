package com.soybeany.bdlib.web.core;

import com.soybeany.bdlib.web.core.request.DataBuilder;
import com.soybeany.bdlib.web.core.request.IRequestFactory;

import java.io.File;


/**
 * <br>Created by Soybeany on 2019/2/26.
 */
public class WebServiceUtils {

    public static DataBuilder.Get get(String url) {
        return new DataBuilder.Get(url);
    }

    public static DataBuilder.Post post(String url) {
        return new DataBuilder.Post(url);
    }

    public static DataBuilder.PostFile postFile(String url, File file) {
        return new DataBuilder.PostFile(url, file);
    }

    public static DataBuilder.PostJson postJson(String url) {
        return new DataBuilder.PostJson(url);
    }

    public static DataBuilder.PostForm postForm(String url) {
        return new DataBuilder.PostForm(url);
    }

    public static DataBuilder.Multipart multiPart(String url) {
        return new DataBuilder.Multipart(url);
    }

    /**
     * 取消指定标签的请求
     */
    public static void cancel(IRequestFactory factory, Object tag) {
        factory.cancel(tag);
    }

    private WebServiceUtils() {

    }
}
