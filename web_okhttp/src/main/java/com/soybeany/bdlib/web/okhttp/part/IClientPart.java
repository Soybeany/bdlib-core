package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IClientPart<RequestBuilder extends IRequestPart> {
    RequestBuilder newRequest(OkHttpClientFactory.IClientSetter setter);
}
