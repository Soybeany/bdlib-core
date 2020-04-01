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
    public void test() {
        // 创建目标实例
        Request request = new Request();
        Vee vee = new Vee();
        // 获取消息管理器
        MsgManager<RequestInvokeMsg, RequestCallbackMsg> rManager = new MsgManager<>();
        MsgManager<ViewInvokeMsg, ViewCallbackMsg> vManager = new MsgManager<>();
        // 创建发送器，并连接
        RequestMsgSender sender1 = new RequestMsgSender();
        VeeMsgSender sender2 = new VeeMsgSender();
        MsgSender.connect(sender1, sender2);
        // 绑定
        rManager.bind(request, sender1);
        vManager.bind(vee, sender2);
        // 发送
        rManager.sendMsg(new RequestCallbackMsg.OnStart("开始了"));
        rManager.sendMsg(new RequestCallbackMsg.OnFinish("结束了"));
        vManager.sendMsg(new ViewCallbackMsg.OnShow("显示了"));
        vManager.sendMsg(new ViewCallbackMsg.onHide("隐藏了"));
        // 解绑
        rManager.unbind();
        vManager.unbind();
    }

    // //////////////////////////////////Sender//////////////////////////////////

    private static class RequestMsgSender extends MsgSender<RequestCallbackMsg, ViewInvokeMsg> {
        @Override
        protected void onSetupMsgProcessors(List<MsgConverter<? extends RequestCallbackMsg, ViewInvokeMsg>> processors) {
            processors.add(new MsgConverter<>(RequestCallbackMsg.OnStart.class, msg -> new ViewInvokeMsg.Show(msg.getData())));
            processors.add(new MsgConverter<>(RequestCallbackMsg.OnFinish.class, msg -> new ViewInvokeMsg.Hide(msg.getData())));
        }
    }

    private static class VeeMsgSender extends MsgSender<ViewCallbackMsg, RequestInvokeMsg> {
        @Override
        protected void onSetupMsgProcessors(List<MsgConverter<? extends ViewCallbackMsg, RequestInvokeMsg>> processors) {
            processors.add(new MsgConverter<>(ViewCallbackMsg.OnShow.class, msg -> new RequestInvokeMsg.Start(msg.getData())));
            processors.add(new MsgConverter<>(ViewCallbackMsg.onHide.class, msg -> new RequestInvokeMsg.Cancel(msg.getData())));
        }
    }

    // //////////////////////////////////Target//////////////////////////////////

    private static class Request implements ITarget<RequestInvokeMsg> {
        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends RequestInvokeMsg>> msgProcessors) {
            msgProcessors.add(new MsgProcessor<>(RequestInvokeMsg.Start.class, msg -> System.out.println("开始:" + msg.getData())));
            msgProcessors.add(new MsgProcessor<>(RequestInvokeMsg.Cancel.class, msg -> System.out.println("结束:" + msg.getData())));
        }
    }

    private static class Vee implements ITarget<ViewInvokeMsg> {
        @Override
        public void onSetupMsgProcessors(List<MsgProcessor<? extends ViewInvokeMsg>> msgProcessors) {
            msgProcessors.add(new MsgProcessor<>(ViewInvokeMsg.Show.class, msg -> System.out.println("显示:" + msg.getData())));
            msgProcessors.add(new MsgProcessor<>(ViewInvokeMsg.Hide.class, msg -> System.out.println("隐藏:" + msg.getData())));
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