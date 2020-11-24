package com.example.demo.springinit.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ：Sterry
 * @description： BeanPostProcessor接口的自定义实现类,
 * 专门用来截获并处理{@link TestInitBean}的实例，它有点像一个wrapper，最后返回的
 * 可能是TestInitBean实例的代理，也可能是这个bean本身
 * @date ：2020/11/9 17:24
 */
@Component
public class TestBeanProcessor implements BeanPostProcessor {
    private static Logger logger = LoggerFactory.getLogger(TestBeanProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        TestInitBean temp = null;
        if (bean instanceof TestInitBean) {
            temp = (TestInitBean) bean;
            if (temp.getInjectedProperty() != null && temp.getInjectedProperty().getName() != null) {
                logger.info("处于BeanPostProcessor的postProcessBeforeInitialization（）方法中，" +
                        "TestInitBean对象的属性testBeanForInject已经被成功注入,当前name值为{},修改其name值为-王二麻子", temp.getInjectedProperty().getName());
                temp.getInjectedProperty().setName("王二麻子");
            }

        }
        return temp;

    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        TestInitBean temp = null;
        if (bean instanceof TestInitBean) {
            temp = (TestInitBean) bean;
            logger.info("处于BeanPostProcessor的postProcessAfterInitialization（）方法中，" +
                    "TestInitBean对象的属性testBeanForInject已经被成功注入,修改其name值为-赵四");

        }
        return temp;
    }


}
