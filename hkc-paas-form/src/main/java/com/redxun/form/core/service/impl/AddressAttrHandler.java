package com.redxun.form.core.service.impl;

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
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.entity.ValueResult;
import com.redxun.form.core.service.AttrHandlerContext;
import com.redxun.form.core.service.IAttrHandler;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.redxun.form.bo.service.TableUtil.getColPre;

/**
 *  地址组件实现。
 *
 */
@Component
public class AddressAttrHandler extends BaseAttrHandler {

    @Resource
    FormBoEntityServiceImpl formBoEntityService;

    @Override
    protected void setInitData(FormBoAttr attr, JSONObject setting, JSONObject jsonObject) {

    }

    @Override
    protected void parseFormSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.getFormJson().put("setting",jsonObject.getString("setting"));
    }

    @Override
    protected void parseDataSetting(FormBoAttr field, Element el, JSONObject jsonObject) {

    }

    @Override
    protected void parseAttrSetting(FormBoAttr field, Element el, JSONObject jsonObject) {
        field.setDataType(Column.COLUMN_TYPE_VARCHAR);
        field.setLength(100);
        if(BeanUtil.isNotEmpty(field.getFormJson())){
            String setting = field.getFormJson().getString("setting");
            if(StringUtils.isNotEmpty(setting)){
                JSONObject extJson = JSONObject.parseObject(field.getExtJson());
                if(BeanUtil.isEmpty(extJson)){
                    extJson=new JSONObject();
                }
                extJson.put("setting",setting);
                field.setExtJson(extJson.toJSONString());
            }
        }
    }

    @Override
    public String getPluginName() {
        return "rx-address";
    }

    @Override
    public String getDescription() {
        return "地址控件";
    }


    @Override
    public int getType() {
        return 2;
    }

    @Override
    public List<Column> getColumns(FormBoAttr attr) {
        //默认全部创建，因为导入子表中有地址控件的话，很难获取到配置
        String settingStr="{\"isAddress\":true,\"isCity\":true,\"isCounty\":true}";
        List<Column> columns=new ArrayList<>();
        //实体变更时，需要先判断extJson的配置
        if(BeanUtil.isNotEmpty(attr.getExtJson())){
            JSONObject extJson = JSONObject.parseObject(attr.getExtJson());
            settingStr = extJson.getString("setting");
        }
        if(BeanUtil.isNotEmpty(attr.getFormJson())){
            settingStr = attr.getFormJson().getString("setting");
        }
        if(BeanUtil.isNotEmpty(attr.getExtJson())) {
            //反向生成是获取关联字段属性
            JSONObject extJson = JSONObject.parseObject(attr.getExtJson());
            String p_code = extJson.getString("p_code");
            if(StringUtils.isNotEmpty(p_code)){
                Column column= TableUtil. getColumnByAttr(attr,false);
                columns.add(column);
                return columns;
            }
        }
        JSONObject setting = JSONObject.parseObject(settingStr);
        Column province=getColumnByAttr(attr,"province","省",50);
        Column provinceCode=getColumnByAttr(attr,"p_code","省Code",50);
        columns.add(province);
        columns.add(provinceCode);
        //判断是否有配置

        if(setting.containsKey("isCity") && setting.getBoolean("isCity")){
            Column city=getColumnByAttr(attr,"city","市",50);
            Column cityCode=getColumnByAttr(attr,"city_code","市Code",50);
            columns.add(city);
            columns.add(cityCode);
        }
        if(setting.containsKey("isCounty") && setting.getBoolean("isCounty")){
            Column county=getColumnByAttr(attr,"county","县(区)",50);
            Column countyCode=getColumnByAttr(attr,"county_code","县(区)Code",50);
            columns.add(county);
            columns.add(countyCode);
        }
        if(setting.containsKey("isAddress") && setting.getBoolean("isAddress")){
            Column address=getColumnByAttr(attr,"address","详细地址",255);
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
        String settingStr="";
        if(BeanUtil.isNotEmpty(attr.getFormJson())){
            settingStr = attr.getFormJson().getString("setting");
        }
        FormBoEntity formBoEntity = formBoEntityService.getByEntId(attr.getEntId());
        JSONObject setting = JSONObject.parseObject(settingStr);
        List<FieldEntity> list=new ArrayList<>();
        String o = (String) json.get(attr.getName());
        if(StringUtils.isEmpty(o)) {
            return new ArrayList<>();
        };
        //判断是否有配置
        boolean flag=false;
        if(BeanUtil.isEmpty(setting)){
            flag=true;
        }
        JSONObject jsonObject = JSONObject.parseObject(o);
        //省
        String province = (String) jsonObject.get("province");
        String provinceCode = (String) jsonObject.get("province_code");
        FieldEntity provinceEntuty=getAddressEnt(attr,province,"province",formBoEntity);
        FieldEntity provinceCodeEntuty=getAddressEnt(attr,provinceCode,"p_code",formBoEntity);
        list.add(provinceEntuty);
        list.add(provinceCodeEntuty);
        //市
        if(flag||setting.getBoolean("isCity")){
            String city = (String) jsonObject.get("city");
            String cityCode = (String) jsonObject.get("city_code");
            FieldEntity cityEntuty=getAddressEnt(attr,city,"city",formBoEntity);
            FieldEntity cityCodeEntuty=getAddressEnt(attr,cityCode,"city_code",formBoEntity);
            list.add(cityEntuty);
            list.add(cityCodeEntuty);
        }
        //县(区)
        if(flag||setting.getBoolean("isCounty")){
            String county = (String) jsonObject.get("county");
            String countyCode = (String) jsonObject.get("county_code");
            FieldEntity countyEntuty=getAddressEnt(attr,county,"county",formBoEntity);
            FieldEntity countyCodeEntuty=getAddressEnt(attr,countyCode,"county_code",formBoEntity);
            list.add(countyEntuty);
            list.add(countyCodeEntuty);
        }
        //详情地址
        if(flag||setting.getBoolean("isAddress")){
            String address = (String) jsonObject.get("address");
            FieldEntity addressEntuty=getAddressEnt(attr,address,"address",formBoEntity);
            list.add(addressEntuty);
        }
        return  list;
    }

    private FieldEntity getAddressEnt(FormBoAttr attr,String value,String code,FormBoEntity formBoEntity){
        FieldEntity entity=new FieldEntity();
        List<FormBoAttr> attrList = formBoEntity.getBoAttrList();
        if(BeanUtil.isNotEmpty(attr.getExtJson())) {
            //反向生成是获取关联字段属性
            JSONObject extJson = JSONObject.parseObject(attr.getExtJson());
            String fieldCode = extJson.getString(code);
            if(StringUtils.isNotEmpty(fieldCode)){
                entity.setName(fieldCode);
                FormBoAttr fieldAttr=null;
                for (FormBoAttr formBoAttr : attrList) {
                    if(formBoAttr.getName().equals(fieldCode)){
                        fieldAttr=formBoAttr;
                        break;
                    }
                }
                if(BeanUtil.isNotEmpty(fieldAttr)){
                    if(StringUtils.isNotEmpty(fieldAttr.getFieldName())){
                        entity.setFieldName(fieldAttr.getFieldName());
                    }else {
                        entity.setFieldName(TableUtil.getFieldName(fieldAttr.getName()));
                    }
                    entity.setValue(value);
                    return entity;
                }
            }
        }
        entity.setName(attr.getName()+  "_"+ code);
        entity.setFieldName(TableUtil.getFieldName(attr.getName() + "_"+ code));;
        entity.setValue(value);
        return entity;
    }

    @Override
    public ValueResult getValue(FormBoAttr attr, Map<String, Object> rowData, boolean isExternal) {
        String settingStr="";
        if(BeanUtil.isNotEmpty(attr.getFormJson())){
            settingStr = attr.getFormJson().getString("setting");
        }
        JSONObject setting = JSONObject.parseObject(settingStr);
        FormBoEntity formBoEntity = formBoEntityService.getByEntId(attr.getEntId());
        JSONObject jsonObject=new JSONObject();
        String provinceVal=getFieldName(attr,"province","_province",formBoEntity);
        String provinceCodeVal=getFieldName(attr,"p_code","_p_code",formBoEntity);
        //判断是否有配置
        boolean flag=false;
        if(BeanUtil.isEmpty(setting)){
            flag=true;
        }
        if(rowData.containsKey(provinceVal)){
            String province = (String) rowData.get(provinceVal);
            String provinceCode = (String) rowData.get(provinceCodeVal);
            jsonObject.put("province",province);
            jsonObject.put("province_code",provinceCode);

            if(flag||setting.getBoolean("isCity")){
                String cityVal=getFieldName(attr,"city","_city",formBoEntity);
                String cityCodeVal=getFieldName(attr,"city_code","_city_code",formBoEntity);
                String city = (String) rowData.get(cityVal);
                String cityCode = (String) rowData.get(cityCodeVal);
                jsonObject.put("city",city);
                jsonObject.put("city_code",cityCode);
            }
            if(flag||setting.getBoolean("isCounty")){
                String countyVal=getFieldName(attr,"county","_county",formBoEntity);
                String countyCodeVal=getFieldName(attr,"county_code","_county_code",formBoEntity);
                String county = (String) rowData.get(countyVal);
                String countyCode = (String) rowData.get(countyCodeVal);
                jsonObject.put("county",county);
                jsonObject.put("county_code",countyCode);
            }
            if(flag||setting.getBoolean("isAddress")){
                String addresssVal=getFieldName(attr,"address","_address",formBoEntity);
                String address = (String) rowData.get(addresssVal);
                jsonObject.put("address",address);
            }
            return ValueResult.exist(jsonObject.toJSONString());
        }
        else{
            return ValueResult.noExist();
        }

    }

    /**
     *
     * @param attr 实体属性
     * @param code 编码
     * @param suffix 后缀
     * @param formBoEntity 业务实体
     * @return
     */
    private String getFieldName(FormBoAttr attr,String code,String suffix,FormBoEntity formBoEntity){
        String fieldName="";
        List<FormBoAttr> attrList = formBoEntity.getBoAttrList();
        if(BeanUtil.isNotEmpty(attr.getExtJson())) {
            //反向生成是获取关联字段属性
            JSONObject extJson = JSONObject.parseObject(attr.getExtJson());
            String fieldCode = extJson.getString(code);
            if(StringUtils.isNotEmpty(fieldCode)) {
                FormBoAttr fieldAttr = null;
                for (FormBoAttr formBoAttr : attrList) {
                    if (formBoAttr.getName().equals(fieldCode)) {
                        fieldAttr = formBoAttr;
                        break;
                    }
                }
                if(BeanUtil.isNotEmpty(fieldAttr)){
                    if(StringUtils.isNotEmpty(fieldAttr.getFieldName())){
                        fieldName=fieldAttr.getFieldName();
                    }else {
                        fieldName=TableUtil.getFieldName(fieldAttr.getName());
                    }
                    return fieldName;
                }

            }
        }
        return TableUtil.getFieldName(attr.getName()+ suffix);
    }

    @Override
    public void removeFields(FormBoAttr attr, Map<String, Object> rowData, boolean external) {
        String settingStr="";
        if(BeanUtil.isNotEmpty(attr.getFormJson())){
            settingStr = attr.getFormJson().getString("setting");
        }
        JSONObject setting = JSONObject.parseObject(settingStr);
        //判断是否有配置
        boolean flag=false;
        if(BeanUtil.isEmpty(setting)){
            flag=true;
        }
        String columnPre= getColPre();
        String provinceVal=(columnPre +attr.getName()+ "_province").toUpperCase();
        String provinceCodeVal=(columnPre +attr.getName() + "_p_code").toUpperCase();
        rowData.remove(provinceVal);
        rowData.remove(provinceCodeVal);

        if(flag||setting.getBoolean("isCity")){
            String cityVal=(columnPre +attr.getName() + "_city").toUpperCase();
            String cityCodeVal=(columnPre +attr.getName() + "_city_code").toUpperCase();
            rowData.remove(cityVal);
            rowData.remove(cityCodeVal);
        }

        if(flag||setting.getBoolean("isCounty")){
            String countyVal=(columnPre +attr.getName() + "_county").toUpperCase();
            String countyCodeVal=(columnPre +attr.getName() + "_county_code").toUpperCase();
            rowData.remove(countyVal);
            rowData.remove(countyCodeVal);
        }

        if(flag||setting.getBoolean("isAddress")){
            String addresssVal=(columnPre +attr.getName() + "_address").toUpperCase();
            rowData.remove(addresssVal);
        }

    }

    /**
     * 处理字段变化的SQL语句。
     *  1. 当原来是文本数据，当前是
     * setting
     * {
     *             "isAddress": true,
     *                 "isCity": true,
     *                 "isCounty": true
     *         }
     * @param boEnt
     * @param boAttr
     * @param sqlList
     * @param tableOperator
     */
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

        String settingStr="";
        if(BeanUtil.isNotEmpty(originAttr.getFormJson())){
            settingStr = originAttr.getFormJson().getString("setting");
        }
        JSONObject setting = JSONObject.parseObject(settingStr);

        Boolean hasAddress= setting.getBoolean("isAddress");
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

        String settingStr="";
        //实体变更时，需要先判断extJson的配置
        if(BeanUtil.isNotEmpty(boAttr.getExtJson())){
            JSONObject extJson = JSONObject.parseObject(boAttr.getExtJson());
            settingStr = extJson.getString("setting");
        }
        if(BeanUtil.isNotEmpty(boAttr.getFormJson())){
            settingStr = boAttr.getFormJson().getString("setting");
        }
        JSONObject setting = JSONObject.parseObject(settingStr);
        Boolean hasAddress=false;
        if(BeanUtil.isNotEmpty(setting)){
            hasAddress= setting.getBoolean("isAddress");
        }

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
            if(BeanUtil.isEmpty(setting)){
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
                if(flag|| !setting.getBoolean("isCity").equals( oldSetting.getBoolean("isCity"))){
                    //true新增字段 false为删除字段
                    if(flag||setting.getBoolean("isCity")){
                        Column city=getColumnByAttr(boAttr,"city","市",50);
                        Column cityCode=getColumnByAttr(boAttr,"city_code","市Code",50);
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
                if(flag|| !setting.getBoolean("isCounty").equals( oldSetting.getBoolean("isCounty"))){
                    //true新增字段 false为删除字段
                    if(flag||setting.getBoolean("isCounty")){
                        Column county=getColumnByAttr(boAttr,"county","县(区)",50);
                        Column countyCode=getColumnByAttr(boAttr,"county_code","县(区)Code",50);
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
                if(flag|| !setting.getBoolean("isAddress").equals(oldSetting.getBoolean("isAddress")) ){
                    //true新增字段 false为删除字段
                    if(flag||setting.getBoolean("isAddress")){
                        Column address=getColumnByAttr(boAttr,"address","详细地址",255);
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
    public JSONObject parseMetadata(FormBoAttr boAttr,boolean isTable) {
        JSONObject fieldJson = super.parseMetadata(boAttr, isTable);
        JSONObject setting =new JSONObject();
        if(BeanUtil.isNotEmpty(boAttr.getExtJson())) {
            //反向生成是获取关联字段属性
            JSONObject extJson = JSONObject.parseObject(boAttr.getExtJson());
            if(StringUtils.isNotEmpty(extJson.getString("city"))){
                setting.put("isCity",true);
            }else {
                setting.put("isCity",false);
            }
            if(StringUtils.isNotEmpty(extJson.getString("county"))){
                setting.put("isCounty",true);
            }else {
                setting.put("isCounty",false);
            }
            if(StringUtils.isNotEmpty(extJson.getString("address"))){
                setting.put("isAddress",true);
            }else {
                setting.put("isAddress",false);
            }
            fieldJson.put("setting",setting);
        }
        return fieldJson;
    }
}
