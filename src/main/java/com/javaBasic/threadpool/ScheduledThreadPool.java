package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPool {
    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // 延遲 2 秒後執行一次   schedule( 業務邏輯 , 時間 , 時間單位 )
        scheduler.schedule(() -> System.out.println("延遲任務"), 2, TimeUnit.SECONDS);

        // 每 1 秒執行一次
        scheduler.scheduleAtFixedRate(() -> System.out.println("週期性任務 at " + System.currentTimeMillis()),
                0, 1, TimeUnit.SECONDS);

        // 讓程式跑 5 秒結束
        Thread.sleep(5000);
        scheduler.shutdown();
    }
}
