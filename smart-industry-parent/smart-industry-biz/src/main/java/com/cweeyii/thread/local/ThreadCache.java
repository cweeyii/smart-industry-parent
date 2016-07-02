package com.cweeyii.thread.local;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenyi on 16/6/12.
 * Email:caowenyi@meituan.com
 */
public class ThreadCache {
    private static final ThreadLocal<ThreadContext> cache = new ThreadLocal<ThreadContext>() {
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };

    private static class ThreadContext {
        String ip;
        long startTime;
        String uri;
        long costTime;
        Map<String,Object> map=new HashMap<>();
    }

    public static void setIp(String ip) {
        cache.get().ip = ip;
    }

    public static String getIp() {
        return cache.get().ip;
    }

    public static void setUri(String uri) {
        cache.get().uri = uri;
    }

    public static String getUri() {
        return cache.get().uri;
    }

    public static void setStartTime() {
        cache.get().startTime = System.currentTimeMillis();
    }

    public static Long getStartTime(){
        return cache.get().startTime;
    }

    public static void setCostTime(Long costTime) {
        cache.get().costTime = costTime;
    }

    public static Long getCostTime() {
        return cache.get().costTime;
    }

    public static void setLocalObject(String name,Object obj){
        if(cache.get().map.get(name)!=null)
            throw new IllegalArgumentException("已经存在的缓存对象");
        cache.get().map.put(name, obj);
    }

    public static Object getObject(String name){
        return cache.get().map.get(name);
    }
}
