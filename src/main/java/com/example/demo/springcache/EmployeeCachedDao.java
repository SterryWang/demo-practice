package com.example.demo.springcache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Employee;


@Component
public class EmployeeCachedDao {
	//@Cacheable("defaultCache")
	@Cacheable("employeesEhcache")
	public Employee findOne(int id) {
		// 本地生成一个，暂时用来模拟从数据库里查数据的流程
		System.out.println("缓存里没有，所以从数据库里查询！");
		Employee employee = new Employee();

		employee.setId(id);
		employee.setName("xiaohua" + id);
		employee.setGender("m");

		return employee;

	}

}
