package com.redxun.bpm.script;

import com.redxun.bpm.script.cls.ActionScriptNode;
import com.redxun.bpm.script.cls.ClassScriptType;
import com.redxun.bpm.script.cls.IScript;
import com.redxun.bpm.script.cls.ProcessVarNode;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.utils.SpringUtil;
import com.redxun.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程脚本引擎
 */
@Component
@Slf4j
public class ProcessScriptEngine {

    @Resource
    GroovyEngine groovyEngine;

    /**
     * 基于流程运行上下文变量执行
     * @param content
     * @param contextData
     * @return
     */
    public Object exeScript(String content, Map<String,Object> contextData){
        Map<String,Object> model=new HashMap<>();
        //1.加入流程变量
        model.put(ProcessVarNode.VAR_PRE,contextData.get(ProcessVarNode.VAR_PRE));
        //2.加入页面提交的审批动态，单据等变量
        model.put(ActionScriptNode.VAR_PRE,contextData.get(ActionScriptNode.VAR_PRE));

        //3.加入单据变量
        model.putAll(contextData);
        //4. 加入函数API变量
        Collection<IScript> beans = SpringUtil.getBeans(IScript.class);
        for(IScript bean:beans){
            ClassScriptType scriptType=  bean.getClass().getAnnotation(ClassScriptType.class);
            if(scriptType!=null){
                model.put(scriptType.type(),bean);
            }
        }

        //添加logger调用。
        model.put("logger",log);
        //替换常量
        content= SysUtil.replaceConstant(content);
        //执行脚本函数
        Object result=groovyEngine.executeScripts(content,model);

        return result;
    }
}
