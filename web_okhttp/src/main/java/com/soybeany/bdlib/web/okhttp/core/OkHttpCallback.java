package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.IterableUtils.IVoidCallback;
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
        int type = call.isCanceled() ? ICallback.TYPE_CANCEL : ICallback.TYPE_NO_RESPONSE;
        forEach((callback, flag) -> callback.onPreTreat(id, type));
        invokeFailureCallback(id, type, null, e, call);
        forEach((callback, flag) -> callback.onFinal(id, type));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void onResponse(Call call, Response response) {
        int id = getCallId(call);
        forEach((callback, flag) -> callback.onPreTreat(id, ICallback.TYPE_NORM));
        int type;
        String code = response.code() + "";
        try (ResponseBody body = getResponseBody(response)) {
            if (response.isSuccessful()) {
                type = parseResponse(id, call, code, body);
            } else {
                invokeFailureCallback(id, type = ICallback.TYPE_HTTP_ERROR, code, null, call);
            }
        } catch (Exception e) {
            invokeFailureCallback(id, type = ICallback.TYPE_PARSE_ERROR, code, e, call);
        }
        int finalType = type;
        forEach((callback, flag) -> callback.onFinal(id, finalType));
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

    // //////////////////////////////////子类重写//////////////////////////////////

    protected CountingResponseBody getNewCountResponseBody(ResponseBody body) {
        return new CountingResponseBody(body);
    }

    protected IVoidCallback<ICallback<Result>> onInvokeSuccessCallback(int id, Result result, Call call) {
        return (callback, flag) -> callback.onSuccess(id, result);
    }

    protected IVoidCallback<ICallback<Result>> onInvokeFailureCallback(int id, int type, String data, Exception e, Call call) {
        return (callback, flag) -> callback.onFailure(id, type, callback.onParseExceptionMsg(id, type, data, e));
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private ResponseBody getResponseBody(Response response) {
        ResponseBody body = response.body();
        return !mDownloadListeners.isEmpty() ? getNewCountResponseBody(body).listeners(set -> set.addAll(mDownloadListeners)) : body;
    }

    private int parseResponse(int id, Call call, String code, ResponseBody body) {
        int type = ICallback.TYPE_NORM;
        try {
            Result result = mParser.parse(body, null);
            invokeSuccessCallback(id, result, call);
        } catch (Exception e) {
            invokeFailureCallback(id, type = ICallback.TYPE_PARSE_ERROR, code, mParser.onParseException(e), call);
        }
        return type;
    }

    private void invokeSuccessCallback(int id, Result result, Call call) {
        forEach(onInvokeSuccessCallback(id, result, call));
    }

    private void invokeFailureCallback(int id, int type, String data, Exception e, Call call) {
        forEach(onInvokeFailureCallback(id, type, data, e, call));
    }

    private void forEach(IVoidCallback<ICallback<Result>> callback) {
        IterableUtils.forEach(mCallbacks, callback);
    }
}
