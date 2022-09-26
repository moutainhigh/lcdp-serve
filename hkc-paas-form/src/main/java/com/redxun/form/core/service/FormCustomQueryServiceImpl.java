package com.redxun.form.core.service;

import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;
import com.redxun.db.CustomQueryConfigBean;
import com.redxun.dboperator.model.Column;
import com.redxun.dto.user.OsInstDto;
import com.redxun.feign.OsInstClient;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.FormCustomQuery;
import com.redxun.form.core.entity.FormRegLib;
import com.redxun.form.core.mapper.FormCustomQueryMapper;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.util.SysUtil;
import freemarker.template.TemplateException;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * [自定查询]业务服务类
 * @author hujun
 */
@Service
public class FormCustomQueryServiceImpl extends SuperServiceImpl<FormCustomQueryMapper, FormCustomQuery> implements BaseService<FormCustomQuery> {

    @Resource
    private FormCustomQueryMapper formCustomQueryMapper;
    @Resource
    private GroovyEngine groovyEngine;
    @Resource
    private ConstVarContext constVarContext;
    @Resource
    private FtlEngine freemarkEngine;
    @Resource
    private CommonDao commonDao;
    @Resource
    private FormRegLibServiceImpl formRegLibService;
    @Resource
    private  FormDataService formDataService;
    @Resource
    OsInstClient osInstClient;


    @Override
    public BaseDao<FormCustomQuery> getRepository() {
        return formCustomQueryMapper;
    }

    public FormCustomQuery getByKey(String key){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",key);
        return getOne(queryWrapper);
    }

    public List getQueryData(String alias,Map<String,Object> params) throws Exception{
        FormCustomQuery formCustomQuery=getByKey(alias);

        String ds=getDsAlias(formCustomQuery);
        //切换数据源
        DataSourceContextHolder.setDataSource(ds);
        Page page=null;
        if(formCustomQuery.getIsPage()==1){
            Object pageObj= params.get("pageIndex");
            if(pageObj!=null){
                page=new Page(Integer.parseInt(pageObj.toString()) ,formCustomQuery.getPageSize());
            }
            if(null==page){
                page=new Page(0,formCustomQuery.getPageSize());
            }
        }
        List list=getData(formCustomQuery, params,page,"");

        DataSourceContextHolder.setDefaultDataSource();

        return list;
    }

    public List getData(FormCustomQuery formCustomQuery, Map<String,Object> requestParams, Page page,String deploy) throws TemplateException, IOException{
        Map<String,Object> handleParams=getParams(formCustomQuery,requestParams);
        String ds=getDsAlias(formCustomQuery);
        if(StringUtils.isEmpty(ds)){
            ds= DataSourceUtil.LOCAL;
        }
        List result=null;
        String sqlStr="sql";
        String table="table";
        String freeMakerSql="freeMarkerSql";
        //自定义SQL
        SqlModel sqlModel=null;

        if(FormCustomQuery.QUERY_SQL.equals(formCustomQuery.getSqlBuildType())){
            Map<String,Object> params=new HashMap<>(handleParams.size()+1);
            params.putAll(handleParams);
            params.put("params",handleParams);

            //处理常量
            String handleSqlDiy=formCustomQuery.getSqlDiy();
            handleSqlDiy= SysUtil.replaceConstant(handleSqlDiy);
            //获取SQL
            String sql = (String)groovyEngine.executeScripts(handleSqlDiy,params);
            sqlModel=new SqlModel(sql,requestParams);

        }else if(FormCustomQuery.QUERY_TABLE.equals(formCustomQuery.getSqlBuildType())){
            sqlModel=buildSql(formCustomQuery,requestParams);
            //是否为下拉树
            if(!"".equals(deploy)){
                String buildTreeSql=buildTreeSql(formCustomQuery,requestParams,deploy);
                sqlModel.setSql(buildTreeSql);
            }
        }else if(FormCustomQuery.QUERY_FREEMARK_SQL.equals(formCustomQuery.getSqlBuildType())){
            String sql = SysUtil.replaceConstant(formCustomQuery.getSql());
            //添加上下文变量。
            addContextVar(handleParams);
            sql = freemarkEngine.parseByStringTemplate(handleParams,sql);
            sqlModel=new SqlModel(sql,handleParams);
        }

        if(formCustomQuery.getIsPage()==1){
            result=commonDao.query(ds,sqlModel.getSql(),sqlModel.getParams(),page);
        }else{
            result=commonDao.query(ds,sqlModel.getSql(),sqlModel.getParams());
        }
        //处理结果集
        List finalResults=handleSqlDiyResult(formCustomQuery,result);
        return finalResults;

    }

