package com.soybeany.connector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 消息管理器，使用相同Target的IMsg与CMsg
 * 使用流程：
 * <br>1.分别有需要通讯的A、B {@link ITarget}
 * <br>2.创建2个{@link MsgManager}，2个{@link MsgSender}，并在合适的时机调用{@link #bind}，与{@link ITarget}进行绑定
 * <br>3.将A、B的{@link MsgSender}使用{@link MsgSender#connect}进行连接
 * <br>4.A、B {@link ITarget}按需使用{@link MsgSender}发送{@link Msg.C}消息，另一个{@link ITarget}便能收到相应的{@link Msg.I}消息
 * <br>5.在合适的时机，A、B {@link MsgSender}调用{@link #unbind()}，与{@link ITarget}进行解绑
 * <br>Created by Soybeany on 2020/3/31.
 */
@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
public class MsgManager<IMsg extends Msg.I, CMsg extends Msg.C> implements MsgCenter.IListener {

    private final Map<Class<?>, ITarget.MsgProcessor.ICallback> mCallbacks = new HashMap<>();
    private boolean mIsBinding;
    private boolean mIsEnd = true;
    private MsgSender<CMsg, ?> mMsgSender;

    // 因遍历发送消息(读锁)时，不能执行unbind(写锁)操作，否则会造成死锁，所以遍历时需将操作放入异步执行队列中
    private DelayTaskExecutor mDelayTaskExecutor = task -> Executors.newSingleThreadExecutor().execute(task);

    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(MsgCenter.Key key, Object msg) {
        ITarget.MsgProcessor.ICallback<IMsg> processor = mCallbacks.get(msg.getClass());
        if (null != processor) {
            processor.onHandleMsg((IMsg) msg);
        }
    }

    // //////////////////////////////////公开方法//////////////////////////////////

    /**
     * 设置用于延迟操作的执行器
     */
    public MsgManager<IMsg, CMsg> delayTaskExecutor(DelayTaskExecutor executor) {
        if (null != executor) {
            mDelayTaskExecutor = executor;
        }
        return this;
    }

    /**
     * 执行绑定，将与{@link MsgCenter}、{@link ITarget}、{@link MsgSender}等进行绑定
     */
    public synchronized void bind(ITarget<IMsg> target, MsgSender<CMsg, ?> msgSender) {
        if (mIsBinding) {
            return;
        }
        mMsgSender = msgSender;
        MsgCenter.register(mMsgSender.cKey, this);
        setupMsgProcessors(target);
        mIsBinding = true;
        mIsEnd = false;
    }

    /**
     * 解除绑定，将与{@link MsgCenter}、{@link ITarget}、{@link MsgSender}等解绑
     */
    public synchronized void unbind() {
        unbind(true);
    }

    /**
     * 发送消息
     *
     * @return 是否发送成功 只有设置了{@link MsgSender}后才可能发送成功并返回true，否则返回false
     */
    public synchronized boolean sendMsg(CMsg msg) {
        if (null == mMsgSender || mIsEnd) {
            return false;
        }
        // 若有结束标识，则延迟发送，并解除绑定
        if (msg instanceof Msg.EndFlag) {
            mIsEnd = true;
            mDelayTaskExecutor.onExecute(() -> {
                mMsgSender.sendCMsg(msg);
                unbind(false);
            });
        }
        // 普通消息，立刻发送
        else {
            mMsgSender.sendCMsg(msg);
        }
        return true;
    }

    // //////////////////////////////////内部方法//////////////////////////////////

    private void setupMsgProcessors(ITarget<IMsg> target) {
        List<ITarget.MsgProcessor<? extends IMsg>> processors = new LinkedList<>();
        target.onSetupMsgProcessors(processors);
        for (ITarget.MsgProcessor<? extends IMsg> processor : processors) {
            mCallbacks.put(processor.clazz, processor.callback);
        }
    }

    private void unbind(boolean considerIsEnd) {
        if (!mIsBinding || (considerIsEnd && mIsEnd)) {
            return;
        }
        mIsBinding = false;
        mCallbacks.clear();
        MsgCenter.unregister(mMsgSender.cKey, this);
        mMsgSender = null;
    }

    public interface DelayTaskExecutor {
        void onExecute(Runnable task);
    }
}
