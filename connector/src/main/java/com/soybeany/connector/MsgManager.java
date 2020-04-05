package com.soybeany.connector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 消息管理器，使用相同Target的IMsg与CMsg
 * 使用流程：
 * <br>1.分别有需要通讯的A、B {@link ITarget}
 * <br>2.创建2个{@link MsgManager}，2个{@link MsgSender}，并在合适的时机调用{@link #bind}，与{@link ITarget}进行绑定
 * <br>3.将A、B的{@link MsgSender}使用{@link MsgSender#connect}进行连接
 * <br>4.A、B {@link ITarget}按需使用{@link MsgSender}发送{@link Msg.C}消息，另一个{@link ITarget}便能收到相应的{@link Msg.I}消息
 * <br>5.在合适的时机，A、B {@link MsgSender}调用{@link #unbind}，与{@link ITarget}进行解绑
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class MsgManager<IMsg extends Msg.I, CMsg extends Msg.C> implements MsgCenter.IListener {

    private final Map<Class<?>, ITarget.MsgProcessor.ICallback> mCallbacks = new HashMap<>();
    private final String mSenderUid = UUID.randomUUID().toString().replaceAll("-", "");
    private boolean mIsBinding;
    private MsgSender<CMsg, ?> mMsgSender;

    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(MsgCenter.Key key, Object msg) {
        // 获取消息处理器，没有对应的处理器则忽略
        ITarget.MsgProcessor.ICallback<IMsg> processor = mCallbacks.get(msg.getClass());
        if (null == processor) {
            return;
        }
        // 若消息的发送者为自己，则忽略此消息，避免死循环
        IMsg iMsg = (IMsg) msg;
        if (mSenderUid.equals(iMsg.senderUid)) {
            return;
        }
        // 若消息还没有发送者，则将发送者指定为自身
        setSenderUidIfNotSet(iMsg);
        // 处理消息
        processor.onHandleMsg(iMsg);
    }

    // //////////////////////////////////公开方法//////////////////////////////////

    /**
     * 执行绑定，将与{@link MsgCenter}、{@link ITarget}、{@link MsgSender}等进行绑定
     *
     * @param inSafeMode 安全模式：在子线程中执行此操作，会导致执行不及时的情况。
     *                   场景-若要在遍历通知期间调用此方法，为防止出现死锁，需设置为true
     */
    public synchronized void bind(ITarget<IMsg> target, MsgSender<CMsg, ?> msgSender, boolean inSafeMode) {
        if (mIsBinding) {
            return;
        }
        mMsgSender = msgSender;
        if (inSafeMode) {
            MsgCenter.registerSafe(mMsgSender.cKey, this);
        } else {
            MsgCenter.register(mMsgSender.cKey, this);
        }
        setupMsgProcessors(target);
        mIsBinding = true;
    }

    /**
     * 解除绑定，将与{@link MsgCenter}、{@link ITarget}、{@link MsgSender}等解绑
     *
     * @param inSafeMode 安全模式：在子线程中执行此操作，会导致执行不及时的情况。
     *                   场景-若要在遍历通知期间调用此方法，为防止出现死锁，需设置为true
     */
    public synchronized void unbind(boolean inSafeMode) {
        if (!mIsBinding) {
            return;
        }
        mIsBinding = false;
        mCallbacks.clear();
        if (inSafeMode) {
            MsgCenter.unregisterSafe(mMsgSender.cKey, this);
        } else {
            MsgCenter.unregister(mMsgSender.cKey, this);
        }
        mMsgSender = null;
    }

    /**
     * 发送消息
     *
     * @return 是否发送成功 只有设置了{@link MsgSender}后才可能发送成功并返回true，否则返回false
     */
    public synchronized boolean sendMsg(CMsg msg) {
        if (null == mMsgSender) {
            return false;
        }
        setSenderUidIfNotSet(msg);
        mMsgSender.sendCMsg(msg);
        return true;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private void setSenderUidIfNotSet(Msg msg) {
        if (null == msg.senderUid) {
            msg.senderUid = mSenderUid;
        }
    }

    private void setupMsgProcessors(ITarget<IMsg> target) {
        List<ITarget.MsgProcessor<? extends IMsg>> processors = new LinkedList<>();
        target.onSetupMsgProcessors(processors);
        for (ITarget.MsgProcessor<? extends IMsg> processor : processors) {
            mCallbacks.put(processor.clazz, processor.callback);
        }
    }

}
