package com.javaBasic.javaBasic.lock;

public class Withdraw implements Runnable {
    private Account account;
    private int amount;

    public Withdraw(Account account, int amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        boolean keepTrying = true;
        while(keepTrying) {
            keepTrying = account.withdraw(amount);
            try {
                Thread.sleep(500); // 模擬下一次嘗試提款的等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " 停止嘗試提款。");
    }
}
