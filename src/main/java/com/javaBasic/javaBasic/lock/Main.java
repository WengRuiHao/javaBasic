package com.javaBasic.javaBasic.lock;

public class Main {
    public static void main(String[] args) {
        Account account = new Account(10000);

        Runnable r1 = new Withdraw(account, 5000);
        Runnable r2 = new Withdraw(account, 4000);
        Runnable r3 = new Withdraw(account, 3000);

        Thread t1 = new Thread(r1);
        t1.setName("小紅");
        Thread t2 = new Thread(r2);
        t2.setName("小黃");
        Thread t3 = new Thread(r3);
        t3.setName("小綠");

        t1.start();
        t2.start();
        t3.start();
    }
}
