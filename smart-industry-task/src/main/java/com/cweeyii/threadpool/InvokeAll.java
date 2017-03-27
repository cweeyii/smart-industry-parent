package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class InvokeAll {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeAll.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int poolSize = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        List<CallableWorker> workerList = new ArrayList<>();
        for (int i = 0; i < poolSize * 2; i++) {
            workerList.add(new CallableWorker());
        }
        List<Future<CallerHandle>> futureList = executorService.invokeAll(workerList);
        executorService.shutdown();
        LOGGER.info("线程池已经被关闭");
        for (Future<CallerHandle> future : futureList) {
            CallerHandle handle = future.get();
            LOGGER.info(handle.getThreadName()+"已经完成");
        }
        LOGGER.info("主线程结束");
    }
}
