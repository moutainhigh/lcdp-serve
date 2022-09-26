package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class TextareaAttrHandler extends BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-textarea";
    }

    @Override
    public String getDescription() {
        return "多行文本框";
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }



    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        String type=jsonObject.getString("type");
        field.setType(type);
        if(Column.COLUMN_TYPE_VARCHAR.equals(type)){
            Integer length=jsonObject.getInteger("length");
            field.setLength(length);
        }
    }

    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable) {
        JSONObject fieldJson = super.parseMetadata(boAttr, isTable);
        return fieldJson;
    }
}
