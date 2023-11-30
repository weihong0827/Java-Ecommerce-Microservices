package tech.qiuweihong.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.stock_release_queue")
public class ProductStockMQListener {
}
