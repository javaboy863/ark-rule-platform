
package com.ark.rule.platform.web;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * springboot启动类.
 *
 */
@ComponentScan(basePackages = {"com.ark"})
@ImportResource({"classpath:applicationContext.xml"})
@SpringBootApplication
@EnableScheduling
@ServletComponentScan("com.ark.rule.platform")
@MapperScan("com.ark.rule.platform.dao")
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("SpringBoot Start Success");
    }

}
