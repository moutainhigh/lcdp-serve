package com.redxun.bpm.core.ext.skip.impl;

import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.TaskOptionType;
import com.redxun.bpm.core.ext.skip.ISkipCondition;
import com.redxun.bpm.core.ext.skip.SkipType;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 当前用户为发起直接跳过
 * @author ray
 */
@Component
public class StarterSkipCondition implements ISkipCondition {

    @Resource
    BpmInstServiceImpl bpmInstService;
    @Override
    public SkipType getType() {
        return new SkipType("starter","当前用户发起");
    }
    
    @Override
    public boolean canSkip(BpmTask bpmTask, String config) {
        String instId= bpmTask.getInstId();
        String assignee=bpmTask.getAssignee();
        if(StringUtils.isEmpty(instId) || StringUtils.isEmpty(assignee)){
            return  false;
        }

        //当前流程发起人是当前审批人，允许通过
        BpmInst bpmInst =  bpmInstService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInst) && assignee.equals(bpmInst.getCreateBy())){
            return true;
        }
        return false;
    }
}
