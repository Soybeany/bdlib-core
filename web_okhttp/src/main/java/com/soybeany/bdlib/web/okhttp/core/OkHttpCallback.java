package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;
import com.soybeany.bdlib.web.okhttp.parser.IParser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.soybeany.bdlib.web.okhttp.core.ICallback.CODE_NOT_DEFINE;

/**
 * <br>Created by Soybeany on 2019/4/9.
 */
public class OkHttpCallback<Result> implements Callback {
    private IParser<Result> mParser;
    private final Set<IProgressListener> mDownloadListeners = new HashSet<>(); // 下载监听器
    private final Set<ICallback<Result>> mCallbacks = new HashSet<>(); // 回调集

    /**
     * 获得用于区分请求的id
     */
    public static int getCallId(Call call) {
        return System.identityHashCode(call);
    }

    public OkHttpCallback(IParser<Result> parser) {
        mParser = parser;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void onFailure(Call call, IOException e) {
        int id = getCallId(call);
        boolean isCanceled = call.isCanceled();
        invokeFailureCallback(id, isCanceled, false, false, CODE_NOT_DEFINE, e);
        forEach((callback, flag) -> callback.onFinal(id, isCanceled));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void onResponse(Call call, Response response) {
        int id = getCallId(call);
        try (ResponseBody body = getResponseBody(response)) {
            if (response.isSuccessful()) {
                parseResponse(id, response.code(), body);
            } else {
                invokeFailureCallback(id, false, true, false, response.code(), null);
            }
        } catch (Exception e) {
            invokeFailureCallback(id, false, false, false, CODE_NOT_DEFINE, e);
        }
        forEach((callback, flag) -> callback.onFinal(id, false));
    }

    // //////////////////////////////////设置方法//////////////////////////////////

    /**
     * 设置下载监听器
     */
    public OkHttpCallback<Result> addDownloadListener(IProgressListener listener) {
        mDownloadListeners.add(listener);
        return this;
    }

    /**
     * 移除下载监听器
     */
    public OkHttpCallback<Result> removeDownloadListener(IProgressListener listener) {
        mDownloadListeners.remove(listener);
        return this;
    }

    /**
     * 添加自定义回调
     */
    public OkHttpCallback<Result> addCallback(ICallback<Result> callback) {
        mCallbacks.add(callback);
        return this;
    }

    /**
     * 移除自定义回调
     */
    public OkHttpCallback<Result> removeCallback(ICallback<Result> callback) {
        mCallbacks.remove(callback);
        return this;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    protected CountingResponseBody getNewCountResponseBody(ResponseBody body) {
        return new CountingResponseBody(body);
    }

    private ResponseBody getResponseBody(Response response) {
        ResponseBody body = response.body();
        return !mDownloadListeners.isEmpty() ? getNewCountResponseBody(body).listeners(set -> set.addAll(mDownloadListeners)) : body;
    }

    private void parseResponse(int id, int code, ResponseBody body) {
        try {
            Result result = mParser.parse(body, null);
            forEach((callback, flag) -> callback.onSuccess(id, result));
        } catch (Exception e) {
            invokeFailureCallback(id, false, true, true, code, mParser.onParseException(e));
        }
    }

    private void invokeFailureCallback(int id, boolean isCanceled, boolean hasResponse, boolean isHttpSuccess, int code, Exception e) {
        forEach((callback, flag) -> callback.onFailure(id, isCanceled, callback.onParseExceptionMsg(id, isCanceled, hasResponse, isHttpSuccess, code, e)));
    }

    private void forEach(IterableUtils.IVoidCallback<ICallback<Result>> callback) {
        IterableUtils.forEach(mCallbacks, callback);
    }
}
