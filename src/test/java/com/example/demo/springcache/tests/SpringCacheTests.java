package com.example.demo.springcache.tests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.entity.Employee;
import com.example.demo.springcache.EmployeeCachedDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class SpringCacheTests {

	@Resource
	private EmployeeCachedDao  employeeCachedDao;
	
	@Test
   public void  testLocalCache() {
	for (int i = 0; i < 3; i++) {
		
		System.out.println("第"+(i+1)+"次查询");
		
		Employee employee=employeeCachedDao.findOne(1);
		
	if(employee!=null) {
		System.out.println(employee.toString());
	}
		
	}
		
	   
   }

}
