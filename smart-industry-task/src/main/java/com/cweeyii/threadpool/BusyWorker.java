package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class BusyWorker implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusyWorker.class);

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
            busyMethod();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("threadName={} 线程结束{}工作", threadName,workerName);
    }

    private void busyMethod() throws InterruptedException{
        int k=0;
        for(long i=0;i<100000L;i++){
            for(long j=0;j<100000L;j++)
                k++;
        }
    }
}
