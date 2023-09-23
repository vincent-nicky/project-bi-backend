package com.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 * - @EnableAspectJAutoProxy是一个Spring Framework注解，用于启用Spring AOP（Aspect-Oriented Programming）的支持。
 * AOP是一种编程范式，允许开发人员通过切面（aspects）来模块化应用程序的横切关注点，例如日志记录、事务管理、安全性等。
 * - @EnableAspectJAutoProxy注解告诉Spring容器启用AspectJ风格的AOP代理。
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.project.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
