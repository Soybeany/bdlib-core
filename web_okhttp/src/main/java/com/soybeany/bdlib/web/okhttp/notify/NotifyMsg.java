package com.soybeany.bdlib.web.okhttp.notify;

/**
 * <br>Created by Soybeany on 2019/5/8.
 */
public class NotifyMsg {
    public String type;
    public Object data;

    public NotifyMsg(String type, Object data) {
        this.type = type;
        this.data = data;
    }


}
