package com.soybeany.bdlib.web.core.request;


import java.util.Map;

/**
 * <br>Created by Soybeany on 2017/1/24.
 */
public class URLParser {

    /**
     * 合并URL
     */
    public static String mergeUrl(String url, Map<String, String> paramMap) {
        if (null == paramMap || 0 == paramMap.size()) {
            return url;
        }

        StringBuilder paramsStr = new StringBuilder(url);
        paramsStr.append("?");
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            paramsStr.append(key).append("=").append(value).append("&");
        }
        paramsStr.deleteCharAt(paramsStr.length() - 1);
        return paramsStr.toString();
    }

    /**
     * 拆分URL
     *
     * @param paramMap 用于储存拆分后的参数
     * @return 去掉参数后的url
     */
    public static String splitURL(String url, Map<String, String> paramMap) {
        String[] strArr;
        if (null == url || null == paramMap || (strArr = url.split("\\?")).length != 2) {
            return url;
        }

        String[] paramArr = strArr[1].split("&");
        for (String paramStr : paramArr) {
            String[] param = paramStr.split("=");
            if (param[0].trim().length() != 0) {
                paramMap.put(param[0], param[1]);
            }
        }
        return strArr[0];
    }

}
