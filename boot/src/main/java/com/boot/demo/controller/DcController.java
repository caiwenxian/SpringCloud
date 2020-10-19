package com.boot.demo.controller;

import com.boot.demo.service.DcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DcController {
    @Autowired
    DcService dcService;

    @GetMapping("/dc/{message}")
    public String dc(@PathVariable String message) throws InterruptedException {
        log.info("inter dc：{}", message);
        return message;
    }

    @GetMapping("/dc2")
    public int dc2() throws InterruptedException {
        log.info("inter dc2");
        return dcService.dcService();
    }

}