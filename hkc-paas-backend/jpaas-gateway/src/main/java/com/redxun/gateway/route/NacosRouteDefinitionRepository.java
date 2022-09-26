package com.redxun.gateway.route;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.redxun.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * nacos路由数据源
 *
 * @author yjy
 * @date 2019/10/7
 * <p>
 *
 *
 */
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
    private static final String SCG_DATA_ID = "scg-routes";
    private static final String SCG_GROUP_ID = "SCG_GATEWAY";

    private ApplicationEventPublisher publisher;

    private ConfigService configService;

    public NacosRouteDefinitionRepository(ApplicationEventPublisher publisher, ConfigService configService) {
        this.publisher = publisher;
        this.configService = configService;
        addListener();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String content = configService.getConfig(SCG_DATA_ID, SCG_GROUP_ID,5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            log.error("getRouteDefinitions by nacos error", e);
        }
        return Flux.fromIterable(CollUtil.newArrayList());
    }

    /**
     * 添加Nacos监听
     */
    private void addListener() {
        try {
            configService.addListener(SCG_DATA_ID, SCG_GROUP_ID, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                    log.info("nacos-addListener-receiveConfigInfo", configInfo);
                }
            });
        } catch (NacosException ex) {
            log.error(ExceptionUtil.getExceptionMessage(ex));
        }
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        try {
            String content = configService.getConfig(SCG_DATA_ID, SCG_GROUP_ID, 5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            RouteDefinition def= route.block();
            String routeId=def.getId();
            for(Iterator<RouteDefinition> it= routeDefinitions.iterator();it.hasNext();){
                RouteDefinition definition=it.next();
                if(def.getId().equals(routeId)){
                    it.remove();
                }
            }
            routeDefinitions.add(def);
            String json= JSONObject.toJSONString(routeDefinitions);
            configService.publishConfig(SCG_DATA_ID,SCG_GROUP_ID,json);
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> delete(Mono<String> monoRoute) {
        try {
            String routeId= monoRoute.block();
            String content = configService.getConfig(SCG_DATA_ID, SCG_GROUP_ID,5000);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            for(Iterator<RouteDefinition> it= routeDefinitions.iterator();it.hasNext();){
                RouteDefinition def=it.next();
                if(def.getId().equals(routeId)){
                    it.remove();
                }
            }
            String json= JSONObject.toJSONString(routeDefinitions);
            configService.publishConfig(SCG_DATA_ID,SCG_GROUP_ID,json);
        } catch (NacosException e) {
            log.error("getRouteDefinitions by nacos error", e);
        }
        return Mono.empty()   ;
    }

    private List<RouteDefinition> getListByStr(String content) {
        if (StrUtil.isNotEmpty(content)) {
            return JSONObject.parseArray(content, RouteDefinition.class);
        }
        return new ArrayList<>(0);
    }
}
