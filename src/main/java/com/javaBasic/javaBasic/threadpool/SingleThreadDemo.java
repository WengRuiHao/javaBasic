package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService single = Executors.newSingleThreadExecutor();

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            single.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "執行任務 " + taskId);
            });
            Thread.sleep(1000);
        }
        single.shutdown();
    }
}
