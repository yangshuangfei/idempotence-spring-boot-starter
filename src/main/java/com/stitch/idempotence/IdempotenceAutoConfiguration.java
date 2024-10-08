package com.stitch.idempotence;

import com.github.benmanes.caffeine.cache.Cache;
import com.stitch.idempotence.aspect.IdempotentAspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

@Configuration
@AutoConfigureAfter(RedisConfig.class)
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotenceAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public DistributedCacheService distributedCacheService(RedisTemplate<String, Object> redisTemplate) {
        return new DistributedCacheService(redisTemplate);
    }

    @Bean
    public IdempotentAspect idempotentAspect(Cache<String, Long> localCache, @Nullable DistributedCacheService distributedCacheService) {
        return new IdempotentAspect(localCache, distributedCacheService);
    }


}
