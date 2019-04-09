package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.core.Data;
import com.soybeany.bdlib.web.core.ICallback;
import com.soybeany.bdlib.web.core.parser.IParser;
import com.soybeany.bdlib.web.core.request.IPreprocessor;
import com.soybeany.bdlib.web.core.request.IRequest;
import com.soybeany.bdlib.web.core.request.IResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.soybeany.bdlib.core.util.IterableUtils.forEach;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public abstract class OkHttpRequest implements IRequest {

    private OkHttpClient.Builder mClient;
    private Data mData;
    private Call mCall;

    private IProgressListener mDownloadListener;
    private List<IPreprocessor> mPreprocessors = Collections.emptyList();

    public static void setupTimeout(OkHttpClient.Builder builder, long sec) {
        builder.connectTimeout(sec, TimeUnit.SECONDS)
                .readTimeout(sec, TimeUnit.SECONDS)
                .writeTimeout(sec, TimeUnit.SECONDS);
    }

    @Override
    public IRequest addExData(String key, Object value) {
        mData.ex.put(key, value);
        return this;
    }

    @Override
    public IRequest timeout(long sec) {
        setupTimeout(mClient, sec);
        return this;
    }

    @Override
    public IRequest downloadListener(IProgressListener listener) {
        mDownloadListener = listener;
        return this;
    }

    @Override
    public <T> void enqueue(IParser<T> parser, List<ICallback<T>> callbacks) {
        buildCall();
        mCall.enqueue(new InnerCallback<>(parser, wrapCallbacks(callbacks)));
    }

    @Override
    public <T> void sync(IParser<T> parser, List<ICallback<T>> callbacks) {
        buildCall();
        InnerCallback<T> innerCallback = new InnerCallback<>(parser, wrapCallbacks(callbacks));
        Response response;
        try {
            response = mCall.execute();
        } catch (IOException e) {
            innerCallback.onFailure(mCall, e);
            return;
        }
        innerCallback.onResponse(mCall, response);
    }

    @Override
    public IResponse execute() throws Exception {
        buildCall();
        return toIResponse(mCall.execute());
    }

    @Override
    public void cancel() {
        Optional.ofNullable(mCall).ifPresent(Call::cancel);
    }

    Object getRequestTag() {
        return mData.tag;
    }

    OkHttpRequest setup(OkHttpClient.Builder client, Data data, List<IPreprocessor> processors) {
        mClient = client;
        Optional.ofNullable(processors).ifPresent(p -> mPreprocessors = processors);
        mData = data;
        return this;
    }

    private void buildCall() {
        Request.Builder builder = new Request.Builder();
        // 预处理数据
        Data data = forEach(mPreprocessors, mData, (p, d, flag) -> p.onHandleRequest(d));
        // 添加请求头
        for (Map.Entry<String, String> entry : data.headers.entrySet()) {
            Optional.ofNullable(entry.getValue()).ifPresent(value -> builder.header(entry.getKey(), value));
        }
        // 自定义设置请求
        onSetupRequest(builder.url(data.url).tag(this), data);
        cancel(); // 取消先前的请求
        mCall = mClient.build().newCall(builder.build());
    }

    private <T> List<ICallback<T>> wrapCallbacks(List<ICallback<T>> callbacks) {
        List<ICallback<T>> newCallbacks = forEach(mPreprocessors, callbacks, (p, c, flag) -> p.onHandleCallbacks(mData, this, c));
        forEach(newCallbacks, (callback, flag) -> callback.onInit(mData));
        return newCallbacks;
    }

    private IResponse toIResponse(Response response) {
        ResponseBody body = response.body();
        if (null != mDownloadListener) {
            body = new CountingResponseBody(body, mDownloadListener);
        }
        return forEach(mPreprocessors, (IResponse) new OkHttpResponse(response, body), (p, r, flag) -> p.onHandleResponse(mData, this, r));
    }

    private <T> void parseResponse(IResponse response, IParser<T> parser, List<ICallback<T>> callbacks) {
        try {
            T result = parser.parse(response, null);
            forEach(callbacks, (callback, flag) -> callback.onSuccess(result, mData));
        } catch (Exception e) {
            invokeFailureCallback(false, true, true, response.httpCode(), parser.onParseException(e), callbacks);
        }
    }

    private <T> void invokeFailureCallback(boolean isCanceled, boolean hasResponse, boolean isHttpSuccess, int code, Exception e, List<ICallback<T>> callbacks) {
        forEach(callbacks, (callback, flag) -> {
            String msg = callback.onGetExceptionMsg(isCanceled, hasResponse, isHttpSuccess, code, e, mData);
            callback.onFailure(isCanceled, msg, mData);
        });
    }

    protected abstract void onSetupRequest(Request.Builder builder, Data data);

    @SuppressWarnings("NullableProblems")
    private class InnerCallback<T> implements Callback {
        private IParser<T> mParser;
        private List<ICallback<T>> mCallbacks;

        InnerCallback(IParser<T> parser, List<ICallback<T>> callbacks) {
            mParser = parser;
            mCallbacks = callbacks;
        }

        @Override
        public void onResponse(Call call, Response response) {
            try (IResponse r = toIResponse(response)) {
                if (r.isHttpSuccess()) {
                    parseResponse(r, mParser, mCallbacks);
                } else {
                    invokeFailureCallback(false, true, false, r.httpCode(), null, mCallbacks);
                }
            } catch (Exception e) {
                invokeFailureCallback(false, false, false, -1, e, mCallbacks);
            }
            forEach(mCallbacks, (callback, flag) -> callback.onFinal(false, mData));
        }

        @Override
        public void onFailure(Call call, IOException e) {
            boolean isCanceled = call.isCanceled();
            invokeFailureCallback(isCanceled, false, false, -1, e, mCallbacks);
            forEach(mCallbacks, (callback, flag) -> callback.onFinal(isCanceled, mData));
        }
    }
}
