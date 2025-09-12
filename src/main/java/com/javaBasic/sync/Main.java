package com.javaBasic.javaBasic.sync;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // 帳戶資源
        Account account = new Account(10000);
        // 提款任務
        Runnable r1 = new Withdraw(account, 5000);
        Runnable r2 = new Withdraw(account, 4000);
        Runnable r3 = new Withdraw(account, 3000);
        // 執行任務
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);

        t1.start();
        t1.join();

        t2.start();
        t2.join();

        t3.start();
        t3.join();
    }
}
