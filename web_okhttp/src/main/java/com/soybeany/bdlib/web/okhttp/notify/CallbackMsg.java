package com.soybeany.bdlib.web.okhttp.notify;

import com.soybeany.bdlib.core.util.notify.INotifyMsg;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class CallbackMsg extends INotifyMsg.Impl<CallbackMsg> implements INotifyMsg.Callback {
    /**
     * 请求开始，data为null
     */
    public static final String TYPE_ON_START = "onStart";

    /**
     * 请求完成，data为{@link FinishReason}
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
}
