package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class InvokerMsg extends INotifyMsg.Impl<InvokerMsg> implements INotifyMsg.Invoker {
    /**
     * 取消请求
     */
    public static final INotifyMsg.Invoker CANCEL_MSG = new InvokerMsg().type("cancel");

    public static void invokeOnCancel(Object data, Runnable runnable) {
        if (CANCEL_MSG.equals(data)) {
            runnable.run();
        }
    }
}
