package com.redxun.form.bo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.datasource.DataSourceConstant;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.dboperator.ITableMeta;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.OperatorContext;
import com.redxun.dboperator.TableMetaContext;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultColumn;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.service.AttrHandlerContext;
import com.redxun.form.core.service.IAttrHandler;
import com.redxun.form.core.service.easyImpl.AttrEasyHandlerContext;
import com.redxun.form.core.service.easyImpl.IAttrEasyHandler;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库表操作类
 */
@Service
public class TableUtil {

    public  static String CONST_YES="yes";
    public  static String CONST_NO="no";
    public final static  String  OP_ADD="add";
    public final static String OP_UPD="upd";
    public final static String OP_DEL="del";

    private List<Column> getColsByAttr(FormBoAttr attr,String genMode) {
        if (FormBoEntity.GENMODE_EASYFORM.equals(genMode)) {
            IAttrEasyHandler attrEasyHandler=AttrEasyHandlerContext.getAttrHandler(attr.getControl());
            return attrEasyHandler.getColumns(attr);
        }
        IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(attr.getControl());
        return attrHandler.getColumns(attr);
    }



    /**
     * 获取生成表的前缀。
     * @return
     */
    public static String getTablePre(){
        String tablePre= SpringUtil.getProperty("prefix.table");
        return  tablePre;
    }

    /**
     * 获取生成列的前缀。
     * @return
     */
    public static String getColPre(){
        String colpre=SpringUtil.getProperty("prefix.colpre");
        return  colpre;
    }

    public static String getFieldName(String name){
        String fieldName=getColPre()+name;
        if (StringUtils.isNotEmpty(fieldName)) {
            return fieldName.toUpperCase();
        }
        return "";
    }


    /**
     * 给定bo 实体对象和 导入前的列和导入之后的列。
     * 生成差异化的SQL语句。
     * <pre>
     *  1. 当表不存在或者表中没有数据的时候，直接生成创建数据库的表。
     *  2. 否则获取变更的数据：
     *      1.增加的列处理
     *      2.删除的列处理
     *      3.更新的列处理
     * </pre>
     * @param boEnt
     * @return
     */
    public List<AlterSql> getAlterSql(FormBoEntity boEnt,boolean delField) throws Exception{
        List<AlterSql> sqlList=new ArrayList<>();
        if(boEnt.getGendb()==0) {
            return sqlList;
        }

        String dsName=boEnt.getDsAlias();
        ITableOperator tableOperator= OperatorContext.getByDsAlias(dsName);
        if(tableOperator==null) {
            throw new RuntimeException("数据源【"+dsName+"】不存在");
        }
        boolean isTableExist= tableOperator.isTableExist(boEnt.getTableName());

        //当表不存在
        if(!isTableExist){
            sqlList= getCreateSqlByBoEnt(boEnt,tableOperator);
            if(isTableExist){
                String dropSql=tableOperator.getDropTableSql(boEnt.getTableName());
                sqlList.add(0,AlterSql.getNoDelaySql(dropSql,TableUtil.OP_DEL));
            }

            return  sqlList;
        }

        //获取数据库中的表元数据
        ITableMeta iTableMeta= TableMetaContext.getByDsAlias(dsName);

        Table table=iTableMeta.getTableByName(boEnt.getTableName());
        List<Column> tableColList = table.getColumnList();

        List<FormBoAttr> curAttrs= boEnt.getBoAttrList();

        FormBoEntity orignBoEnt= boEnt.getOriginEnt();

        //处理添加的SQL语句
        handAddSql( boEnt,curAttrs,sqlList,tableColList, tableOperator );

        if(BeanUtil.isNotEmpty(orignBoEnt)){
            //处理删除SQL
            if(delField){
                handDelSql( boEnt, curAttrs,sqlList,tableColList, tableOperator);
            }
            //处理更新的SQL
            handUpdSql(boEnt,curAttrs,sqlList,tableColList,tableOperator);
        }

        return sqlList;
    }


