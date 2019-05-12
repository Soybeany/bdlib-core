package com.soybeany.bdlib.web.okhttp.core;

import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;
import com.soybeany.bdlib.web.okhttp.notify.RequestCallbackMsg;
import com.soybeany.bdlib.web.okhttp.notify.RequestInvokerMsg;
import com.soybeany.bdlib.web.okhttp.parser.IParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
    private final List<IProgressListener> mDownloadListeners = new LinkedList<>(); // 下载监听器
    private final List<ICallback<Result>> mCallbacks = new LinkedList<>(); // 回调集

    private Notifier<RequestInvokerMsg, RequestCallbackMsg> mNotifier;

    public OkHttpCallback(IParser<Result> parser) {
        mParser = parser;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void onFailure(Call call, IOException e) {
        boolean isCanceled = call.isCanceled();
        invokeFailureCallback(isCanceled, false, false, CODE_NOT_DEFINE, e);
        forEach((callback, flag) -> callback.onFinal(isCanceled));
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void onResponse(Call call, Response response) {
        try (ResponseBody body = getResponseBody(response)) {
            if (response.isSuccessful()) {
                parseResponse(response.code(), body);
            } else {
                invokeFailureCallback(false, true, false, response.code(), null);
            }
        } catch (Exception e) {
            invokeFailureCallback(false, false, false, CODE_NOT_DEFINE, e);
        }
        forEach((callback, flag) -> callback.onFinal(false));
    }

    // //////////////////////////////////设置方法//////////////////////////////////

    public void setNotifier(Notifier<RequestInvokerMsg, RequestCallbackMsg> notifier) {
        mNotifier = notifier;
    }

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

    private ResponseBody getResponseBody(Response response) {
        ResponseBody body = response.body();
        return !mDownloadListeners.isEmpty() ? new CountingResponseBody(body, mDownloadListeners, mNotifier) : body;
    }

    private void parseResponse(int code, ResponseBody body) {
        try {
            Result result = mParser.parse(body, null);
            forEach((callback, flag) -> callback.onSuccess(result));
        } catch (Exception e) {
            invokeFailureCallback(false, true, true, code, mParser.onParseException(e));
        }
    }

    private void invokeFailureCallback(boolean isCanceled, boolean hasResponse, boolean isHttpSuccess, int code, Exception e) {
        forEach((callback, flag) -> callback.onFailure(isCanceled, callback.onParseExceptionMsg(isCanceled, hasResponse, isHttpSuccess, code, e)));
    }

    private void forEach(IterableUtils.IVoidCallback<ICallback<Result>> callback) {
        IterableUtils.forEach(mCallbacks, callback);
    }
}
