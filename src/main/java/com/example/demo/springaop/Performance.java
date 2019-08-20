package com.example.demo.springaop;

import org.springframework.stereotype.Component;
/**
 * 
 * @author wangxg3
 * {@link com.example.demo.springaop.Audience}
 * {@link com.example.demo.springaop.Audience1}
 * {@link com.example.demo.springaop.Audience2}
 *
 */

@Component
public class Performance {

	public void perform() throws Exception {
		System.out.println(" Performers are performing....");
		System.out.println("performance is over!");
		throw new Exception("performance failed!");
	}

}
