package com.soybeany.connector;

/**
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings("WeakerAccess")
public class Msg<T> {

    /**
     * 标识发送者的uid
     */
    public String senderUid;

    /**
     * 包含的数据
     */
    public T data;

    private Msg(T data) {
        this.data = data;
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
