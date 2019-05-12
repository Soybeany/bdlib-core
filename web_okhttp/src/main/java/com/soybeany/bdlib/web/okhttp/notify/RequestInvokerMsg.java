package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class RequestInvokerMsg extends INotifyMsg.Impl<RequestInvokerMsg> implements INotifyMsg.Invoker {
    /**
     * 取消请求
     */
    public static final RequestInvokerMsg CANCEL_MSG = new RequestInvokerMsg().type("cancel");

    public static void invokeOnCancel(Object data, Runnable runnable) {
        if (CANCEL_MSG.equals(data)) {
            runnable.run();
        }
    }
}