    private void addContextVar(Map<String, Object> params) {
        IUser curUser = ContextUtil.getCurrentUser();
        if (curUser != null) {
            params.put(FormBoEntity.FIELD_CREATE_BY, curUser.getUserId());
            params.put("DEP_ID_", curUser.getDeptId());
            params.put(FormBoEntity.FIELD_TENANT, curUser.getTenantId());
        }
    }



    private Map<String,Object> getParams(FormCustomQuery formCustomQuery,Map<String,Object> requestParams){
        String whereSql=formCustomQuery.getWhereField();
        if(StringUtils.isEmpty(whereSql)){
            return Collections.EMPTY_MAP;
        }

        JSONArray whereArray=JSONArray.parseArray(whereSql);
        Map<String,Object> handleParams=new HashMap<>(whereArray.size());
        for(int i=0;i<whereArray.size();i++){
            JSONObject jsonObject=whereArray.getJSONObject(i);
            if(!"param".equals(jsonObject.get("valueSource"))){
                continue;
            }
            String name=jsonObject.getString("fieldName");
            if(!requestParams.containsKey(name)){
                continue;
            }
            Object obj=requestParams.get(name);
            if(obj==null){
                continue;
            }
            if(obj instanceof String){
                String tmp=(String)obj;
                if(StringUtils.isNotEmpty(tmp)){
                    handleParams.put(name,tmp);
                }
            }else{
                handleParams.put(name,obj);
            }
        }
        return handleParams;
    }

    /**
     * 根据自定义查询配置，处理自定义SQL结果集
     * @param formCustomQuery
     * @param result
     * @return
     */
    private List handleSqlDiyResult(FormCustomQuery formCustomQuery,List result){
        JSONArray resultArray=JSONArray.parseArray(formCustomQuery.getResultField());
        if(BeanUtil.isEmpty(resultArray)){
            return null;
        }
        String dbType = DataSourceUtil.getCurrentDbType();
        List finalResults=new ArrayList<String>();
        for(Object object : result){
            Map<String,Object> tmpResult=new HashMap<>(resultArray.size());
            if(object == null){
                continue;
            }
            for(int i=0;i<resultArray.size();i++) {
                Map<String, Object> resultItem = (Map<String, Object>) object;
                String fieldName = resultArray.getJSONObject(i).getString("fieldName");
                String reg = resultArray.getJSONObject(i).getString("reg");

                Object val = resultItem.get(fieldName);
                if (val == null) {
                    continue;
                }
                if (val instanceof ClobProxyImpl) {
                    ClobProxyImpl clob = (ClobProxyImpl) val;
                    val = FileUtil.clobToString(clob);
                }
                if(StringUtils.isNotEmpty(reg)){
                    FormRegLib formRegLib = formRegLibService.getByKey(reg,FormRegLib.MASKING);
                    if (BeanUtil.isNotEmpty(formRegLib) && val instanceof String) {
                        String regText = formRegLib.getRegText();
                        String mentText = formRegLib.getMentText();
                        String obj = val.toString();
                        val = obj.replaceAll(regText, mentText);
                    }
                }
                if (dbType.equals("postgresql")) {
                    fieldName = fieldName.toUpperCase();
                }
                tmpResult.put(fieldName, val);
            }
            finalResults.add(tmpResult);
        }

        return finalResults;
    }

    /**
     * 构建自定义SQL语句
     * @param formCustomQuery
     * @param requestParams
     * @return
     */
    private SqlModel buildSql(FormCustomQuery formCustomQuery,Map<String,Object> requestParams){
        String sql = "SELECT ";
        sql += " " + buildResultField(formCustomQuery.getResultField());
        sql += " FROM " + formCustomQuery.getTableName();
        SqlModel whereSql=buildWhere(formCustomQuery,requestParams);
        sql += " " + whereSql.getSql();
        sql += getOrderBy(formCustomQuery);
        return new SqlModel(sql,whereSql.getParams());
    }


