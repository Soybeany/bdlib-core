package com.soybeany.connector_a;

import com.soybeany.connector.ITarget;
import com.soybeany.connector.Msg;
import com.soybeany.connector.MsgManager;
import com.soybeany.connector.MsgSender;

import org.junit.Test;

import java.util.List;

/**
 * <br>Created by Soybeany on 2020/4/1.
 */
public class TotalTest {

    @Test
    public void test() throws Exception {
        // 创建发送器，并连接
        RequestMsgSender sender1 = new RequestMsgSender();
        VeeMsgSender sender2 = new VeeMsgSender();
        MsgSender.connect(sender1, sender2);
        // 创建目标实例
        Request request = new Request(sender1);
        Vee vee = new Vee(sender2);
        // 发送
        sender1.sendCMsg(new RequestCallbackMsg.OnStart("onStart"));
        sender1.sendCMsg(new RequestCallbackMsg.OnFinish("onFinish"));
        sender2.sendCMsg(new ViewCallbackMsg.onHide("onHide"));
    }

    // //////////////////////////////////Sender//////////////////////////////////

    private static class RequestMsgSender extends MsgSender<RequestCallbackMsg, ViewInvokeMsg> {
        @Override
        protected void onSetupMsgConverters(List<MsgConverter<? extends RequestCallbackMsg, ViewInvokeMsg>> processors) {
            processors.add(new MsgConverter<>(RequestCallbackMsg.OnStart.class, msg -> new ViewInvokeMsg.Show(msg.data)));
            processors.add(new MsgConverter<>(RequestCallbackMsg.OnFinish.class, msg -> new ViewInvokeMsg.Hide(msg.data)));
        }
    }

    private static class VeeMsgSender extends MsgSender<ViewCallbackMsg, RequestInvokeMsg> {
        @Override
        protected void onSetupMsgConverters(List<MsgConverter<? extends ViewCallbackMsg, RequestInvokeMsg>> processors) {
            processors.add(new MsgConverter<>(ViewCallbackMsg.OnShow.class, msg -> new RequestInvokeMsg.Start(msg.data)));
            processors.add(new MsgConverter<>(ViewCallbackMsg.onHide.class, msg -> new RequestInvokeMsg.Cancel(msg.data)));
        }
    }

    // //////////////////////////////////Target//////////////////////////////////

    private static class Request implements ITarget<RequestInvokeMsg> {
        final MsgManager<RequestInvokeMsg, RequestCallbackMsg> manager = new MsgManager<>();
        private RequestMsgSender mSender;

        Request(RequestMsgSender sender) {
            mSender = sender;
            manager.bind(this, mSender, false);
        }

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends RequestInvokeMsg>> msgProcessors) {
            msgProcessors.add(new MsgProcessor<>(RequestInvokeMsg.Start.class, msg -> {
                System.out.println("开始:" + msg.data);
                mSender.sendCMsg(msg.senderUid, new RequestCallbackMsg.OnStart(msg.data));
            }));
            msgProcessors.add(new MsgProcessor<>(RequestInvokeMsg.Cancel.class, msg -> {
                System.out.println("取消了:" + msg.data);
                mSender.sendCMsg(msg.senderUid, new RequestCallbackMsg.OnFinish(msg.data));
            }));
        }
    }

    private static class Vee implements ITarget<ViewInvokeMsg> {
        final MsgManager<ViewInvokeMsg, ViewCallbackMsg> manager = new MsgManager<>();
        private VeeMsgSender mSender;

        Vee(VeeMsgSender sender) {
            mSender = sender;
            manager.bind(this, mSender, false);
        }

        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends ViewInvokeMsg>> msgProcessors) {
            msgProcessors.add(new MsgProcessor<>(ViewInvokeMsg.Show.class, msg -> {
                System.out.println("显示:" + msg.data);
                mSender.sendCMsg(msg.senderUid, new ViewCallbackMsg.OnShow(msg.data));
            }));
            msgProcessors.add(new MsgProcessor<>(ViewInvokeMsg.Hide.class, msg -> {
                System.out.println("隐藏:" + msg.data);
                mSender.sendCMsg(msg.senderUid, new ViewCallbackMsg.onHide(msg.data));
            }));
        }
    }

    // //////////////////////////////////Msg//////////////////////////////////

    private static class RequestInvokeMsg<T> extends Msg.I<T> {
        RequestInvokeMsg(T data) {
            super(data);
        }

        static class Start extends RequestInvokeMsg<String> {
            Start(String data) {
                super(data);
            }
        }

        static class Cancel extends RequestInvokeMsg<String> {
            Cancel(String data) {
                super(data);
            }
        }
    }

    private static class RequestCallbackMsg<T> extends Msg.C<T> {
        RequestCallbackMsg(T data) {
            super(data);
        }

        static class OnStart extends RequestCallbackMsg<String> {
            OnStart(String data) {
                super(data);
            }
        }

        static class OnFinish extends RequestCallbackMsg<String> {
            OnFinish(String data) {
                super(data);
            }
        }

    }

    private static class ViewInvokeMsg<T> extends Msg.I<T> {
        ViewInvokeMsg(T data) {
            super(data);
        }


        static class Show extends ViewInvokeMsg<String> {
            Show(String data) {
                super(data);
            }
        }

        static class Hide extends ViewInvokeMsg<String> {
            Hide(String data) {
                super(data);
            }
        }
    }

    private static class ViewCallbackMsg<T> extends Msg.C<T> {
        ViewCallbackMsg(T data) {
            super(data);
        }

        static class OnShow extends ViewCallbackMsg<String> {
            OnShow(String data) {
                super(data);
            }
        }

        static class onHide extends ViewCallbackMsg<String> {
            onHide(String data) {
                super(data);
            }
        }
    }

}