package com.example.demo.message.amqp;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq 组件配置
 * 还是不使用自动配置，使用手动配置
 * 完全可以模仿{@link org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration}
 * 的写法
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private int port;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String vHost;


    @Bean
    public ConnectionFactory myRabbitCF() {
        CachingConnectionFactory cf = new CachingConnectionFactory(host, port);
        cf.setUsername(username);
        cf.setPassword(password);

        cf.setVirtualHost(vHost);

        return cf;

    }

    @Bean
    public RabbitTemplate myRabbitTemplate(ConnectionFactory myRabbitCF) {

        RabbitTemplate template;
        template = new RabbitTemplate(myRabbitCF);

        return template;
    }

    /**
     * 监听器容器工厂
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory myRabbitListenerContainer() {
        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setConnectionFactory(myRabbitCF());
        listenerContainerFactory.setPrefetchCount(1);



        return listenerContainerFactory;
    }


    /**
     * 必备组件，可以用来在broker一侧创建exchange，queue,binding
     * @return
     */
    @Bean
    public RabbitAdmin myRabbitAdmin() {
        return new RabbitAdmin(myRabbitCF());


    }

}
