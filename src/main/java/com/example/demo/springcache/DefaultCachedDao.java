package com.example.demo.springcache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DefaultCachedDao {
	
	@Cacheable("defaultCache")
	public String  findOne(String begin) {
		//对诗，key（入参begin）是上句,value是下句
		System.out.println("缓存里没有，所以生成一个出来");
		return  "黄河入海流";
	}

}
