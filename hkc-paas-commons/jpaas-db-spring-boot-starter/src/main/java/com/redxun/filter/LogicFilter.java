package com.redxun.filter;

import com.redxun.common.utils.DbLogicDelete;

import javax.servlet.*;
import java.io.IOException;

/**
 * 功能: TODO
 *
 * @author ASUS
 * @date 2022/7/19 18:17
 */
public class LogicFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            //逻辑删除
            boolean logicDel= DbLogicDelete.getLogicDelete();
            DbLogicDelete.setLogicDelete(logicDel);
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            DbLogicDelete.clear();
        }
    }
}
