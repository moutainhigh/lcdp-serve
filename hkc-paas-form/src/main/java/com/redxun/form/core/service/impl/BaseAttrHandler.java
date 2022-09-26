package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.model.Column;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.entity.ValueResult;
import com.redxun.form.core.service.IAttrHandler;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class BaseAttrHandler implements IAttrHandler {

    protected abstract void setInitData(FormBoAttr attr,JSONObject setting,JSONObject jsonObject);

    @Override
    public void getInitData(FormBoAttr attr,JSONObject jsonData) {
        JSONObject settingJson= attr.getDataJson();
        if(BeanUtil.isEmpty(settingJson)){
            if(Column.COLUMN_TYPE_NUMBER.equals(attr.getDataType()) || Column.COLUMN_TYPE_INT.equals(attr.getDataType())){
                jsonData.put(attr.getName(),0);
            }
            else {
                jsonData.put(attr.getName(),"");
            }
            return;
        }

        setInitData(attr,settingJson,jsonData);
        //重新设置json数据
        if(!jsonData.containsKey(attr.getName())){
            if(Column.COLUMN_TYPE_NUMBER.equals(attr.getDataType()) || Column.COLUMN_TYPE_INT.equals(attr.getDataType())){
                jsonData.put(attr.getName(),0);
            }
            else {
                jsonData.put(attr.getName(),"");
            }

        }
    }

    /**
     * 解析表单配置。
     * @param field
     * @param el
     * @param jsonObject
     */
    protected abstract void parseFormSetting(FormBoAttr field,Element el,JSONObject jsonObject);

    /**
     * 解析数据配置。
     * @param field
     * @param el
     * @param jsonObject
     */
    protected abstract void parseDataSetting(FormBoAttr field,Element el,JSONObject jsonObject);

    /**
     * 解析控件属性，这个方法会设置BO字段的属性
     * @param field         字段属性
     * @param el            控件的HTML
     * @param jsonObject    控件的元数据
     */
    protected abstract void parseAttrSetting(FormBoAttr field,Element el,JSONObject jsonObject);

    @Override
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable){
        JSONObject fieldJson=JSONObject.parseObject("{\"comment\":\""+boAttr.getComment()+"\",\"name\":\""+boAttr.getName()+"\"}");
        fieldJson.put("type",boAttr.getDataType());
        if("number".equals(boAttr.getDataType())){
            fieldJson.put("length",boAttr.getLength()+","+boAttr.getDecimalLength());
        }else {
            if(!"date".equals(boAttr.getDataType()) && !"clob".equals(boAttr.getDataType())){
                fieldJson.put("length",boAttr.getLength()+"");
            }
        }
        //控件类型
        String ctlType=boAttr.getControl();
        if(StringUtils.isEmpty(ctlType)){
            ctlType=getPluginName();
        }
        fieldJson.put("ctltype",ctlType);
        fieldJson.put("valmode",boAttr.getIsSingle()==1?"single":"double");
        if(BeanUtil.isNotEmpty(boAttr.getIsNotNull()) && boAttr.getIsNotNull()==1){
            fieldJson.put("required",true);
        }else {
            fieldJson.put("required",false);
        }
        return fieldJson;
    }

    @Override
    public FormBoAttr parse( Element el, JSONObject jsonObject) {
        FormBoAttr attr=new FormBoAttr();
        //控件类型
        attr.setControl(el.attr("ctltype"));
        attr.setComment(jsonObject.getString("comment").trim());
        attr.setName(jsonObject.getString("name").trim());
        //如果有数据类型设置
        if(jsonObject.containsKey("type")){
            String type=jsonObject.getString("type");
            attr.setDataType(type);
            if(Column.COLUMN_TYPE_VARCHAR.equals(type)){
                attr.setLength(jsonObject.getInteger("length"));
                attr.setDecimalLength(0);
            }
            else if(Column.COLUMN_TYPE_NUMBER.equals(type)){
                String length=jsonObject.getString("length");
                String[] aryLen=length.split(",");
                attr.setLength(Integer.parseInt(aryLen[0]));
                attr.setDecimalLength(Integer.parseInt(aryLen[1]));
            } else if(Column.COLUMN_TYPE_INT.equals(type)){
                String length=jsonObject.getString("length");
                String[] aryLen=length.split(",");
                attr.setLength(Integer.parseInt(aryLen[0]));
                if(aryLen.length>1){
                    attr.setDecimalLength(Integer.parseInt(aryLen[1]));
                }else {
                    attr.setDecimalLength(0);
                }
            }
        }
        //extjson
        JSONObject extJson=new JSONObject();
        putJson(extJson,jsonObject,"required");
        putJson(extJson,jsonObject,"placeholder");
        putJson(extJson,jsonObject,"width");

        //是否单值
        if(jsonObject.containsKey("valmode")){
            String valmode=jsonObject.getString("valmode");
            attr.setIsSingle("single".equals(valmode)?1:0);
        }

        attr.setFormJson(extJson);

        parseFormSetting(attr,el,jsonObject);
        parseDataSetting( attr, el, jsonObject);
        parseAttrSetting( attr, el, jsonObject);


        return attr;
    }

    protected void  putJson(JSONObject target,JSONObject source,String name){
        if(source.containsKey(name)){
            target.put(name,source.getString(name));
        }
    }

    protected void setFormJson(FormBoAttr field, JSONObject jsonObject, List<String> list){
        if(BeanUtil.isEmpty(list)){
            return;
        }
        JSONObject json=getJson(jsonObject,list);
        field.setFormJson(json);
    }

    protected void setDataJson(FormBoAttr field, JSONObject jsonObject, List<String> list){
        if(BeanUtil.isEmpty(list)){
            return;
        }
        JSONObject json=getJson(jsonObject,list);
        field.setDataJson(json);
    }

    private JSONObject getJson(JSONObject jsonObject, List<String> list){
        JSONObject json=new JSONObject();
        for(String key :list){
            putJson(json,jsonObject,key);
        }
        return json;
    }

    private JSONObject getJson(String extJson,JSONObject jsonObject, List<String> list){
        JSONObject json=new JSONObject();
        if(StringUtils.isNotEmpty(extJson)){
            json=JSONObject.parseObject(extJson);
        }
        for(String key :list){
            putJson(json,jsonObject,key);
        }
        return json;
    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        List<Column> columns=new ArrayList<>();
        Column column= TableUtil. getColumnByAttr(attr,false);
        columns.add(column);
        if(!attr.single()){
            //获取双值字段绑定的关联字段
            String ref=TableUtil.getRefField(attr);
            if(StringUtils.isEmpty(ref)){
                Column columnComplex= TableUtil.getColumnByAttr(attr,true);
                columns.add(columnComplex);
            }
        }
        return columns;
    }

    private Object getValByDataType(FormBoAttr attr,Object val){
        if(val==null){
            return null;
        }
        String valStr=String.valueOf(val);
        String type=attr.getDataType();
        switch (type){
            case Column.COLUMN_TYPE_VARCHAR:
            case Column.COLUMN_TYPE_CLOB:
                return StringUtils.isEmpty(valStr.trim())?null:valStr.trim();
            case Column.COLUMN_TYPE_NUMBER:
                Object  valNum=handNumber(attr,valStr);
                return  valNum;
            case Column.COLUMN_TYPE_DATE:
                Object obj= handDate(attr,valStr);
                return  obj;
            case Column.COLUMN_TYPE_INT:
                Object  valInt=handNumber(attr,valStr);
                return  valInt;
            default:
                return val;
        }
    }
    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String fieldName=attr.getFieldName().toUpperCase();
        Object val=null;
        if(attr.single()){
            if(rowData.containsKey(fieldName)){
                val=rowData.get(fieldName);
                val=getValByDataType(attr,val);
                return  ValueResult.exist(val);
            }
            else{
                return  ValueResult.noExist();
            }
        }
        else{
            ValueResult result=getComplexVal(rowData,attr,isExternal);
            return result;
        }

    }



    /**
     * 复合字段返回JSON 数据对象。
     * @param rowData
     * @param attr
     * @param external
     * @return
     */
    private ValueResult getComplexVal(Map<String,Object> rowData,FormBoAttr attr,boolean external){
        String label="";
        String value="";
        boolean exist=false;
        String fieldName=attr.getFieldName().toUpperCase();
        if(external){
            if(rowData.containsKey(fieldName) && rowData.get(fieldName)!=null){
                label=(String)rowData.get( attr.getRelField().toUpperCase() );
                value=(String)rowData.get(fieldName  );
                exist=true;
            }
        }
        else{
            if(rowData.containsKey(fieldName) && rowData.get(fieldName)!=null) {
                String refField = TableUtil.getRefField(attr);
                //将关联字段返回
                if(StringUtils.isNotEmpty(refField)){
                    label =(String) rowData.get(attr.getRelField().toUpperCase());
                }else {
                    label = (String) rowData.get(fieldName + FormBoEntity.COMPLEX_NAME);
                }
                value = (String) rowData.get(fieldName);
                exist=true;
            }
        }
        if(StringUtils.isNotEmpty(label)){
            label=label.trim();
        }

        if(StringUtils.isNotEmpty(value)){
            value=value.trim();
            try{
                JSONObject.parseObject(value);
            }catch(Exception e){
                value="\""+value+"\"";
            }
        }else{
            value="\""+value+"\"";
        }

        String str="{\"label\":\""  + label +"\",\"value\":"+value +"}";
        if(exist){
            return  ValueResult.exist(str);
        }
        return  ValueResult.noExist();
    }


    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        List< FieldEntity> list=new ArrayList<>();
        if(!json.containsKey(attr.getName())){
            return list;
        }
        //单值处理
        if(attr.single()){
            FieldEntity entity=new FieldEntity();
            entity.setFieldName(attr.getFieldName());
            entity.setName( attr.getName());
            entity.setValue(getVal(attr,json));

            list.add(entity);
        }
        else{
            //双值控件的数据存储。
            //{label:"",value:""}
            //FormBoEntity.COMPLEX_NAME
            String label="";
            String value="";
            JSONObject extJsonObj=JSONObject.parseObject(attr.getExtJson());
            JSONObject valJson=json.getJSONObject(attr.getName());
            if(valJson!=null){
                label=valJson.getString("label");
                value=valJson.getString("value");
            }

            FieldEntity entity=new FieldEntity();
            entity.setFieldName(attr.getFieldName());
            entity.setName(attr.getName());
            entity.setValue(value);

            FieldEntity entityVal=new FieldEntity();
            if(BeanUtil.isNotEmpty(extJsonObj)&&extJsonObj.containsKey("ref") && StringUtils.isNotEmpty(extJsonObj.getString("ref"))){
                entityVal.setFieldName(extJsonObj.getString("ref"));
                entityVal.setName(extJsonObj.getString("ref"));
            }else {
                entityVal.setFieldName(attr.getFieldName() + FormBoEntity.COMPLEX_NAME);
                entityVal.setName(attr.getName() + FormBoEntity.COMPLEX_NAME);
            }
            entityVal.setValue(label);

            list.add(entity);
            list.add(entityVal);
        }
        return list;
    }




    private Object getVal(FormBoAttr attr,JSONObject json){
        String val=json.getString(attr.getName());
        String type=attr.getDataType();
        switch (type){
            case Column.COLUMN_TYPE_VARCHAR:
            case Column.COLUMN_TYPE_CLOB:
                return StringUtils.isEmpty(val)?null:val;
            case Column.COLUMN_TYPE_NUMBER:
                Object  valNum=handNumber(attr,val);
                return  valNum;
            case Column.COLUMN_TYPE_INT:
                Object  valInt=handNumber(attr,val);
                return  valInt;
            case Column.COLUMN_TYPE_DATE:
                Object obj= handDate(attr,val);
                return  obj;
            default:
                return val;
        }


    }

    /**
     * 处理数字。
     * @param attr
     * @param val
     * @return
     */
    private Object handNumber(FormBoAttr attr,String val){
        if(StringUtils.isEmpty(val)) {
            return null;
        }
        val=val.trim();
        int length=attr.getLength();
        int decimalLen=attr.getDecimalLength();
        if(decimalLen==0){
            if(length<10){
                return Integer.parseInt(val);
            }
            else{
                return Long.parseLong(val);
            }
        }
        else{
            return  Double.parseDouble(val);
        }
    }

    /**
     * 处理日期数据。
     * @param attr
     * @param val
     * @return
     */
    private Object handDate(FormBoAttr attr,String val){
        JSONObject formSetting=attr.getFormJson();
        String format="yyyy-MM-dd";
        if(BeanUtil.isNotEmpty(formSetting)){
            format=formSetting.getString("format");
        }
        return DateUtils.parseDate(val,format);
    }

    @Override
    public void removeFields(FormBoAttr attr, Map<String, Object> rowData, boolean external) {
        String fieldName=attr.getFieldName().toUpperCase();
        if(attr.single()){
            rowData.remove(fieldName);
        }
        else{
            if(external){
                rowData.remove( attr.getRelField().toUpperCase() );
                rowData.remove(fieldName  );
            }
            else{
                rowData.remove(fieldName + FormBoEntity.COMPLEX_NAME);
                rowData.remove(fieldName);
            }
        }
    }

    @Override
    public void handFromByType(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator,boolean isCurrentField) {

    }
}
