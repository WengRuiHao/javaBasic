package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService fixed = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            int taskId = i;
            fixed.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "執行任務 " + taskId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        fixed.shutdown();
    }
}
