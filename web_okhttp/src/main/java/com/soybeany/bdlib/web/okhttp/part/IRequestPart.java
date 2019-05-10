package com.soybeany.bdlib.web.okhttp.part;

import okhttp3.Call;
import okhttp3.Request;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IRequestPart {
    Call newCall(Request request);
}
