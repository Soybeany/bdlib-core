package com.soybeany.bdlib.web.okhttp.parser;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.HandledException;

import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/2/26.
 */
public interface IParser<Result> {

    Result parse(ResponseBody body, IProgressListener listener) throws Exception;

    default HandledException onParseException(Exception e) {
        return e instanceof HandledException ? (HandledException) e : new HandledException("响应解析异常:" + e.getMessage());
    }
}
