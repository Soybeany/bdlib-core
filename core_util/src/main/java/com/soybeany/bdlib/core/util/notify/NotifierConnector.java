package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.java8.Optional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Notifier连接器，负责将2个Notifier连接在一起
 * <br>Created by Soybeany on 2019/5/25.
 */
public class NotifierConnector<N1 extends Notifier, N2 extends Notifier> implements IOnCallListener, IConnectLogic.IApplier<N1, N2> {
    private final IConnectLogic.ApplierImpl<N1, N2> mApplierDelegate = new IConnectLogic.ApplierImpl<>();
    private final Map<Class<? extends INotifyMsg>, Notifier> mDmMap = new HashMap<>();
    private N1 mN1;
    private N2 mN2;

    @Override
    public void onCall(INotifyMsg msg) {
        mApplierDelegate.invoke(msg, mN1, mN2);
        // 自动移除对Notifier的监听
        Optional.ofNullable(mDmMap.remove(msg.getClass())).ifPresent(notifier -> notifier.callback().removeListener(this));
    }

    @Override
    public <Msg extends INotifyMsg> void addLogic(Class<Msg> clazz, IConnectLogic<Msg, N1, N2> logic) {
        mApplierDelegate.addLogic(clazz, logic);
    }

    @Override
    public void removeLogic(IConnectLogic<? extends INotifyMsg, N1, N2> logic) {
        mApplierDelegate.removeLogic(logic);
    }

    public void connectN1(N1 notifier, Class<? extends INotifyMsg> disconnectMsg) {
        connect(mN1, mN1 = notifier, disconnectMsg);
    }

    public void connectN2(N2 notifier, Class<? extends INotifyMsg> disconnectMsg) {
        connect(mN2, mN2 = notifier, disconnectMsg);
    }

    private void connect(Notifier oldNotifier, Notifier newNotifier, Class<? extends INotifyMsg> disconnectMsg) {
        // 检测
        if (null == newNotifier || null == disconnectMsg) {
            System.out.println("连接失败，以下值不能为null:" + newNotifier + "(Notifier)  " + disconnectMsg + "(disconnectMsg)");
            return;
        }
        // 移除旧值
        if (null != oldNotifier) {
            oldNotifier.callback().removeListener(this);
            Iterator<Notifier> iterator = mDmMap.values().iterator();
            while (iterator.hasNext()) {
                if (oldNotifier == iterator.next()) {
                    iterator.remove();
                }
            }
        }
        // 添加新值
        newNotifier.callback().addListener(this);
        mDmMap.put(disconnectMsg, newNotifier);
    }
}
