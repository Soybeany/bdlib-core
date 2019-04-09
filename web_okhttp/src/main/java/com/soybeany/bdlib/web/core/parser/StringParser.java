package com.soybeany.bdlib.web.core.parser;

import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.core.request.IResponse;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public class StringParser implements IParser<String> {
    private static final StringParser mInstance = new StringParser();

    public static StringParser get() {
        return mInstance;
    }

    @Override
    public String parse(IResponse response, IProgressListener listener) throws Exception {
        return FileUtils.readString(response.charStream(), response.contentLength(), listener);
    }
}
