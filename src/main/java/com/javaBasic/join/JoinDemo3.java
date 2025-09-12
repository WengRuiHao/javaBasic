package com.javaBasic.javaBasic.join;

public class JoinDemo3 {
    public static void main(String[] args) {
        Thread t2 = new Thread(() -> {
            System.out.println("下班");
        });

        Thread t1 = new Thread(() -> {
            int count = 10;
            for (int i = 1; i <= 10; i++) {
                System.out.printf("今天要做 %d 工作, 目前已完成 %d / %d\n", count, i, count);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 工作完才能下班
            t2.start();
        });
        t1.start();

        Thread urgenTask = new Thread(() -> {
            System.out.println("處理緊急任務~~");
        });

        try {
            Thread.sleep(3000);
            // 中斷/插件
            Thread.currentThread().interrupt(); // 中斷主執行緒，觸發 InterruptedException
            urgenTask.start(); // 處理緊急任務執行緒
            urgenTask.join();
        } catch (InterruptedException e) {
            System.out.println("下班情緒被打斷，主管交辦緊急任務...");
        }
    }
}