    /**
     * 根据原列和提交的列进行比较获取变化的数据。
     * @param oldList
     * @param newList
     * @return
     */
    public List<FormBoAttr> getChangeAttrEnt(List<FormBoAttr> oldList, List<FormBoAttr> newList){
        List<FormBoAttr> rtnAttrs=new ArrayList<>();
        //使用name 字段
        Map<String,FormBoAttr> oldMap=convertToMap(oldList);

        //使用 originName
        Map<String,FormBoAttr> newMap=convertToMap(newList);
        //1.获取新的 原来的集合不包含新的属性 即为新增。
        for(FormBoAttr attr:newList){
            if(!oldMap.containsKey(attr.getName().toLowerCase())){
                attr.setType(OP_ADD);
                rtnAttrs.add(attr);
            }
        }

        //2.获取删除的 新的不包含 原来的数据。
        for(FormBoAttr attr:oldList){
            if(!newMap.containsKey(attr.getName().toLowerCase())){
                attr.setType(OP_DEL);
                rtnAttrs.add(attr);
            }
        }

        //3.获取更新的
        for(FormBoAttr attr:newList){
            String name=attr.getName().toLowerCase();
            if(oldMap.containsKey(name)){
                FormBoAttr originAttr=oldMap.get(name);
                attr.setOrignAttr(originAttr)
                .setId(originAttr.getId())
                .setEntId(originAttr.getEntId())
                .setType(OP_UPD);
                rtnAttrs.add(attr);
            }
        }
        JSONObject fieldType=new JSONObject();
        try{
            String dbType = DataSourceUtil.getCurrentDbType();
            ConfigService configService = SpringUtil.getBean(ConfigService.class);
            String dbFieldTypeStr = configService.getConfig("dbFieldType", DataSourceConstant.DEFAULT_GROUP,0);
            JSONObject fieldTypeObj= JSONObject.parseObject(dbFieldTypeStr);
            fieldType = fieldTypeObj.getJSONObject(dbType);
        }catch (Exception ex){
            MessageUtil.triggerException("获取数据库字段失败!", ExceptionUtil.getExceptionMessage(ex));
        }
        for (FormBoAttr rtnAttr : rtnAttrs) {
            if(StringUtils.isEmpty(rtnAttr.getDbFieldType())){
                rtnAttr.setDbFieldType(getDbFieldType(fieldType,rtnAttr.getDataType(),"varchar"));
            }
        }
        return rtnAttrs;
    }


    /**
     * 第一次获取表单创建的SQL语句。
     * @param boEntity
     * @return
     */
    public List<AlterSql> getCreateSqlByBoEnt(FormBoEntity boEntity,  ITableOperator tableOperator){
        List<AlterSql>  alterSqls=new ArrayList<>();
        try {
            //设置数据源
            DataSourceContextHolder.setDataSource(boEntity.getDsAlias());
            Table table= getTableByEnt( boEntity);
            List<String> list= tableOperator.getCreateTableSql(table);
            for(String sql:list){
                alterSqls.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
            }
        }catch (Exception ex){
            DataSourceContextHolder.setDefaultDataSource();
        }finally {
            DataSourceContextHolder.setDefaultDataSource();
        }
        return  alterSqls;
    }

    /**
     * 删除物理表。
     * @param boEntity
     * @param tableOperator
     * @return
     */
    public List<AlterSql> getDropSqlByBoEnt(FormBoEntity boEntity,  ITableOperator tableOperator){
        List<AlterSql>  alterSqls=new ArrayList<>();
        alterSqls.add(AlterSql.getNoDelaySql(tableOperator.getDropTableSql(boEntity.getTableName()),TableUtil.OP_DEL));
        return  alterSqls;
    }


    /**
     * 处理添加的列
     * @param boEnt
     * @param attrs
     * @param sqlList
     * @param tableColList
     * @param tableOperator
     */
    private void handAddSql(FormBoEntity boEnt, List<FormBoAttr> attrs, List<AlterSql> sqlList, List<Column> tableColList, ITableOperator tableOperator ){
        //获取添加的SQL
        List<FormBoAttr> addList=getAttrsByType(attrs,OP_ADD);
        if(BeanUtil.isEmpty(addList)) {
            return;
        }
        for(FormBoAttr attr:addList){
            List<Column> list=getColsByAttr(attr,boEnt.getGenMode());
            for(Column col:list){
                //需要先判断字段在表中是否存在
                boolean colExist =isColExist(col.getFieldName(),tableColList);
                if(colExist) {
                    continue;
                }
                List< String> addSqlList= tableOperator.getAddColumnSql(boEnt.getTableName(),col);
                for(String sql:addSqlList){
                    sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
                }
            }
        }
    }

