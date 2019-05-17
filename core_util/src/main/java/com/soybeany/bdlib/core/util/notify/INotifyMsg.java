package com.soybeany.bdlib.core.util.notify;

/**
 * 通知信息
 * <br>Created by Soybeany on 2019/5/8.
 */
public interface INotifyMsg {

    String getType();

    Object getData();

    class Impl<Msg extends IEditable> implements IEditable<Msg> {
        private String mType;
        private Object mData;

        @Override
        public String getType() {
            return mType;
        }

        @Override
        public Object getData() {
            return mData;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Msg type(String type) {
            mType = type;
            return (Msg) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Msg data(Object data) {
            mData = data;
            return (Msg) this;
        }
    }

    /**
     * 编辑模式
     */
    interface IEditable<Msg extends IEditable> extends INotifyMsg {
        Msg type(String type);

        Msg data(Object data);
    }

    /**
     * 主动调用者
     */
    interface Invoker extends INotifyMsg {
    }

    /**
     * 被动回调
     */
    interface Callback extends INotifyMsg {
    }
}
