package com.redxun.ureport.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysUserUtil;
import com.redxun.ureport.enums.HttpCodeEnum;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author shenzhongwen
 * @create 2020-8-12 17:12
 */
public class UreportInterceptor implements HandlerInterceptor {
    private  static final String AUTH_CODE="Authorization";
    private  static final String UREPORT_DESIGNER="/ureport";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = null;
        try {
            handlerMethod = (HandlerMethod) handler;
            StringBuffer url = request.getRequestURL();
            String requstUrl =url.toString();
            if(!requstUrl.contains(UREPORT_DESIGNER)){
                return true;
            }
        } catch (Exception e) {
            response.setStatus(HttpCodeEnum.NOT_FOUND.getCode());
            return false;
        }

        String authorization = request.getHeader(AUTH_CODE);
        if (StringUtils.isEmpty(authorization)) {
            response.getWriter().print(JSONObject.toJSONString("The resource requires Authorization is null"));
            return false;
        }
        String[] tokens =authorization.split("Bearer ");
        authorization=tokens[1];
        JPaasUser loginUser = SysUserUtil.getLoginUser(authorization);
        if(BeanUtil.isEmpty(loginUser)){
            response.getWriter().print(JSONObject.toJSONString("未授权，请重新登录"));
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println("================= jin ru  after ===============");
    }
}
