package com.javaBasic.javaBasic.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

class Account {
    private int balance;
    private final Lock lock = new ReentrantLock();

    public Account(int balance) {
        this.balance = balance;
    }

    // 存錢
    public void deposite(int amount) {
        lock.lock();
        String tName = Thread.currentThread().getName();
        try {
            balance += amount;
            System.out.println(tName + "存入: " + amount + "，餘額: " +balance);
        } finally {
            lock.unlock();
        }
    }

    // 提錢
    public void withdraw(int amount) {
        lock.lock();
        String tName = Thread.currentThread().getName();
        try {
            if(balance >= amount) {
                balance -= amount;
                System.out.println(tName + " 提領: " + amount + "，餘額: " + balance);
            } else {
                System.out.println(tName + " 提領失敗，餘額不足!");
            }
        } finally {
            lock.unlock();
        }
    }
}

public class ThreadPoolExecutorDemo2 {
    public static void main(String[] args) {
        Account account = new Account(1000);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                3,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Stream.iterate(1, i -> i<=20, i -> i+1)
                .forEach(i -> {
                    int amount = (i%2 == 0) ? 200 : 300;
                    threadPoolExecutor.submit(() -> {
                        if(Math.random() > 0.5) {
                            account.deposite(amount);
                        } else {
                            account.withdraw(amount);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
        threadPoolExecutor.shutdown();
    }
}
