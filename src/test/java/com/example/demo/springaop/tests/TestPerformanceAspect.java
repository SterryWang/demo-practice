package com.example.demo.springaop.tests;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.springaop.Performance;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {DemoApplication.class})
public class TestPerformanceAspect {
	
	
	@Resource
	Performance performance;
	
	/**
	 * 事实证明，目标对象Performance 和切面Audience都必须注入SPRING 上下文容器受到SPRING 容器管理才可以执行SPRING AOP的
	 * 切面功能，你可以直接在Audience切面类里用@Component注入容器，也可以在XML配置文件或者@Configuration类里注入切面对象
	 * @throws Exception 
	 */
	@Test
	public void testAudience() throws Exception {
		System.out.println("测试new");
		 // Performance newPerformance = new Performance(); newPerformance.perform();
		 
		System.out.println("测试受SPRING 管理的情况");
		performance.perform();
	}

}
