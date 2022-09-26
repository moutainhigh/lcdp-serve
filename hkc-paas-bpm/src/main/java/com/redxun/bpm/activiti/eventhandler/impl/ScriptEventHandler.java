package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.util.SysUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 脚本事件处理器
 */
@Component
public class ScriptEventHandler implements IEventHandler {
    @Resource
    ProcessScriptEngine processScriptEngine;


    @Override
    public EventHanderType getType() {
        return new EventHanderType("script","脚本");
    }

    @Override
    public void handEvent(BaseEventMessage message) {
        EventConfig eventSetting= message.getEventConfig();
        JSONObject config=eventSetting.getConfig();
        if(BeanUtil.isEmpty(config)) {
            return;
        }
        String script = config.getString("script");
        if(StringUtils.isEmpty(script)){
            return;
        }
        Map<String,Object> vars=message.getVars();
        //获取上下文变量数据。
        Map<String,Object> contextData= ActivitiUtil.getConextData(vars);
        //替换常量
        script= SysUtil.replaceConstant(script);

        processScriptEngine.exeScript(script,contextData);

    }

}
