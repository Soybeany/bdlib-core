package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
@SuppressWarnings("WeakerAccess")
public class RequestMsg {

    // //////////////////////////////////静态方法区//////////////////////////////////

    /**
     * 当cancel时执行
     */
    public static void invokeWhenCancel(INotifyMsg msg, Runnable runnable) {
        if (msg instanceof Cancel) {
            runnable.run();
        }
    }

    // //////////////////////////////////模板区//////////////////////////////////

    public static class Invoker<Data> extends INotifyMsg.InvokerImpl<Data> {
        public Invoker(Data data) {
            super(data);
        }
    }

    public static class Callback<Data> extends INotifyMsg.CallbackImpl<Data> {
        public Callback(Data data) {
            super(data);
        }
    }

    // //////////////////////////////////Invoker区//////////////////////////////////

    /**
     * 取消请求，data为null
     */
    public static class Cancel extends Invoker<Object> {
        public Cancel() {
            super(null);
        }
    }

    // //////////////////////////////////Callback区//////////////////////////////////

    /**
     * 请求开始，data为null
     */
    public static class OnStart extends Callback<Object> {
        public OnStart() {
            super(null);
        }
    }

    /**
     * 请求完成，data为{@link RequestFinishReason}
     */
    public static class OnFinish extends Callback<RequestFinishReason> {
        public OnFinish(RequestFinishReason reason) {
            super(reason);
        }
    }

    /**
     * 上传，data为float的进度(正常 0~1，缺失-1)
     */
    public static class OnUpload extends Callback<Float> {
        public OnUpload() {
            super(null);
        }
    }

    /**
     * 下载，data为float的进度(正常 0~1，缺失-1)
     */
    public static class OnDownload extends Callback<Float> {
        public OnDownload() {
            super(null);
        }
    }
}
