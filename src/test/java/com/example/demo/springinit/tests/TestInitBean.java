package com.example.demo.springinit.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author ：Sterry
 * @description：TODO
 * @date ：2020/11/9 11:07
 */

public class TestInitBean implements InitializingBean, DisposableBean, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static Logger log = LoggerFactory.getLogger(TestInitBean.class);

    @Resource
    private TestBeanForInject injectedProperty;

    public TestInitBean() {
        log.info("构造方法执行，此时injectProperty应为null;injectProperty={}", injectedProperty);

    }

    public static void main(String[] args) throws InterruptedException {
        //
        //SPRING 容器初始化
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("TestInitApplicationContext.xml");
        //顺便测试applicationContextAware接口
        TestInitBean testInitBean = (TestInitBean) applicationContext.getBean("testInitBean");
        log.info(testInitBean.injectedProperty.getName());

        TestBeanForInject injectBean = (TestBeanForInject) applicationContext.getBean("testBeanForInject");
        log.info(injectBean.getName());
        //JVM关闭前会调用这个钩子,钩子方法会调用@PreDestroy注解的方法
        ctx.registerShutdownHook();
        //5s后关闭JVM
        log.info("5s后关闭JVM");
        Thread.sleep(5000);
        log.info("将关闭JVM,会调用ctx的钩子");


    }

    public void setInjectedProperty(TestBeanForInject injectedProperty) {
        this.injectedProperty = injectedProperty;

    }

    /**
     * @description: InitializingBean的初始化方法
     * @date: 2020/11/9  15:10
     * @params:
     * @return:
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("进入afterPropertySet方法");
        log.info("属性injectProperty={}", injectedProperty);
        log.info("更改injectProperty的属性值为\"lisi\"");
        injectedProperty.setName("lisi");
        log.info("退出afterProperty方法");


    }

    /**
     * @description: TestInitApplicationContext.xml中指定的初始化方法
     * @date: 2020/11/9  15:08
     * @params:
     * @return:
     */
    private void initMethod() {
        log.info("进入XMl指定的init方法");
        log.info("查看属性injectProperty的注入情况，injectProperty={}", injectedProperty);
        log.info("退出XMl指定的init方法");

    }

    @PostConstruct
    public void postConstruct() {

        log.info("@PostConstrut....");
        log.info("属性injectProperty已注入：" + (injectedProperty != null));
        log.info("属性injectProperty已注入：" + injectedProperty);

    }

    @PreDestroy
    public void preDestroy() {

        log.info("@PreDestroy.....");
        log.info("属性injectProperty已注入：" + injectedProperty);

    }


    /**
     * @description: xml中定义的destroy前执行的方法
     * @date: 2020/11/9  16:21
     * @params:
     * @return:
     */
    public void xmlDefinedPreDestroy() {
        log.info("进入xmlDefinedPreDestroy.....");
        log.info("属性injectProperty已注入：" + injectedProperty);

    }

    @Override
    public void destroy() throws Exception {
        log.info("进入DisposableBean接口的destroy()方法.....");
        log.info("属性injectProperty已注入：" + injectedProperty);


    }


    /**
     * @description: ApplicationContextAware接口方法，用于注入容器上下文
     * @date: 2020/11/9  14:18
     * @params:
     * @return:
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}
