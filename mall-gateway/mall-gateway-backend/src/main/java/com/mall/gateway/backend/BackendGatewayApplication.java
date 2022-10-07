package com.mall.gateway.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.mall.gateway.backend.dao")
public class BackendGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendGatewayApplication.class, args);
    }
}
