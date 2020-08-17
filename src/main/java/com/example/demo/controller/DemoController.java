package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.message.IEmployMsgService;
import com.example.demo.springcache.EmployeeCachedDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxg3
 */
@RestController
@RequestMapping("/test")
public class DemoController {

    private static Logger log = LoggerFactory.getLogger(DemoController.class);

    @Resource(name = "MyJmsTemplate")
    private JmsOperations jmsTemplate;

    @Resource
    private Queue jmsQueue;

    @Resource
    private Topic jmsTopic;

    @Resource
    private EmployeeCachedDao employeeCachedDao;


    @Resource
    private IEmployMsgService employMsgService;

    @GetMapping("/hello")
    public String testController() {
        return "HEllO  ,Wang!";
    }

    @GetMapping("/userdir")
    public String getUserDir() {
        return System.getProperty("user.dir");
    }

    @GetMapping("/savelist")
    public String testSaveList() {
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            Employee e = new Employee();
            e.setId(i);
            e.setName("葫芦娃" + i);
            list.add(e);


        }

        employeeCachedDao.saveList(list, 100);
        return "查回来的信息为：" + employeeCachedDao.findList(100);
    }


    @GetMapping("/jms/sendObj")
    public String sendObjToTopic(@RequestParam(value = "dstType") String dstType) {
        Employee e = new Employee();
        e.setId(1);
        e.setName("小白");
        e.setAge(18);
        //这种写法会默认目的地类型是队列而不是主题
        log.info("开始发送topic信息了。。。");
        Destination dst;
        if ("topic".equals(dstType)) {
            dst = jmsTopic;
        } else {
            dst = jmsQueue;
        }
        jmsTemplate.send(dst, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(e);
                //return session.createTextMessage("你好，这是发往TOPIC的文本信息！");
            }
        });


        return "发送完毕";
    }

    @GetMapping("/jms/sendStr")
    public String sendStrToTopic() {
        Employee e = new Employee();
        e.setId(1);
        e.setName("小白");
        e.setAge(18);
        //这种写法会默认目的地类型是队列而不是主题

        log.info("开始发送topic信息了。。。");
        jmsTemplate.send("jms.topic", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // return session.createObjectMessage(e);
                return session.createTextMessage("你好，这是发往TOPIC的文本信息！");
            }
        });
        return "发送完毕";

    }


    @GetMapping("/jms/recvFromTopic")
    public String recvFromTopic() {

        log.info("开始监听了。。。");
        Message msg = jmsTemplate.receive(jmsTopic);

        assert msg != null;
        System.out.println("接收到的MESSAGE类型为：" + msg.getClass());

        log.info("接收到的MESSAGE类型为：{}", msg.getClass());
        if (msg instanceof TextMessage) {

            try {
                log.info("接收到的信息为：{}", ((TextMessage) msg).getText());
                return ((TextMessage) msg).getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else if (msg instanceof ObjectMessage) {
            try {
                log.info("接收到的对象为：{}", ((ObjectMessage) msg).getObject());
                return ((ObjectMessage) msg).getObject().toString();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            log.info("接收的信息为：{}", msg);
            return msg.toString();
        }
        return null;

    }

    @GetMapping("/jms/recvFromQueue")
    public String recvFromQueue() {
        log.info("开始监听了。。。");
        Message msg = jmsTemplate.receive(jmsQueue);

        assert msg != null;
        System.out.println("接收到的MESSAGE类型为：" + msg.getClass());

        log.info("接收到的MESSAGE类型为：{}", msg.getClass());
        if (msg instanceof TextMessage) {

            try {
                log.info("接收到的信息为：{}", ((TextMessage) msg).getText());
                return ((TextMessage) msg).getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else if (msg instanceof ObjectMessage) {
            try {
                log.info("接收到的对象为：{}", ((ObjectMessage) msg).getObject());
                return ((ObjectMessage) msg).getObject().toString();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            log.info("接收的信息为：{}", msg);
            return msg.toString();
        }
        return null;
    }

    @GetMapping("/jms/testTransaction")
    public String testTransaction() {
        Employee e = new Employee();
        e.setId(1);
        e.setName("小白");
        e.setAge(18);
        //这种写法会默认目的地类型是队列而不是主题
        log.info("开始发送信息到queue了。。。");
        try {
            employMsgService.sendEmployeeInfo(e, jmsQueue);
        } catch (Exception ex) {

            log.error("消息发送失败，请检查事务是否已经回滚！", ex);
            return "消息发送失败，请检查事务是否已经回滚！";
        }
        return null;

    }
    @GetMapping("/jms/testTransactionRecv")
    public String testTransactionRecv() {

        //这种写法会默认目的地类型是队列而不是主题
        log.info("开始从queue中接收信息了。。。");
        try {
           employMsgService.recvMsg(jmsQueue);
        } catch (Exception ex) {

            log.error("消息接收失败，请检查事务是否已经回滚！", ex);
            return "消息接收失败，请检查事务是否已经回滚！";
        }
        return null;

    }

}
