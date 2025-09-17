package com.javaBasic.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class MQListener {

    @RabbitListener(queues = "hello.queue1")
    public void listenSimpleQueue(String msg) {
        /**
         * 在使用類型 Message 在測試用
         */
//        System.out.println("消費者 收到 hello.queue1 的消息id: [" + msg.getMessageProperties().getMessageId() + "] ");
//        System.out.println("消費者 收到 hello.queue1 的消息: [" + new String(msg.getBody(), StandardCharsets.UTF_8) + "] ");
        System.out.println("消費者 收到 hello.queue1 的消息: [" + msg + "] ");
//        throw new RuntimeException("故意的");
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

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "ruihao.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"} // routingKey
    ))
    public void listenDirectQueue1(String msg) throws InterruptedException {
        System.out.println("消費者1 收到 direct.queue1 的消息: [" + msg + "] ");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            exchange = @Exchange(name = "ruihao.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"} // routingKey
    ))
    public void listenDirectQueue2(String msg) throws InterruptedException {
        System.out.println("消費者2 收到 direct.queue2 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String msg) throws InterruptedException {
        System.out.println("消費者1 收到 topic.queue1 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String msg) throws InterruptedException {
        System.out.println("消費者2 收到 topic.queue2 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "lazy.queue")
    public void listenObject(Map<String, Object> msg) throws InterruptedException {
        System.out.println("消費者 收到 object.queue 的消息: [" + msg + "] ");
    }

//    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(Map<String, Object> msg) throws InterruptedException {
        log.info("消費者 收到 object.queue 的消息: [" + msg + "] ");
    }

    @RabbitListener(queues = "dlx.queue")
    public void listenDlxQueue(String msg) throws InterruptedException {
        log.info("消費者 收到 dlx.queue 的消息: [" + msg + "] ");
    }
}