    /**
     * 构建下拉树SQL语句
     * @param formCustomQuery
     * @param requestParams
     * @param deploy {textfield："",valuefield:"",fatherNode:""}
     * @return
     */
    private String buildTreeSql(FormCustomQuery formCustomQuery,Map<String,Object> requestParams,String deploy){
        Map<String, Object> jsonObjectMap = FormCustomQueryUtil.jsonToMap(deploy);
        String textfield = (String) jsonObjectMap.get("textfield");
        String valuefield = (String) jsonObjectMap.get("valuefield");
        String fatherNode = (String) jsonObjectMap.get("fatherNode");
        String sql = "SELECT A."+textfield+", A."+valuefield+", A."+fatherNode;
        if(requestParams.size()>0){
            sql +=", ( SELECT COUNT( * ) AMOUNT FROM "+formCustomQuery.getTableName()+" B WHERE B."+fatherNode +"= A."+valuefield+" )CHILDS";
        }
        sql += " FROM " + formCustomQuery.getTableName()+" A";
        sql += " where 1=1 ";
        if(requestParams.size()>0){
            sql += " AND A."+fatherNode+"=#{w."+fatherNode+"}";
        }
        return sql;
    }


    /**
     * 处理结果集
     * @param resultField
     * @return
     */
    private String buildResultField(String resultField){
        String resultFieldString="";
        JSONArray queryJsonArray=JSONArray.parseArray(resultField);
        for(int i=0;i<queryJsonArray.size();i++){
            JSONObject jsonObject=queryJsonArray.getJSONObject(i);
            String fieldName=jsonObject.getString("fieldName");
            if(i==0){
                resultFieldString+=fieldName;
                continue;
            }
            resultFieldString+=","+fieldName;
        }
        return resultFieldString;
    }


    /**
     * 构建WHERE语句
     * @param formCustomQuery
     * @param requestParams
     * @return
     */
    private SqlModel buildWhere(FormCustomQuery formCustomQuery,Map<String,Object> requestParams){
        if(StringUtils.isEmpty(formCustomQuery.getWhereField())){
            return new SqlModel("",Collections.EMPTY_MAP);
        }
        String whereSql=" where 1=1";
        JSONArray whereJsonArray=JSONArray.parseArray(formCustomQuery.getWhereField());
        Map<String,Object> params=new HashMap<>(whereJsonArray.size());
        for(int i=0;i<whereJsonArray.size();i++){
            JSONObject jsonObject=whereJsonArray.getJSONObject(i);
            String columnType=jsonObject.getString("columnType");
            String specialSentence="";
            String value=(String) getValue(requestParams,jsonObject);
            if(StringUtils.isEmpty(value)){
                continue;
            }
            //字符类型处理
            if(Column.COLUMN_TYPE_VARCHAR.equals(columnType)){
                specialSentence+=handleVarchar(jsonObject,requestParams,params);
            }else if(Column.COLUMN_TYPE_NUMBER.equals(columnType)){
                specialSentence+=handleNumber(jsonObject,requestParams,params);
            }else if(Column.COLUMN_TYPE_DATE.equals(columnType)){
                specialSentence+=handleDate(jsonObject,requestParams,params);
            }
            whereSql+=" and "+specialSentence;
        }
        return new SqlModel(whereSql,params);
    }

    /**
     * 处理字符类型的SQL
     * @param jsonObject
     * @param requestParams
     * @param params
     * @return
     */
    private String handleVarchar(JSONObject jsonObject,Map<String,Object> requestParams,Map<String,Object> params){
        String fieldName=jsonObject.getString("fieldName");
        String typeOperate=jsonObject.getString("typeOperate");
        String specialSentence=fieldName;
        String value=(String) getValue(requestParams,jsonObject);
        //处理IN查询
        if(CustomQueryConfigBean.TypeOperateIn.equals(typeOperate)){
            specialSentence+=handleInParams(fieldName,value,params);
        }else if(CustomQueryConfigBean.TypeOperateEqual.equals(typeOperate)){
            specialSentence+= " = "+getMyBatisName(fieldName);
            params.put(fieldName,value);
        }else if(CustomQueryConfigBean.TypeOperateNotEqual.equals(typeOperate)){
            specialSentence+= " != "+getMyBatisName(fieldName);
            params.put(fieldName,value);
        }else{
            specialSentence+= " like "+getMyBatisName(fieldName);

            if(CustomQueryConfigBean.TypeOperateLikeLeft.equals(typeOperate)){
                //左模糊
                params.put(fieldName,value+"%");
            }else if(CustomQueryConfigBean.TypeOperateLikeRight.equals(typeOperate)){
                //右模糊
                params.put(fieldName,"%"+value);
            }else{
                //全模糊
                params.put(fieldName,"%"+value+"%");
            }
        }
        return specialSentence;
    }

