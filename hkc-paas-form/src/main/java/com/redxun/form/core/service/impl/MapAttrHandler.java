package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.entity.ValueResult;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MapAttrHandler extends BaseAttrHandler {


    @Override
    public String getPluginName() {
        return "rx-map";
    }

    @Override
    public String getDescription() {
        return "地图控件";
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        List<Column> columns=new ArrayList<>();
        attr.setIsSingle(1);
        Column column= TableUtil.getColumnByAttr(attr,false);
        columns.add(column);

        return columns;
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        List< FieldEntity> list=new ArrayList<>();
        if(!json.containsKey(attr.getName())){
            return list;
        }

        String value="";
        JSONObject valJson=json.getJSONObject(attr.getName());
        if(valJson!=null){
            value=valJson.getString("value");
        }

        FieldEntity entity=new FieldEntity();
        entity.setFieldName(attr.getFieldName());
        entity.setName(attr.getName());
        entity.setValue(value);

        list.add(entity);
        return list;
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String label="";
        String value="";
        String fieldName=attr.getFieldName().toUpperCase();
        value = (String) rowData.get(fieldName);
        if(StringUtils.isNotEmpty(value)){
            value=value.trim();
            JSONObject jo = JSONObject.parseObject(value);
            label = (String)jo.get("address");
        }

        if(StringUtils.isNotEmpty(label)){
            label=label.trim();
        }

        String str = null;
        if(StringUtils.isEmpty(value)){
            str="{\"label\":\""  + label +"\",\"value\":" +"\"\"}";
        }else{
            str="{\"label\":\""  + label +"\",\"value\":"+value +"}";
        }


        return  ValueResult.exist(str);

    }


    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable) {
        JSONObject fieldJson = super.parseMetadata(boAttr, isTable);

        return fieldJson;
    }
}
