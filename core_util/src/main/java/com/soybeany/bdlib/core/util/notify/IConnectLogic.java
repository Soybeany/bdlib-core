package com.soybeany.bdlib.core.util.notify;

/**
 * 连接逻辑
 * <br>Created by Soybeany on 2019/5/25.
 */
public interface IConnectLogic<Msg extends INotifyMsg, N1 extends Notifier, N2 extends Notifier> {
    void onCall(Msg msg, N1 n1, N2 n2);

    interface IApplier<N1 extends Notifier, N2 extends Notifier> {
        <Msg extends INotifyMsg> void addLogic(Class<Msg> clazz, IConnectLogic<Msg, N1, N2> logic);

        void removeLogic(IConnectLogic<? extends INotifyMsg, N1, N2> logic);
    }
}
