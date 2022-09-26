package com.redxun.common.ribbon.filter;


import com.redxun.common.constant.ConfigConstants;
import com.redxun.common.context.LbIsolationContextHolder;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 负载均衡隔离规则过滤器
 *
 * @author yjy
 * @date 2019/9/15
 */
@ConditionalOnClass(Filter.class)
public class LbIsolationFilter extends OncePerRequestFilter {
    @Value("${" + ConfigConstants.CONFIG_RIBBON_ISOLATION_ENABLED + ":false}")
    private boolean enableIsolation;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !enableIsolation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String developer = request.getHeader(CommonConstant.DEVELOPER);
            if(StringUtils.isNotEmpty(developer)){
                LbIsolationContextHolder.setDeveloper(developer);
            }
            filterChain.doFilter(request, response);
        } finally {
            LbIsolationContextHolder.clear();
        }
    }
}
