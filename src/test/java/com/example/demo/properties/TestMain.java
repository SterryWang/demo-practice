package com.example.demo.properties;

import com.example.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestMain {


    @Resource
    TestProperties testProperties;
    @Resource
    TestConfig testConfig;


    @Test
    public void display() {
        System.out.println(testProperties);
        System.out.println(testConfig.getTestBean());
        System.out.println(testConfig.getProperties());

    }

    /**
     * 这里写了main方法只是为了展示一种灵活的加载上下文并进行简单测试的方法，
     * 这种方法因为没有启用SPRING BOOT 的相关自动配置机制（即没有开启@SpringBootTest 功能），
     * 所以无法把配置文件的值加载进来；但是可以验证SPRING  可以通过构造函数完成自动注入（TestConfig
     * 实例化时时候自动注入了TestBean和TestProperties对象，这说明SPRING 在实例化TestConfig时
     * 其实调用了TestConfig自定义的构造方法了）
     *
     * @param args
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        TestBean tb = (TestBean) context.getBean("testBean");
        tb.sayHello();

        //TestProperties  properties=(TestProperties) context.getBean("testProperties");
        TestProperties properties = (TestProperties) context.getBean(TestProperties.class);

        System.out.println(properties);
        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
        System.out.println(Arrays.toString(context.getBeanNamesForType(TestProperties.class)));
        System.out.println(Arrays.toString(context.getBeanNamesForType(TestConfig.class)));


    }


}
