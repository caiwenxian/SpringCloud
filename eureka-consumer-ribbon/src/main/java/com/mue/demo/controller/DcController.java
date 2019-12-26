package com.mue.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DcController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/consumer")
    public String dc() {
        return restTemplate.getForObject("http://eureka-client-1/dc", String.class);
    }

    @GetMapping("/consumer2")
    public String dc2() {
        String str = "";
        if (str != null) {

        }
        return restTemplate.getForObject("http://eureka-consumer/dc2", String.class);
    }


}
