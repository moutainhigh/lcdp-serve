package com.redxun.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.redxun.gateway.utils.ReactiveAddrUtil;
import com.redxun.log.model.GatewayLog;
import com.redxun.log.model.GatewayLogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;


@Slf4j
@Component
public class GatewayLogFilter implements GlobalFilter, Ordered {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    private static final String CONTENT_TYPE = "application/json";

    // 请求来源应用
    private static final String REQUEST_ORIGIN_APP = "Request-Origin-App";

    // 自定义请求头，转发之前删除自定义请求头
    private static final List<String> CUSTOM_HEADERS = Arrays.asList("sign", "timestamp", "random", "Request-Origin-App");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 请求路径
        String requestPath = request.getPath().pathWithinApplication().value();

        // 获取路由信息
        Route route = getGatewayRoute(exchange);

        String ipAddress = ReactiveAddrUtil.getRemoteAddr(request);

        GatewayLog gatewayLog = new GatewayLog();
        gatewayLog.setOrigin(request.getHeaders().getFirst(REQUEST_ORIGIN_APP));
        gatewayLog.setSchema(request.getURI().getScheme());
        gatewayLog.setRequestMethod(request.getMethodValue());
        gatewayLog.setRequestPath(requestPath);
        gatewayLog.setTargetServer(route.getUri().toString());
        gatewayLog.setStartTime(new Date().getTime());
        gatewayLog.setIp(ipAddress);
        gatewayLog.setRouteConfig(JSON.toJSONString(route));
        Map<String, Object> headers = new HashMap<>();
        for (String key : request.getHeaders().keySet()) {
            headers.put(key, request.getHeaders().getFirst(key));
        }
        gatewayLog.setHeaders(JSON.toJSONString(headers));


        MediaType mediaType = request.getHeaders().getContentType();
        if (request.getHeaders().getContentType() != null) {
            gatewayLog.setRequestContentType(request.getHeaders().getContentType().toString());
        }

        if(mediaType != null && (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON.isCompatibleWith(mediaType))){
            return writeBodyLog(exchange, chain, gatewayLog);
        }else{
            return writeBasicLog(exchange, chain, gatewayLog);
        }

    }

    @Override
    public int getOrder() {
        // 必须小于等于-2，否则无法获取相应结果
        return -2;
    }


    /**
     * 获取路由信息
     * @param exchange
     * @return
     */
    private Route getGatewayRoute(ServerWebExchange exchange) {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }



    private Mono<Void> writeBasicLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog gatewayLog) {
        StringBuilder builder = new StringBuilder();
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        gatewayLog.setRequestBody(getUrlParamsByMap(queryParams));

        // 修改Header
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().headers(httpHeaders -> {
            // 删除自定义header
            for (String customHeader : CUSTOM_HEADERS) {
                httpHeaders.remove(customHeader);
            }
        }).build();

        //获取响应体
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);

        return chain.filter(exchange.mutate().request(mutableReq).response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // 打印日志
                    writeAccessLog(gatewayLog);
                }));
    }


    /**
     * 解决 request body 只能读取一次问题，
     * 参考: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
     * @param exchange
     * @param chain
     * @param gatewayLog
     * @return
     */
    private Mono<Void> writeBodyLog(ServerWebExchange exchange, GatewayFilterChain chain, GatewayLog gatewayLog) {
        ServerRequest serverRequest = ServerRequest.create(exchange,messageReaders);

        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body ->{
                    gatewayLog.setRequestBody(body);
                    return Mono.just(body);
                });

        // 通过 BodyInserter 插入 body(支持修改body), 避免 request body 只能获取一次
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

        return bodyInserter.insert(outputMessage,new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // 重新封装请求
                    ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);

                    // 记录响应日志
                    ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);

                    // 记录普通的
                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                            .then(Mono.fromRunnable(() -> {
                                // 打印日志
                                writeAccessLog(gatewayLog);
                            }));
                }));
    }



    /**
     * 打印日志
     * @param gatewayLog 网关日志
     */
    private void writeAccessLog(GatewayLog gatewayLog) {
        applicationEventPublisher.publishEvent(new GatewayLogEvent(this, gatewayLog));
    }



    /**
     * 请求装饰器，重新计算 headers
     * @param exchange
     * @param headers
     * @param outputMessage
     * @return
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers,
                                                       CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    // TODO: this causes a 'HTTP/1.1 411 Length Required' // on
                    // httpbin.org
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }

                // 删除自定义header
                for (String customHeader : CUSTOM_HEADERS) {
                    headers.remove(customHeader);
                }

                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }


    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, GatewayLog gatewayLog) {
        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();

        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Date responseTime = new Date();
                    gatewayLog.setEndTime(responseTime.getTime());
                    // 计算执行时间
                    long executeTime = (responseTime.getTime() - gatewayLog.getStartTime());

                    gatewayLog.setExecuteTime(executeTime);
                    gatewayLog.setStatus(response.getStatusCode().value() == 200 ? "成功" : "失败");

                    // 获取响应类型，如果是 json 就打印
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);


                    if (Objects.equals(this.getStatusCode(), HttpStatus.OK)
                            && StringUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains(CONTENT_TYPE)) {

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            // 合并多个流集合，解决返回体分段传输
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);

                            // 释放掉内存
                            DataBufferUtils.release(join);
                            String responseResult = new String(content, StandardCharsets.UTF_8);

                            gatewayLog.setResponseData(responseResult);

                            return bufferFactory.wrap(content);
                        }));
                    }
                }
                // if body is not a flux. never got there.
                return super.writeWith(body);
            }
        };
    }


    /**
     * 将map参数转换成url参数
     * @param map
     * @return
     */
    private String getUrlParamsByMap(MultiValueMap<String, String> map) {
        if (ObjectUtils.isEmpty(map)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue().get(0));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }
}
