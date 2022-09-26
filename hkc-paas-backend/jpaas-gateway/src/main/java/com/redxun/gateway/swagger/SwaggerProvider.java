package com.redxun.gateway.swagger;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 跨域配置
 *
 * @author yjy
 * @date 2019/10/5
 * <p>
 *
 *
 */
@Component
@EnableConfigurationProperties(SwaggerAggProperties.class)
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;

    @Resource
    private SwaggerAggProperties swaggerAggProperties;

    public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
        this.routeLocator = routeLocator;
        this.gatewayProperties = gatewayProperties;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        Set<String> routes = new HashSet<>();


        Set<String> set= swaggerAggProperties.getGenerateRoutes();
        for(String str:set){
            SwaggerResource resource=new SwaggerResource();
            resource.setName(str);
            resource.setUrl("/api-"+str+"/v2/api-docs");
            resource.setSwaggerVersion("2.0");
            resources.add(resource);
        }

        //取出Spring Cloud Gateway中的route

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(swaggerAggProperties.getSwaggerVersion());
        return swaggerResource;
    }
}
