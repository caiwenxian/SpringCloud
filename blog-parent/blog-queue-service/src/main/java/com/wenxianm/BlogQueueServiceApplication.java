package com.wenxianm;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.wenxianm.mq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName BlogQueueServiceApplication
 * @Author cwx
 * @Date 2021/10/25 17:32
 **/
@EnableFeignClients
@SpringCloudApplication
@EnableBinding(MySink.class)
@EnableApolloConfig
public class BlogQueueServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogQueueServiceApplication.class, args);
    }
}
