package publisher;

import com.javaBasic.PublisherApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
        System.out.println("msg = " + msg);
    }

    @Test
    void testWorkQueue() throws InterruptedException {
            String queueName = "work.queue";
        for (int i = 1; i <= 50; i++) {
            String msg = "hello, worker, message_" + i;
            rabbitTemplate.convertAndSend(queueName, msg);
            Thread.sleep(20);
        }
    }

    @Test
    void testSendFanout() {
        String exchange = "ruihao.fanout";
        String msg = "hello, everyone!";
        rabbitTemplate.convertAndSend(exchange, "", msg);
    }

    @Test
    void testSendDirect() {
        String exchange = "ruihao.direct";
        String msg = "藍色通知, 解除警報 ,哥斯拉是放的氣球!";
        rabbitTemplate.convertAndSend(exchange, "blue", msg);
    }

    @Test
    void testSendTopic() {
        String exchange = "ruihao.topic";
        String msg = "今天天氣挺不錯，我的心情挺好的   ";
        rabbitTemplate.convertAndSend(exchange, "china.wether", msg);
    }

    @Test
    void testSendObject() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("name", "Jack");
        msg.put("age", 21);
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    @Test
    void testConfirmCallback() throws InterruptedException {
        // 1. 創建cd
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        // 2. 添加 ConfirmCallback
        cd.getFuture().thenAccept(confirm -> {
            log.debug("收到 confirm callback 回執");
            if(confirm.isAck()) {
                // 消息發送成功
                log.debug("消送發送成功，收到 ack");
            } else {
                log.error("消息發送失敗，收到 nack ，原因: {}",confirm.getReason());
            }
        }).exceptionally(ex -> {
            log.error("消息回調失敗", ex);
           return null;
        });


        rabbitTemplate.convertAndSend("ruihao.direct", "red", "hello", cd);

        Thread.sleep(2000);
    }
}
