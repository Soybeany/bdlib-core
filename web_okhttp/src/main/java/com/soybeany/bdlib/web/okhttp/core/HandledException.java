package com.soybeany.bdlib.web.okhttp.core;

import java.io.IOException;

/**
 * OkHttp中已处理的异常
 * <br>Created by Soybeany on 2019/3/1.
 */
public class HandledException extends IOException {
    public HandledException(String msg) {
        super(msg);
    }
}
