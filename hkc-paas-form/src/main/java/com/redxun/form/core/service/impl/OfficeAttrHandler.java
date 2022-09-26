package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.script.GroovyEngine;
import com.redxun.constvar.ConstVarContext;
import com.redxun.feign.PublicClient;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OfficeAttrHandler extends BaseAttrHandler {

    @Resource
    GroovyEngine groovyEngine;

    @Override
    public String getPluginName() {
        return "rx-office";
    }

    @Override
    public String getDescription() {
        return "office控件";
    }

    @Resource
    PublicClient publicClient;

    @Resource
    private ConstVarContext constVarContext;

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        JSONObject dataJson= attr.getDataJson();
        //dataJson
        String from=setting.getString("from");
        String settings=setting.getString("setting");

        String val="";
        jsonObject.put(attr.getName(),val);
    }


    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        String from=jsonObject.getString("from");
        JSONObject json=new JSONObject();
        json.put("from",from);

        field.setDataJson(json);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable){
        JSONObject fieldJson=super.parseMetadata(boAttr,isTable);
        fieldJson.put("from","input");
        return fieldJson;
    }
}
