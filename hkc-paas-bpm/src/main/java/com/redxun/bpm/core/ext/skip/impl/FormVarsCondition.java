package com.redxun.bpm.core.ext.skip.impl;

import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.ext.skip.ISkipCondition;
import com.redxun.bpm.core.ext.skip.SkipType;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.common.tool.StringUtils;
import org.activiti.engine.RuntimeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 根据条件判断
 * @author ray
 */
@Component
public class FormVarsCondition implements ISkipCondition {

    @Resource
    ProcessScriptEngine processScriptEngine;
    @Resource
    RuntimeService runtimeService;

    @Override
    public SkipType getType() {
        return new SkipType("formVars","根据条件判断");
    }

    @Override
    public boolean canSkip(BpmTask bpmTask, String config) {

        if(StringUtils.isEmpty(config)){
            return  false;
        }
        String actInstId=bpmTask.getActInstId();
        Map<String,Object> vars= runtimeService.getVariables(actInstId);
        Map<String, Object> conextData = ActivitiUtil.getConextData(vars);
        conextData.put("bpmTask",bpmTask);
        Object obj= processScriptEngine.exeScript(config,conextData);
        if(obj instanceof  Boolean && (Boolean)obj){
            return  true;
        }
        return false;
    }
}
