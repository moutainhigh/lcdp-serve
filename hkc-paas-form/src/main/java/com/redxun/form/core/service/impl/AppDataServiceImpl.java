package com.redxun.form.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.OperatorContext;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.mapper.FormBoAttrMapper;
import com.redxun.form.bo.mapper.FormBoEntityMapper;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.controller.FormDataSourceDefController;
import com.redxun.form.core.entity.FormDataSourceDef;
import com.redxun.form.core.mapper.FormDataSourceDefMapper;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.seata.sqlparser.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * @Description: 与应用相关的数据处理
 * @Author: Elwin ZHANG
 * @Date: 2021/6/25 16:36
 **/
@Slf4j
@Service
public class AppDataServiceImpl {
    protected Logger logger = LoggerFactory.getLogger(AppDataServiceImpl.class);
    @Resource
    private CommonDao commonDao;
    @Resource
    FormBoEntityMapper formBoEntityMapper;
    @Resource
    FormBoAttrMapper formBoAttrMapper;
    @Resource
    FormDataSourceDefMapper formDataSourceDefMapper;
    @Resource
    FormDataSourceDefController formDataSourceDefController;
    @Resource
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    //导出数据Key的前缀
    private static final String KEY_PREFIX = "2.f.";
    //默认数据源名
    private static final String DEFAULT_DS = "form";
    //默认的主键列名
    private static final String DEFAULT_PK_Name = "ID_";

    private static final String TABLE_ENTITY = "form_bo_entity";
    private static final String TABLE_ENT_ATTR= "form_bo_attr";
    private static final String TABLE_DATASOURCE = "form_datasource_def";
    private static final String TABLE_FORM_PC= "form_pc";
    private static final String TABLE_FORM_RELATION= "form_bo_relation";
    private static final String TABLE_FORM_SOLUTION= "form_solution";
    private static final String  COL_DATASOURCE_ALIAS="ALIAS_";
    //应用相关表
    private static final String[] appTables = {"form_datasource_def@ID_@数据源定义",
            "form_bo_entity@ID_@业务实体",
            "form_bo_attr@ID_@业务实体属性",
            "form_bo_def@ID_@业务模型",
            "form_bo_relation@ID_@业务实体关系",
            "form_solution@ID_@表单方案",
            "form_table_formula@ID_@表间公式",
            "form_pc@ID_@PC表单",
            "form_mobile@ID_@手机表单",
            "form_custom@ID_@表单布局定制",
            "form_def_permission@ID_@表单使用权限",
            "form_pdf_template@ID_@表单PDF模板",
            "form_permission@ID_@表单权限配置",
            "form_query_strategy@ID_@查询策略",
            "form_template@ID_@表单模版",
            "form_bo_list@ID_@数据列表/对话框",
            "form_custom_query@ID_@自定义SQL查询",
            "form_save_export@ID_@EXCEL导出配置",
            "form_rule@ID_@表单验证规则",
            "form_reg_lib@REG_ID_@正则表达式替换",
            "form_print_lodop@ID_@表单套打模板",
            "form_business_solution@ID_@表单业务方案",
            "form_chart_data_model@ID_@图表数据模型"};

