package com.redxun.gateway.filter;

import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysUserUtil;
import com.redxun.oauth2.common.properties.SecurityProperties;
import io.netty.buffer.ByteBufAllocator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: szw
 * @Description: 自定义忽略url但必须带上token网关全局过滤器
 * @Date: Created in 2020/10/13.
 */
@Slf4j
@Component
public class HttpTokenRequestFilter implements GlobalFilter, Ordered {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @param exchange
     * @param chain
     * @return get请求参考spring cloud gateway自带过滤器：
     * @see org.springframework.cloud.gateway.filter.factory.AddRequestParameterGatewayFilterFactory
     * <p>
     * post请求参考spring cloud gateway自带过滤器：
     * @see org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     */
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("----自定义忽略url但必须带上token网关全局过滤器生效----");
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String reqUrl = serverHttpRequest.getPath().value();
        //reqUrl是否是httpTokenUrls校验url带token
        boolean isConTianUrl = securityProperties.getIgnore().obtainsHttpTokenUrl(reqUrl);
        if (isConTianUrl) {
            MultiValueMap<String, String> queryParams = serverHttpRequest.getQueryParams();
            if (BeanUtil.isEmpty(queryParams)) {
                log.error("---HttpTokenRequestFilter.filter is error : accessToken is null");
                throw new IllegalStateException("Invalid URI query: accessToken is null");
            }
            String accessToken = queryParams.getFirst("accessToken");
            if (StringUtils.isEmpty(accessToken)) {
                log.error("---HttpTokenRequestFilter.filter is error : accessToken is null");
                throw new IllegalStateException("Invalid URI query: accessToken is null");
            }
            JPaasUser loginUser = SysUserUtil.getLoginUser(accessToken);
            if (BeanUtil.isEmpty(loginUser)) {
                log.error("---HttpTokenRequestFilter.filter is error : accessToken is invalid !");
                throw new IllegalStateException("Invalid URI query: accessToken is invalid !");
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -90;
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     *
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }

}
