package com.example.demo.message.tests;


import com.example.demo.DemoApplication;
import com.example.demo.entity.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.jms.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActiveMQTests {


    @Resource(name = "simpleExecutor")
    ExecutorService executorService;
    @Resource(name = "MyJmsTemplate")
    private JmsOperations jmsTemplate;

    @Resource
    private Queue jmsQueue;

    @Resource
    private Topic jmsTopic;


    private static Logger log = LoggerFactory.getLogger(ActiveMQTests.class);

    /**
     * 测试发送到队列
     * 使用MessageCreator
     */
    @Test
    public void testSendToQueue() {
//将发送到jmsTemplate定义的默认队列
        //演示第一种发送方式，使用messageCreator先创建message再发送
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("你好啊！我是生产者");
            }
        });


    }

    /**
     * 测试从队列获取
     * 使用jmsTemplate先接收再转换
     *
     * @throws JMSException
     */
    @Test
    public void testRecvFromQueue() throws JMSException {
        //第一种接收信息方式，返回类型为Message ,使用类型强制转换为特定类型的Message实现类，本质上
        //读取的还是消息
       /* TextMessage msg = (TextMessage)jmsTemplate.receive("jms.queue");

        System.out.println(msg.getText());*/
        //第二种接收信息转换方式，使用jmsTemplate内部定义的消息转换器，把接收到的message转换为具体的对象Object
        // String msg = (String) jmsTemplate.receiveAndConvert("jms.queue");
        String msg = (String) jmsTemplate.receiveAndConvert(jmsQueue);

        System.out.println(msg);
    }

    /**
     * 测试发送到主题Topic
     * 使用jmsTemplatex先转换对象为msg,再发送
     */
    @Test
    public void testSendToTopic() {
        Employee e = new Employee();
        e.setId(1);
        e.setName("小白");
        e.setAge(18);
        //这种写法会默认目的地类型是队列而不是主题
        log.info("开始发送topic信息了。。。");
        jmsTemplate.send(jmsTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // return session.createObjectMessage(e);
                return session.createTextMessage("你好，这是发往TOPIC的文本信息！");
            }
        });

        //发送主题型目的地的正确写法
        //第二种发送方式，常用，使用jmsTemplate内部消息转换器将对象转换成message再发送
        //jmsTemplate.convertAndSend(jmsTopic, e);


    }

    /**
     * 测试从主题接收信息
     */
    @Test
    public void recvFromTopic() {
        Employee e = (Employee) jmsTemplate.receiveAndConvert(jmsTopic);
        System.out.println(e);

    }


    @Test
    public void recvFromTopic2() throws IOException, JMSException {
        log.info("开始监听了。。。");
        Message msg = jmsTemplate.receive(jmsTopic);

        assert msg != null;
        System.out.println("接收到的MESSAGE类型为：" + msg.getClass());

        log.info("接收到的MESSAGE类型为：{}", msg.getClass());
        if (msg instanceof TextMessage) {

            log.info("接收到的信息为：{}", ((TextMessage) msg).getText());
        } else if (msg instanceof ObjectMessage) {
            log.info("接收到的对象为：{}", ((ObjectMessage) msg).getObject());
        } else {
            log.info("接收的信息为：{}", msg);
        }
        /*ufferedWriter  bufferedWriter = new BufferedWriter(new FileWriter("d:/topicmsg.log"));

        bufferedWriter.write("接收到的MESSAGE类型为：" + msg.getClass());
        bufferedWriter.close();*/
    }


}
