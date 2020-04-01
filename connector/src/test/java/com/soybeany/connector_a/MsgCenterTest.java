package com.soybeany.connector_a;

import com.soybeany.connector.MsgCenter;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br>Created by Soybeany on 2020/4/1.
 */
public class MsgCenterTest {

    @Test
    public void test() throws Exception {
        MsgCenter.Key key = new MsgCenter.Key();
        AtomicInteger integer = new AtomicInteger(0);
        // 注册
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                MsgCenter.register(key, (k, msg) -> {
//                    System.out.println(finalI + "收到:" + msg);
                    integer.incrementAndGet();
                });
            }).start();
        }
        // 发送
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(() -> {
                MsgCenter.sendMsg(key, "通知" + finalI);
            }).start();
        }
        Thread.sleep(1000);
        System.out.println("回调次数:" + integer.get());
    }

    @Test
    public void test2() {
        MsgCenter.Key key = new MsgCenter.Key();
        MsgCenter.register(key, (k, msg) -> {
            System.out.println("收到:" + msg);
        });
        MsgCenter.sendMsg(key, "通知");
    }

}
