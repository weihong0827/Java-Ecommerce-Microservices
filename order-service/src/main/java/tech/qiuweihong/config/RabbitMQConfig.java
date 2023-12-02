package tech.qiuweihong.config;


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
    @Value("${mqconfig.order_event_exchange}")
    private String eventExchange;


    /**
     * delay queue
     */
    @Value("${mqconfig.order_close_delay_queue}")
    private String orderCloseDelayQueue;

    /**
     * delay queue key
     */
    @Value("${mqconfig.order_close_delay_routing_key}")
    private String orderCloseDelayRoutingKey;


    /**
     * close queue
     */
    @Value("${mqconfig.order_close_queue}")
    private String orderCloseQueue;

    /**
     * release queue key
     */
    @Value("${mqconfig.order_close_routing_key}")
    private String orderCloseRoutingKey;

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
    public Exchange orderEventExchange(){
        return new TopicExchange(eventExchange,true,false);
    }
    @Bean
    public Queue orderCloseDelayQueue(){
        Map<String,Object> args = new HashMap<>(3);
        args.put("x-message-ttl",ttl);
        args.put("x-dead-letter-routing-key",orderCloseRoutingKey);
        args.put("x-dead-letter-exchange",eventExchange);
        return new Queue(orderCloseDelayQueue,true,false,false,args);
    }

    @Bean
    public Queue orderCloseQueue(){

        return new Queue(orderCloseQueue,true,false,false);
    }

    @Bean
    public Binding orderCloseBinding(){
        return new Binding(orderCloseQueue,Binding.DestinationType.QUEUE,eventExchange,orderCloseRoutingKey,null);
    }
    @Bean
    public Binding orderCloseDelayBinding(){
        return new Binding(orderCloseDelayQueue,Binding.DestinationType.QUEUE,eventExchange,orderCloseDelayRoutingKey,null);
    }
}
