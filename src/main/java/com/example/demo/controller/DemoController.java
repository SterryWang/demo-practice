package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.springcache.EmployeeCachedDao;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class DemoController {

    @Resource
	private EmployeeCachedDao employeeCachedDao;

	@GetMapping("/hello")
	public  String testController() {
		return  "HEllO  ,Wang!";
	}
    
	@GetMapping("/userdir")
	public  String  getUserDir() {
		return System.getProperty("user.dir");
	}

	@GetMapping("/savelist")
	public String  testSaveList(){
		List<Employee> list = new ArrayList<>();
		for (int i = 0; i < 3; i++) {

			Employee e = new Employee();
			e.setId(i);
			e.setName("葫芦娃" + i);
			list.add(e);


		}

		employeeCachedDao.saveList(list,100);
		return "查回来的信息为："+employeeCachedDao.findList(100);
	}
}
