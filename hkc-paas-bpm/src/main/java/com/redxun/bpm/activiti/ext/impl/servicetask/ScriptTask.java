package com.redxun.bpm.activiti.ext.impl.servicetask;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.ext.IServiceTask;
import com.redxun.bpm.activiti.ext.ServiceType;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.util.SysUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 脚本服务任务
 */
@Component
public class ScriptTask implements IServiceTask {
    @Resource
    ProcessScriptEngine processScriptEngine;

    @Override
    public ServiceType getType() {
        return new ServiceType("脚本任务", "script");
    }


    @Override
    public void handle(DelegateExecution execution, String setting) {
        JSONObject jsonObject = JSONObject.parseObject(setting);
        String script = jsonObject.getString("script");
        if (StringUtils.isEmpty(script)) {
            return;
        }

        Map<String, Object> contextData = ActivitiUtil.getContextData(execution);
        script=SysUtil.replaceConstant(script);
        processScriptEngine.exeScript(script,contextData);
    }


}
