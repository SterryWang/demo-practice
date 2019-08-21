package com.example.demo.springaop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 使用@Around取代其他通知类型来定义切面，综合来看aroud和其他通知类型
 * 相比还是有很大优势的，比如在打印日志，计算用时等方面
 * 但是需要特别留意的是，around通知捕获的异常如果不再往外跑，那么调用方将无法捕获被通知对象的方法里抛出的异常
 * 
 * @author wangxg3
 *
 */

@Component

@Aspect

public class Audience2 {

	@Around("execution(* com.example.demo.springaop.Performance.perform(..))")
	public void watchPerformance(ProceedingJoinPoint jp) throws Throwable {

		try {
			System.out.println("Audience2:silencing cell phone");
			System.out.println("Audience2:taking seats!");
			jp.proceed();

			System.out.println("Audience2:clap clap clap ");
		} catch (Throwable e) {
			// 演出过程出现异常则退票
			System.out.println("Audience2:demanding a refund!");
			//throw e;//异常可以选择继续抛出或者不抛出，特别要注意的是，如果这里不抛出，那么目标类的异常将无法被调用者捕捉到

		}
	}

}
