package com.javaBasic.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置 : exchange(交換機) Queue(拧列) rountingKey(拧列鑰匙) binding(交換機跟拧列的綁定)
 * 有更簡單的寫法: 註解的寫法 請看 MQListener.java 的 listenDirectQueue1 方法
 */
//@Configuration
public class FanoutConfiguration {

    @Bean
    public FanoutExchange fanoutExchange() {
//        ExchangeBuilder.fanoutExchange("ruihao.fanout").build();
        return new FanoutExchange("ruihao.fanout");
    }

    @Bean
    public DirectExchange directExchange() {
//        ExchangeBuilder.DirectExchange("ruihao.fanout").build();
        return new DirectExchange("ruihao.direct");
    }

    @Bean
    public Queue fanoutQueue3() {
//        QueueBuilder.durable("ruihao.queue3").build(); // durable 持久的
        return new Queue("ruihao.queue3");
    }

    @Bean
    public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }

    @Bean
    public Queue fanoutQueue4() {
//        QueueBuilder.durable("ruihao.queue4").build(); // durable 持久的
        return new Queue("ruihao.queue4");
    }

    @Bean
    public Binding fanoutBinding4() {
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange());
    }

    @Bean
    public Queue directQueue1() {
//        QueueBuilder.durable("direct.queue1").build(); // durable 持久的
        return new Queue("direct.queue1");
    }

    @Bean
    public Binding directQueue1BindingRed(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("red");
    }

    @Bean
    public Binding directQueue1BindingBlue(DirectExchange directExchange, Queue directQueue1) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("blue");
    }

    @Bean
    public Queue directQueue2() {
//        QueueBuilder.durable("direct.queue2").build(); // durable 持久的
        return new Queue("direct.queue2");
    }

    @Bean
    public Binding directQueue2BindingRed(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("red");
    }

    @Bean
    public Binding directQueue2BindingYellow(DirectExchange directExchange, Queue directQueue2) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("yellow");
    }
}
