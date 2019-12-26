package com.mue.demo.controller;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("eureka-client-1")
public interface DcInterface {

    @GetMapping("/dc")
    String consumer();
}
