package com.soybeany.bdlib.web.okhttp;

import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.okhttp.counting.CountingResponseBody;
import com.soybeany.bdlib.web.okhttp.parser.IParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.soybeany.bdlib.core.util.IterableUtils.forEach;
import static com.soybeany.bdlib.web.okhttp.ICallback.CODE_NOT_DEFINE;

/**
 * <br>Created by Soybeany on 2019/4/9.
 */
public class OkHttpCallback<Result> implements Callback {
    private IParser<Result> mParser;
    private IProgressListener mDownloadListener; // 下载监听器
    private List<ICallback<Result>> mCallbacks = new LinkedList<>(); // 回调集

    public OkHttpCallback(IParser<Result> parser) {
        mParser = parser;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public void onFailure(Call call, IOException e) {
        boolean isCanceled = call.isCanceled();
        invokeFailureCallback(isCanceled, false, false, CODE_NOT_DEFINE, e);
        forEach(mCallbacks, (callback, flag) -> callback.onFinal(isCanceled));
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
        forEach(mCallbacks, (callback, flag) -> callback.onFinal(false));
    }

    // //////////////////////////////////设置方法//////////////////////////////////

    /**
     * 设置下载监听器
     */
    public OkHttpCallback<Result> downloadListener(IProgressListener listener) {
        mDownloadListener = listener;
        return this;
    }

    /**
     * 添加自定义回调
     */
    public OkHttpCallback<Result> addCallback(ICallback<Result> listener) {
        mCallbacks.add(listener);
        return this;
    }

    /**
     * 移除自定义回调
     */
    public OkHttpCallback<Result> removeCallback(ICallback<Result> listener) {
        mCallbacks.remove(listener);
        return this;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private ResponseBody getResponseBody(Response response) {
        ResponseBody body = response.body();
        return null != mDownloadListener ? new CountingResponseBody(body, mDownloadListener) : body;
    }

    private void parseResponse(int code, ResponseBody body) {
        try {
            Result result = mParser.parse(body, null);
            forEach(mCallbacks, (callback, flag) -> callback.onSuccess(result));
        } catch (Exception e) {
            invokeFailureCallback(false, true, true, code, mParser.onParseException(e));
        }
    }

    private void invokeFailureCallback(boolean isCanceled, boolean hasResponse, boolean isHttpSuccess, int code, Exception e) {
        forEach(mCallbacks, (callback, flag) -> callback.onFailure(isCanceled, callback.onParseExceptionMsg(isCanceled, hasResponse, isHttpSuccess, code, e)));
    }
}