    /**
     * 处理数字SQL
     * @param jsonObject
     * @param requestParams
     * @param params
     * @return
     */
    private String handleNumber(JSONObject jsonObject,Map<String,Object> requestParams,Map<String,Object> params){
        String fieldName=jsonObject.getString("fieldName");
        String typeOperate=jsonObject.getString("typeOperate");
        String specialSentence=fieldName;
        String value=(String) getValue(requestParams,jsonObject);

        if(CustomQueryConfigBean.TypeOperateBetween.equals(typeOperate)){
            String rex="|";
            //BETWEEN查询
            if(value.indexOf(rex)<0){
                return "";
            }
            String[] aryVal=value.split("\\|");
            String start=fieldName+"_start";
            String end=fieldName+"_end";
            specialSentence+= " between "+getMyBatisName(start)+" and "+getMyBatisName(end);
            params.put(start,Integer.parseInt(aryVal[0]));
            params.put(end,Integer.parseInt(aryVal[1]));
        }else if(CustomQueryConfigBean.TypeOperateIn.equals(typeOperate)){
            //IN查询
            specialSentence+=handleInParams(fieldName,value,params);
        }else{
            specialSentence+=typeOperate+getMyBatisName(fieldName);
            params.put(fieldName,Integer.parseInt(value));
        }
        return specialSentence;
    }

    /**
     * 日期类型的处理
     * @param jsonObject
     * @param requestParams
     * @param params
     * @return
     */
    private String handleDate(JSONObject jsonObject,Map<String,Object> requestParams,Map<String,Object> params){
        String fieldName=jsonObject.getString("fieldName");
        String typeOperate=jsonObject.getString("typeOperate");
        String specialSentence=fieldName;
        String value=(String) getValue(requestParams,jsonObject);

        if(CustomQueryConfigBean.TypeOperateBetween.equals(typeOperate)){
            //BETWEEN查询
            String rex="|";
            if(value.indexOf(rex)<0){
                return "";
            }
            String[] aryVal=value.split("\\|");
            String start=fieldName+"_start";
            String end=fieldName+"_end";
            specialSentence+= " between "+getMyBatisName(start)+" and "+getMyBatisName(end);
            params.put(start,Integer.parseInt(aryVal[0]));
            params.put(end,Integer.parseInt(aryVal[1]));
        }else{
            specialSentence+=typeOperate+getMyBatisName(fieldName);
            params.put(fieldName,Integer.parseInt(value));
        }
        return specialSentence;
    }

    /**
     * 处理IN参数
     * @param fieldName
     * @param value
     * @param params
     * @return
     */
    private String handleInParams(String fieldName,String value,Map<String,Object> params){
        if(StringUtils.isEmpty(value)){
            return "";
        }
        String[] inParams=value.split(",");
        String inStrings="";
        for(int i=0;i<inParams.length;i++){
            String name = fieldName+"_"+i;
            if(i==0){
                inStrings=getMyBatisName(name);
                params.put(name,inParams[i]);
                continue;
            }
            inStrings+=","+getMyBatisName(name);
            params.put(name,inParams[i]);
        }
        return " in ("+inStrings+")";
    }

    /**
     * JSON结构
     * @param requestParams
     * @param jsonObject
     * {
     *   fieldName:"字段名",
     *   valueSource:"值来源模式
     *   valueDef:"值定义"
     * }
     * @return
     */
    private Object getValue(Map<String,Object> requestParams,JSONObject jsonObject){
        String fieldName=jsonObject.getString("fieldName");
        String valueSource=jsonObject.getString("valueSource");
        String valueDef=jsonObject.getString("valueDef");
        Map<String,Object> vars=new HashMap<>(16);
        Object val=null;
        String param="param";
        String fixedVar="fixedVar";
        String script="script";
        String constantVar="constantVar";
        if("param".equals(valueSource)){
            //传入参数
            val=requestParams.get(fieldName);
        }else if("fixedVar".equals(valueSource)){
            //固定值
            val=valueDef;
        }else if("script".equals(valueSource)) {
            //脚本
            val=(String)groovyEngine.executeScripts(valueDef,vars);
        }else if("constantVar".equals(valueSource)){
            //常量
            val=constVarContext.getValByKey(valueDef,vars);
        }
        return val;
    }

