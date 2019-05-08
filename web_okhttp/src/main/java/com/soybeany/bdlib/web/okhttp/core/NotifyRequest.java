package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.file.FileUtils;

import okhttp3.Request;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class NotifyRequest {
    public Request request;
    public String key;

    public NotifyRequest(Request request, String key) {
        this.request = request;
        this.key = null != key ? key : FileUtils.getUUID();
    }
}
