package com.example.demo.message.amqp;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author wangxg3
 */
@Configuration
public class RabbitBindings {
    //交换机，队列，路由键，名称常量
    public static final String DIRECT_EXCHANGE = "my_direct_exchange";
    public static final String TOPIC_EXCHANGE = "my_topic_exchange";
    public static final String FANOUT_EXCHANGE = "my_fanout_exchange";
    public static final String HEADERS_EXCHANGE = "my_headers_exchange";
    public static final String SYSTEM_EXCHANGE = "my_system_exchange";

    public static final String QUEUE_A = "my_queue_A";

    public static final String QUEUE_B = "my_queue_B";


    public static final String ROUTINGKEY_A = "my_routingkey.A";

    public static final String ROUTINGKEY_B = "my_routingkey.B";

    /**
     * 实例化 directExchange
     *
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }


    /**
     * 实例化topic exchange
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * 实例化headersExchange
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);

    }

    /**
     * 实例化
     *
     * @return
     */
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);

    }

    @Bean
    public Queue queueA() {
        return new Queue(QUEUE_A);
    }

    @Bean
    public Queue queueB() {
        return new Queue(QUEUE_B);
    }

    /**
     * 绑定queueA到 direct exchange
     * 路由键为routing_a
     *
     * @return
     */
    @Bean
    public Binding bindingAToDirectExchange() {
        //这行代码可以很清晰的表示出exchagne,queue,routingkey三者之间的关系
        return BindingBuilder.bind(queueA()).to(directExchange()).with(ROUTINGKEY_A);

    }

    @Bean
    public Binding bindingBToDirectExchange() {
        //这行代码可以很清晰的表示出exchagne,queue,routingkey三者之间的关系
        return BindingBuilder.bind(queueB()).to(directExchange()).with(ROUTINGKEY_B);

    }

    /**
     * 把队列B通过路由键A绑定到 direct change，
     * 这样一来，队列A 和 队列B 通过同样的绑定键routing key A
     * 绑到了 direct change上，验证发送消息时二者是否会同时收到
     *
     * @return
     */
    @Bean
    public Binding bingdingBToDirectExchangePlus(){
        return BindingBuilder.bind(queueB()).to(directExchange()).with(ROUTINGKEY_A);
    }

    /**
     * 绑定队列A到topic exchange,
     * bingding key 为通配方式
     *
     * @return
     */
    @Bean
    public Binding bindingToTopicExchange() {
        return BindingBuilder.bind(queueA()).to(topicExchange()).with("my_routingkey.#");

    }

    @Bean
    public Binding bingdingBToTopicExchange() {
        return BindingBuilder.bind(queueB()).to(topicExchange()).with("my_routingkey.*");
    }


    /**
     * 绑定队列A到FANOUT exchange
     * FanOutExchange无需指定binding key
     *
     * @return
     */
    @Bean
    public Binding bingdingAToFanOutExchange() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }

    /**
     * 绑定队列B到fanout exchange
     * 无需指定绑定键
     *
     * @return
     */
    @Bean
    public Binding bindingBToFanoutExchange() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }


}
