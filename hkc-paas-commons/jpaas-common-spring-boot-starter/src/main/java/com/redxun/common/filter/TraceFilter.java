package com.redxun.common.filter;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 日志链路追踪过滤器
 *
 * @author yjy
 * @date 2019/9/15
 */
public class TraceFilter implements Filter   {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request=(HttpServletRequest)servletRequest;
            String traceId = request.getHeader(CommonConstant.TRACE_ID_HEADER);
            if (StringUtils.isNotEmpty(traceId)) {
                MDC.put(CommonConstant.LOG_TRACE_ID, traceId);
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
