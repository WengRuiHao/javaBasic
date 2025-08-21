package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.*;

public class ThreadPoolExecutorDemo {
    public static void main(String[] args) throws InterruptedException {
        /**
         * new ThreadPoolExecutor(設置執行緒數量，
         *                          最大執行緒數量，
         *                          等待時間，
         *                          工作佇列，
         *                          執行緒工廠，
         *                          拒絕策略
         *                    )
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                8,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        // 提交 10 個任務
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            Future<String> future = executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "執行任務 " + taskId);
                Thread.sleep(1000);
                return "任務 " + taskId + "完成";
            });

            // 取得任務結果 (這裡用非阻塞方式，延遲再 get)
            new Thread(() -> {
                try {
                    String result = future.get();
                    System.out.println("結果回傳: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            System.out.println("提交任務 " + taskId +
                    " |活躍執行緒: " + executor.getActiveCount() +
                    " |Queue 裡等待數: " + executor.getQueue().size());
        }

        // 平滑關閉池子
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("所有任務已完成，執行緒關閉。");
    }
}
