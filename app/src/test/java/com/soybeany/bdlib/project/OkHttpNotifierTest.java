package com.soybeany.bdlib.project;

import com.soybeany.bdlib.core.util.notify.IConnectLogic;
import com.soybeany.bdlib.core.util.notify.INotifyMsg;
import com.soybeany.bdlib.core.util.notify.Notifier;
import com.soybeany.bdlib.web.okhttp.core.ICallback;
import com.soybeany.bdlib.web.okhttp.core.OkHttpRequestFactory;
import com.soybeany.bdlib.web.okhttp.notify.NotifierCallback;
import com.soybeany.bdlib.web.okhttp.notify.OkHttpNotifierUtils;
import com.soybeany.bdlib.web.okhttp.notify.RequestMsg.OnFinish;
import com.soybeany.bdlib.web.okhttp.notify.RequestMsg.OnStart;
import com.soybeany.bdlib.web.okhttp.notify.RequestNotifier;
import com.soybeany.bdlib.web.okhttp.parser.StringParser;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * <br>Created by Soybeany on 2019/5/27.
 */
public class OkHttpNotifierTest {

    private String mUrl = "http://192.168.137.78:8080/mobile/auth/file";
    private Thread mMainThread = Thread.currentThread();

    @Test
    public void test1() {
        OkHttpNotifierUtils.newClient().newNotifierRequest().newCall(notifier -> {
            notifier.callback().addListener(msg -> {
                System.out.println("收到通知:" + msg);
            });
            return OkHttpRequestFactory.get(mUrl).build();
        }, new OkHttpNotifierUtils.IConnectorSetter<Notifier>() {
            @Override
            public Notifier getNewNotifier() {
                return new Notifier();
            }

            @Override
            public Class<? extends INotifyMsg> getDMClass() {
                return EndMsg.class;
            }

            @Override
            public void onSetupLogic(IConnectLogic.IApplier<RequestNotifier, Notifier> applier) {
                applier.addLogic(OnStart.class, (msg, n1, n2) -> System.out.println("逻辑:" + msg + " n1:" + n1 + " n2:" + n2));
                applier.addLogic(OnFinish.class, (msg, n1, n2) -> System.out.println("逻辑2:" + msg + " n1:" + n1 + " n2:" + n2));
            }

        }).enqueue(new NotifierCallback<>(StringParser.get()).addCallback(new ICallback<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println("成功:" + s);
            }

            @Override
            public void onFailure(boolean isCanceled, String msg) {
                System.out.println("失败:" + msg);
            }

            @Override
            public void onFinal(boolean isCanceled) {
                LockSupport.unpark(mMainThread);
            }
        }));

        LockSupport.park();
    }

    private static class EndMsg implements INotifyMsg {
        @Override
        public Object getData() {
            return null;
        }
    }
}
