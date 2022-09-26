package com.redxun.bpm.core.ext.skip;

import com.redxun.bpm.core.entity.BpmTask;

/**
 * 判断流程是否能够跳过。
 */
public interface ISkipCondition {

    /**
     * 类型
     * @return
     */
    SkipType getType();

    /**
     * 是否可以跳转。
     * @param bpmTask
     * @return
     */
    boolean canSkip(BpmTask bpmTask, String config);

}
