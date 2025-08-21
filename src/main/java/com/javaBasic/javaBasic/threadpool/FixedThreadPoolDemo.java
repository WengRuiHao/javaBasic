package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class FixedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService fixed = Executors.newFixedThreadPool(3);
//        for (int i = 1; i <= 10; i++) {
//            int taskId = i;
//            fixed.submit(() -> {
//                System.out.println(Thread.currentThread().getName() + "執行任務 " + taskId);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//        }

        Stream.iterate(1, i -> i <= 10, i -> i + 1)
                .forEach(i -> {
                    fixed.submit(() -> {
                        System.out.println(Thread.currentThread().getName() + "執行任務 " + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
        fixed.shutdown();
    }
}
