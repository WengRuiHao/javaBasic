package publisher;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.javaBasic.PublisherApplication.class)
public class PublisherTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSendMessage2Queue() {
        System.out.println("123");
        String queueName = "hello.queue1";
        String msg = "hello, amqp!";
        rabbitTemplate.convertAndSend(queueName, msg);
    }
}
