package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NumberAttrHandler extends BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-number";
    }

    @Override
    public String getDescription() {
        return "数字控件";
    }


    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        String val=setting.getString("defaultval");
        if(StringUtils.isEmpty(val)){
            jsonObject.put(attr.getName(),0);
        }
        else {
            if(val.indexOf(".")==-1) {
                jsonObject.put(attr.getName(), Integer.parseInt(val));
            }
            else{
                jsonObject.put(attr.getName(), Double.parseDouble(val));
            }
        }

    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("min");
        fields.add("max");
        fields.add("step");
        fields.add("format");
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
        String length=jsonObject.getString("length");
        if (StringUtils.isEmpty(length)) {
            return;
        }
        String[] aryLen=length.split(",");
        field.setLength(Integer.parseInt( aryLen[0]));
        field.setDataType(Column.COLUMN_TYPE_NUMBER);
        if(aryLen.length>1){
            field.setDecimalLength(Integer.parseInt(aryLen[1]));
        }else {
            field.setDecimalLength(0);
        }
    }
    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable) {
        JSONObject fieldJson = super.parseMetadata(boAttr, isTable);
        fieldJson.put("format", new JSONObject());
        return fieldJson;
    }
}
