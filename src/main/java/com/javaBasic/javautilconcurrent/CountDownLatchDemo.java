package com.javaBasic.javaBasic.javautilconcurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "初始化完成");
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await(); // 等待所有子執行緒完成
        System.out.println("所有服務已啟動，開始執行主邏輯");
    }
}
