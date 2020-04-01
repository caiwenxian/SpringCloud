package com.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    /*@Value("${CALogin}")
    private boolean CALogin;*/

    @RequestMapping("/echo/{message}")
    public String echo(@PathVariable String message) {
        return "hello nacos discovery:" + message;
    }

}
