package com.chengql.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.chengql")
//@EnableAsync//开启异步任务支持
//@EnableScheduling // 开启对计划任务的支持
public class MongodbTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbTemplateApplication.class, args);
    }

}
