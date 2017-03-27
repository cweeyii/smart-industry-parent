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
public class CallableThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallableThreadPool.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int threadSize = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        List<Future<CallerHandle>> futureList=new ArrayList<>();
        for (int i = 0; i < threadSize * 2; i++) {
            CallableWorker worker = new CallableWorker();
            Future<CallerHandle> future = executorService.submit(worker);
            futureList.add(future);
        }
        executorService.shutdown();
        for(Future<CallerHandle> future:futureList){
            CallerHandle callerHandle=future.get();
            LOGGER.info("{} 已经完成任务",callerHandle.getThreadName());
        }
        LOGGER.info("线程池关闭成功");
    }
}
