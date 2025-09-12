package com.javaBasic.javaBasic.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private int balance;
    private final Lock lock = new ReentrantLock();

    public Account(int balance) {
        this.balance = balance;
    }

    public boolean withdraw(int amount) {
        String tName = Thread.currentThread().getName();
        System.out.println(tName + " 嘗試進入ATM...");
        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                try {
                    System.out.println(tName + " 成功進入ATM，開始提款...");
                    Thread.sleep(1000); // 模擬提款處理
                    System.out.println(tName + " 進來提款: " + amount);
                    Thread.sleep(2000);
                    if (balance >= amount) {
                        balance -= amount;
                        System.out.println("提款成功，餘額: " + balance);
                        return true;
                    } else {
                        System.out.println("提款失敗，餘額不足");
                        return false;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                    System.out.println(tName + " 離開ATM。");
                }
            } else {
                System.out.println(tName + " ATM 忙碌中，請稍後再試。");
                return true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
