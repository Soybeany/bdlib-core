package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class InvokerMsg extends INotifyMsg.Impl implements INotifyMsg.Invoker {
    /**
     * 取消请求，data为null
     */
    public static final String TYPE_CANCEL = "cancel";

    public InvokerMsg(String type, Object data) {
        super(type, data);
    }
}
