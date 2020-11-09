package com.mue.demo.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年10月22日 15:11:00
 */

@FeignClient(name = "eureka-client-1", fallback = DcServiceHystrix.class)
public interface DcService {

    @RequestMapping("/hello")
    public String hello();
}

@Component
class DcServiceHystrix implements DcService {

    @Override
    public String hello() {
        return "failure message";
    }
}
