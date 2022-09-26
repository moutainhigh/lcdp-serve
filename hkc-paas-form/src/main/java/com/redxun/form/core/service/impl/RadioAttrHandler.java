package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RadioAttrHandler extends BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-radio";
    }

    @Override
    public String getDescription() {
        return "单选";
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("datasource");
        fields.add("valmode");
        fields.add("mode");
        this.setFormJson(field,jsonObject,fields);
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("defaultval");
        this.setDataJson(field,jsonObject,fields);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_VARCHAR);
        Integer length=jsonObject.getInteger("length");
        field.setLength(length);
    }

    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable) {
        JSONObject fieldJson = super.parseMetadata(boAttr, isTable);
        String from="custom";
        String extJson = boAttr.getExtJson();
        JSONObject dataSource=new JSONObject();
        if(StringUtils.isNotEmpty(extJson)){
            JSONObject jsonObject = JSONObject.parseObject(extJson);
            if(BeanUtil.isNotEmpty(jsonObject.getJSONObject("dataSource"))){
                dataSource = jsonObject.getJSONObject("dataSource");
                from = dataSource.getString("from");
                dataSource.put("from",from);
                String options = dataSource.getString("options");
                if(StringUtils.isNotEmpty(options)){
                    dataSource.put("options", JSONArray.parseArray(options));
                }
            }
        }
        fieldJson.put("datasource", dataSource);
        fieldJson.put("mode","default");
        return fieldJson;
    }
}
