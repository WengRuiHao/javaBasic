package com.ruihao.test;

import com.ruihao.RedisApplication;
import com.ruihao.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testString1() {
        // 寫入一條 String 資料
        redisTemplate.opsForValue().set("name", "虎哥");
        // 獲取 String 資料
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testSaveUser() {
        // 寫入資料
        redisTemplate.opsForValue().set("user:100", new User("虎哥", 21));
        // 取得資料
        User o = (User) redisTemplate.opsForValue().get("user:100");
        System.out.println("o = " + o);
    }
}
