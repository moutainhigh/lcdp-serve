package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultColumn;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FieldEntity;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.entity.ValueResult;
import com.redxun.form.core.service.AttrHandlerContext;
import com.redxun.form.core.service.IAttrHandler;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class DateRangeAttrHandler extends BaseAttrHandler {

    private static final String START="_START";
    private static final String END="_END";
    private static final String SPLITOR=",";

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {
        JSONObject dataJson= attr.getDataJson();
        JSONObject formJson=attr.getFormJson();
        String format=formJson.getString("format");
        boolean defaultnow= dataJson.getBoolean("defaultnow");
        if(defaultnow){
            String val= DateUtils.dateTimeNow(DateUtils.switchFormat(format));
            String value=val +SPLITOR +val;
            jsonObject.put(attr.getName(),value);
        }
    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        List<String> fields=new ArrayList<>();
        fields.add("format");
        this.setFormJson(field,jsonObject,fields);
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        String curUser=jsonObject.getString("defaultnow");
        JSONObject json=new JSONObject();
        json.put("defaultnow",curUser);
        field.setDataJson(json);
    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_DATE);
        field.setLength(20);
    }

    @Override
    public String getPluginName() {
        return "rx-date-range";
    }

    @Override
    public String getDescription() {
        return "日期范围控件";
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        Column colStart=getColumnByAttr(attr,true);
        Column colEnd=getColumnByAttr(attr,false);
        List<Column> columns=new ArrayList<>();
        columns.add(colStart);
        columns.add(colEnd);
        return columns;
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        JSONObject formJson= attr.getFormJson();
        String format=DateUtils.DATE_FORMAT_YMD;
        if(BeanUtil.isNotEmpty(formJson)){
            format=formJson.getString("format");
        }

        String startField=TableUtil.getFieldName(attr.getName() +START);
        String endField=TableUtil.getFieldName(attr.getName() +END);

        if(rowData.containsKey(startField)){
            Date startVal= null;
            if(rowData.get(startField) instanceof LocalDateTime){
                startVal = DateUtils.localDateTimeToDate((LocalDateTime) rowData.get(startField));
            }else{
                startVal = (Date) rowData.get(startField);
            }
            Date endVal= null;
            if(rowData.get(endField) instanceof LocalDateTime){
                endVal = DateUtils.localDateTimeToDate((LocalDateTime) rowData.get(endField));
            }else{
                endVal = (Date) rowData.get(endField);
            }
            String start="";
            String end="";
            if(BeanUtil.isNotEmpty(startVal)){
                start=DateUtils.parseDateToStr(DateUtils.switchFormat(format),startVal);
            }
            if(BeanUtil.isNotEmpty(startVal)){
                end=DateUtils.parseDateToStr(DateUtils.switchFormat(format),endVal);
            }

            if(StringUtils.isEmpty(start)){
                return ValueResult.exist("");
            }
            String val=  start +SPLITOR + end;

            return ValueResult.exist(val);
        }

        return ValueResult.noExist();

    }

    @Override
    public void removeFields(FormBoAttr attr, Map<String, Object> rowData, boolean external) {
        String startField=TableUtil.getFieldName(attr.getName() +START);
        String endField=TableUtil.getFieldName(attr.getName() +END);

        rowData.remove(startField);
        rowData.remove(endField);
    }

    @Override
    public void handFromByType(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator, boolean isCurrentField) {
        //当前为多值，分两种情况，
        // 1.当前和原来都是多值
        // 2.当前多值，原来单值。
        if(isCurrentField){
            handCurField(boEnt,boAttr,sqlList,tableOperator);
        }
        else{
            handOriginField(boEnt,boAttr,sqlList,tableOperator);
        }
    }

    /**
     * 原来 为多值，当前字段不是多值的情况。
     * @param boEnt
     * @param boAttr
     * @param sqlList
     * @param tableOperator
     */
    private void handOriginField(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator) {
        FormBoAttr originAttr = boAttr.getOrignAttr();
        /*
            当前类型不是日期范围控件
            处理方式
            1. 创建新的字段
            2. 将数据进行更新
            3. 删除原来的字段
         */
        //1.添加新字段。
        IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(boAttr.getControl());
        List<Column> newColumns = attrHandler.getColumns(boAttr);
        for (Column col : newColumns) {
            List<String> addColumnSql = tableOperator.getAddColumnSql(boEnt.getTableName(), col);
            for (String sql : addColumnSql) {
                sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_ADD));
            }
        }
        //2.将文本框控件，转化成日期范围控件需要处理的问题。
        List<Column> columns = getColumns(originAttr);
        //删除原来字段
        for (Column col : columns) {
            String fieldName = TableUtil.getFieldName(col.getFieldName());
            String sql = tableOperator.getDropColumnSql(boEnt.getTableName(), fieldName);
            sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_DEL));
        }
    }

    private void handCurField(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator) {
        FormBoAttr originAttr = boAttr.getOrignAttr();
        /*
            原来的类型不是日期范围控件
            处理方式
            1. 删除原来的字段
            2. 新建日期范围字段，如果创建了 日期范围字段，那么 产生更新语句，将数据转到新的字段中。
         */
        if (!originAttr.getControl().equals(this.getPluginName())) {
            //1.将文本框控件，转化成日期范围控件需要处理的问题。
            List<Column> columns = getColumns(boAttr);
            //添加字段
            for (Column col : columns) {
                List<String> addSqlList = tableOperator.getAddColumnSql(boEnt.getTableName(), col);
                for (String sql : addSqlList) {
                    sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_ADD));
                }
            }
            //2.删除原来的字段。
            IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(originAttr.getControl());
            List<Column> originColumns = attrHandler.getColumns(boAttr);
            for (Column column : originColumns) {
                AlterSql delaySql = AlterSql.getDelaySql(tableOperator.getDropColumnSql(boEnt.getTableName(), column.getFieldName()),TableUtil.OP_DEL);
                sqlList.add(delaySql);
            }
        }
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        JSONObject formJson=attr.getFormJson();
        String format=DateUtils.DATE_FORMAT_YMD;
        if(BeanUtil.isNotEmpty(formJson)){
            format=formJson.getString("format");
        }

        String data=json.getString(attr.getName());
        String[] aryDate=data.split(SPLITOR);


        FieldEntity start=getDataEnt(attr,DateUtils.switchFormat(format),true,aryDate[0]);
        FieldEntity end=getDataEnt(attr,DateUtils.switchFormat(format),false,aryDate.length>1?aryDate[1]:"");

        List<FieldEntity> list=new ArrayList<>();
        list.add(start);
        list.add(end);

        return  list;
    }

    private FieldEntity getDataEnt(FormBoAttr attr,String format,boolean start,String val){
        String suffix=start?START:END;

        FieldEntity entity=new FieldEntity();
        entity.setName(attr.getName()+suffix);
        entity.setFieldName(TableUtil.getFieldName(attr.getName() +suffix));
        entity.setValue(DateUtils.parseDate(val,DateUtils.switchFormat(format)));
        return entity;
    }

    private static Column getColumnByAttr(FormBoAttr attr,boolean start){
        Column col=new DefaultColumn();
        String suffix=start?START:END;
        String preComment=start?"开始":"结束";

        col.setFieldName(TableUtil.getFieldName(attr.getName() +suffix));
        col.setColumnType(attr.getDataType());
        col.setDbFieldType(attr.getDbFieldType());
        String comment=attr.getComment() + preComment;
        col.setComment(comment);
        return col;
    }




}
