package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CheckboxAttrHandler extends  BaseAttrHandler {

    @Override
    public String getPluginName() {
        return "rx-checkbox";
    }

    @Override
    public String getDescription() {
        return "复选框";
    }



    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        String val=setting.getString("defaultval");
        jsonObject.put(attr.getName(),val);
    }



    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("defaultval");
        setDataJson(field,jsonObject,fields);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_VARCHAR);
        field.setLength(20);
    }
}
