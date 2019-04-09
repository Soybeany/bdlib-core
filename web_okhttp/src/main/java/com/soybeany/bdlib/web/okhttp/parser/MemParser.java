package com.soybeany.bdlib.web.okhttp.parser;

import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;

import okhttp3.ResponseBody;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public class MemParser implements IParser<byte[]> {
    private static final MemParser mInstance = new MemParser();

    public static MemParser get() {
        return mInstance;
    }

    @Override
    public byte[] parse(ResponseBody body, IProgressListener listener) throws Exception {
        return FileUtils.writeToMem(body.byteStream(), body.contentLength(), listener);
    }
}
