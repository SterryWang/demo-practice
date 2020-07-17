package com.example.demo.springcache.tests;

import javax.annotation.Resource;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DemoApplication;
import com.example.demo.entity.Employee;
import com.example.demo.springcache.DefaultCachedDao;
import com.example.demo.springcache.EmployeeCachedDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class,webEnvironment =SpringBootTest.WebEnvironment.DEFINED_PORT)
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

	/**
	 * 测试unless条件缓存
	 */
	@Test
	public void testUnless() {
		int id = 1;
		Employee e = new Employee();
		e.setId(id);
		e.setName("zhangsan");
		e.setGender("m");

		employeeCachedDao.saveOne(e);

		//第一次查zhangsan,unless设置，先走缓存查不到了，所以应该走数据库查，但如果上面的saveone方法的注释
		//放开，依然会直接走缓存
		System.out.println("查询信息返回为：" + employeeCachedDao.findOne(id));


		//第二次查zhagnsan,unless设置，还是会走数据库
		System.out.println("查询信息返回为：" + employeeCachedDao.findOne(id));


		//第一次查lisi，应该从数据库取，并且写入缓存
		System.out.println("查询信息返回为：" + employeeCachedDao.findOne(2));

		//第二次查lisi,应该直接从缓存区取
		System.out.println("查询信息返回为：" + employeeCachedDao.findOne(2));

	}

	/**
	 * 测试conditon条件缓存
	 */
	@Test
	public void testCondition() {

		int id = 1;
		Employee e = new Employee();
		e.setId(id);
		e.setName("zhangsan");
		e.setGender("m");


		//第一次查zhangsan,condition设置，所以应该直接走数据库查，

		System.out.println("查询信息返回为：" + employeeCachedDao.findOneCondition(id));


		//第二次查zhangsan,condition设置，所以还是应该直接走数据库查，
		System.out.println("查询信息返回为：" + employeeCachedDao.findOneCondition(id));


		//第一次查lisi，应该从数据库取，并且写入缓存
		System.out.println("查询信息返回为：" + employeeCachedDao.findOneCondition(2));

		//第二次查lisi,应该直接从缓存区取
		System.out.println("查询信息返回为：" + employeeCachedDao.findOneCondition(2));


	}

	/**
	 * 测试@CacheEvict 注解，用于缓存条目清除
	 */
	@Test
	public void testEvict() {
		int id = 100;
		//先存入一个数据；
		Employee e = new Employee();
		e.setId(id);
		e.setName("lisi");
		e.setGender("w");

		System.out.println(employeeCachedDao.saveOne(e));
		//查一下是否走缓存
		System.out.println(employeeCachedDao.findOne(3));
		//从缓存中删除
		employeeCachedDao.delete(id);
		//再查一次，应该不是走缓存了

		System.out.println(employeeCachedDao.findOne(id));
	}


	@Test
	@Transactional
	public void testSaveList() {
		List<Employee> list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {

			Employee e = new Employee();
			e.setId(i);
			e.setName("葫芦娃" + i);
			list.add(e);


		}

		employeeCachedDao.saveList(list,100);
		System.out.println("查回来的信息为："+employeeCachedDao.findList(100));
		try {
			//因为DEBUG时，H2-CONSOLE无法访问，所以只能用RUN跑junit,不可以断点DEBUG,通过延时两分钟，来查看h2-console
			Thread.currentThread().sleep(120000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
