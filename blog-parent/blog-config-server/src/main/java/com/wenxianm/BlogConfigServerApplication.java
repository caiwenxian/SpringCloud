package com.wenxianm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 * @ClassName ConfigServerApplication
 * @Author cwx
 * @Date 2021/9/24 15:14
 **/
@SpringBootApplication
@EnableConfigServer
public class BlogConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogConfigServerApplication.class, args);
    }
}
