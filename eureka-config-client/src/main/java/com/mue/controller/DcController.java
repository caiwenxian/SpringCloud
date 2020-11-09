package com.mue.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description：
 * @author: caiwx
 * @createDate ： 2020年10月22日 18:10:00
 */
@RestController
@RefreshScope
public class DcController {

    @Value("${ceshi}")
    private String message;

    @RequestMapping("/hello")
    public String hello() {
        return this.message;
    }
}
