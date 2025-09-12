package com.javaBasic.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQListener {

    @RabbitListener(queues = "hello.queue2")
    public void listenSimpleQueue(String msg) {
        System.out.println("消費者抽到hello.queue1的消息: [" + msg + "] ");
    }
}
