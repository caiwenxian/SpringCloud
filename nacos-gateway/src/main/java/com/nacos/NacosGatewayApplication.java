package com.nacos;

import com.nacos.filter.AuthorizeGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosGatewayApplication.class, args);
    }

    @Bean
    public AuthorizeGatewayFilterFactory authorizeGatewayFilter () {
        return new AuthorizeGatewayFilterFactory();
    }

}
