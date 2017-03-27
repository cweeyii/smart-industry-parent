package com.cweeyii;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

/**
 * Created by wenyi on 16/8/22.
 * Email:caowenyi@meituan.com
 */
public class Test {
    public static void main(String[] args) throws Exception {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 10));
        builder.sessionTimeoutMs(1000);
        builder.connectionTimeoutMs(1000);
        CuratorFramework client = builder.build();

        client.start();
        boolean connectedOrTimedOut = client.getZookeeperClient().blockUntilConnectedOrTimedOut();
        if (!connectedOrTimedOut) {
            throw new RuntimeException("");
        }

// client.create().forPath("/test1");

        InterProcessMutex mutex = new InterProcessMutex(client, "/test2");
        mutex.acquire(1, TimeUnit.MILLISECONDS);
        System.out.println("AAA");
        mutex.release();
    }
}
