package com.hmdp.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements ILock {

    private String name;
    private StringRedisTemplate stringRedisTemplate;

    public SimpleRedisLock(String name, StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.name = name;
    }

    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    @Override
    public boolean tryLock(long timeoutSec) {
        // 獲取線程標示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 獲取鎖
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(KEY_PREFIX + name, threadId, timeoutSec, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    @Override
    public void unlock() {
        // 獲取線程標示
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        // 獲取鎖中的標示
        String id = stringRedisTemplate.opsForValue().get(KEY_PREFIX + name);
        // 判斷標示是否一致
        if (threadId.equals(id)) {
            // 釋放鎖
            stringRedisTemplate.delete(KEY_PREFIX + name);
        }
    }
}