    /**
     * @param appId 应用ID
     * @param  exportCustomTables 是否导出自定义实体的数据
     * @Description: 查询本数据库中应用相关的数据
     **/
    public Map<String, String> query(String appId, boolean exportCustomTables) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(appId)) {
            return map;
        }
        int i = 10;
        //循环查询相关的表名数据，转换为JSON字符串，并添加到map中
        for (String table : appTables) {
            String[] tableProp = table.split("@");
            String tableName = tableProp[0];
            String pkName = tableProp[1];
            String tableDesc = tableProp[2];
            String sql = "select * from " + tableName + " where APP_ID_ ='" + appId + "'";
            List data = commonDao.query(sql);
            if (data == null || data.size() == 0) {
                continue;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dataSource", DEFAULT_DS);
            jsonObject.put("entityId", "");
            jsonObject.put("tableName", tableName);
            jsonObject.put("tableDesc", tableDesc);
            jsonObject.put("pk", pkName);
            jsonObject.put("data", data);
            String key = KEY_PREFIX + i + "." + tableName;
            map.put(key, JSONObject.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd HH:mm:ss"));
            i++;
        }
        if(exportCustomTables) {
            queryCustomTable(appId, map, i);
        }
        return map;
    }

    /**
     * @Description: 导入本数据库中应用相关的数据
     * @param data 应用相关数据
     **/
    public JsonResult install(String  data)  {
        JSONObject object= JSON.parseObject(data);
        String tenantId=object.getString("tenantId");
        String overrides=object.getString("overrides");
        JSONArray arrData=object.getJSONArray("data");
        HashMap logMap=new HashMap();
        JsonResult resultLast=JsonResult.Success();
        //检查数据源，现在不导入数据源了  2021-10-11
        JSONArray dsTable=getTableData(arrData,TABLE_DATASOURCE,true);
        if(!isExistAllDS(dsTable)) {
            return JsonResult.Fail("数据源不存在，请先手工新建同名的数据源！");
        }
        //导入PC表单
        importTable(TABLE_FORM_PC,arrData,tenantId,overrides,logMap);
        //导入表单方案
        importTable(TABLE_FORM_SOLUTION,arrData,tenantId,overrides,logMap);
        //导入表单关系
        importTable(TABLE_FORM_RELATION,arrData,tenantId,overrides,logMap);

        //导入用户自实义实体的表
        JSONArray entData=getTableData(arrData,TABLE_ENTITY,true);
        if(entData!=null) {
            JSONArray entAttrData = getTableData(arrData, TABLE_ENT_ATTR,true);
            JsonResult result=importBoEntity(entData,entAttrData,tenantId,overrides,logMap);
            if(!result.isSuccess()){
                return  result;
            }
            resultLast.setData(result.getData());
        }
        //是否存在手动执行的SQL
        boolean hasAlterSqls=false;
        if(resultLast.getData()!=null) {
            List<AlterSql> sqls = (List<AlterSql>) resultLast.getData();
            if(sqls.size()>0){
                hasAlterSqls=true;
            }
        }
        //循环处理其他剩下的表
        for (Object obj : arrData) {
            JSONObject tableObj =(JSONObject)JSONObject.toJSON(obj);
            String entityId=tableObj.getString("entityId");
            //如果实体有变化，则不再更新实体数据
            if(StringUtils.isNotEmpty(entityId) && hasAlterSqls){
                continue;
            }
            List list=importTableData(tableObj,tenantId,overrides);
            if(list.size()>0){
                String tableName=tableObj.getString("tableName");
                logMap.put(tableName,list);
            }
        }
        if(logMap.size()>0){
            String detail="Form库被覆盖的记录：" + JSONObject.toJSONString(logMap);
            LogContext.put(Audit.DETAIL, detail);
        }

        return resultLast;
    }

    private void  importTable(String tableName,JSONArray arrData, String tenantId, String overrides,HashMap logMap){
        JSONArray tableData=getTableData(arrData,tableName,true);
        if(tableData==null){
            return;
        }
        JSONObject pcObj=new JSONObject();
        pcObj.put("data",tableData);
        pcObj.put("tableName",tableName);
        pcObj.put("pk",DEFAULT_PK_Name);
        pcObj.put("dataSource",DEFAULT_DS);
        List list=importTableData(pcObj,tenantId,overrides);
        if(list.size()>0){
            logMap.put(tableName,list);
        }
    }
    /**
    * @Description: 导入实体及实体属性
     *@param  jsonEntity 实体表数据
     *@param  jsonEntAttr 实体属性表数据
     *@param  tenantId 新的租户ID
     *@param  overrides 相同ID的记录是否覆盖
    * @Author: Elwin ZHANG  @Date: 2021/8/2 15:14
    **/
    private  JsonResult importBoEntity(JSONArray jsonEntity,JSONArray jsonEntAttr,String tenantId, String overrides,HashMap logMap ){
        if(jsonEntAttr==null){
            jsonEntAttr=new JSONArray();
        }
        List logDetail=new ArrayList();
        List<AlterSql> alterSqls=new ArrayList<>();
        //循环将JSON转换成实体对象，调用实体保存方法
        for (Object obj : jsonEntity) {
            JSONObject rowObj = (JSONObject) JSONObject.toJSON(obj);
            String pkId=rowObj.getString(DEFAULT_PK_Name);
            //检查记录是否存在
            FormBoEntity oldEntity= formBoEntityMapper.selectById(pkId);
            boolean isInsert=(oldEntity==null);
            //忽略存在的同ID的实体
            if(!isInsert && overrides.indexOf("'" + TABLE_ENTITY + "'")<0) {
                continue;
            }
            FormBoEntity boEnt=new FormBoEntity();
            boEnt.setId(pkId);
            boEnt.setName(rowObj.getString("NAME_"));
            boEnt.setAlias(rowObj.getString("ALIAS_"));
            boEnt.setGenMode(rowObj.getString("GEN_MODE_"));
            boEnt.setIsMain(rowObj.getInteger("IS_MAIN_"));
            boEnt.setTreeId(rowObj.getString("TREE_ID_"));
            boEnt.setIdField(rowObj.getString("ID_FIELD_"));
            boEnt.setParentField(rowObj.getString("PARENT_FIELD_"));
            boEnt.setVersionField(rowObj.getString("VERSION_FIELD_"));
            boEnt.setGendb(rowObj.getInteger("GENDB_"));
            boEnt.setDsAlias(rowObj.getString("DS_ALIAS_"));
            boEnt.setDsName(rowObj.getString("DS_NAME_"));
            boEnt.setTableName(rowObj.getString("TABLE_NAME_"));
            boEnt.setAppId(rowObj.getString("APP_ID_"));
            boEnt.setGendef(0); //不生成实体模型
            boEnt.setTenantId(tenantId);
            boEnt.setType(TableUtil.OP_ADD);
            if(!isInsert){
                boEnt.setType(TableUtil.OP_UPD);
                //将旧的实体信息注入到实体中
                QueryWrapper wrapper=new QueryWrapper();
                wrapper.eq("ENT_ID_",pkId);
                 List<FormBoAttr> oldAttrs=formBoAttrMapper.selectList(wrapper);
                 oldEntity.setBoAttrList(oldAttrs);
                 boEnt.setOriginEnt(oldEntity);
            }
            addEntAttrs(boEnt,jsonEntAttr,overrides);
            //addOtherAttrs(boEnt);
            try {
                JsonResult result = formBoEntityServiceImpl.importBoEnt(boEnt, isInsert);
                if (!result.isSuccess()) {
                    return  result;
                }
                if(!isInsert) {
                    logDetail.add(boEnt);
                    Object data=result.getData();
                    if(data!=null ){
                        alterSqls.addAll((List<AlterSql>)data);
                    }
                }
            }catch (Exception e){
                String message="导入实体【" + boEnt.getTableName() + "】出错:" +  e.getMessage();
                return JsonResult.Fail(message);
            }
        }
        if(logDetail.size()>0){
            logMap.put(TABLE_ENTITY,logDetail);
        }
        JsonResult resultLast=JsonResult.Success();
        resultLast.setData(alterSqls);
        return resultLast;
    }
    /**
    * @Description: 添加实体被忽略的属性，或者后来又增加的属性；防止被删除
    * @param entity 实体
    * @Author: Elwin ZHANG  @Date: 2021/8/3 10:29
    **/
    private  void  addOtherAttrs(FormBoEntity entity){
        List<String> attrIds=new ArrayList<>();
        for(FormBoAttr attr : entity.getBoAttrList()){
            attrIds.add(attr.getId());
        }
        //查询其他未被附加的属性
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ENT_ID_ ", entity.getId());
        if(attrIds.size()>0) {
            wrapper.notIn("ID_",attrIds);
        }
        List<FormBoAttr> list=formBoAttrMapper.selectList(wrapper);
        if(list==null || list.size()==0){
            return;
        }
        //添加查询结果
        for(FormBoAttr attr: list){
            attr.setType(TableUtil.OP_DEL);
            entity.addAttr(attr);
        }
    }

    /**
    * @Description:  获取实体关联的属性并加入到实体对象中
    * @param entity 实体对象
     * @param  jsonEntAttr 实体属性数组
     *@param  overrides 相同ID的记录是否覆盖
    * @Author: Elwin ZHANG  @Date: 2021/8/2 16:45
    **/
    private  void  addEntAttrs(FormBoEntity entity,JSONArray jsonEntAttr, String overrides){
        if(jsonEntAttr==null){
            return;
        }
        //循环处理
        for (Object obj : jsonEntAttr) {
            JSONObject rowObj = (JSONObject) JSONObject.toJSON(obj);
            String pkId = rowObj.getString(DEFAULT_PK_Name);
            String entId=rowObj.getString("ENT_ID_");
            //不匹配当前实体ID则继续
            if(!entity.getId().equals(entId)){
                continue;
            }
            //检查记录是否存在
            FormBoAttr oldAttr=formBoAttrMapper.selectById(pkId);
            //忽略存在的同ID的实体属性
            if(oldAttr!=null && overrides.indexOf("'" + TABLE_ENT_ATTR + "'")<0) {
                continue;
            }
            FormBoAttr boAttr=new FormBoAttr();
            boAttr.setId(pkId);
            boAttr.setEntId(entId);
            boAttr.setName(rowObj.getString("NAME_"));
            boAttr.setFieldName(rowObj.getString("FIELD_NAME_"));
            boAttr.setComment(rowObj.getString("COMMENT_"));
            boAttr.setDataType(rowObj.getString("DATA_TYPE_"));
            boAttr.setLength(rowObj.getInteger("LENGTH_"));
            boAttr.setDecimalLength(rowObj.getInteger("DECIMAL_LENGTH_"));
            boAttr.setControl(rowObj.getString("CONTROL_"));
            boAttr.setExtJson(rowObj.getString("EXT_JSON_"));
            boAttr.setIsSingle(rowObj.getInteger("IS_SINGLE_"));
            boAttr.setSn(rowObj.getInteger("SN_"));
            boAttr.setAppId(entity.getAppId());
            boAttr.setTenantId(entity.getTenantId());
            boAttr.setType(TableUtil.OP_ADD);
            if(oldAttr!=null){
                boAttr.setType(TableUtil.OP_UPD);
                boAttr.setOrignAttr(oldAttr);
            }
            entity.addAttr(boAttr);
        }
    }

    /**
    * @Description:  从数组中找到某表名对应的数据，并从数组中移除
    * @param arrData  表数组
     * @param     tableName 指定的表名
     * @param  isRemove 是否从数组中移除找到的元素
    * @Author: Elwin ZHANG  @Date: 2021/8/2 15:04
    **/
    private  JSONArray getTableData(JSONArray arrData,String tableName,boolean isRemove){
        JSONObject dsTable=null;
        //循环找到相关表，并从数组中移除
        for (Object obj : arrData) {
            JSONObject tableObj =(JSONObject)JSONObject.toJSON(obj);
            String curName=tableObj.getString("tableName");
            if(tableName.equals(curName)){
                dsTable=tableObj;
                if(isRemove) {
                    arrData.remove(tableObj);
                }
                break;
            }
        }
        if(dsTable==null){
            return null;
        }
        return  dsTable.getJSONArray("data");
    }
    /**
    * @Description:  检查数据源是否全部存在相同别名的记录
    * @param arrData 表记录
    * @Author: Elwin ZHANG  @Date: 2021/10/11 17:40
    **/
    private boolean isExistAllDS(JSONArray arrData){
        if(arrData==null || arrData.size()==0){
            return true;
        }
        for (Object obj : arrData) {
            JSONObject rowObj = (JSONObject) obj;
            String alias = rowObj.getString(COL_DATASOURCE_ALIAS);
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq(COL_DATASOURCE_ALIAS,alias);
            FormDataSourceDef def=formDataSourceDefMapper.selectOne(wrapper);
            if(def==null){
                return false;
            }
        }
        return true;
    }
    /**
    * @Description: 导入数据源，数据源除了导入记录之外，还需要创建数据源，所以要特殊处理
    * @param arrData 表数组
     *@param  tenantId 新的租户ID
     *@param  overrides 相同ID的记录是否覆盖
    * @Author: Elwin ZHANG  @Date: 2021/7/30 11:51
    **/
    private JsonResult importDataSource(JSONArray arrData, String tenantId, String overrides) throws Exception {
        JSONArray dsTable=getTableData(arrData,TABLE_DATASOURCE,true);
        if(dsTable==null){
            return JsonResult.Success();
        }
        //循环将JSON对象转换成数据源对象，调用数据源的保存方法
        for (Object obj : dsTable) {
            JSONObject rowObj =(JSONObject)JSONObject.toJSON(obj);
            FormDataSourceDef dsDef=new FormDataSourceDef();
            String pkId=rowObj.getString(DEFAULT_PK_Name);
            //检查记录是否存在
            String sql ="select 1 as qty from " + TABLE_DATASOURCE + " where " + DEFAULT_PK_Name + " = '" + pkId + "'";
            Object isExist=commonDao.queryOne(sql);

            //忽略存在的数据源
            if(isExist!=null && overrides.indexOf("'" + TABLE_DATASOURCE + "'")<0) {
                continue;
            }
            dsDef.setId(pkId);
            dsDef.setName(rowObj.getString("NAME_"));
            dsDef.setAlias(rowObj.getString("ALIAS_"));
            dsDef.setEnable(rowObj.getString("ENABLE_"));
            dsDef.setSetting(rowObj.getString("SETTING_"));
            dsDef.setDbType(rowObj.getString("DB_TYPE_"));
            dsDef.setInitOnStart(rowObj.getString("INIT_ON_START_"));
            dsDef.setAppName(rowObj.getString("APP_NAME_"));
            dsDef.setAppId(rowObj.getString("APP_ID_"));
            dsDef.setTenantId(tenantId);
            JsonResult result2=formDataSourceDefController.importDataSource(dsDef,isExist==null);
            if(!result2.isSuccess()){
                result2.setMessage("导入数据源【" + dsDef.getName() + "】失败！");
                return  result2;
            }
        }
        return JsonResult.Success();
    }

    /**
     * @Description:  导入应用与应用相关的某个表的数据
     * @param tableObj 表数据
     * @param  tenantId 新的租户ID
     * @param  overrides 相同ID的记录是否覆盖
     * @Author: Elwin ZHANG  @Date: 2021/7/28 16:22
     **/
    private List importTableData(JSONObject tableObj, String tenantId, String overrides){
        String tableName=tableObj.getString("tableName");
        String pkName = tableObj.getString("pk");
        String dsName = tableObj.getString("dataSource");
        JSONArray data = tableObj.getJSONArray("data");
        String userId = ContextUtil.getCurrentUserId();
        String deptId=ContextUtil.getCurrentUser().getDeptId();
        List logDetail=new ArrayList();
        for (Object obj : data) {
            JSONObject row =(JSONObject)JSONObject.toJSON(obj);
            String pkId=row.getString(pkName);
            //替换租户ID，创建人，创建部门，修改人
            if(row.containsKey("TENANT_ID_")){
                row.put("TENANT_ID_",tenantId);
            }
            if(row.containsKey("CREATE_BY_")){
                row.put("CREATE_BY_",userId);
            }
            if(row.containsKey("UPDATE_BY")){
                row.put("UPDATE_BY",userId);
            }
            if(row.containsKey("CREATE_DEP_ID_")){
                row.put("CREATE_DEP_ID_",deptId);
            }
            //检查记录是否存在
            String sql ="select *  from " + tableName + " where " + pkName + " = '" + pkId + "'";

            List oldData=null;
            if(DEFAULT_DS.equals(dsName)){
                oldData=commonDao.query(sql);
                dsName="";
            }else {
                oldData=commonDao.query(dsName,sql);
            }
            //表单定义和实体表的关系，要删除旧的关系直接新增
            if(TABLE_FORM_RELATION.equals(tableName)){
                oldData=null;
                sql ="delete  from " + tableName + " where BODEF_ID_ = '" + row.getString("BODEF_ID_") + "' and ENT_ID_ = '" +  row.getString("ENT_ID_")  + "'";
                if(DEFAULT_DS.equals(dsName)){
                    commonDao.execute(sql);
                }else {
                    commonDao.execute(dsName,sql);
                }
            }
            if(oldData!=null && oldData.size()>0){
                if(overrides.indexOf("'" + tableName + "'")>0){
                    //更新记录
                    updateRecord(tableName,pkName,row,dsName);
                    logDetail.add(oldData);
                }
            }else{
                //新增记录
                insertRecord(tableName,row,dsName);
            }
        }
        return  logDetail;
    }

    /**
     * @Description: 将JSON数据更新到数据库表中
     * @param tableName 表名
     * @param pkName 主键字段名
     * @param row 行数据
     * @Author: Elwin ZHANG  @Date: 2021/7/28 18:02
     **/
    private  void updateRecord(String tableName, String pkName,JSONObject row,String dsName){
        String dbType=DbUtil.getDbType(dsName);
        String sql="update " + tableName + " set ";
        String where=" where " + pkName ;
        for (Map.Entry entry : row.entrySet()) {
            String key=(String)entry.getKey();
            String value=null;
            if(entry.getValue()!=null){
                value=entry.getValue().toString();
                value= DbUtil.handleEscapeChar(value,dsName);
            }
            if(pkName.equals(key)){
                if(JdbcConstants.ORACLE.equals( dbType)){
                    where += "=" + value ;
                }else {
                    where += "='" + value + "'";
                }
            }else{
                if(value==null){
                    sql +=  key+ "=null,";
                }else{
                    if(JdbcConstants.ORACLE.equals( dbType)){
                        sql += key + "=" + value + ",";
                    }else {
                        sql += key + "='" + value + "',";
                    }
                }
            }
        }
        sql=sql.substring(0,sql.length()-1) + where  ;
        if(DEFAULT_DS.equals(dsName)  || dsName.length()==0){
            commonDao.execute(sql);
        }else {
            commonDao.execute(dsName,sql);
        }
    }

    /**
     * @Description: 将JSON数据插入到数据库中
     * @param tableName 表名
     * @param row 行数据
     * @Author: Elwin ZHANG  @Date: 2021/7/28 18:00
     **/
    private  void insertRecord(String tableName, JSONObject row,String dsName){
        String dbType=DbUtil.getDbType(dsName);
        String sql="insert into " + tableName + "(";
        String values=" values(" ;
        for (Map.Entry entry : row.entrySet()) {
            sql +=entry.getKey() + ",";
            String value=null;
            if(entry.getValue()!=null){
                value=entry.getValue().toString();
                value= DbUtil.handleEscapeChar(value,dsName);
            }
            if(value==null){
                values += "null,";
            }else{
                if(JdbcConstants.ORACLE.equals( dbType)) {
                    values += value + ",";
                }else {
                    values += "'" + value + "',";
                }
            }
        }
        sql=sql.substring(0,sql.length()-1) +") ";
        values=values.substring(0,values.length()-1) +") ";
        if(DEFAULT_DS.equals(dsName) || dsName.length()==0 ) {
            commonDao.execute(sql + values);
        }else {
            commonDao.execute(dsName,sql + values);
        }
    }

    /**
     * @param array 表信息数组
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @Description: 校验将要导入的表信息
     * @Author: Elwin ZHANG  @Date: 2021/7/20 11:39
     **/
    public JsonResult checkTables(Object[] array) {
        boolean hasDataSourceTable=false;
        List<JSONObject> list = new ArrayList<>();
        JSONArray entities = getTableData((JSONArray)JSONArray.toJSON(array),TABLE_ENTITY,false);
        for (Object obj : array) {
            JSONObject tableObj = (JSONObject)JSONObject.toJSON(obj);
            String tableName = tableObj.getString("tableName");
            String tableDesc = tableObj.getString("tableDesc");
            String pkName = tableObj.getString("pk");
            JSONArray data = tableObj.getJSONArray("data");
            JSONObject info = new JSONObject();
            info.put("tableName",tableName);
            info.put("tableDesc", tableDesc);
            info.put("totalQty", data.size());
            String entityId=tableObj.getString("entityId");
            if(StringUtils.isEmpty(entityId)) {
                long repeatQty = getRepeatQty(data, tableName, pkName);
                info.put("repeatQty", repeatQty);
                //数据源检查
                if(TABLE_DATASOURCE.equals(tableName)){
                    hasDataSourceTable=true;
                    String msg=checkDataSourceDef(entities);
                    info.put("message",msg);
                }
            }else{
                if(checkEntityTable(entities,entityId)){
                    info.put("repeatQty", 1);
                    info.put("message","物理表已存在");
                }else {
                    info.put("repeatQty", 0);
                }
            }
            list.add(info);
        }
        //如果不存在数据源表，也要通过实体表来检查
        if(!hasDataSourceTable){
            JSONObject info = new JSONObject();
            info.put("tableName",TABLE_DATASOURCE);
            info.put("tableDesc", "数据源定义");
            info.put("totalQty", 0);
            info.put("repeatQty", 0);
            String msg=checkDataSourceDef(entities);
            info.put("message",msg);
            list.add(info);
        }
        JsonResult result=JsonResult.Success();
        result.setData(list);
        return result;
    }

    /**
     * @param array 实体表记录
     * @param entityId  检查的实体ID
     * @return boolean
     * @Description: 校验某一个实体表是否存在
     * @Author: Elwin ZHANG  @Date: 2021/7/20 17:45
     **/
    public boolean checkEntityTable(JSONArray array,String entityId) {
        if(array==null){
            return false;
        }
        for (Object obj : array) {
            JSONObject rowObj = (JSONObject)JSONObject.toJSON(obj);
            //不是当前实体则跳过
            String id=rowObj.getString("ID_");
            if(!entityId.equals(id)){
                continue;
            }
            //不生成数据库物理表的实体，返回假
            String genDb=rowObj.getString("GENDB_");
            if(!"1".equals(genDb)){
                return  false;
            }
            String dsAlias=rowObj.getString("DS_ALIAS_");
            String tableName=rowObj.getString("TABLE_NAME_");
            String sql="select count(1)  as id from " + TABLE_ENTITY +" where DS_ALIAS_='" + dsAlias + "' and TABLE_NAME_='" + tableName
                    + "' and GENDB_='1'";
            Object  cnt=commonDao.queryOne(sql);
            if(cnt!=null){
                long tmp=Long.parseLong(cnt.toString());
                return tmp>0;
            }
            return  false;
        }
        return  false;
    }

    /**
    * @Description: 数据源提醒；要考虑到共享数据源，2022-2-21日重构
    * @param  array     实体表记录数组
    * @return java.lang.String 校验信息
    * @Author: Elwin ZHANG  @Date: 2021/10/11 16:55
    **/
    private  String checkDataSourceDef(JSONArray array){
        HashSet<String> setAlias=new HashSet<String>();
        String notExistsDS="";
        if (array.size() == 0) {
            return "";
        }
        //将实体表中非默认的数据源找出来放到集合中
        for (Object obj : array) {
            JSONObject rowObj = (JSONObject) obj;
            String alias = rowObj.getString("DS_ALIAS_");
            if(!"LOCAL".equals(alias)){
                setAlias.add(alias);
            }
        }
        if(setAlias.size()==0){
            return "";
        }
        //检查这些数据源是否存在
        for (String strDsAlias:setAlias ){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq(COL_DATASOURCE_ALIAS,strDsAlias);
            FormDataSourceDef def=formDataSourceDefMapper.selectOne(wrapper);
            if(def==null){
                notExistsDS+=","+ strDsAlias ;
            }
        }
        String dataSources=StringUtils.join(setAlias);
        String msg="请检查别名为：【" + dataSources  +"】的数据源";
        if(notExistsDS.length()>0){
            msg +="，其中别名为：【" + notExistsDS.substring(1) + "】的数据源不存在，需要先手动创建" ;
        }
        return msg;
    }
    /**
     * @param array     表记录数组
     * @param tableName 表名
     * @param pkName    表主键字段名
     * @return int 表记录重复数量
     * @Description: 检查表的记录是否重复
     * @Author: Elwin ZHANG  @Date: 2021/7/20 11:40
     **/
    private long getRepeatQty(JSONArray array, String tableName, String pkName) {
        if (array.size() == 0) {
            return 0;
        }
        String ids = "";
        for (Object obj : array) {
            JSONObject rowObj = (JSONObject) obj;
            String pkId = rowObj.getString(pkName);
            ids += ",'" + pkId + "'";
        }
        ids = "(" + ids.substring(1) + ")";
        String sql = "select count(" + pkName + ") as qty from " + tableName + " where " + pkName + " in " + ids;
        Object object=commonDao.queryOne(sql);
        long qty = Long.parseLong(object.toString());
        return qty;
    }

    /**
     * @param appId 应用ID,
     * @param map   保存查询结果的map
     * @Description: 查询用户自定义的表单数据
     * @Author: Elwin ZHANG  @Date: 2021/7/2 18:43
     **/
    private void queryCustomTable(String appId, Map<String, String> map, int i) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_ ", appId);
        List<FormBoEntity> list = formBoEntityMapper.selectList(wrapper);
        if (list == null || list.size() == 0) {
            return;
        }
        //循环处理应用关联的实体表
        for (FormBoEntity entity : list) {
            try {
                String dsAlias = entity.getDsAlias();
                String tableName = entity.getTableName().toLowerCase();
                String sql = "select * from " + tableName ;
                List data = null;
                try{
                    data=commonDao.query(dsAlias, sql);
                } catch (Exception e){
                    log.error(e.getMessage(),e);
                }
                if (data == null || data.size() == 0) {
                    continue;
                }
                //实体的数据源
                String dataSource = entity.getDsAlias();
                if ("LOCAL".equals(dataSource)) {
                    dataSource = DEFAULT_DS;
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("dataSource", dataSource);
                jsonObject.put("entityId", entity.getId());
                jsonObject.put("tableName", tableName);
                jsonObject.put("tableDesc", entity.getName());
                jsonObject.put("pk", DEFAULT_PK_Name);
                jsonObject.put("data", data);
                String key = KEY_PREFIX + i + "." + tableName;
                map.put(key, JSONObject.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd HH:mm:ss"));
                i++;
            } catch (Exception e) {
                logger.warn("查询表数据失败：" + entity.getTableName(), e);
            }
        }
    }

    /**
     * @param appId 应用ID
     * @Description: 删除本数据库中应用相关的数据
     **/
    public void delete(String appId) {
        HashMap logMap=new HashMap();
        //先删除应用根据实体定义创建的表
        deleteUserEntityTable(appId,logMap);
        //再删除相关的系统表中的数据
        for (int i = appTables.length - 1; i >= 0; i--) {
            String[] tableProp = appTables[i].split("@");
            String tableName = tableProp[0];
            String sql="select  * FROM " + tableName + " WHERE APP_ID_='" + appId + "'";
            List oldData=commonDao.query(sql);
            if(!TABLE_DATASOURCE.equals(tableName) ) {
                if(oldData!=null && oldData.size()>0) {
                    sql = "DELETE FROM  " + tableName + " WHERE APP_ID_='" + appId + "'";
                    commonDao.execute(sql);
                    logMap.put(tableName,oldData);
                }
            }
        }
        //最后删除数据源
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_ ", appId);
        List<FormDataSourceDef> list =formDataSourceDefMapper.selectList(wrapper);
        if (list != null && list.size() > 0) {
            for (FormDataSourceDef entity : list) {
                formDataSourceDefController.del(entity.getId());
            }
            logMap.put(TABLE_DATASOURCE,list);
        }
        if(logMap.size()>0){
            String detail="Form库删除的记录：" + JSONObject.toJSONString(logMap);
            LogContext.put(Audit.DETAIL, detail);
        }
    }

    /**
     * @param appId 应用Id
     * @Description: 删除应用中实体创建的物理表
     * @Author: Elwin ZHANG  @Date: 2021/7/1 16:20
     **/
    private void deleteUserEntityTable(String appId,HashMap logMap) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_ ", appId);
        wrapper.eq("GENDB_ ", "1");
        wrapper.ne("GEN_MODE_ ", "db");

        List<FormBoEntity> list = formBoEntityMapper.selectList(wrapper);
        if (list == null || list.size() == 0) {
            return;
        }
        //循环处理应用关联的实体表
        for (FormBoEntity entity : list) {
            try {
                String dsAlias = entity.getDsAlias();
                String tableName=entity.getTableName();
                String sql="select  * FROM " +tableName;
                List oldData=null;
                if(DEFAULT_DS.equals(dsAlias)){
                    oldData=commonDao.query(sql);
                    dsAlias="";
                }else {
                    oldData=commonDao.query(dsAlias,sql);
                }
                if(oldData!=null && oldData.size()>0) {
                    ITableOperator tableOperator = OperatorContext.getByDsAlias(dsAlias);
                    tableOperator.dropTable(tableName);
                    logMap.put(tableName,oldData);
                }
            } catch (Exception e) {
                logger.warn("删除实体表失败：" + entity.getTableName(), e);
                MessageUtil.triggerException("删除实体表失败：" + e.getMessage());
            }
        }
    }
}
