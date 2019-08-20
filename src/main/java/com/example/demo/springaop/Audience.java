package com.example.demo.springaop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
/**
 * 使用最基础的切面方式
 * @author wangxg3
 *
 */
@Aspect
@Component
public class Audience {
	
	

	@Before("execution(* com.example.demo.springaop.Performance.perform(..))")
	public void silenceCellPhone() {
		System.out.println("silencing cell phone");
	}
	@Before("execution(* com.example.demo.springaop.Performance.perform(..))")
	public void takeSeats() {
		
		System.out.println("taking seats!");
	}
	@AfterReturning("execution(* com.example.demo.springaop.Performance.perform(..))")
	public void applause() {
		System.out.println("clap clap clap ");
		
	}
	@AfterThrowing("execution(* com.example.demo.springaop.Performance.perform(..))")
	public  void demandRefund() {
		System.out.println("demanding a refund!");
	}
}
