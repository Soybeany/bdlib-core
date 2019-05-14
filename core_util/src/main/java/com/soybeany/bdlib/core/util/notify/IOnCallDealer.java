package com.soybeany.bdlib.core.util.notify;

/**
 * <br>Created by Soybeany on 2019/5/11.
 */
public interface IOnCallDealer {
    void onCall(INotifyMsg msg);

    /**
     * 若要在收到通知后移除自身，则修改此值为true，否则会产生修改异常
     */
    default boolean needToBeRemoved() {
        return false;
    }
}
