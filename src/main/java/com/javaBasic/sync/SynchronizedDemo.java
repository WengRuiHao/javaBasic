package com.javaBasic.javaBasic.sync;

public class SynchronizedDemo {
    private  int count = 0;

    public synchronized void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedDemo demo = new SynchronizedDemo();

        Thread t1 = new Thread(() -> {
            for (int i=0;i<10000;i++) demo.increase();
        });

        Thread t2 = new Thread(() -> {
            for (int i=0;i<10000;i++) demo.increase();
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("最後結果: " + demo.getCount());
    }
}
