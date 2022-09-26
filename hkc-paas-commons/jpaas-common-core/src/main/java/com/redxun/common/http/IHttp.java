package com.redxun.common.http;

import java.util.Map;

/**
 * resful接口实现
 *
 * @author hujun
 */
public interface IHttp {
    /**
     * get请求 键值对
     *
     * @param url
     * @param reqHeaders
     * @return
     * @throws Exception
     */
    HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> params) throws Exception;

    /**
     * get请求 body内容
     *
     * @param url
     * @param reqHeaders
     * @param content
     * @return
     * @throws Exception
     */
    HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, String content) throws Exception;

    /**
     * post请求，键值对
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @return
     * @throws Exception
     */
    HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params) throws Exception;

    /**
     * post请求，body内容
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @return
     * @throws Exception
     */
    HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content) throws Exception;
    /**
     * put请求，键值对
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @return
     * @throws Exception
     */
    HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params) throws Exception;

    /**
     * put请求，body内容
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @return
     * @throws Exception
     */
    HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content) throws Exception;
    /**
     * delete请求，键值对
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param params
     * @return
     * @throws Exception
     */
    HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params) throws Exception;

    /**
     * delete请求，body内容
     *
     * @param url
     * @param reqHeaders
     * @param queryParams
     * @param content
     * @return
     * @throws Exception
     */
    HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content) throws Exception;

}
