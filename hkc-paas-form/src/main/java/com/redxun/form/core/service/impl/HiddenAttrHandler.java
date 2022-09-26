package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class HiddenAttrHandler extends BaseAttrHandler {

    @Resource
    GroovyEngine groovyEngine;

    @Override
    public String getPluginName() {
        return "rx-form-hidden";
    }

    @Override
    public String getDescription() {
        return "隐藏域";
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        String settings=setting.getString("setting");
        String defaultval=setting.getString("defaultval");
        if(StringUtils.isNotEmpty(defaultval)){
            jsonObject.put(attr.getName(),defaultval);
        }else  if(StringUtils.isNotEmpty(settings)){
            Map<String,Object> params=new HashMap<>();
            String val= (String) groovyEngine.executeScripts(settings,params);
            jsonObject.put(attr.getName(),val);
        }
    }


    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        JSONObject json=new JSONObject();
        json.put("setting",jsonObject.getString("script"));
        json.put("defaultval",jsonObject.getString("defaultval"));

        field.setDataJson(json);
    }


    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }
}
