package com.soybeany.bdlib.web.okhttp.part;

import com.soybeany.bdlib.core.util.storage.MessageCenter;
import com.soybeany.bdlib.core.util.storage.MessageCenter.ICallback;

/**
 * 与{@link ICallback}相比，{@link ICallback}侧重于业务，此类侧重于监听状态变更(一般由框架调用)
 * <br>Created by Soybeany on 2019/5/7.
 */
public interface IStateListener {

    static void cancel(String uid) {
        MessageCenter.notifyNow(uid, null);
    }

    /**
     * @param cancelUid {@link #cancel(String)}中使用能取消指定请求
     */
    void onStart(String cancelUid);

    void onFinish();
}
