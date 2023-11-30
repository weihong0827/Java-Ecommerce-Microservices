package tech.qiuweihong.biz;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.qiuweihong.CouponApplication;
import tech.qiuweihong.config.RabbitMQConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CouponApplication.class)
@Slf4j
public class MQTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendDelayMessage(){
        rabbitTemplate.convertAndSend("coupon.event.exchange","coupon.release.delay.routing.key","delay message");
    }
}