    public List<FormBoAttr> getAttrsByType(List<FormBoAttr> list, String type){
        List<FormBoAttr> rtnList= list.stream().filter(p->type.equals(p.getType())).collect(Collectors.toList());
        return rtnList;
    }

    /**
     * 处理更新的列。
     * @param boEnt
     * @param attrs
     * @param sqlList
     * @param tableColList
     * @param tableOperator
     */
    private void handDelSql(FormBoEntity boEnt,List<FormBoAttr> attrs, List<AlterSql> sqlList, List<Column> tableColList, ITableOperator tableOperator){
        List<FormBoAttr> delList=getAttrsByType(attrs,OP_DEL);
        if(BeanUtil.isEmpty(delList)) {
            return;
        }

        for(FormBoAttr attr:delList){
            List<Column> list=getColsByAttr(attr,boEnt.getGenMode());
            for(Column col:list){
                //需要先判断字段在表中是否存在
                boolean colExist =isColExist(col.getFieldName(),tableColList);
                if(!colExist){
                    continue;
                }

                String sql= tableOperator.getDropColumnSql(boEnt.getTableName(),col.getFieldName());
                sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_DEL));
            }
        }
    }

    private boolean isColExist(String fileName,List<Column> tableColList){
        boolean colExist = false;
        for(Column tabCol : tableColList) {
            if(tabCol.getFieldName().equalsIgnoreCase(fileName)) {
                colExist = true;
                break;
            }
        }
        return  colExist;
    }



    /**
     * 将列表转成map对象。
     * @param attrs
     * @return
     */
    private static Map<String, FormBoAttr> convertToMap(List<FormBoAttr>  attrs){
        Map<String, FormBoAttr> map = attrs.stream().collect(Collectors.toMap(p->p.getName().toLowerCase(), p -> p));
        return map;
    }



    /**
     * 处理字段变更的情况。
     * 控件变更的情况有：
     * <pre>
     *     1.控件类型的变更。
     *      处理单值变双值，双值变单值的问题。
     *     2.字段长度发生变化。
     *     	长变短，短变长。
     *	   3.字段类型发生变化。
     * </pre>
     * @param boEnt
     * @param attrs
     * @param sqlList
     * @param tableColList
     * @param tableOperator
     */
    private void handUpdSql(FormBoEntity boEnt,List<FormBoAttr> attrs , List<AlterSql> sqlList, List<Column> tableColList, ITableOperator tableOperator ){
        List<FormBoAttr> updList=getAttrsByType(attrs,OP_UPD);
        if(BeanUtil.isEmpty(updList)) {
            return;
        }
        for(FormBoAttr attr: updList){
            boolean handled=  handComplexField(boEnt,attr,sqlList,tableOperator);
            if(handled) {
                continue;
            }

            handColumn( boEnt, attr,  sqlList, tableOperator);
        }
    }

    /**
     * 是否有单双值控件的转换，如果有则先进行转换。
     * @param boEnt
     * @param boAttr
     * @param sqlList
     * @param tableOperator
     * @return
     */
    private boolean handComplexField(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator){
        FormBoAttr originAttr=boAttr.getOrignAttr();
        if(FormBoEntity.GENMODE_EASYFORM.equals(boEnt.getGenMode())){
            IAttrEasyHandler attrEasyHandler=AttrEasyHandlerContext.getAttrHandler(boAttr.getControl());
            if(attrEasyHandler.getType() == 2){
                attrEasyHandler.handFromByType(boEnt,boAttr,sqlList,tableOperator,true);
            }
            else{
                //如果原来的字段为多值的情况。
                attrEasyHandler = AttrEasyHandlerContext.getAttrHandler(originAttr.getControl());
                if(attrEasyHandler.getType()==2){
                    attrEasyHandler.handFromByType(boEnt,boAttr,sqlList,tableOperator,false);
                }
                else{
                    return handDoubleSingle(boEnt,boAttr,originAttr,sqlList,tableOperator);
                }
            }
        }else {
            IAttrHandler attrHandler = AttrHandlerContext.getAttrHandler(boAttr.getControl());
            //多值(非双值)
            if (attrHandler.getType() == 2) {
                attrHandler.handFromByType(boEnt, boAttr, sqlList, tableOperator,true);
            }
            else{
                //如果原来的字段为多值的情况。
                attrHandler = AttrHandlerContext.getAttrHandler(originAttr.getControl());
                if(attrHandler.getType()==2){
                    attrHandler.handFromByType(boEnt, boAttr, sqlList, tableOperator,false);
                }
                else{
                    return handDoubleSingle(boEnt,boAttr,originAttr,sqlList,tableOperator);
                }
            }
        }
        return  true;

    }


    private Boolean handDoubleSingle(FormBoEntity boEnt,FormBoAttr boAttr,FormBoAttr originAttr,List<AlterSql> sqlList,ITableOperator tableOperator){
        if( boAttr.getIsSingle().equals( originAttr.getIsSingle())) {
            return false;
        }
        if(boAttr.single()){
            handToSingle( boEnt, boAttr, sqlList,tableOperator);
        }
        //原来是单值，改成双值处理。
        else{
            handFromSingle( boEnt, boAttr, sqlList,tableOperator);
        }
        return  true;
    }




    /**
     *  原来是双值，改成单值
     *  1.需要删除一个列
     *  2.对列进行处理。
     *      数据类型是否变化
     *          1.没有变化
     *              1.长度变长，进行处理
     *              2.长度变短
     *                  生成变短的语句，延后执行
     *          2.类型变化
     *              生成语句延后执行。
     * @param boEnt
     * @param boAttr
     * @param sqlList
     */
    private void handToSingle(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator){
        String colPre=getColPre();
        String name=colPre + boAttr.getName() +"_name";
        String dropNameSql=tableOperator.getDropColumnSql(boEnt.getTableName(),name);
        sqlList.add(AlterSql.getDelaySql(dropNameSql,TableUtil.OP_DEL));

        handColumn( boEnt, boAttr,  sqlList, tableOperator);
    }

    /**
     * 判断数据的列是否改变。
     * <pre>
     *     判断列类型是否改变。
     *         如果没变化
     *         判断类型的长度是否发生变化。
     * </pre>
     * @param attr1
     * @param attr2
     * @return
     */
    private boolean isColChange(FormBoAttr attr1, FormBoAttr attr2){

        if(attr1.getDataType().equalsIgnoreCase(attr2.getDataType()) ){
            if( Column.COLUMN_TYPE_VARCHAR.equalsIgnoreCase(attr1.getDataType())){
                if(!attr1.getLength().equals(attr2.getLength())){
                    return true;
                }
            }
            if( Column.COLUMN_TYPE_NUMBER.equalsIgnoreCase(attr1.getDataType())){
                if(!attr1.getLength().equals(attr2.getLength()) || !attr1.getDecimalLength().equals(attr2.getDecimalLength())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 处理列的变化。
     * <pre>
     *     1.类型没有变化的处理
     *     2.类型发生变化的处理。
     * </pre>
     * @param boEnt
     * @param boAttr
     * @param sqlList
     * @param tableOperator
     */
    private void handColumn(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator){
        //String colPre=getColPre();
        FormBoAttr originAttr=boAttr.getOrignAttr();
        //数据类型是否改变
        String orginType=originAttr.getDataType();
        String curType=boAttr.getDataType();
        if(orginType.equalsIgnoreCase(curType)){
            if(orginType.equals(Column.COLUMN_TYPE_VARCHAR)){
                handVarchar(boEnt,boAttr,sqlList,tableOperator,false);
            }
            else {
                handNumber(boEnt,boAttr,sqlList,tableOperator,false);
            }
        }
        else{
            Column originCol= getColumnByAttr(originAttr,false);
            Column column= getColumnByAttr(boAttr,false);
            //处理数据类型变化的情况。
            handDbTypeChange( boEnt, originCol, column, sqlList, tableOperator);
        }
        //字段名变化
        //当前FieldName为空是默认为表单生成则比较Name
        if(StringUtils.isNotEmpty(boAttr.getFieldName())){
            if(!originAttr.getFieldName().equals(boAttr.getFieldName())){
                Column originCol= getColumnByAttr(originAttr,false);
                Column column= getColumnByAttr(boAttr,false);
                handFieldName(boEnt,column,originCol,sqlList,tableOperator);
            }
        }else {
            if(!originAttr.getName().equals(boAttr.getName())){
                Column originCol= getColumnByAttr(originAttr,false);
                Column column= getColumnByAttr(boAttr,false);
                handFieldName(boEnt,column,originCol,sqlList,tableOperator);
            }
        }
    }

    private void handFromSingle(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator){
        //获取数据库中的表元数据
        ITableMeta iTableMeta= TableMetaContext.getByDsAlias(boEnt.getDsAlias());
        Table table=iTableMeta.getTableByName(boEnt.getTableName());
        List<Column> tableColList = table.getColumnList();
        //1.添加一个列
        Column addcolumn= getColumnByAttr( boAttr, true);
        List<String> nameSqls=tableOperator.getAddColumnSql(boEnt.getTableName(),addcolumn);
        for(String sql:nameSqls){
            //判断字段是否存在
            boolean colExist =isColExist(addcolumn.getFieldName(),tableColList);
            if(colExist) {
                continue;
            }
            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_ADD));
        }

        handColumn( boEnt, boAttr,  sqlList, tableOperator);
    }


    /**
     * 处理字符串列。
     *  判断长度是否发生变化。
     *      1.长度增加处理
     *      2.长度减小处理
     * @param boEnt
     * @param boAttr
     * @param sqlList
     * @param tableOperator
     * @param isComplex
     */
    private void handVarchar(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator, boolean isComplex){

        FormBoAttr originAttr=boAttr.getOrignAttr();
        boolean change= isColChange(boAttr,originAttr);
        if(!change) {
            return;
        }
        //新的长度比原来大
        Column column= getColumnByAttr( boAttr, isComplex);
        List<String> list= tableOperator.getUpdateColumnSql(boEnt.getTableName(),column.getFieldName(),column);
        if(boAttr.getLength()>originAttr.getLength()){
            for(String sql :list){
                sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_UPD));
            }
        }
        else if(boAttr.getLength()<originAttr.getLength()){
            for(String sql :list){
                sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_UPD));
            }
        }
    }

    /**
     * 处理数字。
     *    1.长度增加处理
     *    2.长度减小处理
     * @param boEnt
     * @param boAttr
     * @param sqlList
     * @param tableOperator
     * @param isComplex
     */
    private void handNumber(FormBoEntity boEnt, FormBoAttr boAttr, List<AlterSql> sqlList, ITableOperator tableOperator, boolean isComplex){
        FormBoAttr originAttr=boAttr.getOrignAttr();
        boolean change= isColChange(boAttr,originAttr);
        if(!change) {
            return;
        }
        //新的长度比原来大
        Column column= getColumnByAttr( boAttr, isComplex);
        List<String> list= tableOperator.getUpdateColumnSql(boEnt.getTableName(),column.getFieldName(),column);
        if(boAttr.getLength()>=originAttr.getLength() && boAttr.getDecimalLength() >=originAttr.getDecimalLength()){
            for(String sql :list){
                sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_UPD));
            }
        }
        else {
            for(String sql :list){
                sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_UPD));
            }
        }
    }

    /**
     * 变更列类型。
     * <pre>
     *     1.将需要备份的列改名改成 name +"_BAK"
     *     2.创建新的列
     *     3.将数据copy到新的列。
     *     4.删除备份列
     * </pre>
     * @param boEnt
     * @param originCol
     * @param column
     * @param sqlList
     */
    private void handDbTypeChange(FormBoEntity boEnt, Column originCol, Column column, List<AlterSql> sqlList, ITableOperator tableOperator){
        //1.将列备份修改列名。
        String tableName=boEnt.getTableName();
        String originName=originCol.getFieldName();
        String bakField=originCol.getFieldName() + "_BAK";
        originCol.setFieldName(bakField);
        List<String> list=tableOperator.getUpdateColumnSql(tableName,originName,originCol);
        for (String sql:list){
            sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_UPD)) ;
        }

        //2.创建新的列
        list=tableOperator.getAddColumnSql(tableName,column);
        for (String tmp:list){
            sqlList.add(AlterSql.getDelaySql(tmp,TableUtil.OP_ADD)) ;
        }

        //4.将数据copy到新的列
        String sql="UPDATE " + tableName +" set "+originName+"=" + bakField +";";
        sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_UPD));
        //5.删除备份列
        sql=tableOperator.getDropColumnSql(tableName,bakField);

        sqlList.add(AlterSql.getDelaySql(sql,TableUtil.OP_DEL));
    }


    /**
     * 根据bo实体获取表对象。
     * @param boEnt
     * @return
     */
    private Table getTableByEnt(FormBoEntity boEnt){
        Table table=new DefaultTable();

        table.setComment(boEnt.getName());
        table.setTableName(boEnt.getTableName());

        List<Column> cols=new ArrayList<Column>();
        List<FormBoAttr> boAttrs=  boEnt.getBoAttrList();

        for(Iterator<FormBoAttr> it=boAttrs.iterator();it.hasNext();){
            FormBoAttr attr=it.next();
            if( (TableUtil.OP_DEL.equals( attr.getType()) || "default".equals(attr.getDataType())) && !FormBoEntity.GENMODE_DB.equals(boEnt.getGenMode())) {
                continue;
            }
            List<Column> col= getColsByAttr(attr,boEnt.getGenMode());
            cols.addAll(col);
        }
        //非数据库
        if(!FormBoEntity.GENMODE_DB.equals(boEnt.getGenMode())){
            List<Column> columns= getCommonCols(cols);
            table.setColumnList(columns);
        }else {
            List<Column> list=new ArrayList<Column>();
            list.addAll(cols);
            table.setColumnList(list);
        }
        return table;
    }



    /**
     * 在cols 增加通用列。
     * @param cols
     * @return
     */
    public static List<Column> getCommonCols(List<Column> cols){
        Boolean createField = SysPropertiesUtil.getBoolean("createField");
        JSONObject fieldType=new JSONObject();
        try{
            String dbType = DataSourceUtil.getCurrentDbType();
            ConfigService configService = SpringUtil.getBean(ConfigService.class);
            String dbFieldTypeStr = configService.getConfig("dbFieldType", DataSourceConstant.DEFAULT_GROUP,0);
            JSONObject fieldTypeObj= JSONObject.parseObject(dbFieldTypeStr);
            fieldType = fieldTypeObj.getJSONObject(dbType);
        }catch (Exception ex){
            MessageUtil.triggerException("获取数据库字段失败!", ExceptionUtil.getExceptionMessage(ex));
        }

        List<Column> list=new ArrayList<Column>();

        Column colPk=new DefaultColumn();
        colPk.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colPk.setDbFieldType(getDbFieldType(fieldType,colPk.getColumnType(),"varchar"));
        colPk.setCharLen(64);
        colPk.setIsPk(true);
        colPk.setComment("主键");
        colPk.setFieldName(FormBoEntity.FIELD_PK);

        Column colFk=new DefaultColumn();
        colFk.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colFk.setDbFieldType(getDbFieldType(fieldType,colFk.getColumnType(),"varchar"));
        colFk.setCharLen(64);
        colFk.setComment("外键");
        colFk.setFieldName(FormBoEntity.FIELD_FK);

        Column colUser=new DefaultColumn();
        colUser.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colUser.setDbFieldType(getDbFieldType(fieldType,colUser.getColumnType(),"varchar"));
        colUser.setCharLen(64);
        colUser.setComment("创建人ID");
        colUser.setFieldName(FormBoEntity.FIELD_CREATE_BY);

        Column colCreateTime=new DefaultColumn();
        colCreateTime.setColumnType(Column.COLUMN_TYPE_DATE);
        colCreateTime.setDbFieldType(getDbFieldType(fieldType,colCreateTime.getColumnType(),"datetime"));
        colCreateTime.setComment("创建时间");
        colCreateTime.setFieldName(FormBoEntity.FIELD_CREATE_TIME);

        Column colUpdTime=new DefaultColumn();
        colUpdTime.setColumnType(Column.COLUMN_TYPE_DATE);
        colUpdTime.setDbFieldType(getDbFieldType(fieldType,colUpdTime.getColumnType(),"datetime"));
        colUpdTime.setComment("更新时间");
        colUpdTime.setFieldName(FormBoEntity.FIELD_UPDATE_TIME);

        Column colUpdBy=new DefaultColumn();
        colUpdBy.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colUpdBy.setDbFieldType(getDbFieldType(fieldType,colUpdBy.getColumnType(),"varchar"));
        colUpdBy.setCharLen(64);
        colUpdBy.setComment("更新人");
        colUpdBy.setFieldName(FormBoEntity.FIELD_UPDATE_BY);
        //租户ID
        Column colTenantId=new DefaultColumn();
        colTenantId.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colTenantId.setDbFieldType(getDbFieldType(fieldType,colTenantId.getColumnType(),"varchar"));
        colTenantId.setCharLen(64);
        colTenantId.setComment("租户ID");
        colTenantId.setFieldName(FormBoEntity.FIELD_TENANT);
        //实例ID
        Column instId=new DefaultColumn();
        instId.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        instId.setDbFieldType(getDbFieldType(fieldType,instId.getColumnType(),"varchar"));
        instId.setCharLen(64);
        instId.setComment("流程实例ID");
        instId.setFieldName(FormBoEntity.FIELD_INST);

        //draft(草稿),runing(运行),complete(完成)
        Column status=new DefaultColumn();
        status.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        status.setDbFieldType(getDbFieldType(fieldType,status.getColumnType(),"varchar"));
        status.setCharLen(20);
        status.setComment("状态");
        status.setFieldName(FormBoEntity.FIELD_INST_STATUS_);

        Column colGroup=new DefaultColumn();
        colGroup.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colGroup.setDbFieldType(getDbFieldType(fieldType,colGroup.getColumnType(),"varchar"));
        colGroup.setCharLen(64);
        colGroup.setComment("组ID");
        colGroup.setFieldName(FormBoEntity.FIELD_CREATE_DEP);

        //增加父ID字段
        Column colParent=new DefaultColumn();
        colParent.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colParent.setDbFieldType(getDbFieldType(fieldType,colParent.getColumnType(),"varchar"));
        colParent.setCharLen(64);
        colParent.setComment("父ID");
        colParent.setFieldName(FormBoEntity.FIELD_PARENTID);

        //增加版本字段
        Column colVersion=new DefaultColumn();
        colVersion.setColumnType(Column.COLUMN_TYPE_NUMBER);
        colVersion.setDbFieldType(getDbFieldType(fieldType,colVersion.getColumnType(),"decimal"));
        colVersion.setIntLen(14);
        colVersion.setDecimalLen(0);
        colVersion.setComment("版本号");
        colVersion.setFieldName(FormBoEntity.FIELD_UPDATE_VERSION);
        colVersion.setDefaultValue("1");

        Column colCompany=new DefaultColumn();
        colCompany.setColumnType(Column.COLUMN_TYPE_VARCHAR);
        colCompany.setDbFieldType(getDbFieldType(fieldType,colVersion.getColumnType(),"varchar"));
        colCompany.setCharLen(24);
        colCompany.setComment("公司ID");
        colCompany.setFieldName(FormBoEntity.FIELD_COMPANY);
        colCompany.setDefaultValue("0");

        list.add(colPk);
        list.add(colFk);
        list.add(colParent);
        list.add(colVersion);

        list.addAll(cols);

        list.add(instId);
        list.add(status);

        list.add(colTenantId);

        list.add(colCreateTime);
        list.add(colUser);

        list.add(colUpdBy);
        list.add(colUpdTime);

        list.add(colGroup);
        list.add(colCompany);

        if(createField){
            //创建人名称
            Column colUserName=new DefaultColumn();
            colUserName.setColumnType(Column.COLUMN_TYPE_VARCHAR);
            colUserName.setDbFieldType(getDbFieldType(fieldType,colUserName.getColumnType(),"varchar"));
            colUserName.setCharLen(64);
            colUserName.setComment("创建人名称");
            colUserName.setFieldName(FormBoEntity.FIELD_CREATE_BY_NAME);
            list.add(colUserName);

            //更新人名称
            Column colUpdName=new DefaultColumn();
            colUpdName.setColumnType(Column.COLUMN_TYPE_VARCHAR);
            colUpdName.setDbFieldType(getDbFieldType(fieldType,colUpdName.getColumnType(),"varchar"));
            colUpdName.setCharLen(64);
            colUpdName.setComment("更新人名称");
            colUpdName.setFieldName(FormBoEntity.FIELD_UPDATE_BY_NAME);
            list.add(colUpdName);
        }

        return list;
    }

    public static Column getColumnByAttr(FormBoAttr attr,  boolean isComplex){
        Column col=new DefaultColumn();

        String name=isComplex ? FormBoEntity.COMPLEX_NAME:"";
        if(StringUtils.isNotEmpty(attr.getFieldName())){
            col.setFieldName(attr.getFieldName().toUpperCase()+ name);
        }else {
            col.setFieldName(getFieldName(attr.getName() + name));
        }
        //标准字段
        if("default".equals(attr.getDataType()) && StringUtils.isNotEmpty(attr.getExtJson())){
            JSONObject extJson = JSONObject.parseObject(attr.getExtJson());
            String mappingField = extJson.getString("mappingField");
            String[] dateArr=new String[]{FormBoEntity.FIELD_CREATE_TIME,FormBoEntity.FIELD_UPDATE_TIME};
            if(Arrays.asList(dateArr).contains(mappingField)){
                col.setColumnType(Column.COLUMN_TYPE_DATE);
            }else if(FormBoEntity.FIELD_UPDATE_VERSION.equals(mappingField)){
                col.setColumnType(Column.COLUMN_TYPE_NUMBER);
            }else {
                col.setColumnType(Column.COLUMN_TYPE_VARCHAR);
            }
        }else {
            col.setColumnType(attr.getDataType());
        }
        col.setDbFieldType(attr.getDbFieldType());
        String comment=attr.getComment();
        //获取双值字段绑定的关联字段
        String ref=TableUtil.getRefField(attr);
        if(!attr.single() && !isComplex && StringUtils.isEmpty(ref)){
            comment+="ID";
        }

        col.setComment(comment);

        if(Column.COLUMN_TYPE_VARCHAR.equals(col.getColumnType())){
            col.setCharLen(attr.getLength());
        }
        else if(Column.COLUMN_TYPE_NUMBER.equals(col.getColumnType())){
            col.setIntLen(attr.getLength()-attr.getDecimalLength());
            col.setDecimalLen(attr.getDecimalLength());
        }
        //设置主键
        if(BeanUtil.isNotEmpty(attr.getIsPk()) && attr.getIsPk()==1){
            col.setIsPk(true);
        }
        return col;
    }

    /**
     *
     * @param fieldType nacos配置
     * @param columnType 字段类型
     * @param defType 默认数据库字段类型
     * @return
     */
    private static String getDbFieldType(JSONObject fieldType,String columnType, String defType){
        String dbFieldType=defType;
        if(BeanUtil.isNotEmpty(fieldType)){
            JSONArray jsonArray = fieldType.getJSONArray(columnType);
            if(BeanUtil.isNotEmpty(jsonArray)&& jsonArray.size()>0){
                dbFieldType = jsonArray.getString(0);
            }
        }
        return dbFieldType;
    }
    /**
     *获取双值字段的关联字段
     * @param attr 字段实体属性
     * @return
     */
    public static String getRefField(FormBoAttr attr){
        if(attr.single()){
            return "";
        }
        //获取双值字段绑定的关联字段
        String ref="";
        String extJson = attr.getExtJson();
        if(StringUtils.isNotEmpty(extJson)){
            JSONObject jsonObject = JSON.parseObject(extJson);
            ref = jsonObject.getString("ref");
        }
        return  ref;
    }

    /**
     * 处理字段名变化
     * @param boEnt
     * @param column
     * @param originCol
     * @param sqlList
     * @param tableOperator
     */
    private void handFieldName(FormBoEntity boEnt, Column column, Column originCol, List<AlterSql> sqlList, ITableOperator tableOperator) {
        List<String> list= tableOperator.getUpdateColumnSql(boEnt.getTableName(),originCol.getFieldName(),column);
        for (String sql:list){
            sqlList.add(AlterSql.getNoDelaySql(sql,TableUtil.OP_UPD)) ;
        }
    }


}
