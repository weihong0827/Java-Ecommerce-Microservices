package tech.qiuweihong.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.qiuweihong.model.CouponRecordMessage;
import tech.qiuweihong.service.CouponRecordService;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.coupon_release_queue}")
public class CouponRecordMQListener {
    @Autowired
    private CouponRecordService couponRecordService;

    @Autowired
    private RedissonClient redissonClient;
    @RabbitHandler
    public void releaseCouponRecord(CouponRecordMessage recordMessage, Message message, Channel channel){
        log.info("Received Message: release coupon record {}",recordMessage);
        long msgTag = message.getMessageProperties().getDeliveryTag();
        boolean flag = couponRecordService.releaseCouponRecord(recordMessage);

        // prevent the same record from entering at the same time
        Lock lock = redissonClient.getLock("lock:coupon_record_release:"+recordMessage.getTaskId());
        lock.lock();

        try {
            if (flag) {
                channel.basicAck(msgTag, false);
            }else{
                log.error("release coupon failed {}",recordMessage);
                channel.basicReject(msgTag,true);
            }
        } catch (IOException e) {
            log.error("release coupon exception e:{},msg:{}",e,recordMessage);
        }finally {
            lock.unlock();
        }


    }
}
