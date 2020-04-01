package com.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/echo/{message}")
    public String echo(@PathVariable String message) {
        restTemplate.getForObject("http://nacos-service-config/config/get", Object.class);
        System.out.println("");
        return restTemplate.getForObject("http://nacos-service-provider/provider/echo/" + message, String.class);
    }
}
