package com.javaBasic.javaBasic.concurrent;

import java.util.concurrent.*;
import java.util.stream.Stream;

public class CompletionServiceDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService fixed = Executors.newFixedThreadPool(3);
        // CompletionService → 先完成的先取出，效率更高
        CompletionService<String> cs = new ExecutorCompletionService<>(fixed);

        Stream.iterate(1, i -> i <= 5, i -> i + 1)
                .forEach(i -> {
                    cs.submit(() -> {
                        try {
                            String tName = Thread.currentThread().getName();
                            long time = (long) (Math.random() * 2000);
                            Thread.sleep(time);
                            System.out.println(time);
                            return tName + "任務 " + i + " 完成";
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });

        for (int i = 1; i <= 5; i++) {
            Future<String> future = cs.take();
            System.out.println(future.get());
        }

        fixed.shutdown();
    }
}
