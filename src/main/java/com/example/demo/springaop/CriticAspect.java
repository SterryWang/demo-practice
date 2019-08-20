package com.example.demo.springaop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
/**
 * 评论家切面，表演结束后，对表演进行评论
 * 本利主要演示的是给切面注入依赖的情形
 * @author wangxg3
 *
 */
@Aspect
@Component
public class CriticAspect {
	
	@Pointcut("execution(* com.example.demo.springaop.Performance.perform(..))")
	public void Performance() {
		
	}
	
	
	@AfterReturning("")
	public void critit()

}
