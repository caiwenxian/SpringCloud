package com.mue.demo;

import com.netflix.discovery.shared.Application;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(true).run(args);
//        SpringApplication.run(DemoApplication.class, args);
    }

    @FeignClient(value = "eureka-consumer-feign", configuration = UploadService.MultipartSupportConfig.class)
    public interface UploadService {

        /**
         * 上传文件
         * @author caiwx
         * @date 2019/12/25 11:15
         * @return
         */
        @PostMapping(value = "uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        String uploadFile(@RequestPart(value = "file") MultipartFile file);

        @Configuration
        class MultipartSupportConfig {
            @Bean
            public Encoder feignFormEncoder() {
                return new SpringFormEncoder();
            }
        }
    }
}
