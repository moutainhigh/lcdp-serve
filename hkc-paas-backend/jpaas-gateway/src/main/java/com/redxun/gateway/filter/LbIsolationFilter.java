package com.redxun.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.redxun.common.constant.ConfigConstants;
import com.redxun.common.tool.constant.CommonConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 传递负载均衡隔离值
 *
 * @author yjy
 * @date 2019/10/7
 * <p>
 *
 *
 */
@Component
@ConditionalOnProperty(name = ConfigConstants.CONFIG_RIBBON_ISOLATION_ENABLED, havingValue = "true")
public class LbIsolationFilter extends LoadBalancerClientFilter {
    public LbIsolationFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
        super(loadBalancer, properties);
    }

    @Override
    protected ServiceInstance choose(ServerWebExchange exchange) {
        String developer = exchange.getRequest().getHeaders().getFirst(CommonConstant.DEVELOPER);
        if (StrUtil.isNotEmpty(developer)) {
            if (this.loadBalancer instanceof RibbonLoadBalancerClient) {
                RibbonLoadBalancerClient client = (RibbonLoadBalancerClient) this.loadBalancer;
                String serviceId = ((URI) exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).getHost();
                return client.choose(serviceId, developer);
            }
        }
        return super.choose(exchange);
    }
}
