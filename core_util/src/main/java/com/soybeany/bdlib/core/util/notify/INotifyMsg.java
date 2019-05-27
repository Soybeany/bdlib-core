package com.soybeany.bdlib.core.util.notify;

/**
 * 通知信息
 * <br>Created by Soybeany on 2019/5/8.
 */
public interface INotifyMsg<Data> {

    /**
     * 具体数据
     */
    Data getData();

    class Impl<Data> implements INotifyMsg<Data> {
        private Data mData;

        public Impl(Data data) {
            mData = data;
        }

        @Override
        public Data getData() {
            return mData;
        }

        public void setData(Data data) {
            mData = data;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + " - " + getData();
        }
    }

    class InvokerImpl<Data> extends Impl<Data> implements IInvoker<Data> {
        public InvokerImpl(Data data) {
            super(data);
        }
    }

    class CallbackImpl<Data> extends Impl<Data> implements ICallback<Data> {
        public CallbackImpl(Data data) {
            super(data);
        }
    }

    interface IInvoker<Data> extends INotifyMsg<Data> {
    }

    interface ICallback<Data> extends INotifyMsg<Data> {
    }
}
