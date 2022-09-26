package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.bpm.feign.SystemClient;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Web请求事件处理器
 */
@Component
public class WebReqEventHandler  implements IEventHandler {
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    SystemClient systemClient;

    @Override
    public EventHanderType getType() {
        return new EventHanderType("webreq","WEB调用请求");
    }

    @Override
    public void handEvent(BaseEventMessage message) {
        EventConfig eventSetting= message.getEventConfig();
        JSONObject config=eventSetting.getConfig();
        if(BeanUtil.isEmpty(config)) {
            return;
        }
        String script=config.getString("script");
        String alias = config.getJSONObject("webDef").getString("alias");
        String headerData = config.getString("header");
        String paramsData=config.getString("params");
        Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
        Map<String,Object> bpmParam = message.getVars();
        params.put("bpmParam",bpmParam);
        params.put("actInstId",message.getActInstId());
        IExecutionCmd cmd = ProcessHandleUtil.getProcessCmd();
        Map<String, Object> map;
        if(cmd!=null){
            map = json2Map(cmd.getBoDataMap());
        }else{
            map = json2Map(message.getFormData());
        }
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
        try {
            JSONObject startParam = new JSONObject();
            startParam.put("alias", alias);
            startParam.put("header", headerData);
            startParam.put("params", paramsData);
            startParam.put("paramData", JSONObject.toJSONString(params));
            JsonResult jr = systemClient.start(startParam);
            if (StringUtils.isEmpty(script)) {
                return;
            }
            params.put("result", jr.getData());
            try {
                groovyEngine.executeScripts(script, params);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("执行调用Web服务后的脚本：（" + script + ",参数：" + params.toString() + "），出错原因：" + e.getMessage());
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("执行Web请求（"+"）调用出现异常："+e.getMessage());
        }
    }

    /**
     * JSON转为Map
     *
     * @param jsonObject
     * @return Map<String, Object>
     * @throws
     * @since 1.0.0
     */
    private  static Map<String, Object> json2Map(JSONObject jsonObject) {
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
    private static Map<String, Object> parseFormData(String formAlias, String json) {
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
