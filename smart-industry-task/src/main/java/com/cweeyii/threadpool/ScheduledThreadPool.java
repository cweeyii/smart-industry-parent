package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class ScheduledThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledThreadPool.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //testScheduledMethod();
        //testFixScheduledMethod();
        testFixWithScheduledMethod();
    }

    private static void testScheduledMethod() throws ExecutionException, InterruptedException {
        int poolSize = 2;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize);
        List<Future<CallerHandle>> futureList = new ArrayList<>();
        for (int i = 0; i < poolSize * 2; i++) {
            futureList.add(executorService.schedule(new CallableWorker(), 2, TimeUnit.SECONDS));
        }
        executorService.shutdown();
        LOGGER.info("线程池已经被关闭");
        for (Future<CallerHandle> future : futureList) {
            CallerHandle handle = future.get();
            LOGGER.info(handle.getThreadName() + "已经完成");
        }
        LOGGER.info("主线程结束");
    }

    private static void testFixScheduledMethod() throws ExecutionException, InterruptedException {
        int poolSize = 2;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize);
        for (int i = 0; i < poolSize * 2; i++) {
            executorService.scheduleAtFixedRate(new RunableWorker(), 0, 5, TimeUnit.SECONDS);
        }
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        executorService.shutdown();
        LOGGER.info("线程池已经被关闭");
        LOGGER.info("主线程结束");
    }

    private static void testFixWithScheduledMethod() throws ExecutionException, InterruptedException {
        int poolSize = 2;
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize);
        for (int i = 0; i < poolSize * 2; i++) {
            executorService.scheduleWithFixedDelay(new RunableWorker(), 0, 5, TimeUnit.SECONDS);
        }
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        executorService.shutdown();
        LOGGER.info("线程池已经被关闭");
        LOGGER.info("主线程结束");
    }
}
