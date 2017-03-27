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
public class WorkerThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerThreadPool.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int threadSize = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        List<Future<CallerHandle>> futureList = new ArrayList<>();
        for (int i = 0; i < threadSize * 2; i++) {
            RunableWorker worker = new RunableWorker();
            CallerHandle handle = new CallerHandle();
            Future<CallerHandle> future = executorService.submit(worker, handle);
            futureList.add(future);
        }
        for (Future<CallerHandle> future : futureList) {
            CallerHandle callerHandle = future.get();
            LOGGER.info("{} 已经完成任务", callerHandle.getThreadName());
        }
        executorService.shutdown();
        LOGGER.info("线程池关闭成功");
    }
}
