package com.chengql.template;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ComponentScan("com.chengql")
@MapperScan("com.chengql")
//@EnableAsync//开启异步任务支持
//@EnableScheduling // 开启对计划任务的支持
@EnableTransactionManagement // 事务
public class MysqlTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(MysqlTemplateApplication.class, args);
    }

}
