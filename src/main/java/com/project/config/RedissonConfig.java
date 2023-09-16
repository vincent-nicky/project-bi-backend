package com.project.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
@EnableRedisHttpSession
public class RedissonConfig {

    private Integer database;
    private String host;
    private Integer port;
    private String password;

    // start redisConfig
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        // 创建 RedisStandaloneConfiguration 来配置连接信息
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        // 设置密码
        configuration.setPassword(password);
        // 连接
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration);
        // 设置要连接的数据库索引，这里默认为0
        lettuceConnectionFactory.setDatabase(database);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

//    /**
//     * 在springboot中使用spring-session的时候，
//     * 在不同的域名下面需要配置cookie主域否则session共享不生效
//     * @return
//     */
//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
//        //cookie名字
//        defaultCookieSerializer.setCookieName("sessionId");
//        //不同子域时设置
//        //defaultCookieSerializer.setDomainName("xxx.com");
//        //设置各web应用返回的cookiePath一致
//        defaultCookieSerializer.setCookiePath("/");
//        return defaultCookieSerializer;
//    }

    // end redisConfig

    // start redissonConfig
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setPassword(password)
                .setDatabase(database)
                .setAddress("redis://" + host + ":" + port);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
    // end redissonConfig
}
