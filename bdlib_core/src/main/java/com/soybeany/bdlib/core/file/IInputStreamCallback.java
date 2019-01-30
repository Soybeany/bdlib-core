package com.soybeany.bdlib.core.file;

import java.io.InputStream;

/**
 * 输入流回调
 * <br>Created by Soybeany on 2017/5/4.
 */
public interface IInputStreamCallback<Result> {
    /**
     * 处理输入流时的回调
     */
    Result onHandle(InputStream stream);
}
