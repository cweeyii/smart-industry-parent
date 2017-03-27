package com.cweeyii.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Created by wenyi on 16/10/16.
 * Email:caowenyi@meituan.com
 */
public class CallableWorker implements Callable<CallerHandle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallableWorker.class);

    @Override
    public CallerHandle call() throws Exception {
        String threadName = Thread.currentThread().getName();
        Thread.sleep(2000);
        CallerHandle handle = new CallerHandle(threadName);
        LOGGER.info("{} 结束任务", threadName);
        return handle;
    }
}
