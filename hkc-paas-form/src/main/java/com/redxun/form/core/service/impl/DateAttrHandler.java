package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.core.entity.ValueResult;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class DateAttrHandler extends  BaseAttrHandler {

    @Override
    public String getPluginName() {
        return "rx-date";
    }

    @Override
    public String getDescription() {
        return "日期控件";
    }


    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

        String name=attr.getName();
        Boolean defaultnow= setting.getBoolean("defaultnow");

        if(defaultnow){
            JSONObject formJson =attr.getFormJson();
            String format= formJson.getString("format");
            String val= DateUtils.dateTimeNow(DateUtils.switchFormat(format));
            jsonObject.put(name,val);
        }
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

        List<String> fields=new ArrayList<>();
        fields.add("format");
        fields.add("showtime");
        fields.add("today");
        fields.add("relate");
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
        field.setDataType(Column.COLUMN_TYPE_DATE);
        field.setLength(20);
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String fieldName=attr.getFieldName().toUpperCase();
        Object val=null;
        JSONObject formJson=attr.getFormJson();
        if(rowData.containsKey(fieldName)){
            val=rowData.get(fieldName);
            //如果数据为空直接显示为空。
            if(val==null){
                return ValueResult.exist("");
            }
            String format=DateUtils.DATE_FORMAT_YMD;
            if(BeanUtil.isNotEmpty(formJson)){
                format=formJson.getString("format");
            }

            Date date = null;
            if(val instanceof LocalDateTime){
                date = DateUtils.localDateTimeToDate((LocalDateTime) val);
            }else{
                date = (Date) val;
            }

            String rtn= DateUtils.parseDateToStr( DateUtils.switchFormat(format),date);
            return  ValueResult.exist(rtn);
        } else{
            return  ValueResult.noExist();
        }

    }

    @Override
    public void removeFields(FormBoAttr attr, Map<String, Object> rowData, boolean external) {
        super.removeFields(attr, rowData, external);
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        JSONObject formJson=attr.getFormJson();
        String format=DateUtils.DATE_FORMAT_YMD;
        String data=json.getString(attr.getName());
        if(BeanUtil.isNotEmpty(formJson)){
            format=formJson.getString("format");
        }
        FieldEntity entity=new FieldEntity();
        entity.setName(attr.getName());
        entity.setFieldName(attr.getFieldName());
        String s = DateUtils.switchFormat(format);
        if(StringUtils.isNotEmpty(data)){
            entity.setValue(DateUtils.parseDate(data,s));
        }
        List<FieldEntity> list=new ArrayList<>();
        list.add(entity);
        return list;
    }

}
