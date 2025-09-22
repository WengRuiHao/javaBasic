package com.ruihao.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruihao.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

@SpringBootTest
public class RedisStringTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testString() {
        // 寫入一條 String 資料
        stringRedisTemplate.opsForValue().set("name", "虎哥");
        // 獲取 String 資料
        Object name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testSaveUser() throws JsonProcessingException {
        // 創建對象
        User user = new User("虎哥", 21);
        // 手動序列化
        String json = mapper.writeValueAsString(user);
        // 寫入資料
        stringRedisTemplate.opsForValue().set("user:100", json);
        // 取得資料
        String jsonUser = stringRedisTemplate.opsForValue().get("user:100");
        User user1 = mapper.readValue(jsonUser, User.class);
        System.out.println("o = " + user1);
    }

    @Test
    void testHash() {
        stringRedisTemplate.opsForHash().put("user:400", "name", "虎哥");
        stringRedisTemplate.opsForHash().put("user:400", "age", "21");

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:400");
        System.out.println("entries = "+ entries);
    }
}
