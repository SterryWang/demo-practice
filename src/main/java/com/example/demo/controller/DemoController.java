package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DemoController {
	
	
	@GetMapping("/hello")
	public  String testController() {
		return  "HEllO  ,Wang!";
	}
    
	@GetMapping("/userdir")
	public  String  getUserDir() {
		return System.getProperty("user.dir");
	}
}
