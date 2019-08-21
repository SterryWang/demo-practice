package com.example.demo.springcache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
/**
 * 缓存管理器通常只有一个，所以我们在测试的时候注意注解掉其他的缓存管理器
 * 关于REDIS缓存管理器，我们后续有时间再补充
 * @author wangxg3
 *
 */
@Configuration
@EnableCaching
public class CachingConfig {
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();

	}

	@Bean
	public EhCacheCacheManager ehCacheManager() {
		return new EhCacheCacheManager();
	}

	@Bean
	public EhCacheManagerFactoryBean ehCache() {
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();

		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("config/cacheConfig/ehcache.xml"));

		return ehCacheManagerFactoryBean;

	}

}
