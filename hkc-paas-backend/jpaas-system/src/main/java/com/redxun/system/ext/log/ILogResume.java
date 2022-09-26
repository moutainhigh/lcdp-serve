package com.redxun.system.ext.log;

/**
 * 处理日志恢复接口类
 */
public interface ILogResume {

    /**
     * 返回类型。
     * @return
     */
    String getType();

    /**
     * 恢复操作
     * @param action
     * @param detail
     * @param extParams
     */
    void resume(String action,String detail,String extParams);
}
