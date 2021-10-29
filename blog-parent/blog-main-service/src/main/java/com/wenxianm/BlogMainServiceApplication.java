package com.wenxianm;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.wenxianm.mq.MySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName BlogMainServiceApplication
 * @Author cwx
 * @Date 2021/9/25 15:58
 **/
@MapperScan("com.wenxianm.**.dao")
@EnableFeignClients
@EnableScheduling
@EnableAsync
@SpringCloudApplication
@EnableTransactionManagement
@EnableBinding(MySource.class)
@EnableApolloConfig
public class BlogMainServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogMainServiceApplication.class, args);
    }
}
