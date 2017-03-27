package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class MainExit {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainExit.class);

    public static void main(String[] args) {
        Thread thread1=new Thread(new SleepWorker());
        Thread thread2=new Thread(new SleepWorker());
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        LOGGER.info("主线程main已经完成={}",Thread.currentThread().getName());
    }
}
