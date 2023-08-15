package com.tsinghua.tagsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableAsync
@MapperScan("com.tsinghua.tagsystem.dao.mapper")
public class TagSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagSystemApplication.class, args);
    }
}
