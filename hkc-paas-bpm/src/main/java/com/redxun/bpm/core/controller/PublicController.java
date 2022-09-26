package com.redxun.bpm.core.controller;


import com.alibaba.fastjson.JSONArray;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.EventHandlerContext;
import com.redxun.bpm.activiti.ext.ServiceConext;
import com.redxun.bpm.activiti.ext.ServiceType;
import com.redxun.bpm.activiti.processhandler.ProcessHandlerExecutor;
import com.redxun.bpm.activiti.user.ExecutorCalcContext;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.core.entity.BpmInstVarsType;
import com.redxun.bpm.core.ext.FormStatus.FormStatusContext;
import com.redxun.bpm.core.ext.FormStatus.FormStatusType;
import com.redxun.bpm.core.ext.skip.SkipConditionContext;
import com.redxun.bpm.core.ext.skip.SkipType;
import com.redxun.constvar.ConstVarContext;
import com.redxun.constvar.ConstVarType;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bpm/core/public")
@Api(tags = "任务处理相关人")
public class PublicController {

    @Resource
    private ProcessHandlerExecutor processHandlerExecutor;

    @GetMapping("/getEventHandler")
    public List<EventHanderType> getEventHandler(){
        List<EventHanderType> list = EventHandlerContext.getEventHandlerTypes();
        return list;
    }

    @GetMapping("/getSkipContions")
    public List<SkipType> getSkipContions() {
        List<SkipType> list = SkipConditionContext.getTypes();
        return list;
    }

    @GetMapping("/getServiceTasks")
    public List<ServiceType> getServiceTasks() {
        List<ServiceType> list = ServiceConext.getServiceTypes();
        return list;
    }

    @GetMapping("/getVariables")
    public List<BpmInstVarsType> getVariables() {
        List<BpmInstVarsType> list = new ArrayList<>();
        list.add(new BpmInstVarsType(BpmInstVars.BUS_KEY));
        list.add(new BpmInstVarsType(BpmInstVars.INST_ID));
        list.add(new BpmInstVarsType(BpmInstVars.PROCESS_SUBJECT));
        list.add(new BpmInstVarsType(BpmInstVars.DEF_ID));
        list.add(new BpmInstVarsType(BpmInstVars.BILL_NO));
        list.add(new BpmInstVarsType(BpmInstVars.BILL_TYPE));
        list.add(new BpmInstVarsType(BpmInstVars.START_USER_ID));
        list.add(new BpmInstVarsType(BpmInstVars.START_DEP_ID));
        return list;
    }


    @GetMapping("/getExecutorTypes")
    public List<ExecutorType> getExecutorTypes() {
        List<ExecutorType> list = ExecutorCalcContext.getTypes();
        return list;
    }

    @GetMapping("/getConstVars")
    public List<ConstVarType> getConstVars() {
        List<ConstVarType> list = ConstVarContext.getTypes();
        return list;
    }

    @GetMapping("/getBpmHandlers")
    public JSONArray getBpmHandlers() {
        JSONArray list = processHandlerExecutor.getHandlers();
        return list;
    }

    @GetMapping("/getFormStatus")
    public List<FormStatusType> getFormStatus() {
        List<FormStatusType> list = FormStatusContext.getTypes();
        return list;
    }


}
