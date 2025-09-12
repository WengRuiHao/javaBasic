package com.javaBasic.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQListener {

    @RabbitListener(queues = "hello.queue1")
    public void listenSimpleQueue(String msg) {
        System.out.println("消費者 收到 hello.queue1 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消費者1 收到 work.queue 的消息: [" + msg + "] ");
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消費者2 收到 work.queue 的消息: [" + msg + "] ");
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) throws InterruptedException {
        System.out.println("消費者1 收到 fanout.queue1 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) throws InterruptedException {
        System.out.println("消費者2 收到 fanout.queue2 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String msg) throws InterruptedException {
        System.out.println("消費者1 收到 direct.queue1 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueue2(String msg) throws InterruptedException {
        System.out.println("消費者2 收到 direct.queue2 的消息: [" + msg + "] ");
    }
}
