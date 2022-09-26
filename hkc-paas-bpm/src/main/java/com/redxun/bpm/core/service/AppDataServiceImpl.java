package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.mapper.BpmDefMapper;
import com.redxun.bpm.flow.entity.ActGeBytearray;
import com.redxun.bpm.flow.service.ActGeBytearrayServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.feign.common.SystemClient;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.seata.sqlparser.util.JdbcConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 与应用相关的数据处理
 * @Author: Elwin ZHANG
 * @Date: 2021/6/25 16:22
 **/
@Service
public class AppDataServiceImpl {
    @Resource
    private CommonDao commonDao;
    @Resource
    private BpmDefMapper bpmDefMapper;
    @Autowired
    BpmDefService bpmDefService;
    @Resource
    private SystemClient systemClient;

    //导出数据Key的前缀
    private static final String KEY_PREFIX = "3.b.";
    //默认数据源名
    private static final String DEFAULT_DS = "bpm";
    //默认的主键列名
    private static final String DEFAULT_PK_Name = "ID_";
    //流程定义表
    private  static final String TABLE_BPM_DEF="bpm_def";
    //保存流程图信息的表，此表要最后导入
    private  static final String TABLE_ACT_BYTE_ARRAY="act_ge_bytearray";

    //默认数据库类型
    private  static  String DEFAULT_DB_TYPE="";

