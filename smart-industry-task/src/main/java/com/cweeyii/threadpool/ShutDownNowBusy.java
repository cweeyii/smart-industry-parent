package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class ShutDownNowBusy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownNowBusy.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        int workerNum = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(workerNum);
        for (int i = 0; i < workerNum*3; i++) {
            BusyWorker worker=new BusyWorker();
            worker.setWorkerName("worker" + i);
            executorService.submit(worker);
        }
        List<Runnable> workerList=executorService.shutdownNow();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        LOGGER.info("调用shutdown会立即返回不会等待线程池任务执行完");
        for(Runnable woker:workerList){
            FutureTask<BusyWorker> futureTask=(FutureTask<BusyWorker>)woker;
            futureTask.run();
        }
    }
}
