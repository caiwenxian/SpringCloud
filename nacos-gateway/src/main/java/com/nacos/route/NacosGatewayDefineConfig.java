package com.nacos.route;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Component
@NacosPropertySource(dataId = "${nacos.config.data-id}", autoRefreshed = true)
public class NacosGatewayDefineConfig implements CommandLineRunner {


    @NacosInjected
    private ConfigService configService;

    @Value("${nacos.config.data-id}")
    private String dataId;

    @Value("${nacos.config.group}")
    private String group;

    @Autowired
    NacosDynamicRouteService nacosDynamicRouteService;

    private void addRouteNacosListen() {
        try {
            String configInfo = configService.getConfig(dataId, group, 5000);
            System.out.println("从Nacos返回的配置：" + configInfo);
            getNacosDataRoutes(configInfo);
            //注册Nacos配置更新监听器，用于监听触发
            configService.addListener(dataId, group, new Listener()  {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println("Nacos更新了！");
                    System.out.println("接收到数据:"+configInfo);
                    getNacosDataRoutes(configInfo);
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (Exception e) {

        }
    }

    private void getNacosDataRoutes(String configInfo) {
        List<RouteDefinition> list = JSON.parseArray(configInfo, RouteDefinition.class);
        list.stream().forEach(definition -> {
            System.out.println(""+JSON.toJSONString(definition));
            String result = nacosDynamicRouteService.update(definition);
            System.out.println(result);
        });
    }

    @Override
    public void run(String... args) throws Exception {
        addRouteNacosListen();
    }


}
