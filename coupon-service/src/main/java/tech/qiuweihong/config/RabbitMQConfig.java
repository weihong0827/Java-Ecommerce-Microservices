package tech.qiuweihong.config;

import com.rabbitmq.client.AMQP;
import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class RabbitMQConfig {
    /**
     * Exchange
     */
    @Value("${mqconfig.coupon_event_exchange}")
    private String eventExchange;


    /**
     * delay queue
     */
    @Value("${mqconfig.coupon_release_delay_queue}")
    private String couponReleaseDelayQueue;

    /**
     * delay queue key
     */
    @Value("${mqconfig.coupon_release_delay_routing_key}")
    private String couponReleaseDelayRoutingKey;


    /**
     * release queue
     */
    @Value("${mqconfig.coupon_release_queue}")
    private String couponReleaseQueue;

    /**
     * release queue key
     */
    @Value("${mqconfig.coupon_release_routing_key}")
    private String couponReleaseRoutingKey;

    /**
     * ttl
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public Exchange couponEventExchange(){
        return new TopicExchange(eventExchange,true,false);
    }
    @Bean
    public Queue couponReleaseDelayQueue(){
        Map<String,Object> args = new HashMap<>(3);
        args.put("x-message-ttl",ttl);
        args.put("x-message-routing-key",couponReleaseDelayRoutingKey);
        args.put("x-message-exchange",eventExchange);
        return new Queue(couponReleaseDelayQueue,true,false,false,args);
    }

    @Bean
    public Queue couponReleaseQueue(){

        return new Queue(couponReleaseQueue,true,false,false);
    }

    @Bean
    public Binding couponReleaseBinding(){
        return new Binding(couponReleaseQueue,Binding.DestinationType.QUEUE,eventExchange,couponReleaseRoutingKey,null);
    }
    @Bean
    public Binding couponReleaseDelayBinding(){
        return new Binding(couponReleaseDelayQueue,Binding.DestinationType.QUEUE,eventExchange,couponReleaseDelayRoutingKey,null);
    }
}
