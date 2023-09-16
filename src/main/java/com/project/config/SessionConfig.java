package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {

    /*
    (依旧没有解决，但能看到redis中session的值)
    Session 序列化一致性：确保 Session 中的对象可以被正确地序列化和反序列化。
    默认情况下，Spring Session 使用 Java 的序列化方式，但这可能会导致不一致性问题。
    你可以考虑使用 JSON 或其他序列化方式来确保一致性。
    */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
