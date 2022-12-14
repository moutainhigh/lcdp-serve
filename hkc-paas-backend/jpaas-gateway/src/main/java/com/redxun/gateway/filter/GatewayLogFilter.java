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

    // ??????????????????
    private static final String REQUEST_ORIGIN_APP = "Request-Origin-App";

    // ?????????????????????????????????????????????????????????
    private static final List<String> CUSTOM_HEADERS = Arrays.asList("sign", "timestamp", "random", "Request-Origin-App");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // ????????????
        String requestPath = request.getPath().pathWithinApplication().value();

        // ??????????????????
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
        // ??????????????????-2?????????????????????????????????
        return -2;
    }


    /**
     * ??????????????????
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

        // ??????Header
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().headers(httpHeaders -> {
            // ???????????????header
            for (String customHeader : CUSTOM_HEADERS) {
                httpHeaders.remove(customHeader);
            }
        }).build();

        //???????????????
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);

        return chain.filter(exchange.mutate().request(mutableReq).response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> {
                    // ????????????
                    writeAccessLog(gatewayLog);
                }));
    }


    /**
     * ?????? request body ???????????????????????????
     * ??????: org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory
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

        // ?????? BodyInserter ?????? body(????????????body), ?????? request body ??????????????????
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH);

        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

        return bodyInserter.insert(outputMessage,new BodyInserterContext())
                .then(Mono.defer(() -> {
                    // ??????????????????
                    ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);

                    // ??????????????????
                    ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);

                    // ???????????????
                    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                            .then(Mono.fromRunnable(() -> {
                                // ????????????
                                writeAccessLog(gatewayLog);
                            }));
                }));
    }



    /**
     * ????????????
     * @param gatewayLog ????????????
     */
    private void writeAccessLog(GatewayLog gatewayLog) {
        applicationEventPublisher.publishEvent(new GatewayLogEvent(this, gatewayLog));
    }



    /**
     * ?????????????????????????????? headers
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

                // ???????????????header
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
     * ??????????????????
     * ?????? DataBufferFactory ????????????????????????????????????
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
                    // ??????????????????
                    long executeTime = (responseTime.getTime() - gatewayLog.getStartTime());

                    gatewayLog.setExecuteTime(executeTime);
                    gatewayLog.setStatus(response.getStatusCode().value() == 200 ? "??????" : "??????");

                    // ?????????????????????????????? json ?????????
                    String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);


                    if (Objects.equals(this.getStatusCode(), HttpStatus.OK)
                            && StringUtils.isNotBlank(originalResponseContentType)
                            && originalResponseContentType.contains(CONTENT_TYPE)) {

                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            // ???????????????????????????????????????????????????
                            DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer join = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[join.readableByteCount()];
                            join.read(content);

                            // ???????????????
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
     * ???map???????????????url??????
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
