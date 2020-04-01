package com.soybeany.connector;

/**
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings("WeakerAccess")
public class Msg<T> {
    private T mData;

    Msg(T data) {
        mData = data;
    }

    public T getData() {
        return mData;
    }

    /**
     * 主动消息
     */
    public static class I<T> extends Msg<T> {
        public I(T data) {
            super(data);
        }
    }

    /**
     * 回调消息
     */
    public static class C<T> extends Msg<T> {
        public C(T data) {
            super(data);
        }
    }
}
