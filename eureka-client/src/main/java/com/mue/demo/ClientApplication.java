package com.mue.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.mue.demo.dao")
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Resource
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(dataSource);

    }
}
