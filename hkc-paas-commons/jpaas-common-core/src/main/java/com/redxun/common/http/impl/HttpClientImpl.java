package com.redxun.common.http.impl;

import com.redxun.common.http.HttpDeleteWithEntity;
import com.redxun.common.http.HttpGetWithEntity;
import com.redxun.common.http.HttpRtnModel;
import com.redxun.common.http.IHttp;
import com.redxun.common.tool.BeanUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 基于HttpClient实现
 *
 * @author hujun
 */
@Component
public class HttpClientImpl implements IHttp {

    private String defaultCharset = "utf-8";

    private SSLContext _sslContext;

    /**
     * 创建忽略SSL认证的Context
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv2");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                    throws java.security.cert.CertificateException {
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    /**
     * 创建默认的SSL客户端
     * @return
     */
    private CloseableHttpClient createSSLClientDefault() {
        try {
            if (_sslContext == null) {
                _sslContext = createIgnoreVerifySSL();
            }
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(_sslContext);

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    /**
     * 执行http请求。
     * @param httpReq 请求对象
     * @param reqHeaders 请求头
     * @param charset 编码
     * @return
     * @throws Exception
     */
    private HttpRtnModel sendRequest(HttpRequestBase httpReq, Map<String, String> reqHeaders, String charset) throws Exception {
        if (BeanUtil.isNotEmpty(reqHeaders)) {
            for (Map.Entry<String, String> ent : reqHeaders.entrySet()) {
                httpReq.addHeader(ent.getKey(), ent.getValue());
            }
        }
        HttpRtnModel rtnModel = new HttpRtnModel();
        CloseableHttpClient httpclient = createSSLClientDefault();
        CloseableHttpResponse response = httpclient.execute(httpReq);
        InputStream instream = null;
        try {
            rtnModel.setStatusCode(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                rtnModel.addHeader(header.getName(), header.getValue());
            }
            if (entity != null) {
                instream = entity.getContent();
                StringWriter writer = new StringWriter();
                IOUtils.copy(instream, writer, charset);
                rtnModel.setContent(writer.toString());
            }
        } finally {
            if (instream != null) {
                instream.close();
            }
            response.close();
        }
        return rtnModel;
    }

    /**
     * 发起Delete Method的请求
     * @param url URL地址
     * @param reqHeaders 请求头
     * @param queryParams 查询参数
     * @param params 提交参数
     * @param charset 编码
     * @return
     * @throws Exception
     */
    private HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, Map<String, String> params, String charset) throws Exception {
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
        HttpDeleteWithEntity httpDelete = new HttpDeleteWithEntity(uriBuilder.build());
        if (BeanUtil.isNotEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Iterator<String> keyIt = params.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String val = params.get(key);
                nvps.add(new BasicNameValuePair(key, val));
            }
            UrlEncodedFormEntity urlBodyEnt = new UrlEncodedFormEntity(nvps, charset);
            httpDelete.setEntity(urlBodyEnt);
        }
        HttpRtnModel rtnModel = sendRequest(httpDelete, reqHeaders, charset);

        return rtnModel;
    }

    /**
     * 发起Delete Method的请求
     * @param url URL地址
     * @param reqHeaders 请求头
     * @param queryParams 查询参数
     * @param content 内容（String )
     * @param charset 编码
     * @return
     * @throws Exception
     */
    private HttpRtnModel deleteFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content, String charset) throws Exception {
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
        HttpDeleteWithEntity httpDelete = new HttpDeleteWithEntity(uriBuilder.build());
        httpDelete.setEntity(new StringEntity(content, charset));

        HttpRtnModel rtnModel = sendRequest(httpDelete, reqHeaders, charset);
        return rtnModel;
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
    private HttpRtnModel putFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, Map<String, String> params, String charset) throws Exception {
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
        HttpPut httpPut = new HttpPut(uriBuilder.build());
        if (BeanUtil.isNotEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Iterator<String> keyIt = params.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String val = params.get(key);
                nvps.add(new BasicNameValuePair(key, val));
            }
            UrlEncodedFormEntity urlBodyEnt = new UrlEncodedFormEntity(nvps, charset);
            httpPut.setEntity(urlBodyEnt);
        }
        HttpRtnModel rtnModel = sendRequest(httpPut, reqHeaders, charset);

        return rtnModel;
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
        HttpPut httpPut = new HttpPut(uriBuilder.build());
        httpPut.setEntity(new StringEntity(content, charset));

        HttpRtnModel rtnModel = sendRequest(httpPut, reqHeaders, charset);
        return rtnModel;
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
    private HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> queryParams, Map<String, String> params, String charset) throws Exception {
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
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        if (BeanUtil.isNotEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Iterator<String> keyIt = params.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String val = params.get(key);
                nvps.add(new BasicNameValuePair(key, val));
            }
            UrlEncodedFormEntity urlBodyEnt = new UrlEncodedFormEntity(nvps, charset);
            httpPost.setEntity(urlBodyEnt);
        }
        HttpRtnModel rtnModel = sendRequest(httpPost, reqHeaders, charset);

        return rtnModel;
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
        HttpPost httpPost = new HttpPost(uriBuilder.build());
        httpPost.setEntity(new StringEntity(content, charset));

        HttpRtnModel rtnModel = sendRequest(httpPost, reqHeaders, charset);
        return rtnModel;
    }

    /**
     * 发起Get请求
     * @param url
     * @param reqHeaders
     * @param params
     * @param charset
     * @return
     * @throws Exception
     */
    private HttpRtnModel getFromUrl(String url, Map<String, String> reqHeaders, Map<String, String> params, String charset) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (BeanUtil.isNotEmpty(params)) {
            List<NameValuePair> list = new LinkedList<>();
            Iterator<String> keyIt = params.keySet().iterator();
            while (keyIt.hasNext()) {
                String key = keyIt.next();
                String val = params.get(key);
                list.add(new BasicNameValuePair(key, val));
            }
            uriBuilder.setParameters(list);
        }
        HttpGet httpget = new HttpGet(uriBuilder.build());
        HttpRtnModel rtnModel = sendRequest(httpget, reqHeaders, charset);

        return rtnModel;
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
        HttpGetWithEntity httpget = new HttpGetWithEntity(url);
        httpget.setEntity(new StringEntity(content, charset));
        HttpRtnModel rtnModel = sendRequest(httpget, reqHeaders, charset);
        return rtnModel;
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
    public HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, Map<String, String> params) throws Exception {
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
    public HttpRtnModel postFromUrl(String url, Map<String, String> reqHeaders,Map<String,String> queryParams, String content) throws Exception {
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
        return this.putFromUrl(url,reqHeaders,queryParams,params,defaultCharset);
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
        return this.putFromUrl(url,reqHeaders,queryParams,defaultCharset);
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
        return this.deleteFromUrl(url,reqHeaders,queryParams,params);
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
        return this.deleteFromUrl(url,reqHeaders,queryParams,content,defaultCharset);
    }

}
