package com.javaBasic.javaBasic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "thread.pool")
@Data
public class ThreadPoolProperties {
    private int coreSize;
    private int maxSize;
    private int queueCapacity;
    private int keepAlive;
}
