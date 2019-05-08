package com.soybeany.bdlib.core.util.notify;

/**
 * 通知信息
 * <br>Created by Soybeany on 2019/5/8.
 */
public interface INotifyMsg {

    String getType();

    Object getData();

    class Impl implements INotifyMsg {
        private String mType;
        private Object mData;

        public Impl(String type, Object data) {
            this.mType = type;
            this.mData = data;
        }

        @Override
        public String getType() {
            return mType;
        }

        @Override
        public Object getData() {
            return mData;
        }
    }

    /**
     * 主动调用者
     */
    interface Invoker extends INotifyMsg {
        static String getRealKey(String key) {
            return "invoker:" + key;
        }
    }

    /**
     * 被动回调
     */
    interface Callback extends INotifyMsg {
        static String getRealKey(String key) {
            return "callback:" + key;
        }
    }
}
