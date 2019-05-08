package com.soybeany.bdlib.web.okhttp.notify;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class CallbackMsg extends NotifyMsg {

    /**
     * 请求开始，data为null
     */
    public static final String TYPE_ON_START = "onStart";

    /**
     * 请求完成，data为null
     */
    public static final String TYPE_ON_FINISH = "onFinish";

    /**
     * 上传，data为float的进度(正常 0~1，缺失-1)
     */
    public static final String TYPE_ON_UPLOAD = "onUpload";

    /**
     * 下载，data为float的进度(正常 0~1，缺失-1)
     */
    public static final String TYPE_ON_DOWNLOAD = "onDownload";

    public static String getRealKey(String key) {
        return "callback:" + key;
    }

    public CallbackMsg(String type, Object data) {
        super(type, data);
    }
}
