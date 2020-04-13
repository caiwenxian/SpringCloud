package com.nacos;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "提供者接口", description = "备注信息")
@RestController
@RequestMapping("/provider")
public class ProviderController {

    /*@Value("${CALogin}")
    private boolean CALogin;*/

    @ApiOperation("echo接口")
    @GetMapping("/echo/{message}")
    public String echo(@PathVariable String message) {
        return "hello nacos discovery:" + message;
    }


    @ApiOperation("sample接口")
    @GetMapping("/sample")
    public String echo(sampleModel sampleModel) {
        return "ok";
    }

}
