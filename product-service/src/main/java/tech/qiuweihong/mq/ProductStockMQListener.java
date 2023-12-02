package tech.qiuweihong.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.AMQImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.qiuweihong.model.ProductMessage;
import tech.qiuweihong.service.ProductService;

import java.io.IOException;

@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.stock_release_queue")
public class ProductStockMQListener{
    @Autowired
    ProductService productService;
    @RabbitHandler
    public void releaseProductStock(ProductMessage productMessage, Message message,Channel channel) throws IOException {
        long msgTag = message.getMessageProperties().getDeliveryTag();
        boolean flag = productService.releaseStock(productMessage);
        if (flag){
            channel.basicAck(msgTag,false);
        }else{
            channel.basicReject(msgTag,true);
        }

    }


}