    /**
     * 转换参数#{fieldName}
     * @param fieldName
     * @return
     */
    private String getMyBatisName(String fieldName){
        return "#{w."+fieldName+"}";
    }

    /**
     * 构建ORDER BY语句
     * @param formCustomQuery
     * @return
     */
    private String getOrderBy(FormCustomQuery formCustomQuery){
        if(StringUtils.isEmpty(formCustomQuery.getOrderField())){
            return "";
        }
        String orderFieldString=" order by ";
        JSONArray orderJsonArray=JSONArray.parseArray(formCustomQuery.getOrderField());
        if(orderJsonArray.size()==0){
            return "";
        }
        for(int i=0;i<orderJsonArray.size();i++){
            JSONObject jsonObject=orderJsonArray.getJSONObject(i);
            String fieldName=jsonObject.getString("fieldName");
            String typeOrder=jsonObject.getString("typeOrder");
            String order=StringUtils.isEmpty(typeOrder)?"ASC":typeOrder;
            if(i==0){
                orderFieldString+=fieldName+" "+order;
                continue;
            }
            orderFieldString+=","+fieldName+" "+order;
        }
        return orderFieldString;
    }

    /**
     * 判断是否存在。
     * @param customQuery
     * @return
     */
    public boolean isExist(FormCustomQuery customQuery){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",customQuery.getKey());
        if(StringUtils.isNotEmpty(customQuery.getId())){
            wrapper.ne("ID_",customQuery.getId());
        }
        Integer rtn= formCustomQueryMapper.selectCount(wrapper);
        return  rtn>0;
    }

    /**
     * 导入自定义查询列表。
     * @param file
     * @param treeId
     */
    public void importCustomQuery(MultipartFile file, String treeId) {
        StringBuilder sb=new StringBuilder();

        sb.append("导入自定义查询,导入的自定义查询如下:");

        JSONArray formQueryArray  = FormExOrImportHandler.readZipFile(file);
        String appId=formDataService.getAppIdByTreeId(treeId);
        for (Object obj:formQueryArray) {
            JSONObject queryObj = (JSONObject)obj;
            JSONObject formCustomQuery = queryObj.getJSONObject("formCustomQuery");
            if(BeanUtil.isEmpty(formCustomQuery)){
                continue;
            }

            String formCustomQueryStr = formCustomQuery.toJSONString();
            FormCustomQuery formcustomquery = JSONObject.parseObject(formCustomQueryStr,FormCustomQuery.class);

            sb.append(formcustomquery.getName() +"("+formcustomquery.getId()+"),");
            formcustomquery.setAppId(appId);
            formcustomquery.setTreeId(treeId);
            String id = formcustomquery.getId();
            FormCustomQuery oldQuery = get(id);
            if(BeanUtil.isNotEmpty(oldQuery)) {
                //应用外，或应用ID相同才更新
                if(StringUtils.isEmpty(appId) || appId.equals(oldQuery.getAppId())) {
                    update(formcustomquery);
                }else{
                    formcustomquery.setId(IdGenerator.getIdStr());
                    insert(formcustomquery);
                }
            }
            else{
                insert(formcustomquery);
            }

        }

        sb.append("导入到分类:[" + treeId +"]下");

        LogContext.put(Audit.DETAIL,sb.toString());

    }


    private String getDsAlias(FormCustomQuery formCustomQuery){
        String dsAlias="";
        //租户使用 则取租户的表单数据源
        if("1".equals(formCustomQuery.getIsTenant())){
            String tenantId = ContextUtil.getCurrentTenantId();
            OsInstDto osInstDto = osInstClient.getById(tenantId);
            if(BeanUtil.isNotEmpty(osInstDto) && StringUtils.isNotEmpty(osInstDto.getDatasource()) ){
                dsAlias=JSONObject.parseObject(osInstDto.getDatasource()).getString("value");
            }
        }else {
            dsAlias=formCustomQuery.getDsAlias();
        }
        return dsAlias;
    }

}
