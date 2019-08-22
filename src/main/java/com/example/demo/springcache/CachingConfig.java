package com.example.demo.springcache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 缓存管理器通常只有一个，所以我们在测试的时候注意注解掉其他的缓存管理器 关于REDIS缓存管理器，我们后续有时间再补充
 * 
 * @author wangxg3
 *
 */
@Configuration
@EnableCaching
public class CachingConfig {
	/**
	 * ConcurrentMapCacheManager缓存管理器
	 * 
	 * @return
	 */

	
	  @Bean public CacheManager cacheManager() { ConcurrentMapCacheManager
	  concurrentMapCacheManager = new ConcurrentMapCacheManager(); Set<String>
	  nameSet = new HashSet<String>(); nameSet.add("defaultCache");
	  concurrentMapCacheManager.setCacheNames(nameSet); return
	  concurrentMapCacheManager;
	  
	  }
	 

	/**
	 * EhCache缓存管理器
	 * 
	 * @return
	 */

	@Bean
	public EhCacheCacheManager ehCacheManager(net.sf.ehcache.CacheManager cm) {
		return new EhCacheCacheManager(cm);
	}

	/**
	 * EhCache缓存管理器工厂类
	 * 
	 * @return
	 */

	@Bean
	public EhCacheManagerFactoryBean ehCache() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();

		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("configCache/ehcache.xml"));

		return ehCacheManagerFactoryBean;

	}

}
