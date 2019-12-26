package com.mue.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DcController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("/dc")
    public String dc() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-consumer");
        System.out.println(serviceInstance.toString());
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }

}