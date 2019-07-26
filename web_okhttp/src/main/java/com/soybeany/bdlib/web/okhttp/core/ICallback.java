package com.soybeany.bdlib.web.okhttp.core;

/**
 * 自定义回调
 * <br>Created by Soybeany on 2019/2/20.
 */
public interface ICallback<Result> {

    // //////////////////////////////////状态区//////////////////////////////////

    /**
     * 正常 无data
     */
    int TYPE_NORM = 0;

    /**
     * 取消  无data
     */
    int TYPE_CANCEL = -1;

    /**
     * 无响应  无data
     */
    int TYPE_NO_RESPONSE = -2;

    /**
     * HTTP层面的错误  data:状态码
     */
    int TYPE_HTTP_ERROR = -3;

    /**
     * 解析错误 data:状态码
     */
    int TYPE_PARSE_ERROR = -4;

    // //////////////////////////////////方法区//////////////////////////////////

    /**
     * 使用标准方式解析提示语
     */
    static String parseStdMsg(int type, String data, Exception e) {
        String errMsg = (null != e ? e.getMessage() : "缺失异常信息");
        if (e instanceof HandledException) {
            return errMsg;
        }
        switch (type) {
            case TYPE_CANCEL:
                return "请求已中断(" + errMsg + ")";
            case TYPE_NO_RESPONSE:
                return "无法连接到服务器(" + errMsg + ")";
            case TYPE_HTTP_ERROR:
                return "服务器响应异常" + "(" + data + ")";
            case TYPE_PARSE_ERROR:
                return "解析异常(" + errMsg + ")";
        }
        return "意外异常(" + errMsg + ")";
    }

    /**
     * 将异常解析为信息
     */
    default String onParseExceptionMsg(int id, int type, String data, Exception e) {
        return parseStdMsg(type, data, e);
    }

    /**
     * 预处理的回调({@link #onSuccess}及{@link #onFailure}前调用)
     */
    default void onPreTreat(int id, int type) {
    }

    /**
     * 成功时的回调
     */
    void onSuccess(int id, Result result);

    /**
     * 失败时的回调
     *
     * @param type 失败类型 含{@link #TYPE_CANCEL}、{@link #TYPE_NO_RESPONSE}、{@link #TYPE_HTTP_ERROR}等
     */
    void onFailure(int id, int type, String msg);

    /**
     * 最终的回调({@link #onSuccess}及{@link #onFailure}后调用)
     */
    default void onFinal(int id, int type) {
    }

    // //////////////////////////////////拓展//////////////////////////////////

    interface Empty<Result> extends ICallback<Result> {
        @Override
        default void onSuccess(int id, Result result) {
        }

        @Override
        default void onFailure(int id, int type, String msg) {
        }
    }

    class Wrapper<Result> implements ICallback<Result> {
        private ICallback<Result> mTarget;

        public Wrapper(ICallback<Result> target) {
            mTarget = target;
        }

        @Override
        public String onParseExceptionMsg(int id, int type, String data, Exception e) {
            return mTarget.onParseExceptionMsg(id, type, data, e);
        }

        @Override
        public void onPreTreat(int id, int type) {
            mTarget.onPreTreat(id, type);
        }

        @Override
        public void onSuccess(int id, Result result) {
            mTarget.onSuccess(id, result);
        }

        @Override
        public void onFailure(int id, int type, String msg) {
            mTarget.onFailure(id, type, msg);
        }

        @Override
        public void onFinal(int id, int type) {
            mTarget.onFinal(id, type);
        }
    }
}
