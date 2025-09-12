package publisher;

import com.javaBasic.PublisherApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PublisherApplication.class)
public class PublisherTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testSendMessage2Queue() {
        String queueName = "hello.queue1";
//        String msg = "hello, amqp!";
        String msg = "你好, 接收者!";
        rabbitTemplate.convertAndSend(queueName, msg);
    }
}
