package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.DateUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FormBoAttr;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MonthAttrHandler extends  BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-month";
    }

    @Override
    public String getDescription() {
        return "月份控件";
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        String name=attr.getName();
        Boolean defaultnow= setting.getBoolean("defaultnow");

        if(defaultnow){
            JSONObject formJson =attr.getFormJson();
            String format= formJson.getString("format");
            String val= DateUtils.dateTimeNow(format);
            jsonObject.put(name,val);
        }
    }



    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("format");
        fields.add("ctl");
        fields.add("script");
        fields.add("compare");

        this.setFormJson(field,jsonObject,fields);
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("defaultnow");
        this.setDataJson(field,jsonObject,fields);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_VARCHAR);
        field.setLength(20);
    }
}
