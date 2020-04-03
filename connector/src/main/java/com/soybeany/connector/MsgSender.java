package com.soybeany.connector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 消息发送器，使用不同Target的IMsg与CMsg
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings("WeakerAccess")
public abstract class MsgSender<CMsg extends Msg.C, IMsg extends Msg.I> {

    public final MsgCenter.Key cKey = new MsgCenter.Key();
    private final Map<Class<?>, MsgConverter.ICallback> mCallbacks = new HashMap<>();
    private MsgCenter.Key mIKey;

    public static void connect(MsgSender<? extends Msg.C, ? extends Msg.I> sender1, MsgSender<? extends Msg.C, ? extends Msg.I> sender2) {
        sender1.mIKey = sender2.cKey;
        sender2.mIKey = sender1.cKey;
    }

    {
        setupMsgProcessors();
    }

    /**
     * 用此方法发送消息，消息接收者禁止修改{@link MsgCenter}中相应key的{@link MsgCenter.IListener}，否则会造成死锁
     */
    public void sendCMsg(CMsg msg) {
        MsgCenter.sendMsg(cKey, msg);
        sendMsgWithIKey(msg);
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private void setupMsgProcessors() {
        List<MsgConverter<? extends CMsg, IMsg>> converters = new LinkedList<>();
        onSetupMsgConverters(converters);
        for (MsgConverter<? extends CMsg, IMsg> converter : converters) {
            mCallbacks.put(converter.clazz, converter.callback);
        }
    }

    @SuppressWarnings("unchecked")
    private void sendMsgWithIKey(CMsg cMsg) {
        if (null == mIKey || null == cMsg) {
            return;
        }
        MsgConverter.ICallback<CMsg, IMsg> callback = mCallbacks.get(cMsg.getClass());
        IMsg iMsg;
        if (null != callback && null != (iMsg = callback.toIMsg(cMsg))) {
            MsgCenter.sendMsg(mIKey, iMsg);
        }
    }

    // //////////////////////////////////抽象方法//////////////////////////////////

    protected abstract void onSetupMsgConverters(List<MsgConverter<? extends CMsg, IMsg>> processors);

    // //////////////////////////////////内部类//////////////////////////////////

    public static class MsgConverter<CMsg extends Msg.C, IMsg extends Msg.I> {
        public final Class<CMsg> clazz;
        public final ICallback<CMsg, IMsg> callback;

        public MsgConverter(Class<CMsg> clazz, ICallback<CMsg, IMsg> callback) {
            this.clazz = clazz;
            this.callback = callback;
        }

        public interface ICallback<CMsg extends Msg.C, IMsg extends Msg.I> {
            /**
             * A.Target的回调消息转B.Target的主动消息
             *
             * @return 转换后的消息，若没有，可返回null
             */
            IMsg toIMsg(CMsg msg);
        }
    }
}
