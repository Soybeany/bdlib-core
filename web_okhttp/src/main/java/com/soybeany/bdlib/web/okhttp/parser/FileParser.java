package com.soybeany.bdlib.web.okhttp.parser;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.core.util.file.FileUtils;
import com.soybeany.bdlib.core.util.file.IProgressListener;

import java.io.File;

import okhttp3.ResponseBody;

/**
 * 文件解析器，若传入临时文件，则会先将文件下载到临时文件，再替换目标文件
 * <br>Created by Soybeany on 2019/2/27.
 */
public class FileParser implements IParser<File> {
    private File mDestFile;
    private File mTmpFile;

    public FileParser(File destFile, File tmpFile) {
        mDestFile = destFile;
        mTmpFile = tmpFile;
    }

    @Override
    public File parse(ResponseBody body, IProgressListener listener) throws Exception {
        try {
            FileUtils.writeToFile(body.byteStream(), Optional.ofNullable(mTmpFile).orElse(mDestFile), body.contentLength(), listener);
            FileUtils.copyFile(mTmpFile, mDestFile);
        } finally {
            FileUtils.deleteFile(mTmpFile);
        }
        return mDestFile;
    }
}
