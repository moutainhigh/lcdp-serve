package com.redxun.bpm.core.ext.skip.impl;

import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.ext.skip.ISkipCondition;
import com.redxun.bpm.core.ext.skip.SkipType;
import org.springframework.stereotype.Component;

/**
 * 不需要条件直接跳过
 * @author ray
 */
@Component
public class NoConditionSkipCondition  implements ISkipCondition {

    @Override
    public SkipType getType() {
        return new SkipType("noCondition","不需要条件直接跳过");
    }

    @Override
    public boolean canSkip(BpmTask bpmTask, String config) {
        return true;
    }
}