    @Resource
    private ActGeBytearrayServiceImpl actGeBytearrayService;
    /**
     * @param appId 应用ID
     * @Description: 查询本数据库中应用相关的数据
     **/
    public Map<String, String> query(String appId) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(appId)) {
            return map;
        }
        int i = 10;
        //系统流程定义表
        String sql = "select * from BPM_DEF where APP_ID_ ='" + appId + "' AND IS_MAIN_='YES'  and STATUS_='DEPLOYED' ";
        addData2Map(map, sql, TABLE_BPM_DEF, i, "DEF_ID_", "系统流程定义");
        i++;
        String inStr = " (select ACT_DEP_ID_ FROM  bpm_def " +
                " WHERE APP_ID_ ='" + appId + "' AND IS_MAIN_='YES'  and STATUS_='DEPLOYED' ) ";
        //查询关联的Activiti 的表{ "act_re_procdef";
        sql = "SELECT * FROM act_re_procdef WHERE DEPLOYMENT_ID_ IN " + inStr;
        addData2Map(map, sql, "act_re_procdef", i, DEFAULT_PK_Name, "activiti流程定义");
        i++;
        //查询关联的Activiti 的表{ act_re_deployment"};
        sql = "SELECT * FROM act_re_deployment WHERE ID_ IN " + inStr;
        addData2Map(map, sql, "act_re_deployment", i, DEFAULT_PK_Name, "activiti流程发布");
        i++;
        //查询关联的Activiti 的表{ ""act_ge_bytearray"};
        sql = "SELECT * FROM act_ge_bytearray WHERE DEPLOYMENT_ID_ IN " + inStr;
        addData2Map(map, sql, "act_ge_bytearray", i, DEFAULT_PK_Name, "activiti流程图数据");
        return map;
    }

    /**
     * @param array 表信息数组
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @Description: 校验将要导入的表信息
     * @Author: Elwin ZHANG  @Date: 2021/7/20 11:39
     **/
    public JsonResult checkTables(Object[]  array) {
        List<JSONObject> list = new ArrayList<>();
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
            JSONObject rowObj = (JSONObject)JSONObject.toJSON(obj);
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
     * @Description: 将查询结果加到Map中
     * @Author: Elwin ZHANG  @Date: 2021/7/5 11:12
     **/
    private void addData2Map(Map<String, String> map, String sql, String tableName, int index, String pkName, String tableDesc) {
        List data = commonDao.query(sql);
        if (data == null || data.size() == 0) {
            return;
        }
        if(TABLE_ACT_BYTE_ARRAY.equals(tableName)){
            convertBytes2String(data);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("dataSource", DEFAULT_DS);
        jsonObject.put("tableName", tableName);
        jsonObject.put("tableDesc", tableDesc);
        jsonObject.put("pk", pkName);
        jsonObject.put("data", data);
        String key = KEY_PREFIX + index + "." + tableName;
        map.put(key, JSONObject.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd HH:mm:ss"));
    }

    /**
    * @Description:  将BYTES_字段转为字符串
    * @param data act_ge_bytearray 表的记录
    * @Author: Elwin ZHANG  @Date: 2021/8/26 17:28
    **/
    private  void  convertBytes2String(List data){
        String colName="BYTES_";
        for (Object obj : data) {
            HashMap row=(HashMap)obj;
            byte[] bytes =(byte[])row.get(colName);
            String newValue=new String(bytes);
            row.put(colName,newValue);
        }
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
        JSONObject  byteArray=null;
        HashMap logMap=new HashMap();
        //循环处理每一张表
        for (Object obj : arrData) {
            JSONObject tableObj =(JSONObject)JSONObject.toJSON(obj);
            String tableName=tableObj.getString("tableName");
            if(TABLE_ACT_BYTE_ARRAY.equals(tableName)){
                byteArray=tableObj; //此表要最后导入
            }else {
                List list= importTableData(tableObj, tenantId, overrides);
                if(list.size()>0){
                    logMap.put(tableName,list);
                }
            }
        }
        if(byteArray!=null) {
            //Oracle 的Blob 类型要特殊处理
            List list=importByteArray(byteArray, tenantId, overrides);
            if (list!=null && list.size() > 0) {
                //logMap.put(TABLE_ACT_BYTE_ARRAY, list);  //oracle不兼容，先不写本表日志
            }
        }
        if(logMap.size()>0){
            String detail="Bpm库被覆盖的记录：" + JSONObject.toJSONString(logMap);
            LogContext.put(Audit.DETAIL, detail);
        }
        return JsonResult.Success();
    }

    /**
    *  功能：导入ACT_BYTE_ARRAY表
     * @param tableObj 表数据
     * @param  tenantId 新的租户ID
     * @param  overrides 相同ID的记录是否覆盖
    * @author  Elwin ZHANG
    * @date 2022/1/10 16:39
    **/
    private List importByteArray(JSONObject tableObj, String tenantId, String overrides){
        String tableName=TABLE_ACT_BYTE_ARRAY;
        String pkName = tableObj.getString("pk");
        JSONArray data = tableObj.getJSONArray("data");
        List logDetail=new ArrayList();
        for (Object obj : data) {
            JSONObject row =(JSONObject)JSONObject.toJSON(obj);
            String pkId=row.getString(pkName);
            ActGeBytearray bytearray=new ActGeBytearray();
            bytearray.setId(pkId);
            bytearray.setPkId(pkId);
            bytearray.setRev(row.getInteger("REV_"));
            bytearray.setDeploymentId(row.getString("DEPLOYMENT_ID_"));
            bytearray.setName(row.getString("NAME_"));
            bytearray.setGenerated(row.getShort("GENERATED_"));
            String bytes=row.getString("BYTES_");
            try {
                bytearray.setBytes(bytes.getBytes("UTF-8"));
            }catch (Exception ee){}
            actGeBytearrayService.saveOrUpdate(bytearray);
            //检查记录是否存在
            String sql ="select *  from " + tableName + " where " + pkName + " = '" + pkId + "'";
            List oldData=commonDao.query(sql);
            if(oldData!=null && oldData.size()>0){
                if(overrides.indexOf("'" + tableName + "'")>0){
                    //更新记录
                    actGeBytearrayService.update(bytearray);
                    logDetail.add(oldData);
                }
            }else{
                //新增记录
                actGeBytearrayService.save(bytearray);
            }
        }
        return logDetail;
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
     * @param appId 应用ID
     * @Description: 删除本数据库中应用相关的数据
     **/
    public void delete(String appId) {
        //查询应用相关的流程定义
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_ ", appId);
        List<BpmDef> lstDefs = bpmDefMapper.selectList(wrapper);
        if (lstDefs == null || lstDefs.size() == 0) {
            return;
        }
        //循环删除流程定义
        for (BpmDef bpmdef : lstDefs) {
            if(bpmdef.getActDefId()==null) {
                bpmDefService.delete(bpmdef.getPkId());
            }else {
                bpmDefService.delCacadeFlow(bpmdef);
            }
        }
        HashMap logMap=new HashMap();
        logMap.put(TABLE_BPM_DEF,lstDefs);
        String detail="Bpm库删除的记录：" + JSONObject.toJSONString(logMap);
        LogContext.put(Audit.DETAIL, detail);
    }

    /**
     * @Description:  查找系统树所属应用ID
     * @param treeId sys_tree表的主键值
     * @return java.lang.String  sys_tree表记录中APP_ID_字段的值
     * @Author: Elwin ZHANG  @Date: 2021/7/6 17:16
     **/
    public String getAppIdByTreeId(String treeId){
        try {
            String url = "/system/core/sysTree/get";
            JSONObject params = new JSONObject();
            params.put("pkId", treeId);
            Object o = systemClient.executeGetApi(url, params);
            HashMap<String,Object> result = (HashMap<String,Object>) o;
            HashMap<String,Object> data=(HashMap<String,Object>)result.get("data");
            String appId = data.get("appId").toString();
            return appId;
        }catch (Exception e ){
            return "";
        }
    }
}

