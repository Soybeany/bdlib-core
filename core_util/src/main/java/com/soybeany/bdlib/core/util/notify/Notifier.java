package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.storage.IExecutable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 通知者，提供通知执行(invoker)与监听回调(callback)功能
 * <br>Created by Soybeany on 2019/5/11.
 */
public class Notifier<InvokerMsg extends INotifyMsg.Invoker, CallbackMsg extends INotifyMsg.Callback> {
    private final Invoker<InvokerMsg> mInvokerFunc;
    private final Callback<CallbackMsg> mCallbackFunc;

    public Notifier() {
        this(null);
    }

    public Notifier(IExecutable executable) {
        this(executable, executable);
    }

    public Notifier(IExecutable invoker, IExecutable callback) {
        String notifyKey = FileUtils.getUUID();
        mInvokerFunc = new Invoker<>(invoker, notifyKey);
        mCallbackFunc = new Callback<>(callback, notifyKey);
    }

    // //////////////////////////////////方法区//////////////////////////////////

    /**
     * 使用主动的功能
     */
    public Invoker<InvokerMsg> invoker() {
        return mInvokerFunc;
    }

    /**
     * 使用回调的功能
     */
    public Callback<CallbackMsg> callback() {
        return mCallbackFunc;
    }

    // //////////////////////////////////内部类区//////////////////////////////////

    public static class Invoker<Msg extends INotifyMsg.Invoker> extends DealerFunc<Msg> {
        Invoker(IExecutable executable, String key) {
            super(executable, "invoker:" + key);
        }
    }

    public static class Callback<Msg extends INotifyMsg.Callback> extends DealerFunc<Msg> {
        Callback(IExecutable executable, String key) {
            super(executable, "callback:" + key);
        }
    }

    private static class DealerFunc<Msg extends INotifyMsg> implements MessageCenter.ICallback {
        private final Set<IOnCallDealer> mDealers = new HashSet<>();
        private final IExecutable mExecutable;
        private final String mKey;

        DealerFunc(IExecutable executable, String key) {
            mExecutable = (null != executable ? executable : IExecutable.MULTI_WORK_THREAD);
            mKey = key;
        }

        @Override
        public synchronized void onCall(Object data) {
            if (!(data instanceof INotifyMsg)) {
                return;
            }
            // 回调并检测dealer的移除意向
            Iterator<IOnCallDealer> iterator = mDealers.iterator();
            while (iterator.hasNext()) {
                IOnCallDealer dealer = iterator.next();
                dealer.onCall((INotifyMsg) data);
                if (dealer.needToBeRemoved()) {
                    iterator.remove();
                }
            }
        }

        public synchronized void addDealer(IOnCallDealer dealer) {
            if (mDealers.isEmpty()) {
                MessageCenter.register(mExecutable, mKey, this);
            }
            if (null != dealer) {
                mDealers.add(dealer);
            }
        }

        public synchronized void removeDealer(IOnCallDealer dealer) {
            if (null != dealer) {
                mDealers.remove(dealer);
            }
            if (mDealers.isEmpty()) {
                MessageCenter.unregister(this);
            }
        }

        public void notifyNow(Msg msg) {
            MessageCenter.notifyNow(mKey, msg);
        }
    }
}
