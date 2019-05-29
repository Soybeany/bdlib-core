package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingRequestBody;

import java.util.HashSet;
import java.util.Set;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * <br>Created by Soybeany on 2019/5/29.
 */
@SuppressWarnings("NullableProblems")
public class OkHttpRequestBuilder extends Request.Builder {
    private final Set<IProgressListener> mUploadListeners = new HashSet<>(); // 上传监听器

    @Override
    public Request.Builder method(String method, RequestBody body) {
        if (null != body && !mUploadListeners.isEmpty()) {
            body = getNewCountingRequestBody(body).listeners(set -> set.addAll(mUploadListeners));
        }
        return super.method(method, body);
    }

    public OkHttpRequestBuilder addUploadListener(IProgressListener listener) {
        mUploadListeners.add(listener);
        return this;
    }

    public OkHttpRequestBuilder removeUploadListener(IProgressListener listener) {
        mUploadListeners.remove(listener);
        return this;
    }

    protected CountingRequestBody getNewCountingRequestBody(RequestBody body) {
        return new CountingRequestBody(body);
    }
}
