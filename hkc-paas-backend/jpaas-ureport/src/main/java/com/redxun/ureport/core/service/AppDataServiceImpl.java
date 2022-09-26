package com.redxun.ureport.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.seata.sqlparser.util.JdbcConstants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 与应用相关的数据处理
 * @Author: Elwin ZHANG
 * @Date: 2021/6/25 16:42
 **/
@Service
public class AppDataServiceImpl {
    @Resource
    private CommonDao commonDao;
    //导出数据Key的前缀
    private  static  final String KEY_PREFIX="4.u.";
    //默认数据源名
    private  static  final  String DEFAULT_DS="ureport";
    //应用相关的表名
    private  static final  String Table_Name="ureport_file";
    //默认的主键列名
    private  static  final  String DEFAULT_PK_Name="ID_";
    //默认数据库类型
    private  static  String DEFAULT_DB_TYPE="";

    /**
     * @Description:  查询本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    public Map<String,String> query(String appId){
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isEmpty(appId)){
            return map;
        }
        int i=10;
        String sql="select * from " + Table_Name + " where APP_ID_ ='" + appId + "'";
        List data=commonDao.query(sql);
        if (data==null || data.size()==0){
            return  map;
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("dataSource",DEFAULT_DS);
        jsonObject.put("tableName",Table_Name);
        jsonObject.put("tableDesc","报表定义");
        jsonObject.put("pk",DEFAULT_PK_Name);
        jsonObject.put("data",data);
        String key=KEY_PREFIX +i +"." + Table_Name;
        map.put(key,JSONObject.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd HH:mm:ss"));
        return map;
    }

    /**
     * @Description: 导入本数据库中应用相关的数据
     * @param data 应用相关数据
     **/
    public JsonResult install(String  data){
        DEFAULT_DB_TYPE=DbUtil.getDbType("");
        JSONObject object= JSON.parseObject(data);
        String tenantId=object.getString("tenantId");
        String overrides=object.getString("overrides");
        JSONArray arrData=object.getJSONArray("data");
        HashMap logMap=new HashMap();
        //循环处理每一张表
        for (Object obj : arrData) {
            JSONObject tableObj =(JSONObject)JSONObject.toJSON(obj);
            List list=importTableData(tableObj,tenantId,overrides);
            if(list.size()>0){
                String tableName=tableObj.getString("tableName");
                logMap.put(tableName,list);
            }
        }
        if(logMap.size()>0){
            String detail="Ureport库被覆盖的记录：" + JSONObject.toJSONString(logMap);
            LogContext.put(Audit.DETAIL, detail);
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
        JSONArray data = tableObj.getJSONArray("data");
        String userId =ContextUtil.getCurrentUserId();
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
            String sql ="select * from " + tableName + " where " + pkName + " = '" + pkId + "'";
            List oldData=commonDao.query(sql);
            if(oldData!=null && oldData.size()>0){
                if(overrides.indexOf("'" + tableName + "'")>0){
                    //更新记录
                    updateRecord(tableName,pkName,row);
                    logDetail.add(oldData);
                }
            }else{
                //新增记录
                insertRecord(tableName,row);
            }
        }
        return logDetail;
    }

    /**
    * @Description: 将JSON数据更新到数据库表中
     * @param tableName 表名
     * @param pkName 主键字段名
     * @param row 行数据
    * @Author: Elwin ZHANG  @Date: 2021/7/28 18:02
    **/
    private  void updateRecord(String tableName, String pkName,JSONObject row){
        String sql="update " + tableName + " set ";
        String where=" where " + pkName ;
        for (Map.Entry entry : row.entrySet()) {
            String key=(String)entry.getKey();
            String value=null;
            if(entry.getValue()!=null){
                value=entry.getValue().toString();
                value= DbUtil.handleEscapeChar(value,"");
            }
            if(pkName.equals(key)){
                if(JdbcConstants.ORACLE.equals( DEFAULT_DB_TYPE)){
                    where += "=" + value ;
                }else {
                    where += "='" + value + "'";
                }
            }else{
                if(value==null){
                    sql +=  key+ "=null,";
                }else{
                    if(JdbcConstants.ORACLE.equals( DEFAULT_DB_TYPE)){
                        sql += key + "=" + value + ",";
                    }else {
                        sql += key + "='" + value + "',";
                    }
                }
            }
        }
        sql=sql.substring(0,sql.length()-1) + where  ;
        commonDao.execute(sql);
    }

    /**
    * @Description: 将JSON数据插入到数据库中
    * @param tableName 表名
     * @param row 行数据
    * @Author: Elwin ZHANG  @Date: 2021/7/28 18:00
    **/
    private  void insertRecord(String tableName, JSONObject row){
        String sql="insert into " + tableName + "(";
        String values=" values(" ;
        for (Map.Entry entry : row.entrySet()) {
            sql +=entry.getKey() + ",";
            String value=null;
            if(entry.getValue()!=null){
                value=entry.getValue().toString();
                value= DbUtil.handleEscapeChar(value,"");
            }
            if(value==null){
                values += "null,";
            }else{
                if(JdbcConstants.ORACLE.equals( DEFAULT_DB_TYPE)) {
                    values += value + ",";
                }else {
                    values += "'" + value + "',";
                }
            }
        }
        sql=sql.substring(0,sql.length()-1) +") ";
        values=values.substring(0,values.length()-1) +") ";
        commonDao.execute(sql + values);
    }
    /**
     * @param array 表信息数组
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @Description: 校验将要导入的表信息
     * @Author: Elwin ZHANG  @Date: 2021/7/20 11:39
     **/
    public JsonResult checkTables(Object[]   array) {
        List<JSONObject> list = new ArrayList<>();
        for (Object obj : array) {
            JSONObject tableObj =(JSONObject)JSONObject.toJSON(obj);
            String tableName = tableObj.getString("tableName");
            String tableDesc = tableObj.getString("tableDesc");
            String pkName = tableObj.getString("pk");
            JSONArray data = tableObj.getJSONArray("data");
            JSONObject info = new JSONObject();
            info.put("tableName",tableName);
            info.put("tableDesc", tableDesc);
            info.put("totalQty", data.size());
            long repeatQty=getRepeatQty(data,tableName,pkName);
            info.put("repeatQty", repeatQty);
            list.add(info);
        }
        JsonResult result=JsonResult.Success();
        result.setData(list);
        return result;
    }

    /**
     * @param array     表记录数组
     * @param tableName 表名
     * @param  pkName        表主键字段名
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
            JSONObject rowObj =(JSONObject)JSONObject.toJSON(obj);
            String pkId = rowObj.getString( pkName);
            ids += ",'" + pkId + "'";
        }
        ids =  "(" + ids.substring(1) + ")";
        String sql ="select count(" + pkName+ ") as qty from " + tableName + " where " + pkName + " in " + ids;
        Object object=commonDao.queryOne(sql);
        long qty = Long.parseLong(object.toString());
        return qty;
    }

    /**
     * @Description: 删除本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    public void delete( String appId){
        HashMap logMap=new HashMap();
        String sql="select  * FROM " + Table_Name + " WHERE APP_ID_='" + appId + "'";
        List oldData=commonDao.query(sql);
        if(oldData!=null && oldData.size()>0) {
            sql = "DELETE FROM " + Table_Name + " WHERE APP_ID_='" + appId + "'";
            commonDao.execute(sql);  //删除关联的报表
            logMap.put(Table_Name,oldData);
            String detail="Ureport库删除的记录：" + JSONObject.toJSONString(logMap);
            LogContext.put(Audit.DETAIL, detail);
        }
    }
}
