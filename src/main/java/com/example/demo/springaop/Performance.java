package com.example.demo.springaop;

import org.springframework.stereotype.Component;
/**
 * 
 * @author wangxg3
 * 关于此类中方法的切面可以参见以下链接
 * {@link com.example.demo.springaop.Audience}
 * {@link com.example.demo.springaop.Audience1}
 * {@link com.example.demo.springaop.Audience2}
 * {@link com.example.demo.springaop.CriticAspect}
 *
 */

@Component
public class Performance {

	public void perform() throws Exception {
		System.out.println(" Performers are performing....");
		System.out.println("performance is over!");
		//throw new Exception("performance failed!");
	}

}
