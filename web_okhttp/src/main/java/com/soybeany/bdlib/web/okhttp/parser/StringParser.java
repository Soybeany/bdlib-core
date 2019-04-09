package com.soybeany.bdlib.web.okhttp.parser;

import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;

import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public class StringParser implements IParser<String> {
    private static final StringParser mInstance = new StringParser();

    public static StringParser get() {
        return mInstance;
    }

    @Override
    public String parse(ResponseBody body, IProgressListener listener) throws Exception {
        return FileUtils.readString(body.charStream(), body.contentLength(), listener);
    }
}
