package com.wenxianm;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName BlogMainServiceApplication
 * @Author cwx
 * @Date 2021/9/25 15:58
 **/
@EnableFeignClients
@EnableScheduling
@EnableAsync
@SpringCloudApplication
public class BlogMainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogMainServiceApplication.class, args);
    }
}