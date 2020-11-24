package com.example.demo.springinit.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ：Sterry
 * @description：TODO
 * @date ：2020/11/24 17:07
 */
@Component
public class TestDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    private final Logger log = LoggerFactory.getLogger(TestDestructionAwareBeanPostProcessor.class);

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {

        log.info("进入TestDestructionAwareBeanPostProcessor的postProcessBeforeDestruction方法，待处理bean={}",
                bean);

    }

    /**
     * 对于bean类型为TestInitBean的bean 才执行上面的
     * {@link #postProcessBeforeDestruction }方法
     * @param bean
     * @return
     */
    @Override
    public boolean requiresDestruction(Object bean) {
        return bean instanceof TestInitBean;
    }
}
