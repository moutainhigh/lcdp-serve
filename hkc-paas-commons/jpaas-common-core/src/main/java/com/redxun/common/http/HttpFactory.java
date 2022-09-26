package com.redxun.common.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * http工厂类
 *
 * @author hujun
 */
@Service
public class HttpFactory {

    public static String HTTP_CLIENT = "httpClientImpl";
    public static String RESFUL_TEMPLATE = "resfulTemplateImpl";

    @Autowired
    private final Map<String, IHttp> httpMap = new ConcurrentHashMap<>();

    private IHttp getHttpByKey(String key) {
        return httpMap.get(key);
    }

    public IHttp getHttpByHttpClient() {
        return getHttpByKey(HTTP_CLIENT);
    }

    public IHttp getHttpByResfulTemplate() {
        return getHttpByKey(RESFUL_TEMPLATE);
    }

}
