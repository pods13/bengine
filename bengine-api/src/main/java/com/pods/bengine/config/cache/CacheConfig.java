package com.pods.bengine.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final String PAGES_CACHE_NAME = "pages";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(PAGES_CACHE_NAME);
    }

    @CacheEvict(allEntries = true, cacheNames = {PAGES_CACHE_NAME})
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void pagesCacheEvict() {
    }
}