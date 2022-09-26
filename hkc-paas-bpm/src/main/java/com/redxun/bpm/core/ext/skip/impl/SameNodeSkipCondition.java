package com.redxun.bpm.core.ext.skip.impl;

import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.ext.skip.ISkipCondition;
import com.redxun.bpm.core.ext.skip.SkipType;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import org.springframework.stereotype.Component;

/**
 * 相邻节点相同用户审批
 * @author ray
 */
@Component
public class SameNodeSkipCondition implements ISkipCondition {

    @Override
    public SkipType getType() {
        return new SkipType("sameNode","相邻节点相同用户审批");
    }

    @Override
    public boolean canSkip(BpmTask bpmTask, String config) {

        String assignee=bpmTask.getAssignee();
        if(StringUtils.isEmpty(assignee)){
            return false;
        }
        String curUserId= ContextUtil.getCurrentUserId();

        Boolean rtn=curUserId.equals(assignee);
        return rtn;
    }
}
