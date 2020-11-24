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

    //@Resource
    private TestBeanForInject injectedProperty;

    /**
     * 构造器也参与bean的声明周期，我们也注明下
     */
    public TestInitBean() {
        log.info("构造方法执行，此时injectProperty应为null;实际injectProperty={}", injectedProperty);

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
        //容器上下文注册钩子，JVM关闭前会调用这个钩子,钩子方法会调用@PreDestroy注解的方法
        ctx.registerShutdownHook();
        //5s后关闭JVM
        log.info("5s后关闭JVM");
        Thread.sleep(5000);
        log.info("将关闭JVM,会调用ctx的钩子");


    }

    public TestBeanForInject getInjectedProperty() {
        return injectedProperty;
    }

    @Resource
    public void setInjectedProperty(TestBeanForInject injectedProperty) {
        log.info("正在通过SPRING的@Resource注解，执行属性injectedProperty的依赖输入，注入前属性为injectedProperty={}", this.injectedProperty);
        this.injectedProperty = injectedProperty;
        log.info("依赖注入后injectedProperty={}",this.injectedProperty);

    }

    /**
     * @description: InitializingBean的初始化方法
     * @date: 2020/11/9  15:10
     * @params:
     * @return:
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("进入InitializedBean接口的afterPropertySet()方法");
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

        log.info("进入注解@PostConstrut标注的方法postConstruct()....");
        log.info("属性injectProperty已注入：" + (injectedProperty != null));
        log.info("属性injectProperty已注入：" + injectedProperty);
        log.info("退出注解@PostConstrut标注的方法postConstruct()");

    }

    @PreDestroy
    public void preDestroy() {

        log.info("进入注解@PreDestroy标注的方法preDestroy().....");
        log.info("属性injectProperty已注入：" + injectedProperty);
        log.info("退出注解@PreDestroy标注的方法preDestroy()");

    }


    /**
     * @description: xml中定义的destroy前执行的方法
     * @date: 2020/11/9  16:21
     * @params:
     * @return:
     */
    public void xmlDefinedPreDestroy() {
        log.info("进入xml指定的destroy-method方法，xmlDefinedPreDestroy().....");
        log.info("属性injectProperty已注入：" + injectedProperty);
        log.info("退出xml指定的destroy-method方法，xmlDefinedPreDestroy().....");

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
