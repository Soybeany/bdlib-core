package com.soybeany.bdlib.web.core;


/**
 * <br>Created by Soybeany on 2019/2/20.
 */
public interface ICallback<Result> {

    default void onInit(Data data) {
    }

    default String onGetExceptionMsg(boolean isCanceled, boolean hasResponse, boolean isHttpSuccess, int code, Exception e, Data data) {
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

    void onSuccess(Result result, Data data);

    void onFailure(boolean isCanceled, String msg, Data data);

    default void onFinal(boolean isCanceled, Data data) {
    }

    interface Empty<Result> extends ICallback<Result> {
        @Override
        default void onSuccess(Result result, Data data) {
        }

        @Override
        default void onFailure(boolean isCanceled, String msg, Data data) {
        }
    }

    class Wrapper<Result> implements ICallback<Result> {
        private ICallback<Result> mCallback;

        public Wrapper(ICallback<Result> callback) {
            mCallback = callback;
        }

        @Override
        public void onInit(Data data) {
            mCallback.onInit(data);
        }

        @Override
        public String onGetExceptionMsg(boolean isCancel, boolean hasResponse, boolean isHttpSuccess, int code, Exception e, Data data) {
            return mCallback.onGetExceptionMsg(isCancel, hasResponse, isHttpSuccess, code, e, data);
        }

        @Override
        public void onSuccess(Result result, Data data) {
            mCallback.onSuccess(result, data);
        }

        @Override
        public void onFailure(boolean isCanceled, String msg, Data data) {
            mCallback.onFailure(isCanceled, msg, data);
        }

        @Override
        public void onFinal(boolean isCanceled, Data data) {
            mCallback.onFinal(isCanceled, data);
        }
    }
}
