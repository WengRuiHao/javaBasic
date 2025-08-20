package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService cashed = Executors.newCachedThreadPool();

        for (int i = 1; i <= 20; i++) {
            int taskId = i;
            cashed.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "執行任務 " + taskId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
//            System.out.println("目前池子大小: " + executor.getPoolSize() +
//                    "，活動執行緒: " + executor.getActiveCount());
        }
        cashed.shutdown();
    }
}
