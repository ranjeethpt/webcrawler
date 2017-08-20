package com.test.web.crawler.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
public class CacheConfiguration {

    @Configuration
    @EnableCaching
    @LocalCache
    protected static class LocalCacheConfiguration {

        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
            simpleCacheManager.setCaches(newArrayList(new ConcurrentMapCache("CONNECT_CACHE")));
            return simpleCacheManager;
        }
    }
}
