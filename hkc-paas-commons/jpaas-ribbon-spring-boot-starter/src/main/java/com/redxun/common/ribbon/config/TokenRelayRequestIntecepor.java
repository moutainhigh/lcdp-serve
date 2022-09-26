package com.redxun.common.ribbon.config;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

/**
 * 在 使用feign调用时，需要将 当前请求的 Authorization 转发到调用的请求头中。
 * 这样 被调用方就可以根据这个 Authorization 获取到当前人的身份信息。
 * @author ray
 */
public class TokenRelayRequestIntecepor  implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        IUser user=ContextUtil.getCurrentUser();
        if(user!=null) {
            String userStr=JSONObject.toJSONString(user);
            try {
                userStr=URLEncoder.encode(userStr,"UTF-8");
                requestTemplate.header(SecurityConstants.CUR_USER, userStr);
            }catch (Exception e){
            }
        }
        if(attributes==null){
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String auth=request.getHeader(SecurityConstants.Authorization);
        // 1. 将token传递
        if (StringUtils.isNotBlank(auth)) {
            requestTemplate.header(SecurityConstants.Authorization, auth);
        }
        // 2.传递TRACEID。
        String traceId = MDC.get(CommonConstant.LOG_TRACE_ID);
        if (StringUtils.isNotBlank(traceId)) {
            requestTemplate.header(CommonConstant.TRACE_ID_HEADER, traceId);
        }
        // 3. 传递DEVELOPER。
        String developer=request.getHeader(CommonConstant.DEVELOPER);
        if (StringUtils.isNotBlank(developer)) {
            requestTemplate.header(CommonConstant.DEVELOPER, developer);
        }

        String url = requestTemplate.path();
        if (url.startsWith("//")) {
            url = "http:" + url;
            requestTemplate.target(url);
            requestTemplate.uri("");
        }
    }
}
