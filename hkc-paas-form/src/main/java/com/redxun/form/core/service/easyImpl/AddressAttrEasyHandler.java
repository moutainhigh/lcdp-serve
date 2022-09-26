package com.redxun.form.core.service.easyImpl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.redxun.form.bo.service.TableUtil.getColPre;

@Component("easyAddressAttrHandler")
public class AddressAttrEasyHandler extends BaseAttrEasyHandler {
    @Override
    public String getPluginName() {
        return "address";
    }

    @Override
    public String getDescription() {
        return "地址控件";
    }

    @Override
    public FormBoAttr parse(JSONObject jsonObject) {
        FormBoAttr attr=super.parse(jsonObject);
        attr.setDataType(Column.COLUMN_TYPE_VARCHAR);
        attr.setLength(100);
        attr.setIsSingle(0);
        return attr;
    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        List<Column> columns=new ArrayList<>();
        Column province=getColumnByAttr(attr,"province","省",20);
        Column provinceCode=getColumnByAttr(attr,"p_code","省Code",20);
        columns.add(province);
        columns.add(provinceCode);
        //判断是否有配置

        if(options.containsKey("isCity") && options.getBoolean("isCity")){
            Column city=getColumnByAttr(attr,"city","市",20);
            Column cityCode=getColumnByAttr(attr,"city_code","市Code",20);
            columns.add(city);
            columns.add(cityCode);
        }
        if(options.containsKey("isCounty") && options.getBoolean("isCounty")){
            Column county=getColumnByAttr(attr,"county","县(区)",20);
            Column countyCode=getColumnByAttr(attr,"county_code","县(区)Code",20);
            columns.add(county);
            columns.add(countyCode);
        }
        if(options.containsKey("isAddress") && options.getBoolean("isAddress")){
            Column address=getColumnByAttr(attr,"address","详细地址",64);
            columns.add(address);
        }
        return columns;
    }

    private static Column getColumnByAttr(FormBoAttr attr,String code,String remark,int charLen){
        Column col=new DefaultColumn();
        col.setFieldName(TableUtil.getFieldName(attr.getName()+ "_"+ code));
        col.setColumnType(attr.getDataType());
        col.setComment(remark);
        col.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        col.setCharLen(charLen);
        return col;
    }

