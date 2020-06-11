package com.example.demo.springcache;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * 缓存管理器SPRING 容器中只能注入一个，否则报错 如果要使用多个缓存管理器，有两种解决方案：
 * 一、使用SPRING提供的CompositeCacheManager把多个缓存管理器组合起来使用
 * 二、使用@Primary注解，定义一个首选CacheManager
 * 第二种方案虽然可行，但是到头来真正可用的仍然只有一个CacheManager,具体原理可参见
 * {@link https://blog.csdn.net/qq_26440803/article/details/90145543}
 * 所以第一种方案才能真正做到同时使用多个缓存管理器 所以我们在测试的时候注意注解掉其他的缓存管理器 关于REDIS缓存管理器，我们后续有时间再补充
 * FactoryBean的bean name 就是它生产的bean的bean name
 * 依赖注入时，可以使用@Qulifier进行注入以便区分，如果不用的话，默认按照类型进行注入
 * 
 * @author wangxg3
 *
 */
@Configuration
@EnableCaching
public class CachingConfig {
	/**
	 * ConcurrentMapCacheManager缓存管理器
	 * 比较简单的一个内存管理器
	 * 
	 * @return
	 */

	@Bean(name = "defaultCacheManager")
	@Primary
	public CacheManager cacheManager() {
		ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
		Set<String> nameSet = new HashSet<String>();
		/*
		 * 我们可以给缓存加上名称集合，这样就可以把缓存管理器划分成不同的分区，就像数据库里不同的表一样，
		 * 这样在添加或者查找缓存是就可以通过指定名字来筛选缓存范围了
		 */
		nameSet.add("defaultCache");
		nameSet.add("employeeCache");
		concurrentMapCacheManager.setCacheNames(nameSet);
		return concurrentMapCacheManager;

	}

	/**
	 * EhCache缓存管理器
	 * 
	 * @return
	 */

	@Bean(name = "ehCacheCacheManager")
	public EhCacheCacheManager ehCacheManager(@Qualifier("ehCacheManager") net.sf.ehcache.CacheManager cm) {
		return new EhCacheCacheManager(cm);
	}

	/**
	 * EhCache缓存管理器工厂类
	 * ehCache支持按照配置文件进行配置
	 * 可以配置缓存大小，存活时间等
	 * @return
	 */

	@Bean(name = "ehCacheManager")
	@Qualifier("ehCacheManager")
	public EhCacheManagerFactoryBean ehCache() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();

		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("configCache/ehcache.xml"));

		return ehCacheManagerFactoryBean;

	}

}
