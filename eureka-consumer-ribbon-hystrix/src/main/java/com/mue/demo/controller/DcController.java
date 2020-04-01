package com.mue.demo.controller;

import com.mue.demo.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
public class DcController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ConsumerService consumerService;

    @GetMapping("/consumer")
    public String dc() {
        return consumerService.consumer();
    }

    @GetMapping("/consumer2")
    public String dc2() {
        String str = "";
        if (str != null) {

        }
        return restTemplate.getForObject("http://eureka-consumer/dc2", String.class);
    }


}
