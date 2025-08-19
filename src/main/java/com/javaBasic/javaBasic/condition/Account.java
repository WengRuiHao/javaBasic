package com.javaBasic.javaBasic.condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private int balance;
    private final Lock lock = new ReentrantLock();
    private final Condition noMoney = lock.newCondition();

    public Account(int balance) {
        this.balance = balance;
    }

    // 存款
    public void deposit(int amount) {
        String tName = Thread.currentThread().getName();
        lock.lock();
        try {
            balance += amount;
            System.out.println(tName + " 存款 " + amount + "，餘額: " + balance);
            noMoney.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // 提款
    public void withdraw(int amount) {
        String tName = Thread.currentThread().getName();
        System.out.println(tName + " 嘗試進入ATM...");
        lock.lock();
        try {
            System.out.println(tName + " 成功進入ATM，開始提款...");
            Thread.sleep(1000);
            while (balance < amount) { // 餘額不足 -> 等待
                System.out.println(tName + " 想提款 " + amount + "，但餘額不足(目前: " + balance + " )");
                noMoney.await(); // 等待通知
            }
            balance -= amount;
            System.out.println(tName + " 提款 " + amount + " 成功，剩餘餘額: " + balance);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

}
