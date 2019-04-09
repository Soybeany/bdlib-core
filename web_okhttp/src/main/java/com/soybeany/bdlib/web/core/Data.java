package com.soybeany.bdlib.web.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求使用的数据
 * <br>Created by Soybeany on 2019/2/26.
 */
public class Data {

    // //////////////////////////////////请求方法区//////////////////////////////////

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;

    // //////////////////////////////////POST类型区//////////////////////////////////

    public static final int POST_TYPE_NOT_DEFINE = 0;
    public static final int POST_TYPE_JSON = 1;
    public static final int POST_TYPE_FILE = 2;
    public static final int POST_TYPE_FORM = 3;
    public static final int POST_TYPE_MULTIPART = 4;

    // //////////////////////////////////常规字段区//////////////////////////////////

    public int method;
    public int postType;
    public String url;
    public final Map<String, String> headers = new HashMap<>();
    public final Map<String, String> params = new HashMap<>();
    public Object tag;

    public final Map<String, Object> ex = new HashMap<>();
}