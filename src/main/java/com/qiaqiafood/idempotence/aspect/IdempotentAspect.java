package com.qiaqiafood.idempotence.aspect;

import com.github.benmanes.caffeine.cache.Cache;
import com.qiaqiafood.idempotence.CacheType;
import com.qiaqiafood.idempotence.DistributedCacheService;
import com.qiaqiafood.idempotence.annotation.Idempotent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
public class IdempotentAspect {
    private final Cache<String, Long> localCache;
    private final DistributedCacheService distributedCacheService;

    public IdempotentAspect(Cache<String, Long> localCache, DistributedCacheService distributedCacheService) {
        this.localCache = localCache;
        this.distributedCacheService = distributedCacheService;
    }

    @Around("@annotation(com.qiaqiafood.idempotence.annotation.Idempotent)")
    public Object preventDuplicateSubmit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent annotation = method.getAnnotation(Idempotent.class);

        String key = annotation.key();
        if (!StringUtils.hasLength(key)) {
            key = generateKey(joinPoint);
        }

        CacheType cacheType = annotation.type();
        long timeout = annotation.timeout();
        TimeUnit timeUnit = annotation.timeUnit();

        if (cacheType == CacheType.LOCAL) {
            if (localCache.getIfPresent(key) != null) {
                throw new RuntimeException("重复请求,请稍后再试");
            } else {
                localCache.put(key, System.currentTimeMillis());
            }
        } else if (cacheType == CacheType.DISTRIBUTED) {
            if (distributedCacheService.containsKey(key)) {
                throw new RuntimeException("重复请求,请稍后再试");
            } else {
                distributedCacheService.put(key, System.currentTimeMillis(), timeout, timeUnit);
            }
        }

        return joinPoint.proceed();
    }

    private String generateKey(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg.toString());
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }


}
