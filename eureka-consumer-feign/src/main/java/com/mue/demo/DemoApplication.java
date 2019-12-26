package com.mue.demo;

import com.netflix.discovery.shared.Application;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DemoApplication.class).web(true).run(args);
//        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * 接收文件
     * @author caiwx
     * @date 2019/12/25 11:15
     * @return
     */
    @RestController
    public class UploadController {

        @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public String handleFileUpload(@RequestPart(value = "file") MultipartFile file) {
            try {
                File saveFile = new File("D://" + new Date().getTime() + file.getOriginalFilename());
                file.transferTo(saveFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file.getOriginalFilename();
        }

    }
}
