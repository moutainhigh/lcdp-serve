package com.redxun.common.filter;

import com.redxun.common.utils.MessageUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 业务消息过滤器。
 */
public class MessageFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            //默认清理数据源
            HttpServletRequest request=(HttpServletRequest)servletRequest;
            MessageUtil.init();
            filterChain.doFilter(request, servletResponse);
        } finally {
            MessageUtil.clear();

        }
    }
}
