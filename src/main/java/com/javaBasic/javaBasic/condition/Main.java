package com.javaBasic.javaBasic.condition;

public class Main {
    public static void main(String[] args) {
        Account account = new Account(0);
        Runnable r1 = ()-> {
            account.withdraw(5000);
        };
        Runnable r2 = ()-> {
            account.withdraw(3000);
        };
        Runnable r3 = ()-> {
            try {
                Thread.sleep(2000); // 過兩秒才存錢
                account.deposit(4000);
                Thread.sleep(2000);
                account.deposit(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable r4 = ()-> {
            account.withdraw(4000);
        };
        Thread t1 = new Thread(r1);
        t1.setName("小紅");
        Thread t2 = new Thread(r2);
        t2.setName("小黃");
        Thread t3 = new Thread(r3);
        t3.setName("小綠");
        Thread t4 = new Thread(r4);
        t4.setName("小藍");

        t1.start();
        t2.start();
        t3.start();
        t4.start();

    }
}
