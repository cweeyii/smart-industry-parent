package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class SleepWorker implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SleepWorker.class);
    private String workerName;


    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("threadName={} 线程结束{}工作", threadName,workerName);
    }
}
