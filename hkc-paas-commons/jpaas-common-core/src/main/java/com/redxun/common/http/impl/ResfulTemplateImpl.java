package com.redxun.common.http.impl;

import com.redxun.common.http.HttpRtnModel;
import com.redxun.common.http.IHttp;
import com.redxun.common.tool.BeanUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * 基于Spring-ResfulTemplate封装，实现对Restful API的常规调用
 * @author hujun
 */
@Component
public class ResfulTemplateImpl implements IHttp {
    private String defaultCharset = "utf-8";

    @Resource(name = "restTemplateExternal")
    RestTemplate restTemplate;

    /**
     * 获取请求头
     * @param reqHeaders
     * @return
     */
    private HttpHeaders getRequestHeaders(Map<String, String> reqHeaders) {
        HttpHeaders requestHeaders = new HttpHeaders();
        if (BeanUtil.isNotEmpty(reqHeaders)) {
            for (Map.Entry<String, String> ent : reqHeaders.entrySet()) {
                requestHeaders.add(ent.getKey(), ent.getValue());
            }
        }
        return requestHeaders;
    }

    /**
     * 获取请求实体
     * @param params
     * @param headers
     * @return
     */
    private HttpEntity getRequestEntity(Map<String, String> params,HttpHeaders headers){
        MultiValueMap<String,String> body=new LinkedMultiValueMap<>();
        if (BeanUtil.isNotEmpty(params)) {
            for (Map.Entry<String, String> ent : params.entrySet()) {
                body.add(ent.getKey(), ent.getValue());
            }
        }
        HttpEntity httpEntity=new HttpEntity<>(body,headers);
        return httpEntity;
    }

    /**
     * 发起送求
     * @param requestEntity
     * @param url
     * @param method
     * @param queryParams
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel sendRequest(HttpEntity requestEntity, String url, HttpMethod method,Map<String,String> queryParams, String charset) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (BeanUtil.isNotEmpty(queryParams)) {
            List<NameValuePair> list = new LinkedList<>();
            Iterator<String> keyIt = queryParams.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String val = queryParams.get(key);
                list.add(new BasicNameValuePair(key, val));
            }
            uriBuilder.setParameters(list);
        }
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.build(), method, requestEntity, String.class);
        HttpRtnModel rtnModel = new HttpRtnModel();
        rtnModel.setStatusCode(response.getStatusCodeValue());
        HttpHeaders respHeaders = response.getHeaders();
        for (Map.Entry<String, List<String>> ent : respHeaders.entrySet()) {
            rtnModel.addHeader(ent.getKey(), ent.getValue().get(0));
        }
        rtnModel.setContent(response.getBody());
        return rtnModel;
    }

    /**
     * 发起DELETE请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<Object> requestEntity = getRequestEntity(params,headers);
        return sendRequest(requestEntity, url, HttpMethod.DELETE,queryParams, charset);
    }

    /**
     * 发起删除请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<String> requestEntity = new HttpEntity<>(content, headers);
        return sendRequest(requestEntity, url, HttpMethod.DELETE,queryParams, charset);
    }

    /**
     * 发起PUT请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<Object> requestEntity = getRequestEntity(params,headers);
        return sendRequest(requestEntity, url, HttpMethod.PUT,queryParams, charset);
    }

    /**
     * 发起PUT请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<String> requestEntity = new HttpEntity<>(content, headers);
        return sendRequest(requestEntity, url, HttpMethod.PUT,queryParams, charset);
    }

    /**
     * 发起POST请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<Object> requestEntity = getRequestEntity(params,headers);
        return sendRequest(requestEntity, url, HttpMethod.POST,queryParams, charset);
    }

    /**
     * 发起POST请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<String> requestEntity = new HttpEntity<>(content, headers);
        return sendRequest(requestEntity, url, HttpMethod.POST,queryParams, charset);
    }

    /**
     * 发起GET请求
     * @param url
     * @param reqHeaders
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> params, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<Object> requestEntity = getRequestEntity(new HashMap<>(),headers);
        return sendRequest(requestEntity, url, HttpMethod.GET,params, charset);
    }

    /**
     * 发起GET请求
     * @param url
     * @param reqHeaders
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, String content, String charset) throws Exception {
        HttpHeaders headers = getRequestHeaders(reqHeaders);
        HttpEntity<String> requestEntity = new HttpEntity<>(content, headers);
        return sendRequest(requestEntity, url, HttpMethod.GET,new HashMap<>(), charset);
    }

    /**
     * 发起GET请求
     * @param url
     * @param reqHeaders
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> params) throws Exception {
        HttpRtnModel rtnModel = getFromUrl(url, reqHeaders, params, defaultCharset);
        return rtnModel;
    }

    /**
     * 发起GET请求
     * @param url
     * @param reqHeaders
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, String content) throws Exception {
        HttpRtnModel rtnModel = getFromUrl(url, reqHeaders, content, defaultCharset);
        return rtnModel;
    }

    /**
     * 向服务端发送请求并接收返回的数据。
     *
     * @param url
     * @param reqHeaders cookie数据
     *                   JSESSIONID=eadc003dd;\"
     * @param queryParams
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String, String> queryParams, Map<String, String> params) throws Exception {
        HttpRtnModel rtn = postFromUrl(url, reqHeaders,queryParams, params, defaultCharset);
        return rtn;
    }

    /**
     * 发送请求内容为字符串。
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String, String> queryParams, String content) throws Exception {
        HttpRtnModel rtnModel = postFromUrl(url, reqHeaders,queryParams, content, defaultCharset);
        return rtnModel;
    }

    /**
     * 发起PUT请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, Map<String, String> params) throws Exception {
        HttpRtnModel rtn = putFromUrl(url, reqHeaders,queryParams, params, defaultCharset);
        return rtn;
    }

    /**
     * 发起PUT请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, String content) throws Exception {
        HttpRtnModel rtnModel = putFromUrl(url, reqHeaders,queryParams, content, defaultCharset);
        return rtnModel;
    }

    /**
     * 发起DELETE请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, Map<String, String> params) throws Exception {
        HttpRtnModel rtn = deleteFromUrl(url, reqHeaders,queryParams, params, defaultCharset);
        return rtn;
    }

    /**
     * 发起DELETE请求
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @return
     * @throws Exception
     */
    @Override
    public HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, String content) throws Exception {
        HttpRtnModel rtnModel = deleteFromUrl(url, reqHeaders,queryParams, content, defaultCharset);
        return rtnModel;
    }
}
