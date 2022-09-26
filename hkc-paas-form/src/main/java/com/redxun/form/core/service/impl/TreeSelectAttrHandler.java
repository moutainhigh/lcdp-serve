package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TreeSelectAttrHandler extends  BaseAttrHandler {
    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("datasource");
        fields.add("valmode");
        setFormJson(field,jsonObject,fields);
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

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
        JSONObject datasource=new JSONObject();
        datasource.put("from","");
        fieldJson.put("datasource", datasource);
        fieldJson.put("mode","default");
        return fieldJson;
    }

    @Override
    public String getPluginName() {
        return "rx-tree-select";
    }

    @Override
    public String getDescription() {
        return "下拉树控件";
    }
}
