package com.soybeany.bdlib.web.okhttp.notify;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class InvokerMsg extends NotifyMsg {
    /**
     * 取消请求，data为null
     */
    public static final String TYPE_CANCEL = "cancel";

    public static String getRealKey(String key) {
        return "invoker:" + key;
    }

    public InvokerMsg(String type, Object data) {
        super(type, data);
    }
}
