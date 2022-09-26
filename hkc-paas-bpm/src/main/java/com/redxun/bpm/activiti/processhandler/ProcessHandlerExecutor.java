package com.redxun.bpm.activiti.processhandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 流程处理器执行器
 * @author csx
 */
@Service
public class ProcessHandlerExecutor {

    /**
     * 执行结束处理器。
     * @param processConfig
     */
    public void handEndHandler(ProcessConfig processConfig,BpmInst bpmInst){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String endHandler= processConfig.getProcessEndHandler();
        if(StringUtils.isEmpty(endHandler)){
            return;
        }

        ProcessHandler handler= SpringUtil.getBean(endHandler);
        if(handler==null){
            return;
        }
        handler.endHandle(bpmInst);
    }

    /**
     * 在流程启动时执行前置处理器。
     * @param processConfig
     */
    public void handStartBeforeHandler(ProcessConfig processConfig,BpmInst bpmInst){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String startPreHandler= processConfig.getProcessStartPreHandler();
        if(StringUtils.isEmpty(startPreHandler)){
            return;
        }
        ProcessHandler handler= SpringUtil.getBean(startPreHandler);
        if(handler==null){
            return;
        }
        handler.processStartPreHandle(cmd,bpmInst);
    }

    /**
     * 处理流程启动之前的处理器。
     * @param processConfig
     * @param bpmInst
     */
    public void handStartAfterHandler(ProcessConfig processConfig,BpmInst bpmInst){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String startAfterHandler= processConfig.getProcessStartAfterHandler();
        if(StringUtils.isEmpty(startAfterHandler)){
            return;
        }
        ProcessHandler handler= SpringUtil.getBean(startAfterHandler);
        if(handler==null){
            return;
        }
        handler.processStartAfterHandle(processConfig,cmd,bpmInst);
    }


    /**
     * 处理任务开始处理器。
     * @param taskConfig
     * @param bpmTask
     * @param busKey
     */
    public void handTaskBeforeHandler(UserTaskConfig taskConfig, BpmTask bpmTask,String busKey){
        String taskPreHandler = taskConfig.getTaskPreHandler();
        IExecutionCmd  cmd=  ProcessHandleUtil.getProcessCmd();

        if(StringUtils.isEmpty(taskPreHandler)){
            return;
        }
        ProcessHandler handler= SpringUtil.getBean(taskPreHandler);
        if(handler==null){
            return;
        }
        handler.taskPreHandle(cmd,bpmTask,busKey);
    }

    /**
     * 处理任务完成处理器。
     * @param taskConfig
     * @param nodeId
     * @param busKey
     */
    public void handTaskAfterHandler(UserTaskConfig taskConfig, String nodeId,String busKey){
        String taskAfterHandler = taskConfig.getTaskAfterHandler();

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        if(StringUtils.isEmpty(taskAfterHandler)){
            return;
        }
        ProcessHandler  handler= SpringUtil.getBean(taskAfterHandler);
        if(handler==null){
            return;
        }
        handler.taskAfterHandle(cmd,nodeId,busKey);
    }

    /**
     * 获取handler实现。
     * @return
     */
    public JSONArray getHandlers(){
        Collection<ProcessHandler> beans = SpringUtil.getBeans(ProcessHandler.class);
        JSONArray jsonArray=new JSONArray();
        for(ProcessHandler o:beans){
            JSONObject obj=new JSONObject();
            String name=StringUtils.makeFirstLetterLowerCase(o.getClass().getSimpleName());
            obj.put("label",o.getName());
            obj.put("value",name);
            jsonArray.add(obj);
        }
        return jsonArray;
    }

}
