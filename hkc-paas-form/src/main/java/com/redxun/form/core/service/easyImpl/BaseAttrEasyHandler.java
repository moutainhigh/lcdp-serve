package com.redxun.form.core.service.easyImpl;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class BaseAttrEasyHandler implements IAttrEasyHandler {

    protected abstract void setInitData(FormBoAttr attr,JSONObject setting,JSONObject jsonObject);

    @Override
    public void getInitData(FormBoAttr attr,JSONObject jsonData) {
        JSONObject settingJson= JSONObject.parseObject(attr.getExtJson());

        JSONObject options= settingJson.getJSONObject("options");
        if(options.containsKey("valmode")){
            String valmode=options.getString("valmode");
            if("double".equals(valmode)){
                jsonData.put(attr.getName(), "{\"label\":\"\",\"value\":\"\"}");
            }
            else{
                jsonData.put(attr.getName(), "");
            }
        }
        else{
            jsonData.put(attr.getName(), "");
        }
        setInitData(attr,settingJson,jsonData);
    }

    protected void setValMode(JSONObject json,FormBoAttr attr){
        JSONObject options = json.getJSONObject("options");
        //复选列表
        String valmode = options.getString("valmode");
        if ("single".equals(valmode)) {
            attr.setIsSingle(1);
        } else {
            attr.setIsSingle(0);
        }
    }



    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=new FormBoAttr();
        //控件类型
        attr.setControl(jsonObject.getString("type"));
        attr.setComment(jsonObject.getString("label").trim());
        attr.setName(jsonObject.getString("model").trim());
        JSONObject options=jsonObject.getJSONObject("options");
        //如果有数据类型设置
        if(options.containsKey("type")){
            String type=options.getString("type");
            type="text".equals(type)?"varchar":type;
            attr.setDataType(type);
            if(Column.COLUMN_TYPE_VARCHAR.equals(type)){
                attr.setLength(options.getInteger("length"));
            }
            else if(Column.COLUMN_TYPE_NUMBER.equals(type)){
                Integer length=options.getInteger("length");
                Integer decimalLength=options.getInteger("decimalLength");
                attr.setLength(length);
                attr.setDecimalLength(decimalLength);
            }
        }
        attr.setExtJson(jsonObject.toJSONString());
        return attr;
    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        List<Column> columns=new ArrayList<>();
        Column column= TableUtil. getColumnByAttr(attr,false);
        columns.add(column);
        if(!attr.single()){
            Column columnComplex= TableUtil.getColumnByAttr(attr,true);
            columns.add(columnComplex);
        }
        return columns;
    }


    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String fieldName=attr.getFieldName().toUpperCase();
        Object val=null;
        if(attr.single()){
            if(rowData.containsKey(fieldName)){
                val=rowData.get(fieldName);
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
            if(rowData.containsKey(fieldName)){
                label=(String)rowData.get( attr.getRelField().toUpperCase() );
                value=(String)rowData.get(fieldName  );
                exist=true;
            }
        }
        else{
            if(rowData.containsKey(fieldName)) {
                label = (String) rowData.get(fieldName + FormBoEntity.COMPLEX_NAME);
                value = (String) rowData.get(fieldName);
                exist=true;
            }
        }
        String str="{\"label\":\""  + label +"\",\"value\":\""+value +"\"}";
        if(exist){
            return  ValueResult.exist(str);
        }
        return  ValueResult.noExist();
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

            String val=json.getString(attr.getName());
            if(StringUtils.isNotEmpty(val)){
                JSONObject valJson=JSONObject.parseObject(val);
                label=valJson.getString("label");
                value=valJson.getString("value");
            }

            FieldEntity entity=new FieldEntity();
            entity.setFieldName(attr.getFieldName());
            entity.setName(attr.getName());
            entity.setValue(value);

            FieldEntity entityVal=new FieldEntity();
            entityVal.setFieldName(attr.getFieldName() + FormBoEntity.COMPLEX_NAME);
            entityVal.setName(attr.getName() + FormBoEntity.COMPLEX_NAME);
            entityVal.setValue(label);

            list.add(entity);
            list.add(entityVal);
        }
        return list;
    }

    @Override
    public void handFromByType(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator,boolean isCurrentField) {

    }
}
