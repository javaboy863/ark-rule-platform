package com.ark.rule.platform.common.util;


import java.util.Objects;
import java.util.Random;

/**
 */
public class ThreadLocalContainer {

    /**
     * uri 线程副本变量
     */
    private static final ThreadLocal<String> uriThreadLocal = new ThreadLocal<String>();

    /**
     * uri 线程副本变量
     */
    private static final ThreadLocal<String> uniqueLogIdThreadLocal = new ThreadLocal<String>();

    /**
     * http request 字符串 线程副本变量
     */
    private static final ThreadLocal<String> requestStringThreadLocal = new ThreadLocal<String>();



    private static final ThreadLocal<String> postBodyThreadLocal = new ThreadLocal<>();

    private static final String TAG = "\\^";
    private static final int START = 10000000;

    private static final int END = 99999999;


    public static String generateRandomNumber() {
        long timeStamp = System.currentTimeMillis() / 1000;
        int randomInt = new Random().nextInt(END - START) + START;
        return new StringBuilder().append(timeStamp).append(randomInt).toString();
    }


    /**
     * 设置uri,同时设置当前线程唯一的日志id.
     *
     * @param uri
     */
    public static void setURI(String uri) {
        if (uri != null) {
            String[] strArray = uri.split(TAG);
            // 长度为1,表示是第一次设置,否则是重新设置
            if (strArray.length == 1) {
                String logId = generateRandomNumber();
                uniqueLogIdThreadLocal.set(logId);
                uriThreadLocal.set(new StringBuilder().append(uri).append("^").append(logId).toString());
            } else {
                uriThreadLocal.set(uri);
                uniqueLogIdThreadLocal.set(strArray[1]);
            }
        }
    }



    /**
     * 获取threadlocal的url.
     *
     * @return
     */
    public static String getURL() {
        String uri = uriThreadLocal.get();
        return uri == null ? "" : uri;
    }

    /***
     * 查询http request请求的串
     * @return
     */
    public static String getRequestString() {
        String requestString = requestStringThreadLocal.get();
        return requestString == null ? "" : requestString;
    }

    /***
     * 给查询串赋值
     * @return
     */
    public static void setRequestString(String requestString) {
        String request = requestString == null ? "" : requestString;
        requestStringThreadLocal.set(request);
    }

    public static void setPostBody(String postBody) {
        postBodyThreadLocal.set(Objects.toString(postBody,""));
    }

    public static String getPostBody() {
        return Objects.toString(postBodyThreadLocal.get(),"");
    }

    /**
     * 清理uriThreadLocal.
     */
    public static void clearUriThreadLocal() {
        uriThreadLocal.remove();
    }

    /**
     * 清理uniqueLogIdThreadLocal.
     */
    public static void clearUniqueLogIdThreadLocal() {
        uniqueLogIdThreadLocal.remove();
    }


    public static void clear() {
        postBodyThreadLocal.remove();
        requestStringThreadLocal.remove();
    }

}
