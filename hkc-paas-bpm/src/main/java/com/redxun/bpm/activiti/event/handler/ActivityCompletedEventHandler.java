package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.entity.ProcessNextCmd;
import com.redxun.bpm.core.service.BpmCheckHistoryServiceImpl;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiActivityEvent;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * Activity活动完成事件处理器
 * @author
 */
@Slf4j
public class ActivityCompletedEventHandler implements EventHandler<ActivitiActivityEvent> {

    @Override
    public EventType getEventType() {
        return EventType.ACTIVITY_COMPLETED;
    }

    @Override
    public void handle(ActivitiActivityEvent eventEntity) {
        log.info("handle========================" + eventEntity.toString());
        //更新期执行路径
        updateRuPath(eventEntity);
        //创建审批历史
        createBpmCheckHistory(eventEntity);
    }

    /**
     * 创建操作日志
     * @param eventEntity
     */
    public void createBpmCheckHistory(ActivitiActivityEvent eventEntity){
        if("receiveTask".equals(eventEntity.getActivityType())) {
            IExecutionCmd cmd = ProcessHandleUtil.getProcessCmd();
            BpmCheckHistoryServiceImpl bpmCheckHistoryService = SpringUtil.getBean(BpmCheckHistoryServiceImpl.class);
            BpmInstServiceImpl bpmInstService = SpringUtil.getBean(BpmInstServiceImpl.class);
            BpmInst bpmInst = bpmInstService.getByActInstId(eventEntity.getProcessInstanceId());
            bpmCheckHistoryService.createHistory(bpmInst, eventEntity.getActivityId(), eventEntity.getActivityName(), cmd.getCheckType(), "", cmd.getOpinion());
        }
    }

    /**
     * 更新执行路径的信息
     * @param eventEntity
     */
    public void updateRuPath(ActivitiActivityEvent eventEntity){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String instId=cmd.getInstId();
        String nodeId=eventEntity.getActivityId();

        BpmRuPathServiceImpl bpmRuPathService=SpringUtil.getBean(BpmRuPathServiceImpl.class);
        BpmRuPath ruPath=bpmRuPathService.getLatestByInstIdNodeId(instId,nodeId);
        if(ruPath==null) {return;}

        ruPath.setAssignee(ContextUtil.getCurrentUserId());
        // 设置代理人，表示代理谁来执行
        if(cmd instanceof ProcessNextCmd){
            ruPath.setToUserId(((ProcessNextCmd)cmd).getAgentToUserId());
        }
        ruPath.setEndTime(new Date());
        //noinspection AlibabaUndefineMagicConstant
        if(cmd!=null && BpmConst. USER_TASK.equals(ruPath.getNodeType())){
            if(StringUtils.isNotEmpty(cmd.getCheckType())){
                ruPath.setJumpType(cmd.getCheckType());
            }
        }
        //更新其数据
        bpmRuPathService.update(ruPath);

        cmd.setPreNodeId(nodeId);
    }
}
