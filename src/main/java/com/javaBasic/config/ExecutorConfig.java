package com.javaBasic.javaBasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ExecutorConfig {

    @Bean("customExecutor")
    public Executor customExecutor(ThreadPoolProperties props) {
        return new ThreadPoolExecutor(
                props.getCoreSize(),
                props.getMaxSize(),
                props.getKeepAlive(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(props.getQueueCapacity()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
