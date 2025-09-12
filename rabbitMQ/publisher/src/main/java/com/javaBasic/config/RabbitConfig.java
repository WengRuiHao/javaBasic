package com.javaBasic.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;

    @PostConstruct
    public void init() {
        System.out.println("RabbitMQ Host: " + rabbitHost);
        System.out.println("RabbitMQ Port: " + rabbitPort);
    }
}

