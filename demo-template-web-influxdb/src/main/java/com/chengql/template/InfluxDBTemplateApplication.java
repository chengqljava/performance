package com.chengql.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ComponentScan("com.chengql")
//@EnableAsync//开启异步任务支持
@EnableScheduling // 开启对计划任务的支持
public class InfluxDBTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfluxDBTemplateApplication.class, args);
    }

}
