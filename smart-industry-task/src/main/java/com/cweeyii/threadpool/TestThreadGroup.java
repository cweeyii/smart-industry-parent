package com.cweeyii.threadpool;

/**
 * Created by wenyi on 16/10/23.
 * Email:caowenyi@meituan.com
 */
public class TestThreadGroup {
    public static void main(String[] args){
        System.out.println("main_thread_group="+Thread.currentThread().getThreadGroup().getName());
        Thread t=new Thread(new SleepWorker());
        t.setDaemon(true);
        t.start();
        System.out.println("new_thread_group=" + t.getThreadGroup().getName());

    }
}
