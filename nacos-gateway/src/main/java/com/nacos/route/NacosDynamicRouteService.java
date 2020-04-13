package com.nacos.route;

import org.springframework.cloud.gateway.route.RouteDefinition;

public interface NacosDynamicRouteService {

    String update(RouteDefinition gatewayDefine);
}
