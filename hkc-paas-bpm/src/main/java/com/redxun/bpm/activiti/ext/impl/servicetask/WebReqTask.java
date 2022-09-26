package com.redxun.bpm.activiti.ext.impl.servicetask;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.ext.IServiceTask;
import com.redxun.bpm.activiti.ext.ServiceType;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.bpm.feign.SystemClient;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.mq.BpmInputOutput;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * web请求任务。
 */
@Component
public class WebReqTask implements IServiceTask {
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    SystemClient systemClient;
    @Resource
    BpmInputOutput inputOutput;

    @Override
    public ServiceType getType() {
        return new ServiceType("WEB请求任务", "webReqTask");
    }

    @Override
    public void handle(DelegateExecution execution, String setting) {
        JSONObject jsonObject = JSONObject.parseObject(setting);
        String script = jsonObject.getString("script");
        String alias = jsonObject.getJSONObject("webDef").getString("alias");
        String headerData = jsonObject.getString("header");
        String paramsData = jsonObject.getString("params");
        String async = jsonObject.getString("async");
        IExecutionCmd cmd = ProcessHandleUtil.getProcessCmd();
        Map<String, Object> params = new HashMap<>();
        params.put("bpmParam", execution.getVariables());
        Map<String, Object> map = json2Map(cmd.getBoDataMap());
        if (BeanUtil.isNotEmpty(map)) {
            Iterator<String> it = map.keySet().iterator();
            Map<String, Object> tmpMap = new HashMap<>(map.size());
            while (it.hasNext()) {
                String formAlias = it.next();
                String json = JSONObject.toJSONString(map.get(formAlias));
                tmpMap.putAll(parseFormData(formAlias, json));
            }
            params.putAll(tmpMap);
            params.put("formParam", tmpMap);
        }
        params.put("actInstId", execution.getProcessInstanceId());
        params.put("cmd", cmd);
        //异步调用
        if (MBoolean.TRUE_LOWER.val.equals(async)) {
            // TODO 异步处理代码
            BaseEventMessage baseEventMessage=new BaseEventMessage();
            EventConfig eventConfig=new EventConfig();
            eventConfig.setConfig(jsonObject);
            eventConfig.setHanderType("webreq");
            baseEventMessage.setEventConfig(eventConfig);
            baseEventMessage.setFormData(cmd.getBoDataMap());
            baseEventMessage.setVars(execution.getVariables());
            baseEventMessage.setActInstId(execution.getProcessInstanceId());
            sendToMq(baseEventMessage);
        } else {//同步调用
            JSONObject startParam = new JSONObject();
            startParam.put("alias", alias);
            startParam.put("header", headerData);
            startParam.put("params", paramsData);
            startParam.put("paramData", JSONObject.toJSONString(params));
            JsonResult jr = systemClient.start(startParam);
            if (StringUtils.isEmpty(script)) {
                return;
            }
            params.put("execution", execution);
            params.put("result", jr.getData());
            //流程变量
            params.putAll(execution.getVariables());
            groovyEngine.executeScripts(script, params);
        }
    }

    private void sendToMq(BaseEventMessage eventMessage){
        inputOutput.eventOutput().send(MessageBuilder.withPayload(eventMessage).build());
    }

    /**
     * JSON转为Map
     *
     * @param jsonObject
     * @return Map<String, Object>
     * @throws
     * @since 1.0.0
     */
    public static Map<String, Object> json2Map(JSONObject jsonObject) {
        Map<String, Object> dataMap = new HashMap<>(jsonObject.size());
        Iterator<String> its = jsonObject.keySet().iterator();
        while (its.hasNext()) {
            String key = its.next();
            Object val = jsonObject.get(key);
            dataMap.put(key, val);
        }
        return dataMap;
    }

    /**
     * 解析表单数据
     *
     * @param formAlias
     * @param json
     * @return
     */
    public static Map<String, Object> parseFormData(String formAlias, String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> dataMap = new HashMap<>(jsonObject.size());
        Iterator<String> its = jsonObject.keySet().iterator();
        while (its.hasNext()) {
            String key = its.next();
            Object val = jsonObject.get(key);
            dataMap.put(formAlias + "_" + key, val);
        }
        return dataMap;
    }
}
