package com.ruihao.test;


import com.ruihao.RedisApplication;
import com.ruihao.pojo.User;
import com.ruihao.util.JedisConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Map;

@SpringBootTest
public class JedisTest {
    private Jedis jedis;

    @BeforeEach
    void setUp() {
        // 1.建立連接
//        jedis = new Jedis("localhost", 6379);
        // 2. 設置密碼
//        jedis.auth("123321");
        jedis = JedisConnectionFactory.getJedis();
        // 3. 選擇庫
        jedis.select(0);
    }

    @Test
    void testString() {
        // 存入數據
        String result = jedis.set("name", "虎哥");
        System.out.println("result = " + result);
        // 獲取數據
        String name = jedis.get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testHash() {
        // 插入 hash數據
        jedis.hset("user:1", "name", "Jack");
        jedis.hset("user:1", "age", "21");
        // 獲取
        Map<String, String> map = jedis.hgetAll("user:1");
        System.out.println(map);
    }

    @AfterEach
    void tearDown() {
        if (jedis != null) {
            jedis.close();
        }
    }
}
