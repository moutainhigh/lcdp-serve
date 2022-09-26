package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.feign.SystemClient;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.constvar.ConstVarContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 第三方接口事件处理器
 */
@Component
public class ApiInterfaceEventHandler implements IEventHandler {
    /**
     * 常量
     */
    private static final String S_CONSTANT = "constantVar";
    /**
     * 脚本
     */
    private static final String S_SCRIPT = "script";
    /**
     * 表单数据
     */
    private static final String S_FORM_PARAM = "formParam";
    /**
     * 流程变量数据
     */
    private static final String S_BPM_PARAM = "bpmParam";
    /**
     * 固定值
     */
    private static final String S_FIXED_VAR = "fixedVar";
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    SystemClient systemClient;
    @Resource
    ConstVarContext constVarContext;
    @Override
    public EventHanderType getType() {
        return new EventHanderType("apiInterface","第三方接口");
    }

    @Override
    public void handEvent(BaseEventMessage message) {
        EventConfig eventSetting= message.getEventConfig();
        JSONObject config=eventSetting.getConfig();
        if(BeanUtil.isEmpty(config)) {
            return;
        }
        String script=config.getString("script");
        String interfaceId=config.getString("interface");
        String apiDataType=config.getString("apiDataType");
        JSONObject paramsData=config.getJSONObject("params");

        Map<String,Object> params = new HashMap<>(SysConstant.INIT_CAPACITY_16);
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
            JSONObject interfaceParams=new JSONObject();
            JSONArray pathParamsMapping=paramsData.getJSONArray("pathParamsMapping");
            interfaceParams.put("pathParams",getParamsValue(pathParamsMapping,params,false));
            JSONArray headersMapping=paramsData.getJSONArray("headersMapping");
            interfaceParams.put("headers",getParamsValue(headersMapping,params,false));
            JSONArray queryMapping=paramsData.getJSONArray("queryMapping");
            interfaceParams.put("querys",getParamsValue(queryMapping,params,false));
            JSONArray bodyMapping=paramsData.getJSONArray("bodyMapping");
            interfaceParams.put("bodys",getParamsValue(bodyMapping,params,"json".equals(apiDataType)));

            JsonResult jr = systemClient.executeApi(interfaceId,interfaceParams);
            if (StringUtils.isEmpty(script)) {
                return;
            }
            params.put("result", jr.getData());
            try {
                groovyEngine.executeScripts(script, params);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("执行调用API接口后的脚本：（" + script + ",参数：" + params.toString() + "），出错原因：" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("执行API接口（"+"）调用出现异常："+e.getMessage());
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

    /**
     * 获取请求参数内容
     * @param paramArray
     * @param params
     * @param isJson
     * @return
     */
    private Object getParamsValue(JSONArray paramArray,Map<String,Object> params,boolean isJson) {
        if (isJson) {
            return getParamsValueJson(paramArray.getJSONObject(0),params);
        }
        JSONObject json=new JSONObject();
        for (Object obj : paramArray) {
            JSONObject paramJson=(JSONObject) obj;

            json.put(paramJson.getString("paramName"),getValue(params, paramJson));
        }
        return json;
    }

    private Object getParamsValueJson(JSONObject root,Map<String,Object> params){
        if ("object".equals(root.getString("paramType"))) {
            JSONObject obj=new JSONObject();
            for(int i=0;i<root.getJSONArray("children").size();i++){
                JSONObject child=root.getJSONArray("children").getJSONObject(i);
                obj.put(child.getString("paramName"),getParamsValueJson(child,params));
            }
            return obj;
        } else if ("array".equals(root.getString("paramType"))) {
            JSONArray array=new JSONArray();
            array.add(getParamsValueJson(root.getJSONArray("children").getJSONObject(0),params));
            return array;
        } else {
            return getValue(params,root);
        }
    }

    /**
     * 获取参数的值。
     *
     * @param params
     * @param jsonObject
     * @return
     */
    private Object getValue(Map<String, Object> params, JSONObject jsonObject) {
        String valSource = jsonObject.getJSONObject("valueSource").getString("key");
        String valueDef = (String) jsonObject.get("valueDef");
        Object val = null;
        //表单数据
        if (S_FORM_PARAM.equals(valSource)) {
            Map<String, Object> map = (Map<String, Object>) params.get("formParam");
            if (BeanUtil.isNotEmpty(map)) {
                val = map.get(valueDef);
            }
        }
        //流程变量数据
        else if (S_BPM_PARAM.equals(valSource)) {
            Map<String, Object> map = (Map<String, Object>) params.get("bpmParam");
            val = map.get(valueDef);
        }
        // 固定值
        else if (S_FIXED_VAR.equals(valSource)) {
            val = valueDef;
        }
        // 脚本
        else if (S_SCRIPT.equals(valSource)) {
            val = groovyEngine.executeScripts(valueDef, params);
        }
        //常量
        else if (S_CONSTANT.equals(valSource)) {
            val = constVarContext.getValByKey(valueDef, params);
        }
        //url参数
        else {
            val = jsonObject.get("value");
        }
        return val;
    }
}
