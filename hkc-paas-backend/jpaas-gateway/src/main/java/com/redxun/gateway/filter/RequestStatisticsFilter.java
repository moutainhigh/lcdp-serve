package com.redxun.gateway.filter;

import com.redxun.gateway.utils.ReactiveAddrUtil;
import eu.bitwalker.useragentutils.UserAgent;
import com.redxun.log.monitor.PointUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 请求统计分析埋点过滤器
 *
 * @author yjy
 * @date 2019/10/7
 * <p>
 *
 *
 */
@Component
public class RequestStatisticsFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        UserAgent userAgent = UserAgent.parseUserAgentString(headers.get("User-Agent"));
        //埋点
        PointUtil.debug("1", "request-statistics",
                "ip=" + ReactiveAddrUtil.getRemoteAddr(request)
                        + "&browser=" + userAgent.getBrowser()
                        + "&operatingSystem=" + userAgent.getOperatingSystem());

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
