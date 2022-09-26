package com.redxun.ureport.filter;

import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysUserUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author chengdongyi
 * @description: 请求参数过滤器
 * @date 2019/7/1 10:33
 */
public class ParamsFilter implements Filter {

    public static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    public static final String SEPARATOR = ",";
    private Set<String> excludesUrls;
    private  static final String AUTH_CODE="Authorization";
    private  static final String UREPORT_DESIGNER="/ureport";
    private  static final String ACCESS_TOKEN="accessToken";


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String param = filterConfig.getInitParameter(PARAM_NAME_EXCLUSIONS);
        if (param != null && param.trim().length() != 0) {
            this.excludesUrls = new HashSet(Arrays.asList(param.split(SEPARATOR)));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestUri = httpRequest.getRequestURI();

        if (this.isExclusion(requestUri)) {
            // 不过滤
            filterChain.doFilter(request, response);
        } else {
            boolean isForward =isForward(httpRequest);
            if (isForward) {
                // 不过滤
                filterChain.doFilter(request, response);
            } else {
                // 过滤,拦截到错误页面
                httpRequest.getRequestDispatcher("/error").forward(request,response);
            }
        }
    }

    private boolean isForward(HttpServletRequest request){
        boolean isForward=true;
        try {
            StringBuffer url = request.getRequestURL();
            String requstUrl =url.toString();
            if(requstUrl.contains(UREPORT_DESIGNER)){
                Map<String, String[]> requestMap = request.getParameterMap();
                if(BeanUtil.isEmpty(requestMap) ||!requestMap.containsKey(ACCESS_TOKEN)){
                    isForward=false;
                }else {
                    String[] authorizations =requestMap.get(ACCESS_TOKEN);
                    isForward=checkToken(authorizations[0]);
                }
            }
        } catch (Exception e) {
            return false;
        }

        String authorization = request.getHeader(AUTH_CODE);
        if (StringUtils.isEmpty(authorization) && !isForward) {
            return false;
        }
        if (StringUtils.isNotEmpty(authorization)) {
            String[] tokens =authorization.split("Bearer ");
            isForward=checkToken(tokens[1]);
        }

        return isForward;
    }

    private boolean checkToken(String authorization){
        JPaasUser loginUser = SysUserUtil.getLoginUser(authorization);
        if(BeanUtil.isEmpty(loginUser)){
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {

    }

    public boolean isExclusion(String requeristUri) {
        if (this.excludesUrls == null) {
            return false;
        }
        for (String url : this.excludesUrls) {
            if (requeristUri.contains(url)) {
                return true;
            }
        }
        return false;
    }

}
