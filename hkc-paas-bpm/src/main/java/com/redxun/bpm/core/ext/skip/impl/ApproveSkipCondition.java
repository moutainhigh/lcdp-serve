package com.redxun.bpm.core.ext.skip.impl;

import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.TaskOptionType;
import com.redxun.bpm.core.ext.skip.ISkipCondition;
import com.redxun.bpm.core.ext.skip.SkipType;
import com.redxun.bpm.core.service.BpmCheckHistoryServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 当前用户曾审批
 * @author ray
 */
@Component
public class ApproveSkipCondition implements ISkipCondition {

    @Resource
    BpmCheckHistoryServiceImpl checkHistoryService;

    @Override
    public SkipType getType() {
        return new SkipType("approve","当前用户曾审批");
    }

    @Override
    public boolean canSkip(BpmTask bpmTask, String config) {
        String instId= bpmTask.getInstId();
        String assignee=bpmTask.getAssignee();
        if(StringUtils.isEmpty(assignee)){
            return  false;
        }
        List<BpmCheckHistory> checkHistorys = checkHistoryService.getByInstUser(instId, assignee);
        if(BeanUtil.isNotEmpty(checkHistorys)){
            BpmCheckHistory checkHistory=checkHistorys.get(0);
            if(TaskOptionType.AGREE.name().equals( checkHistory.getJumpType())){
                return true;
            }
        }
        return false;
    }
}
