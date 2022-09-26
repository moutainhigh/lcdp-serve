package com.redxun.common.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * http get请求带body内容
 *
 * @author hujun
 */
public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

    private final static String METHOD_NAME = "GET";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpGetWithEntity() {
        super();
    }

    public HttpGetWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }
}
