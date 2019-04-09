package com.soybeany.bdlib.web.core.request;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * 响应的封装
 * <br>Created by Soybeany on 2019/2/26.
 */
public interface IResponse extends Closeable {

    /**
     * http请求是否成功
     */
    boolean isHttpSuccess();

    /**
     * http状态码
     */
    int httpCode();

    /**
     * 请求的url
     */
    String requestUrl();

    /**
     * 响应头
     */
    Map<String, String> headers();

    /**
     * 字符流
     */
    Reader charStream();

    /**
     * 字节流
     */
    InputStream byteStream();

    /**
     * 内容长度
     */
    long contentLength();

    /**
     * 内容类型
     */
    String contentType();

}
