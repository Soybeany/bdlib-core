package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.web.core.request.IResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public class OkHttpResponse implements IResponse {
    private final Response mResponse;
    private final ResponseBody mBody;

    public OkHttpResponse(Response response, ResponseBody body) {
        mResponse = response;
        mBody = body;
    }

    @Override
    public boolean isHttpSuccess() {
        return mResponse.isSuccessful();
    }

    @Override
    public int httpCode() {
        return mResponse.code();
    }

    @Override
    public String requestUrl() {
        return mResponse.request().url().toString();
    }

    @Override
    public Map<String, String> headers() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : mResponse.headers().toMultimap().entrySet()) {
            map.put(entry.getKey(), entry.getValue().get(0));
        }
        return map;
    }

    @Override
    public Reader charStream() {
        return mBody.charStream();
    }

    @Override
    public InputStream byteStream() {
        return mBody.byteStream();
    }

    @Override
    public long contentLength() {
        return mBody.contentLength();
    }

    @Override
    public String contentType() {
        MediaType type = mBody.contentType();
        return null != type ? type.toString() : null;
    }

    @Override
    public void close() throws IOException {
        mResponse.close();
    }
}
