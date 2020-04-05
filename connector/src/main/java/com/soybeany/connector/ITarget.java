package com.soybeany.connector;

import java.util.List;

/**
 * 目标对象
 * <br>Created by Soybeany on 2020/4/1.
 */
public interface ITarget<IMsg extends Msg.I> {

    void onSetupMsgProcessors(List<MsgProcessor<? extends IMsg>> processors);

    // //////////////////////////////////内部类//////////////////////////////////

    @SuppressWarnings("WeakerAccess")
    class MsgProcessor<T extends Msg> {
        public final Class<T> clazz;
        public final ICallback<T> callback;

        public MsgProcessor(Class<T> clazz, ICallback<T> callback) {
            this.clazz = clazz;
            this.callback = callback;
        }

        /**
         * 若要在此方法中使用{@link MsgSender}发送消息，使用{@link MsgSender#sendCMsg(String, Msg.C)}，
         * 并将senderUid设置为入参msg的senderUid，以避免“消息的循环发送与处理”
         */
        public interface ICallback<T extends Msg> {
            void onHandleMsg(T msg);
        }
    }
}
