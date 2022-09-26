package com.redxun.common.filter;


import com.redxun.common.model.JPaasUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SysUserUtil;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class WebContextFilter implements  Filter  {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            //默认清理数据源
            HttpServletRequest request=(HttpServletRequest)servletRequest;
            JPaasUser jPaasUser= SysUserUtil.getLoginUser();
            if(jPaasUser!=null){
                ContextUtil.setCurrentUser(jPaasUser);
            }

            filterChain.doFilter(request, servletResponse);
        } finally {
            ContextUtil.clearUser();

        }
    }
}
