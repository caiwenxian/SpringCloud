package com.wenxianm;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务
 * @ClassName BlogQuartzService
 * @Author cwx
 * @Date 2021/9/26 11:01
 **/
@EnableScheduling
@EnableFeignClients
@SpringCloudApplication
@EnableEurekaClient
public class BlogQuartzServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogQuartzServiceApplication.class, args);
    }
}
