package com.stitch.idempotence.annotation;

import com.stitch.idempotence.CacheType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    CacheType type() default CacheType.LOCAL;

    long timeout() default 5;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    String key() default "";
}
