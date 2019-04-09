package com.soybeany.bdlib.web.core.request;

import com.soybeany.bdlib.web.core.Data;
import com.soybeany.bdlib.web.core.ICallback;

import java.util.List;

/**
 * <br>Created by Soybeany on 2019/2/28.
 */
public interface IPreprocessor {
    /**
     * 用于请求前的参数预处理(如加密)
     */
    default Data onHandleRequest(Data data) {
        return data;
    }

    /**
     * 用于回调的预处理(如使部分回调在指定线程中执行)
     */
    default <T> List<ICallback<T>> onHandleCallbacks(Data data, IRequest request, List<ICallback<T>> callbacks) {
        return callbacks;
    }

    /**
     * 用于请求后对响应的预处理(如解密、自动重发、自动登录等)
     *
     * @param response 若与返回的response不同，则需自行将其关闭
     */
    default IResponse onHandleResponse(Data data, IRequest request, IResponse response) throws Exception {
        return response;
    }
}
