package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class RunableWorker implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunableWorker.class);
    private String threadName;

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        this.threadName = Thread.currentThread().getName();
        try {
            Thread.sleep(2000);
            LOGGER.info("{} 线程处理提交任务完成",threadName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
