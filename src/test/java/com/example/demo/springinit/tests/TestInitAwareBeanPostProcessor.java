package com.example.demo.springinit.tests;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * @author ：Sterry
 * @description：TODO
 * @date ：2020/11/24 17:46
 */
public class TestInitAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return bean instanceof  TestInitBean;
    }
}
