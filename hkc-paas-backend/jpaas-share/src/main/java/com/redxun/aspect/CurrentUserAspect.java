package com.redxun.aspect;

import com.redxun.api.org.IOrgService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 功能: 增加切面解决API调用的时候每次都需要调用获取用户的问题。
 *
 * @author ASUS
 * @date 2022/4/28 10:44
 */
@Aspect
@Slf4j
@Configuration
public class CurrentUserAspect {


    /**
     * 拦截有AuditLog的方法。
     */
    @Pointcut("@annotation(com.redxun.annotation.CurrentUser)")
    public void allMethod(){}

    @Resource
    UserClient userClient;

    @Around(value = "allMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String account=request.getHeader("account");
        String userId=request.getHeader("userId");

        if(StringUtils.isEmpty(account) && StringUtils.isEmpty(userId)){
            return JsonResult.Fail("用户账号和用户ID必须设置一个！");
        }
        JPaasUser user=null;
        if(StringUtils.isNotEmpty(account)){
            user=userClient.findByUsername(account);
            if(BeanUtil.isEmpty(user)){
                return JsonResult.Fail("用户不存在，账号传入错误！");
            }
        }
        else {
            user=userClient.getUserById(userId);
            if(BeanUtil.isEmpty(user)){
                return JsonResult.Fail("用户不存在,用户ID传入错误！");
            }
        }
        ContextUtil.setCurrentUser(user);
        //执行
        Object rtn= joinPoint.proceed();

        ContextUtil.clearUser();
        return rtn;
    }


}
