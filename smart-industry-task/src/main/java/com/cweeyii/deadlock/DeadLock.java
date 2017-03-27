package com.cweeyii.deadlock;

import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wenyi on 16/11/26.
 * Email:caowenyi@meituan.com
 */
public class DeadLock {
    private Object firstObject=new Object();
    private Object secondObject=new Object();

    public void method1() throws InterruptedException {
        synchronized (firstObject){
            Thread.sleep(1000);
            synchronized (secondObject){
                System.out.println("method1 be called");
            }
        }
    }

    public void method2() throws InterruptedException {
        synchronized (secondObject){
            Thread.sleep(1000);
            synchronized (firstObject){
                System.out.println("method1 be called");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService=new ThreadPoolExecutor(
                4, 4, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                final SecurityManager s = System.getSecurityManager();
                ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
                        .getThreadGroup();
                final Thread t = new Thread(group, r, String.format(Locale.ROOT, "%s",
                        "deadlock-pool"), 0);
                t.setDaemon(false);
                t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        }, new ThreadPoolExecutor.CallerRunsPolicy());

        final DeadLock deadLock=new DeadLock();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    deadLock.method1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    deadLock.method2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
        while(!executorService.isTerminated()){
            System.out.println("线程池中还有任务在进行....");
            Thread.sleep(10000);
        }
        System.out.println("线程池中任务已经完成");
    }
}
