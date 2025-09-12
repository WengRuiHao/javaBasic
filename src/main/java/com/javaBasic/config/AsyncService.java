package com.javaBasic.javaBasic.config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async("customExecutor")
    public void sendEmail(String email) {
        System.out.println(Thread.currentThread().getName() + " 處理寄信給: " + email);
        try {
            Thread.sleep(3000); // 模擬耗時操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("已寄出：" + email);
    }
}
