package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RateAttrHandler extends BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-rate";
    }

    @Override
    public String getDescription() {
        return "评论";
    }


    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        String val=setting.getString("defaultval");
        jsonObject.put(attr.getName(),val);
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("allowHalf");
        fields.add("iconType");
        fields.add("count");
        setFormJson(field,jsonObject,fields);
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("defaultval");
        setDataJson(field,jsonObject,fields);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_NUMBER);
        field.setLength(5);
        field.setDecimalLength(1);
    }
}
