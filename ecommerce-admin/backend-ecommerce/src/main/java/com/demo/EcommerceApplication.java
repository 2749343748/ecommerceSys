package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 电商管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.demo.mapper")
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
        System.out.println("===================================");
        System.out.println("  电商管理系统启动成功!");
        System.out.println("  接口文档: http://localhost:8088/doc.html");
        System.out.println("===================================");
    }
}
