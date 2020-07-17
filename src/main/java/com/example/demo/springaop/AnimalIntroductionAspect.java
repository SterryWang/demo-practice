package com.example.demo.springaop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * 测试引入的AOP，使用切面为PERSON类引入ANIMAL接口，
 * 使得其子类也同时具有ANIMAL接口的功能，看上去好像也同时
 * 实现了ANIMAL接口一样
 * 重点学习@DeclaredParents注解的使用
 * {@link com.example.demo.springaop.tests.TestAnimalIntroAspect}
 *
 * @author wangxg3
 *
 */
@Aspect
@Component
public class AnimalIntroductionAspect {
	
	//为所有Person接口的实现类引入Animal接口功能，接口默认实现FemaleAnimal.class
	@DeclareParents(value="com.example.demo.springaop.Person",defaultImpl=FemaleAnimal.class)
	public Animal animal;//要引入的接口，通过@DeclaredParents赋予其默认的实现类
    

}
