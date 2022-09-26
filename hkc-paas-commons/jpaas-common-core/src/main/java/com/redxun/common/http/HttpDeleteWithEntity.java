package com.redxun.common.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * http delete请求带body内容
 *
 * @author hujun
 */
public class HttpDeleteWithEntity extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    /**
     * 获取方法（必须重载）
     *
     * @return
     */
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDeleteWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteWithEntity(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteWithEntity() {
        super();
    }
}
