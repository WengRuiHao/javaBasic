package com.javaBasic.javaBasic.javautilconcurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
            System.out.println("所有子任務準備完成，開始下一階段");
        });

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "準備中...");
                try {
                    Thread.sleep(1000);
                    cyclicBarrier.await(); //等待其他人
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName() + " 開始執行");
            }).start();
        }
    }
}
