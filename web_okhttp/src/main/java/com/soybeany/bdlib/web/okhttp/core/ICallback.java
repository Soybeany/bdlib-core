package com.soybeany.bdlib.web.okhttp.core;

/**
 * 自定义回调
 * <br>Created by Soybeany on 2019/2/20.
 */
public interface ICallback<Result> {
    /**
     * 未定义的状态码
     */
    int CODE_NOT_DEFINE = -1;

    /**
     * 将异常解析为信息
     */
    default String onParseExceptionMsg(int id, boolean isCanceled, boolean hasResponse, boolean isHttpSuccess, int code, Exception e) {
        String errMsg = null != e ? e.getMessage() : "缺失异常信息";
        if (e instanceof HandledException) {
            return errMsg;
        }
        if (isCanceled) {
            return "请求已中断(" + errMsg + ")";
        }
        if (!hasResponse) {
            return "无法连接到服务器(" + errMsg + ")";
        }
        if (!isHttpSuccess) {
            return "服务器响应异常" + "(" + code + ")";
        }
        return "意外异常(" + errMsg + ")";
    }

    /**
     * 预处理的回调({@link #onSuccess}及{@link #onFailure}前调用)
     */
    default void onPreTreat(int id, boolean hasResponse) {
    }

    /**
     * 成功时的回调
     */
    void onSuccess(int id, Result result);

    /**
     * 失败时的回调
     */
    void onFailure(int id, boolean isCanceled, String msg);

    /**
     * 最终的回调({@link #onSuccess}及{@link #onFailure}后调用)
     */
    default void onFinal(int id, boolean isCanceled) {
    }

    interface Empty<Result> extends ICallback<Result> {
        @Override
        default void onSuccess(int id, Result result) {
        }

        @Override
        default void onFailure(int id, boolean isCanceled, String msg) {
        }
    }
}
