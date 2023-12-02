package tech.qiuweihong.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import tech.qiuweihong.model.OrderMessage;
import tech.qiuweihong.service.ProductOrderService;

import java.io.IOException;

@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.order_close_queue}")
public class ProductOrderMQListener {
    @Autowired
    private ProductOrderService productOrderService;

    @RabbitHandler
    public void closeProductOrder(OrderMessage orderMessage, Message message, Channel channel) throws IOException {
        long msgTag = message.getMessageProperties().getDeliveryTag();

        boolean flag = productOrderService.closeProductOrder(orderMessage);


        if (flag){
            channel.basicAck(msgTag,false);
        }else{
            channel.basicReject(msgTag,true);
        }

    }
}
