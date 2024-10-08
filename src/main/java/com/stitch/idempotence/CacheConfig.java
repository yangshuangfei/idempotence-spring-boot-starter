package com.stitch.idempotence;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    @ConditionalOnMissingBean(Cache.class)
    public Cache<String, Long> localCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS) // Default expiration time
                .build();
    }
}
