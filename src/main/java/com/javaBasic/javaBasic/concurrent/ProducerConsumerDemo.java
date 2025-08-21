package com.javaBasic.javaBasic.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

        // 生產者
        Runnable producer = () -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.put("任務" + i);
                    System.out.println("生產: " + i);
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // 消費者
        Runnable consumer = () -> {
            while (true) {
                String task = null;
                try {
                    task = queue.take();
                    System.out.println(Thread.currentThread().getName() + " 處理 " + task);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        };

        new Thread(producer).start();
        new Thread(consumer).start();
//        new Thread(consumer).start();
    }
}
