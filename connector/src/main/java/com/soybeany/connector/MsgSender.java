package com.soybeany.connector;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 消息发送器，1个CKey，支持关联多个IKey
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings("WeakerAccess")
public abstract class MsgSender<CMsg extends Msg.C, IMsg extends Msg.I> {

    public final String uid = UUID.randomUUID().toString().replaceAll("-", "");
    private final ReadWriteLock mLock = new ReentrantReadWriteLock();
    private final Map<Class<?>, MsgConverter.ICallback> mCallbacks = new HashMap<>();
    public final MsgCenter.Key cKey = new MsgCenter.Key();
    private final Set<MsgCenter.Key> mIKeys = new LinkedHashSet<>();

    public void connect(MsgSender<?, ?> sender, boolean inSafeMode) {
        if (null == sender) {
            return;
        }
        LockUtils.execute(mLock.writeLock(), () -> mIKeys.add(sender.cKey), inSafeMode);
    }

    public void disconnect(MsgSender<?, ?> sender, boolean inSafeMode) {
        if (null == sender) {
            return;
        }
        LockUtils.execute(mLock.writeLock(), () -> mIKeys.remove(sender.cKey), inSafeMode);
    }

    {
        setupMsgProcessors();
    }

    /**
     * 为消息设置指定的senderUid，并发送
     */
    public void sendCMsg(String senderUid, CMsg msg) {
        msg.senderUid = senderUid;
        MsgCenter.sendMsg(cKey, msg);
        sendMsgWithIKey(msg);
    }

    /**
     * 使用默认Uid设置消息
     */
    public void sendCMsg(CMsg msg) {
        sendCMsg(uid, msg);
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
        if (mIKeys.isEmpty() || null == cMsg) {
            return;
        }
        LockUtils.execute(mLock.readLock(), () -> {
            MsgConverter.ICallback<CMsg, IMsg> callback = mCallbacks.get(cMsg.getClass());
            IMsg iMsg;
            if (null != callback && null != (iMsg = callback.toIMsg(cMsg))) {
                iMsg.senderUid = cMsg.senderUid;
                for (MsgCenter.Key iKey : mIKeys) {
                    MsgCenter.sendMsg(iKey, iMsg);
                }
            }
        }, false);
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
