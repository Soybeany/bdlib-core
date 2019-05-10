package com.soybeany.bdlib.web.okhttp.part;

import okhttp3.Call;

/**
 * <br>Created by Soybeany on 2019/4/11.
 */
public interface IRequestPart<T> {
    Call newCall(T request);
}