    @Override
    public List<FieldEntity> getFieldEntity(FormBoAttr attr, JSONObject json) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        List<FieldEntity> list=new ArrayList<>();
        String o = (String) json.get(attr.getName());
        if(StringUtils.isEmpty(o) || BeanUtil.isEmpty(options)) {
            return new ArrayList<>();
        };
        JSONObject jsonObject = JSONObject.parseObject(o);
        //省
        String province = (String) jsonObject.get("province");
        String provinceCode = (String) jsonObject.get("province_code");
        FieldEntity provinceEntuty=getAddressEnt(attr,province,"province");
        FieldEntity provinceCodeEntuty=getAddressEnt(attr,provinceCode,"p_code");
        list.add(provinceEntuty);
        list.add(provinceCodeEntuty);
        //市
        if(options.getBoolean("isCity")){
            String city = (String) jsonObject.get("city");
            String cityCode = (String) jsonObject.get("city_code");
            FieldEntity cityEntuty=getAddressEnt(attr,city,"city");
            FieldEntity cityCodeEntuty=getAddressEnt(attr,cityCode,"city_code");
            list.add(cityEntuty);
            list.add(cityCodeEntuty);
        }
        //县(区)
        if(options.getBoolean("isCounty")){
            String county = (String) jsonObject.get("county");
            String countyCode = (String) jsonObject.get("county_code");
            FieldEntity countyEntuty=getAddressEnt(attr,county,"county");
            FieldEntity countyCodeEntuty=getAddressEnt(attr,countyCode,"county_code");
            list.add(countyEntuty);
            list.add(countyCodeEntuty);
        }
        //详情地址
        if(options.getBoolean("isAddress")){
            String address = (String) jsonObject.get("address");
            FieldEntity addressEntuty=getAddressEnt(attr,address,"address");
            list.add(addressEntuty);
        }
        return  list;
    }

    private FieldEntity getAddressEnt(FormBoAttr attr,String value,String code){
        FieldEntity entity=new FieldEntity();
        entity.setName(attr.getName()+  "_"+ code);
        entity.setFieldName(TableUtil.getFieldName(attr.getName() + "_"+ code));;
        entity.setValue(value);
        return entity;
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        JSONObject jsonObject=new JSONObject();
        String provinceVal=TableUtil.getFieldName(attr.getName()+ "_province");
        String provinceCodeVal=TableUtil.getFieldName(attr.getName() + "_p_code");
        //判断是否有配置
        boolean flag=false;
        if(BeanUtil.isEmpty(options)){
            flag=true;
        }
        if(rowData.containsKey(provinceVal)){
            String province = (String) rowData.get(provinceVal);
            String provinceCode = (String) rowData.get(provinceCodeVal);
            jsonObject.put("province",province);
            jsonObject.put("province_code",provinceCode);

            if(flag||options.getBoolean("isCity")){
                String cityVal=TableUtil.getFieldName(attr.getName() + "_city");
                String cityCodeVal=TableUtil.getFieldName(attr.getName() + "_city_code");
                String city = (String) rowData.get(cityVal);
                String cityCode = (String) rowData.get(cityCodeVal);
                jsonObject.put("city",city);
                jsonObject.put("city_code",cityCode);
            }
            if(flag||options.getBoolean("isCounty")){
                String countyVal=TableUtil.getFieldName(attr.getName() + "_county");
                String countyCodeVal=TableUtil.getFieldName(attr.getName() + "_county_code");
                String county = (String) rowData.get(countyVal);
                String countyCode = (String) rowData.get(countyCodeVal);
                jsonObject.put("county",county);
                jsonObject.put("county_code",countyCode);
            }
            if(flag||options.getBoolean("isAddress")){
                String addresssVal=TableUtil.getFieldName(attr.getName() + "_address");
                String address = (String) rowData.get(addresssVal);
                jsonObject.put("address",address);
            }
            return ValueResult.exist(jsonObject.toJSONString());
        }
        else{
            return ValueResult.noExist();
        }
    }

    @Override
    public void removeFields(FormBoAttr attr, Map<String, Object> rowData, boolean external) {
        JSONObject obj=JSONObject.parseObject(attr.getExtJson());
        JSONObject options=obj.getJSONObject("options");
        //判断是否有配置
        boolean flag=false;
        if(BeanUtil.isEmpty(options)){
            flag=true;
        }
        String columnPre= getColPre();
        String provinceVal=(columnPre +attr.getName()+ "_province").toUpperCase();
        String provinceCodeVal=(columnPre +attr.getName() + "_p_code").toUpperCase();
        rowData.remove(provinceVal);
        rowData.remove(provinceCodeVal);

        if(flag||options.getBoolean("isCity")){
            String cityVal=(columnPre +attr.getName() + "_city").toUpperCase();
            String cityCodeVal=(columnPre +attr.getName() + "_city_code").toUpperCase();
            rowData.remove(cityVal);
            rowData.remove(cityCodeVal);
        }

        if(flag||options.getBoolean("isCounty")){
            String countyVal=(columnPre +attr.getName() + "_county").toUpperCase();
            String countyCodeVal=(columnPre +attr.getName() + "_county_code").toUpperCase();
            rowData.remove(countyVal);
            rowData.remove(countyCodeVal);
        }

        if(flag||options.getBoolean("isAddress")){
            String addresssVal=(columnPre +attr.getName() + "_address").toUpperCase();
            rowData.remove(addresssVal);
        }
    }

    @Override
    public void handFromByType(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator,boolean isCurrentField) {
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
    private void handOriginField(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator){
        FormBoAttr originAttr=boAttr.getOrignAttr();

        JSONObject obj=JSONObject.parseObject(originAttr.getExtJson());
        JSONObject options=obj.getJSONObject("options");

        Boolean hasAddress= options.getBoolean("isAddress");
        /*
            当前类型不是地址控件
            处理方式
            1. 创建新的字段
            2. 将数据进行更新
            3. 删除原来的字段
         */

        //1.添加新字段。
        IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(boAttr.getControl());
        List<Column> newColumns = attrHandler.getColumns(boAttr);
        for(Column col:newColumns){
            List<String> addColumnSql = tableOperator.getAddColumnSql(boEnt.getTableName(), col);
            for(String sql:addColumnSql){
                sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_ADD));
            }
        }

        //2.原来有地址控件。
        if(hasAddress){
            String colName=TableUtil.getFieldName( boAttr.getName());
            String sql="update %s set  %s=%s_address;";
            sql= String.format(sql, boEnt.getTableName() , colName,colName);
            sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_UPD));
        }


        //3.将文本框控件，转化成地址栏控件需要处理的问题。
        List<Column> columns = getColumns(originAttr);
        //删除原来字段
        for(Column col:columns){
            String fieldName=TableUtil.getFieldName(col.getFieldName());
            String sql= tableOperator.getDropColumnSql(boEnt.getTableName(),fieldName);
            sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_DEL));
        }


    }

    private void handCurField(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator){
        FormBoAttr originAttr=boAttr.getOrignAttr();

        JSONObject obj=JSONObject.parseObject(boAttr.getExtJson());
        JSONObject options=obj.getJSONObject("options");

        Boolean hasAddress= options.getBoolean("isAddress");

        /*
            原来的类型不是地址控件
            处理方式
            1. 删除原来的字段
            2. 新建地址字段，如果创建了 地址字段，那么 产生更新语句，将数据转到新的字段中。
         */
        if(!originAttr.getControl().equals(this.getPluginName())){

            //1.将文本框控件，转化成地址栏控件需要处理的问题。

            List<Column> columns = getColumns(boAttr);
            //添加字段
            for(Column col:columns){
                List< String> addSqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),col);
                for(String sql:addSqlList){
                    sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_ADD));
                }
            }
            //2.原来有地址控件。
            if(hasAddress){
                String colName=originAttr.getFieldName();
                String sql="update %s set  %s_address=%s;";
                sql= String.format(sql, boEnt.getTableName() , colName,colName);
                sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_UPD));
            }
            //3.删除原来的字段。
            IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(originAttr.getControl());
            List<Column> originColumns = attrHandler.getColumns(boAttr);
            for(Column column:originColumns){
                AlterSql delaySql = AlterSql.getDelaySql(tableOperator.getDropColumnSql(boEnt.getTableName(), column.getFieldName()),TableUtil.OP_DEL);
                sqlList.add(delaySql);
            }
        }
        else{
            //判断是否有配置
            boolean flag=false;
            if(BeanUtil.isEmpty(options)){
                flag=true;
            }
            FormBoEntity originEnt = boEnt.getOriginEnt();
            if(BeanUtil.isEmpty(originEnt)){
                return;
            }
            JSONObject oldFormJson = originAttr.getFormJson();
            if(BeanUtil.isNotEmpty(oldFormJson)){
                JSONObject oldSetting = JSONObject.parseObject(oldFormJson.getString("setting"));
                String columnPre=getColPre();
                //市
                if(flag|| !options.getBoolean("isCity").equals( oldSetting.getBoolean("isCity"))){
                    //true新增字段 false为删除字段
                    if(flag||options.getBoolean("isCity")){
                        Column city=getColumnByAttr(boAttr,"city","市",20);
                        Column cityCode=getColumnByAttr(boAttr,"city_code","市Code",20);
                        List<String> citySqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),city);
                        for(String sql:citySqlList){
                            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
                        }
                        List< String> cityCodeSqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),cityCode);
                        for(String sql:cityCodeSqlList){
                            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
                        }
                    }else {
                        String cityVal=(columnPre +boAttr.getName() + "_city").toUpperCase();
                        String cityCodeVal=(columnPre +boAttr.getName() + "_city_code").toUpperCase();
                        String dropNameSql=tableOperator.getDropColumnSql(boEnt.getTableName(),cityVal);
                        String dropCodeSql=tableOperator.getDropColumnSql(boEnt.getTableName(),cityCodeVal);
                        sqlList.add(AlterSql.getNoDelaySql(dropNameSql,TableUtil.OP_DEL));
                        sqlList.add(AlterSql.getNoDelaySql(dropCodeSql,TableUtil.OP_DEL));
                    }
                }
                //县
                if(flag|| !options.getBoolean("isCounty").equals( oldSetting.getBoolean("isCounty"))){
                    //true新增字段 false为删除字段
                    if(flag||options.getBoolean("isCounty")){
                        Column county=getColumnByAttr(boAttr,"county","县(区)",20);
                        Column countyCode=getColumnByAttr(boAttr,"county_code","县(区)Code",20);
                        List<String> citySqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),county);
                        for(String sql:citySqlList){
                            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
                        }
                        List< String> cityCodeSqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),countyCode);
                        for(String sql:cityCodeSqlList){
                            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
                        }
                    }else {
                        String countyVal=(columnPre +boAttr.getName() + "_county").toUpperCase();
                        String countyCodeVal=(columnPre +boAttr.getName() + "_county_code").toUpperCase();
                        String dropNameSql=tableOperator.getDropColumnSql(boEnt.getTableName(),countyVal);
                        String dropCodeSql=tableOperator.getDropColumnSql(boEnt.getTableName(),countyCodeVal);
                        sqlList.add(AlterSql.getNoDelaySql(dropNameSql,TableUtil.OP_DEL));
                        sqlList.add(AlterSql.getNoDelaySql(dropCodeSql,TableUtil.OP_DEL));
                    }
                }
                //详情地址
                if(flag|| !options.getBoolean("isAddress").equals(oldSetting.getBoolean("isAddress")) ){
                    //true新增字段 false为删除字段
                    if(flag||options.getBoolean("isAddress")){
                        Column address=getColumnByAttr(boAttr,"address","详细地址",64);
                        List<String> citySqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),address);
                        for(String sql:citySqlList){
                            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
                        }
                    }else {
                        String addresssVal=(columnPre +boAttr.getName() + "_address").toUpperCase();
                        String dropNameSql=tableOperator.getDropColumnSql(boEnt.getTableName(),addresssVal);
                        sqlList.add(AlterSql.getNoDelaySql(dropNameSql,TableUtil.OP_DEL));
                    }
                }

            }

        }
    }

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }
}