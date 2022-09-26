package com.redxun.bpm.core.service;


import com.alibaba.fastjson.JSONArray;
import com.redxun.bpm.activiti.config.JumpSkipOptions;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.ext.skip.ISkipCondition;
import com.redxun.bpm.core.ext.skip.SkipConditionContext;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * 处理任务跳过。
 */
@Service
@Slf4j
public class BpmTaskSkipService {

    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;


    /**
     * 跳过任务。
     * @param cmd
     */
    public void handSkipTask(IExecutionCmd  cmd )   {
        String checkType=cmd.getCheckType();
        //驳回时不能跳过。
        if(TaskOptionType.BACK.name().equals( checkType) ||
                TaskOptionType.BACK_SPEC.name().equals(checkType) ||
                TaskOptionType.BACK_TO_STARTOR.name().equals(checkType)){
            return;
        }

        List<BpmTask> tasks= cmd.getTasks();
        if(BeanUtil.isEmpty(tasks)){
            return;
        }

        Iterator<BpmTask> taskIt=tasks.iterator();

        while(taskIt.hasNext()) {
            BpmTask task=taskIt.next();
            JumpSkipOptions jumpSkipOptions=getJumpSkipOptions(task);
            if(jumpSkipOptions==null){
                continue;
            }
            boolean skip= handOneTaskSkip( jumpSkipOptions, task);
            if(skip){
                taskIt.remove();
            }
        }
    }

    /**
     * 获取跳转选项。
     * @param task
     * @return
     */
    private JumpSkipOptions getJumpSkipOptions(BpmTask task){
        UserTaskConfig userTaskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(task.getActDefId(),task.getKey());
        JumpSkipOptions jumpSkipOptions= userTaskConfig.getJumpSkipOptions();
        if(BeanUtil.isNotEmpty(jumpSkipOptions)){
            String type=jumpSkipOptions.getType();
            if(StringUtils.isNotEmpty(type)){
                return jumpSkipOptions;
            }
        }
        ProcessConfig processConfig= bpmDefService.getProcessConfig(task.getActDefId());
        jumpSkipOptions=processConfig.getJumpSkipOptions();
        if(BeanUtil.isNotEmpty(jumpSkipOptions)){
            String type=jumpSkipOptions.getType();
            if(StringUtils.isNotEmpty(type)){
                return jumpSkipOptions;
            }
        }
        return  null;
    }

    private boolean handOneTaskSkip(JumpSkipOptions jumpSkipOptions,BpmTask task)  {
        String types=jumpSkipOptions.getType();
        JSONArray ary=JSONArray.parseArray(types);
        boolean skip=false;

        for(int i=0;i<ary.size();i++){
            String type=ary.getString(i);
            ISkipCondition skipCondition = SkipConditionContext.getConditionByType(type);
            boolean rtn= skipCondition.canSkip(task,jumpSkipOptions.getExpression());
            if(!rtn){
                continue;
            }

            skipTask(task);
            skip=true;
            break;
        }
        return skip;
    }




    /**
     * 跳过任务。
     * @param bpmTask
     */
    private void skipTask(BpmTask bpmTask)  {
        AbstractExecutionCmd taskCmd= (AbstractExecutionCmd)ProcessHandleUtil.getProcessCmd();


        ProcessNextCmd cmd= new ProcessNextCmd();
        cmd.setTaskId(bpmTask.getTaskId());
        cmd.setOpinion("");
        cmd.setCheckType(TaskOptionType.SKIP.name());
        cmd.setFormData(taskCmd.getFormData());
        cmd.setBoDataMap(taskCmd.getBoDataMap());

        //设置到上下文。
        ProcessHandleUtil.setProcessCmd(cmd);
        //跳过则删除任务的线程变量
        bpmTaskService.completeTask(cmd,true);
        //IUser iUser = ContextUtil.getCurrentUser();
        //提交审批历史。
        //bpmCheckHistoryService.createHistory(bpmTask,TaskOptionType.SKIP.name(),"","",iUser.getUserId(),iUser.getDeptId(),taskCmd.getOpFiles());
    }

}
