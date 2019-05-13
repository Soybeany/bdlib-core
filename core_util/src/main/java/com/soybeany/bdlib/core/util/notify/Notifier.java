package com.soybeany.bdlib.core.util.notify;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.IterableUtils;
import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.storage.IExecutable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 通知者，需在合适的地方手动调用{@link #register}与{@link #unregister}
 * <br>Created by Soybeany on 2019/5/11.
 */
public class Notifier<InvokerMsg extends INotifyMsg.Invoker, CallbackMsg extends INotifyMsg.Callback> {
    private final Invoker<InvokerMsg> mInvokerFunc;
    private final Callback<CallbackMsg> mCallbackFunc;
    private final List<DealerFunc> mFuncList;

    public Notifier() {
        this(null);
    }

    public Notifier(IExecutable executable) {
        this(executable, executable);
    }

    public Notifier(IExecutable invoker, IExecutable callback) {
        String notifyKey = FileUtils.getUUID();
        mFuncList = Arrays.asList(
                mInvokerFunc = new Invoker<>(invoker, notifyKey),
                mCallbackFunc = new Callback<>(callback, notifyKey)
        );
    }

    // //////////////////////////////////方法区//////////////////////////////////

    /**
     * 进行注册
     */
    public void register() {
        IterableUtils.forEach(mFuncList, (func, flag) -> func.register());
    }

    /**
     * 进行注销
     */
    public void unregister() {
        IterableUtils.forEach(mFuncList, (func, flag) -> func.unregister());
    }

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
        private final char[] mLock = new char[0];

        DealerFunc(IExecutable executable, String key) {
            mExecutable = (null != executable ? executable : IExecutable.MULTI_WORK_THREAD);
            mKey = key;
        }

        @Override
        public void onCall(Object data) {
            synchronized (mLock) {
                if (data instanceof INotifyMsg) {
                    IterableUtils.forEach(mDealers, (dealer, flag) -> dealer.onCall((INotifyMsg) data));
                } else if (data instanceof TempDealerMsg) {
                    TempDealerMsg dealerMsg = (TempDealerMsg) data;
                    IterableUtils.forEach(dealerMsg.dealers, (dealer, flag) -> dealer.onCall(dealerMsg.msg));
                }
            }
        }

        public void addDealer(IOnCallDealer dealer) {
            synchronized (mLock) {
                Optional.ofNullable(dealer).ifPresent(mDealers::add);
            }
        }

        public void removeDealer(IOnCallDealer dealer) {
            synchronized (mLock) {
                Optional.ofNullable(dealer).ifPresent(mDealers::remove);
            }
        }

        public void notifyNow(Msg msg) {
            MessageCenter.notifyNow(mKey, msg);
        }

        /**
         * 若在发送通知后就需要立刻注销，则使用此方法
         */
        public void notifyAndUnregister(Msg msg) {
            MessageCenter.notifyNow(mKey, new TempDealerMsg(mDealers, msg));
            unregister();
        }

        void register() {
            synchronized (mLock) {
                MessageCenter.register(mExecutable, mKey, this);
            }
        }

        void unregister() {
            synchronized (mLock) {
                MessageCenter.unregister(this);
                mDealers.clear(); // 清空集合，为新一轮作准备
            }
        }
    }

    private static class TempDealerMsg {
        final Set<IOnCallDealer> dealers = new HashSet<>();
        INotifyMsg msg;

        TempDealerMsg(Set<IOnCallDealer> dealers, INotifyMsg msg) {
            this.dealers.addAll(dealers);
            this.msg = msg;
        }
    }
}
