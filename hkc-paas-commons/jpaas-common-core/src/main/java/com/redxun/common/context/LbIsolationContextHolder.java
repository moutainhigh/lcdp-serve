package com.redxun.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 负载均衡策略Holder
 *
 * @author yjy
 * @date 2019/9/2
 */
public class LbIsolationContextHolder {
    /**
     * 开发者上下文线程
     */
    private static final ThreadLocal<String> DEVELOPER_CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置开发者
     * @param developer
     */
    public static void setDeveloper(String developer) {
        DEVELOPER_CONTEXT.set(developer);
    }

    /**
     * 获取开发者
     * @return
     */
    public static String getDeveloper() {
        return DEVELOPER_CONTEXT.get();
    }

    public static void clear() {
        DEVELOPER_CONTEXT.remove();
    }
}
