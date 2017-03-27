package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class ShutDownWait {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownWait.class);

    public static void main(String[] args) throws InterruptedException {
        int workerNum = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(workerNum);
        for (int i = 0; i < workerNum; i++) {
            executorService.submit(new SleepWorker());
        }
        executorService.shutdown();
        LOGGER.info("shutdown=" + executorService.isShutdown() + " terminate=" + executorService.isTerminated());
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        LOGGER.info("shutdown="+executorService.isShutdown()+" terminate="+executorService.isTerminated());
        LOGGER.info("调用shutdown会立即返回不会等待线程池任务执行完");
    }
}
