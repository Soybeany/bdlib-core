package com.soybeany.bdlib.web.core.request;

import com.soybeany.bdlib.core.java8.Optional;
import com.soybeany.bdlib.web.core.Data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 请求数据的建造器
 * <br>Created by Soybeany on 2019/2/26.
 */
public class DataBuilder {

    protected final Data mData = new Data();

    public DataBuilder(int method, int postType, String url) {
        mData.method = method;
        mData.postType = postType;
        mData.url = Optional.ofNullable(url).orElseThrow(() -> new RuntimeException("请求的url不能为null"));
    }

    public DataBuilder headers(Map<String, String> params) {
        mData.headers.clear();
        mData.headers.putAll(params);
        return this;
    }

    public DataBuilder addHeader(String key, String value) {
        mData.headers.put(key, value);
        return this;
    }

    public DataBuilder tag(Object tag) {
        Optional.ofNullable(tag).ifPresent(t -> mData.tag = tag);
        return this;
    }

    public <Request extends IRequest> Request build(IRequestFactory<Request> factory) {
        return factory.build(mData);
    }

    public static class WithParam extends DataBuilder {
        protected WithParam(int method, int postType, String url) {
            super(method, postType, url);
        }

        public DataBuilder.WithParam params(Map<String, String> params) {
            mData.params.clear();
            mData.params.putAll(params);
            return this;
        }

        public DataBuilder.WithParam addParam(String key, String value) {
            mData.params.put(key, value);
            return this;
        }
    }

    // //////////////////////////////////实际建造器//////////////////////////////////

    public static class Get extends DataBuilder.WithParam {
        public Get(String url) {
            super(Data.METHOD_GET, Data.POST_TYPE_NOT_DEFINE, url);
        }
    }

    public static class Post extends DataBuilder.WithParam {
        public Post(String url) {
            super(Data.METHOD_POST, Data.POST_TYPE_FORM, url);
        }
    }

    public static class PostForm extends DataBuilder.WithParam {
        public PostForm(String url) {
            super(Data.METHOD_POST, Data.POST_TYPE_FORM, url);
        }
    }

    public static class PostJson extends DataBuilder.WithParam {
        public PostJson(String url) {
            super(Data.METHOD_POST, Data.POST_TYPE_JSON, url);
        }
    }

    public static class PostFile extends DataBuilder {
        public static final String KEY_FILE = "key_file";

        public PostFile(String url, File file) {
            super(Data.METHOD_POST, Data.POST_TYPE_FILE, url);
            mData.ex.put(KEY_FILE, file);
        }
    }

    public static class Multipart extends DataBuilder.WithParam {
        public static final String KEY_FILES = "key_files";

        public Multipart(String url) {
            super(Data.METHOD_POST, Data.POST_TYPE_MULTIPART, url);
        }

        @SuppressWarnings("unchecked")
        public Multipart addFile(FileInfo info) {
            List<FileInfo> fileInfoList = (List<FileInfo>) mData.ex.get(KEY_FILES);
            if (null == fileInfoList) {
                mData.ex.put(KEY_FILES, fileInfoList = new LinkedList<>());
            }
            fileInfoList.add(info);
            return this;
        }

        public static class FileInfo {
            public String key;
            public String fileName;
            public File file;
        }
    }
}
