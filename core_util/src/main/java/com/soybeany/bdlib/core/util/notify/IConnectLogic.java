package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.storage.KeySetStorage;


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

    class ApplierImpl<N1 extends Notifier, N2 extends Notifier> implements IApplier<N1, N2> {
        private final KeySetStorage<Class<? extends INotifyMsg>, IConnectLogic<INotifyMsg, N1, N2>> mLogicStorage = new KeySetStorage<>();

        @Override
        @SuppressWarnings("unchecked")
        public <Msg extends INotifyMsg> void addLogic(Class<Msg> clazz, IConnectLogic<Msg, N1, N2> logic) {
            mLogicStorage.putVal(clazz, (IConnectLogic<INotifyMsg, N1, N2>) logic);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void removeLogic(IConnectLogic<? extends INotifyMsg, N1, N2> logic) {
            mLogicStorage.removeVal((IConnectLogic<INotifyMsg, N1, N2>) logic);
        }

        public void invoke(INotifyMsg msg, N1 n1, N2 n2) {
            mLogicStorage.invokeVal(msg.getClass(), logic -> logic.onCall(msg, n1, n2));
        }
    }
}
