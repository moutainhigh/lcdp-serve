//package com.redxun.db.config;
//
//import org.springframework.context.annotation.Import;
//
//import java.lang.annotation.*;
//
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Import(TxDefinitionRegistrar.class)
//public @interface EnableTxManager {
//
//    /**
//     * 切点,默认是medbanks 下所有service
//     */
//    String[] pointcut() default {"* com.redxun..service..*(..)"};
//    /**
//     * 超时时间
//     */
//    int txMethodTimeOut() default 10;
//}
