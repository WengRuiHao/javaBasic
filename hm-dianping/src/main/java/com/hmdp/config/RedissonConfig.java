package com.hmdp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.29.86.187:6379").setPassword("abc123");
        // 創建 Redisson 對象
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient2() {
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.29.86.187:6380").setPassword("abc123");
        // 創建 Redisson 對象
        return Redisson.create(config);
    }

    @Bean
    public RedissonClient redissonClient3() {
        // 配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.29.86.187:6381").setPassword("abc123");
        // 創建 Redisson 對象
        return Redisson.create(config);
    }
}
