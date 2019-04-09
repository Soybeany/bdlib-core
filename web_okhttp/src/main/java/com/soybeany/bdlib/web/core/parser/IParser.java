package com.soybeany.bdlib.web.core.parser;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.core.HandledException;
import com.soybeany.bdlib.web.core.request.IResponse;

/**
 * <br>Created by Soybeany on 2019/2/26.
 */
public interface IParser<Result> {

    Result parse(IResponse response, IProgressListener listener) throws Exception;

    default HandledException onParseException(Exception e) {
        e.printStackTrace();
        return e instanceof HandledException ? (HandledException) e : new HandledException("响应解析异常:" + e.getMessage());
    }
}
