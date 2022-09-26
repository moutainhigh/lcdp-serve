package com.redxun.datasource;

import javax.servlet.*;
import java.io.IOException;

/**
 * 在拦截器中实现数据源的清空处理
 */
public class DataSourceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try{
            filterChain.doFilter(servletRequest,servletResponse);
        }
        finally {
            DataSourceContextHolder.clearDataSource();
        }
    }
}
