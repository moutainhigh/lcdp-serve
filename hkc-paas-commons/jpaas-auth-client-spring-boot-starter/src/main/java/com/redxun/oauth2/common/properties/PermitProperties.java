package com.redxun.oauth2.common.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配置需要放权的url白名单
 *
 * @author yjy
 */
@Setter
@Getter
public class PermitProperties {
    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] ENDPOINTS = {
            "/oauth/**",
            "/actuator/**",
            "/*/v2/api-docs",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**"
    };

    /**
     * 设置不用认证的url
     */
    private String[] httpUrls = {};

    /**
     * 设置xss攻击忽略url
     */
    private String[] xssUrls = {};

    /**
     * 忽略认证但url必须带accessToken的地址
     */
    private String[] httpTokenUrls = {};

    public String[] getUrls() {
        if (httpUrls == null || httpUrls.length == 0) {
            return ENDPOINTS;
        }
        List<String> list = new ArrayList<>();
        for (String url : ENDPOINTS) {
            list.add(url);
        }
        for (String url : httpUrls) {
            list.add(url);
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * reqUrl是否是xss忽略URL
     * @param reqUrl
     * @return
     */
    public boolean containsXssUrl(String reqUrl){
        for (String url : xssUrls) {
            Pattern regex = Pattern.compile(url, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE |
                    Pattern.DOTALL | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(reqUrl);
            if(regexMatcher.find()){
                return true;
            }
        }
        return false;
    }

    /**
     * reqUrl是否是httpTokenUrls校验url带token
     * @param reqUrl
     * @return
     */
    public boolean obtainsHttpTokenUrl(String reqUrl){
        for (String url : httpTokenUrls) {
            Pattern regex = Pattern.compile(url, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE |
                    Pattern.DOTALL | Pattern.MULTILINE);
            Matcher regexMatcher = regex.matcher(reqUrl);
            if(regexMatcher.find()){
                return true;
            }
        }
        return false;
    }
}
