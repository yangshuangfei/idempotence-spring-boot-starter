# 限流

## 引入依赖

```xml
<dependency>
    <groupId>io.github.yangshuangfei</groupId>
    <artifactId>idempotence-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 别的依赖

##### 使用本地模式时需要引入caffeine

```xml
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```



##### 使用redis模式时需要引入redis

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```



##### 还需要引入切面和JSON工具

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
</dependency>
```



## 本地模式
本地模式中只需要配置成local即可
```yaml
idempotent.cache-type=local
```

```Java
@Idempotent(type = CacheType.LOCAL, timeout = 5)
```



## Redis模式
还需要配置redis连接信息
```yaml
idempotent.cache-type=distributed
```

```java
@Idempotent(type = CacheType.DISTRIBUTED, timeout = 5)
```

