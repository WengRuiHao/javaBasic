package com.javaBasic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple.retry", name = "enabled", havingValue = "true")
public class ErrorConfiguration {

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange("error.direct");
    }

    /**
     * 如果註解建立 Exchange Queue Binding 有用 lazy Queue 這邊也統一需要設定 lazy Queue 不然會導致Binding失敗
     */
    @Bean
    public Queue errorQueue() {
        return QueueBuilder.durable("error.queue").lazy().build();
//        return new Queue("error.queue");
    }

    @Bean
    public Binding errorBinding(DirectExchange errorExchange, Queue errorQueue) {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with("error");
    }

    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
