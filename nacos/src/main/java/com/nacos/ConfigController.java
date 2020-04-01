package com.nacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Value("${CALogin}")
    private boolean CALogin;

    @RequestMapping("/get")
    @ResponseBody
    public sampleModel get() {
        sampleModel sampleModel = new sampleModel(useLocalCache, CALogin);
        return sampleModel;
//        return this.useLocalCache;
    }
}