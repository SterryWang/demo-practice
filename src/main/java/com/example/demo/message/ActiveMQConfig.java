package com.example.demo.message;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;

import javax.jms.*;

/**
 * @author wangxg3
 * ActiveMQ消息代理配置类
 */
@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
public class ActiveMQConfig {

    /**
     * 自定义队列名属性，表队列名
     */
    @Value("${spring.activemq.queue-name}")
    private String queueName;
    /**
     * 自定义属性，表主题名
     */
    @Value("${spring.activemq.topic-name}")
    private String topicName;

    /**
     * ActiveMQ连接工厂
     *
     * @param properties
     * @return
     */
    @Bean("MyActiveMQCF")
    public ActiveMQConnectionFactory jmsConnectionFactory(ActiveMQProperties properties) {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();


        //packages的设置是sonar提醒的，是考虑安全性的问题
        ActiveMQProperties.Packages packages = properties.getPackages();
        if (packages.getTrustAll() != null) {
            connectionFactory.setTrustAllPackages(packages.getTrustAll());
        }
        /*if (!packages.getTrusted().isEmpty()) {
            connectionFactory.setTrustedPackages(packages.getTrusted());
        }*/
        connectionFactory.setTrustAllPackages(true);
        //设置消息代理服务器的地址，其实还可以用户密码，超时时间等，不赘述
        if (properties.getBrokerUrl() != null) {
            connectionFactory.setBrokerURL(properties.getBrokerUrl());
        }
        if (properties.getUser() != null && properties.getPassword() != null) {
            connectionFactory.setUserName(properties.getUser());
            connectionFactory.setPassword(properties.getPassword());


        }
        ActiveMQPrefetchPolicy  prefetchPolicy  =  new ActiveMQPrefetchPolicy();

        //设置PrefetchPolicy
        prefetchPolicy.setQueuePrefetch(0);
        prefetchPolicy.setDurableTopicPrefetch(1);
        prefetchPolicy.setTopicPrefetch(1);
        connectionFactory.setPrefetchPolicy(prefetchPolicy);








        return connectionFactory;
    }

    /**
     * 队列
     *
     * @return
     */
    @Bean(name = {"activeMQQueue"})
    public Queue activeMQQueue() {
        return new ActiveMQQueue(queueName);
    }

    /**
     * 主题
     *
     * @return
     */
    @Bean(name = {"activeMQTopic", "defJmsDst"})
    public Topic activeMQTopic() {
        return new ActiveMQTopic(topicName);
    }

    /**
     * jms模板
     *
     * @param connectionFactory
     * @param dst
     * @return
     */
    @Bean(name = "MyJmsTemplate")
    public JmsTemplate jmsTemplate(@Qualifier("MyActiveMQCF") ActiveMQConnectionFactory connectionFactory, @Qualifier("defJmsDst") Destination dst) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        //设置默认的目的地，即使设置了默认目的地，使用JmsTemplate发送消息时依然可以指定目的地
        jmsTemplate.setDefaultDestination(dst);
        jmsTemplate.setSessionTransacted(true);

        //是否使用订阅模式，当目的地为字符串形式时，jmstemplate将默认发送或者接收到主题类型的目的地
        // jmsTemplate.setPubSubDomain(true);


        return jmsTemplate;

    }

    @Bean
    public MyJmsMessageListener myJmsMessageListener() {
        return new MyJmsMessageListener();
    }

    @Bean
    public MessageListenerContainer msgListenerContainer(@Qualifier("MyActiveMQCF") ConnectionFactory cf, @Qualifier("defJmsDst") Destination destination, @Qualifier("myJmsMessageListener") Object messageListener) {

        DefaultMessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer();
        //注入消息监听器
        messageListenerContainer.setMessageListener(messageListener);
        //注入连接工厂
        messageListenerContainer.setConnectionFactory(cf);
        //设置消息目的地
        messageListenerContainer.setDestination(destination);

        //设置持久化订阅，非订阅模式其实无需设置
        messageListenerContainer.setClientId("wangxglalala");
        messageListenerContainer.setSubscriptionDurable(true);
       // messageListenerContainer.setSubscriptionName("hey,jude");


        //开启会话事务
        //messageListenerContainer.setSessionTransacted(true);






        return messageListenerContainer;
    }


}
