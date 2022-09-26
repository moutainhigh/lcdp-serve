package com.redxun.log.aspect;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Log切面
 */
@Aspect
@Slf4j
public class LogAspect {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 拦截有AuditLog的方法。
     */
    @Pointcut("@annotation(com.redxun.log.annotation.AuditLog)")
    public void allMethod(){}


    private IAuditService auditService;

    public LogAspect( IAuditService auditService) {
        this.auditService = auditService;
    }


    @Around(value = "allMethod()")
    public Object around(ProceedingJoinPoint  joinPoint) throws Throwable {

        Long start=System.currentTimeMillis();
        //执行
        Object rtn= joinPoint.proceed();

        Long durationTime=System.currentTimeMillis() -start;
        //记录日志
        handLog(durationTime,joinPoint);

        return rtn;
    }

    /**
     * 记录日志。
     * @param duration
     * @param joinPoint
     */
    private void handLog(Long duration,ProceedingJoinPoint  joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ip= RequestUtil.getIpAddress(request);
        Audit audit=new Audit();

        String isLog=LogContext.getByKey(Audit.IS_LOG);
        //为NO时不记录日志
        if(MBoolean.NO.name().equals(isLog)){
            return;
        }
        String action=LogContext.getByKey(Audit.ACTION);
        String pkVal=LogContext.getByKey(Audit.PK);
        String detail=LogContext.getByKey(Audit.DETAIL);
        String busType=LogContext.getByKey(Audit.BUS_TYPE);
        String operation=LogContext.getByKey(Audit.OPERATION);

        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();

        Class clazz = joinPoint.getTarget().getClass();
        ClassDefine classDefine= (ClassDefine) clazz.getAnnotation(ClassDefine.class);
        Method method=methodSignature.getMethod();

        AuditLog apiOperation= method.getAnnotation(AuditLog.class);

        IUser user= ContextUtil.getCurrentUser();

        audit.setId(IdGenerator.getIdStr());
        audit.setAppName(applicationName);
        audit.setIp(ip);

        audit.setClassName(clazz.getName());
        if(classDefine!=null) {
            audit.setModule(classDefine.packageName());
            audit.setSubModule(classDefine.title());
        }
        audit.setMethodName(method.getName());

        audit.setAction(action);
        audit.setPkValue(pkVal);
        //时长
        audit.setDuration(duration);

        if(apiOperation!=null){
            //从上下文获取
            if(StringUtils.isNotEmpty(operation)){
                audit.setOperation(operation);
            }
            else{
                audit.setOperation(apiOperation.operation());
            }
        }

        if(user!=null){
            audit.setUserName(user.getFullName());
            audit.setCreateBy(user.getUserId());
            audit.setCreateTime(new Date());
            audit.setCreateDepId(user.getDeptId());
            audit.setTenantId(user.getTenantId());
        }
        audit.setDetail(detail);
        audit.setBusType(busType);
        if(StringUtils.isNotEmpty(busType)) {
            audit.setIsResume(MBoolean.NO.name());
        }
        LogContext.clear();
        auditService.save(audit);
    }

}
