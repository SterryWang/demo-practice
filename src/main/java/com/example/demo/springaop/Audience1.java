package com.example.demo.springaop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
/**
 * 使用@pointcut方式定义切点
 * @author wangxg3
 *
 */
@Component
@Aspect
public class Audience1 {
	
	
	
	@Pointcut("execution(* com.example.demo.springaop.Performance.perform(..))")
	public void performance() {}
	

	@Before("performance()")
	public void silenceCellPhone() {
		System.out.println("Audience1:silencing cell phone");
	}
	@Before("performance()")
	public void takeSeats() {
		
		System.out.println("Audience1:taking seats!");
	}
	@AfterReturning("performance()")
	public void applause() {
		System.out.println("Audience1:clap clap clap ");
		
	}
	@AfterThrowing(value="performance()")
	public  void demandRefund() {
		System.out.println("Audience1:demanding a refund!");
	}

}
