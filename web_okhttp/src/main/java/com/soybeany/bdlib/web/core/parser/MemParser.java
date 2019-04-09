package com.soybeany.bdlib.web.core.parser;

import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;
import com.soybeany.bdlib.web.core.request.IResponse;

/**
 * <br>Created by Soybeany on 2019/2/27.
 */
public class MemParser implements IParser<byte[]> {
    private static final MemParser mInstance = new MemParser();

    public static MemParser get() {
        return mInstance;
    }

    @Override
    public byte[] parse(IResponse response, IProgressListener listener) throws Exception {
        return FileUtils.writeToMem(response.byteStream(), response.contentLength(), listener);
    }
}
