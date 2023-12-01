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
    @Value("${mqconfig.stock_event_exchange}")
    private String eventExchange;


    /**
     * delay queue
     */
    @Value("${mqconfig.stock_release_delay_queue}")
    private String stockReleaseDelayQueue;

    /**
     * delay queue key
     */
    @Value("${mqconfig.stock_release_delay_routing_key}")
    private String stockReleaseDelayRoutingKey;


    /**
     * release queue
     */
    @Value("${mqconfig.stock_release_queue}")
    private String stockReleaseQueue;

    /**
     * release queue key
     */
    @Value("${mqconfig.stock_release_routing_key}")
    private String stockReleaseRoutingKey;

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
    public Exchange stockEventExchange(){
        return new TopicExchange(eventExchange,true,false);
    }
    @Bean
    public Queue stockReleaseDelayQueue(){
        Map<String,Object> args = new HashMap<>(3);
        args.put("x-message-ttl",ttl);
        args.put("x-dead-letter-routing-key",stockReleaseRoutingKey);
        args.put("x-dead-letter-exchange",eventExchange);
        return new Queue(stockReleaseDelayQueue,true,false,false,args);
    }

    @Bean
    public Queue stockReleaseQueue(){

        return new Queue(stockReleaseQueue,true,false,false);
    }

    @Bean
    public Binding stockReleaseBinding(){
        return new Binding(stockReleaseQueue,Binding.DestinationType.QUEUE,eventExchange,stockReleaseRoutingKey,null);
    }
    @Bean
    public Binding stockReleaseDelayBinding(){
        return new Binding(stockReleaseDelayQueue,Binding.DestinationType.QUEUE,eventExchange,stockReleaseDelayRoutingKey,null);
    }
}
