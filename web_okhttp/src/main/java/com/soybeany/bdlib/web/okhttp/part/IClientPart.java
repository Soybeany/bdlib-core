package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.web.okhttp.core.OkHttpClientFactory;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IClientPart<RequestPart extends IRequestPart> {
    IClientPart<RequestPart> addSetter(OkHttpClientFactory.IClientSetter setter);

    IClientPart<RequestPart> removeSetter(OkHttpClientFactory.IClientSetter setter);

    RequestPart newRequest();
}
