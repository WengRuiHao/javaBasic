package com.ruihao.jedis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

public class JedisConnectionFactory {
    private  static final JedisPool jedisPool;

    static {
        // 配置連接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(8); // 最大連線數量
        jedisPoolConfig.setMaxIdle(8); // 最大空閒連接
        jedisPoolConfig.setMinIdle(0); // 最小空閒連接
        jedisPoolConfig.setMaxWait(Duration.ofSeconds(1)); // 設置最常等待時間
        // 創建連接池對象
        jedisPool = new JedisPool(jedisPoolConfig,"localhost",6379,1000,"123321");
    }

    public static Jedis getJedis() {
        return jedisPool.getResource();
    }
}
