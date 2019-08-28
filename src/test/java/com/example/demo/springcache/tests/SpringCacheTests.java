package com.example.demo.springcache.tests;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.entity.Employee;
import com.example.demo.springcache.DefaultCachedDao;
import com.example.demo.springcache.EmployeeCachedDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class SpringCacheTests {

	@Resource
	private EmployeeCachedDao employeeCachedDao;
	@Resource
	private DefaultCachedDao defaultCachedDao;

	@Test
	public void testLocalCache() {
		for (int i = 0; i < 3; i++) {

			System.out.println("第" + (i + 1) + "次查询");

			Employee employee = employeeCachedDao.findOne(1);

			if (employee != null) {
				System.out.println(employee.toString());
			}

		}

		// 测试CacheManager通过NAMESET进行缓存分区，所以这两个DAO是分别存储到不同分区的

		for (int i = 0; i < 3; i++) {

			System.out.println("第" + (i + 1) + "次查询");

			System.out.println(defaultCachedDao.findOne("白日依山尽"));

		}

	}

	@Test
	public void testCachePut() {
		int id = 100;
		Employee employee = new Employee();

		employee.setId(id);
		employee.setName("xiaobai" + id);
		employee.setGender("m");
		
		employeeCachedDao.saveOne(employee);
		
		System.out.println(employeeCachedDao.findOne(id));
	}

}
