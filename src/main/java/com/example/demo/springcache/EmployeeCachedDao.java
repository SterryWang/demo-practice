package com.example.demo.springcache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Employee;

@Component
public class EmployeeCachedDao {
	// @Cacheable("defaultCache")
	// @Cacheable({"employeesEhcache"})
	@Cacheable("employeeCache")
	public Employee findOne(int id) {
		// 本地生成一个，暂时用来模拟从数据库里查数据的流程
		System.out.println("缓存里没有，所以从数据库里查询！查到后会把返回结果通过切面放到缓存里的");
		Employee employee = new Employee();

		employee.setId(id);
		employee.setName("xiaohua" + id);
		employee.setGender("m");

		return employee;

	}

	@CachePut(value = "employeeCache", key = "#result.id")
	public Employee saveOne(Employee e) {
		// 模拟把数据保存到数据库
		System.out.println("数据已保存到数据库,同时也将通过切面自动保存到缓存中");
		return e;
	}

}
