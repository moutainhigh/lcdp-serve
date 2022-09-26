//package com.redxun.db.config;
//
//import org.springframework.aop.Advisor;
//import org.springframework.aop.aspectj.AspectJExpressionPointcut;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.interceptor.*;
//import org.springframework.util.Assert;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author ray
// */
//
//public class GlobalTransactionConfig implements InitializingBean {
//
//    /**
//     * 超时时间
//     */
//    private int txMethodTimeOut;
//    /**
//     * 切点表达式
//     */
//    private String pointcut;
//
//    private String readMethod="*";
//
//    private String writeMethod="add*,save*,insert*,upd*,del*,remove*,persist*,do*,hand*";
//
//
//
//
//
//    public GlobalTransactionConfig() {
//    }
//
//
//    @Autowired
//    private PlatformTransactionManager platformTransactionManager;
//
//    @Bean
//    public TransactionInterceptor txAdvice() {
//        /* 配置事务管理规则,声明具备管理事务方法名.这里使用public void addTransactionalMethod(String methodName, TransactionAttribute attr)*/
//        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
//        Map<String, TransactionAttribute> nameMap = new HashMap<String, TransactionAttribute>();
//        /*只读事物、不做更新删除等*/
//        /*事务管理规则*/
//        RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();
//        /*设置当前事务是否为只读事务，true为只读*/
//        readOnlyRule.setReadOnly(true);
//        readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        //require事务。
//        RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();
//        /*抛出异常后执行切点回滚*/
//        requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
//        /*PROPAGATION_REQUIRED:事务隔离性为1，若当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。 */
//        requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        /*设置事务失效时间，超过10秒,可根据hytrix，则回滚事务*/
//        requireRule.setTimeout(txMethodTimeOut);
//
//        String[] aryRead=readMethod.split(",");
//        for(String read:aryRead){
//            nameMap.put(read,readOnlyRule);
//        }
//
//        String[] aryWrite=writeMethod.split(",");
//        for(String write:aryRead){
//            nameMap.put(write,requireRule);
//        }
//        source.setNameMap(nameMap);
//        return new TransactionInterceptor(platformTransactionManager, source);
//
//    }
//
//    /**
//     * 设置切面=切点pointcut+通知TxAdvice
//     *
//     * @return Advisor
//     */
//    @Bean
//    public Advisor txAdviceAdvisor() {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression(this.pointcut);
//        return new DefaultPointcutAdvisor(pointcut, txAdvice());
//
//    }
//
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Assert.state(this.txMethodTimeOut >= -1, "txMethodTimeOut must be set");
//        Assert.hasText(this.pointcut, "pointCut must be set");
//    }
//
//    public int getTxMethodTimeOut() {
//        return txMethodTimeOut;
//    }
//
//    public void setTxMethodTimeOut(int txMethodTimeOut) {
//        this.txMethodTimeOut = txMethodTimeOut;
//    }
//
//    public String getPointcut() {
//        return pointcut;
//    }
//
//    public void setPointcut(String pointcut) {
//        this.pointcut = pointcut;
//    }
//}
