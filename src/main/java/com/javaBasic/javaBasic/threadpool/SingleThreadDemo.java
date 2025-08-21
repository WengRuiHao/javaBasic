package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class SingleThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService single = Executors.newSingleThreadExecutor();

//        for (int i = 1; i <= 5; i++) {
//            int taskId = i;
//            single.submit(() -> {
//                System.out.println(Thread.currentThread().getName() + "執行任務 " + taskId);
//            });
//            Thread.sleep(1000);
//        }

        Stream.iterate(1, i -> i <= 5, i -> i + 1)
                .forEach(i -> {
                    single.submit(() -> {
                        System.out.println(Thread.currentThread().getName() + "執行任務 " + i);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });

        single.shutdown();
    }
}
