package com.mue.demo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @description：
 * 创建ServletInitializer.java，继承 SpringBootServletInitializer ，
 * 覆盖 configure()，把启动类 Application 注册进去。外部 Web 应用服务器构建 Web
 * Application Context 的时候，会把启动类添加进去
 * @author: caiwx
 * @createDate ： 2020年10月13日 10:38:00
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ClientApplication.class);
    }

}
