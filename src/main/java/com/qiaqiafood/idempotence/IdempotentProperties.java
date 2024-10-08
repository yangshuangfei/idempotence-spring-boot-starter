package com.qiaqiafood.idempotence;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "idempotent")
public class IdempotentProperties {

    private CacheType cacheType = CacheType.LOCAL;

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }
}
