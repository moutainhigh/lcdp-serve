package com.redxun.datasource;

import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 启用分布式事务拦截。
 * 在需要分布式事务的 添加注解 GlobalTrans 开启事务。
 * 拦截
 */
//@Aspect
//@Component
@Slf4j
public class DbTransAspect {

    String[] aryMethod=new String[]{"upd","do","remove","del","upd","save","add","persist","hand"};

    /**
     * 在切片中增加事务的统一管理
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(* com.redxun.*.*.service..*(..))")
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        boolean needTrans=needTransaction(proceedingJoinPoint);
        if(!needTrans){
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        }
        Object obj = null;
        try {
            GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
            MethodSignature methodSignature= (MethodSignature) proceedingJoinPoint.getSignature();
            log.debug( methodSignature.getMethod().getName() +"---------" + RootContext.getXID() +"----------------------");

            tx.begin(30000, "global-trans");
            //可以加参数
            obj = proceedingJoinPoint.proceed();

            GlobalTransactionContext.reload(RootContext.getXID()).commit();


        } catch (Throwable throwable) {
            throwable.printStackTrace();
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();

            throw   throwable;
        }

        return obj;
    }



    /**
     * 判断方法上是否有GlobalTrans 签名事务。
     * @param point
     * @return
     */
    private boolean needTransaction(JoinPoint point){
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
//        GlobalTrans trans= method.getAnnotation(GlobalTrans.class);
        String methodSign=method.getName().toLowerCase();
        for(String m:aryMethod){
            if(methodSign.startsWith(m)){
                return  true;
            }
        }
        return  false;


    }

}