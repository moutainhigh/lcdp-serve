package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.SecurityConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.db.CommonDao;
import com.redxun.db.DbUtil;
import com.redxun.feign.org.OrgClient;
import com.redxun.feign.org.OsGradeRoleClient;
import com.redxun.feign.org.OsGroupMenuClient;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.oauth2.common.config.AppConfig;
import com.redxun.oauth2.common.properties.AppProperties;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppActionLog;
import com.redxun.system.core.mapper.SysAppActionLogMapper;
import com.redxun.system.core.mapper.SysAppMapper;
import com.redxun.system.feign.BpmClient;
import com.redxun.system.feign.FormClient;
import com.redxun.system.feign.PortalClient;
import com.redxun.system.feign.UreportClient;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.sqlparser.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * [sys_app]业务服务类
 */
@Slf4j
@Service
public class SysAppServiceImpl extends SuperServiceImpl<SysAppMapper, SysApp> implements BaseService<SysApp> {
    @Resource
    private SysAppMapper sysAppMapper;
    @Resource
    private SysAppActionLogMapper sysAppActionLogMapper;
    @Resource
    private SysAppManagerServiceImpl sysAppManagerService;
    @Autowired
    private RedisRepository redisRepository;
    @Resource
    private CommonDao commonDao;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private PortalClient portalClient;
    @Autowired
    private FormClient formClient;
    @Autowired
    private BpmClient bpmClient;
    @Autowired
    private UreportClient ureportClient;
    @Resource
    private OsGradeRoleClient osGradeRoleClient;
    @Resource
    private OsGroupMenuClient osGroupMenuClient;
    @Autowired
    private OrgClient orgClient;


    @Override
    public BaseDao<SysApp> getRepository() {
        return sysAppMapper;
    }

    private String clientRedisKey(String clientId) {
        return SecurityConstants.CACHE_CLIENT_KEY + ":" + clientId;
    }

    //导出数据Key的前缀
    private static final String KEY_PREFIX = "1.s.";
    //数据源名称
    private static final String SYSTEM_DS = "system";
    private static final String FORM_DS = "form";
    private static final String UREPORT_DS = "ureport";
    private static final String PORTAL_DS = "portal";
    private static final String BPM_DS = "bpm";
    //缓存2小时
    private static final int CACHE_TIME = 60 * 60 * 2;
    private static final String TABLE_APP = "sys_app";
    private  static final  String REPEAT_QTY="repeatQty";
    //应用相关表名
    private static final String[] appTables = {"sys_app@APP_ID_@应用",
            "sys_tree@TREE_ID_@分类树",
            "sys_dic@DIC_ID_@数据字典",
            "sys_seq_id@SEQ_ID_@流水号",
            "sys_invoke_script@ID_@表单调用脚本",
            "sys_menu@MENU_ID_@菜单",
            "sys_webreq_def@ID_@WEB请求调用定义"
    };

    //默认数据库类型
    private  static  String DEFAULT_DB_TYPE="";

    /**
     * @param appId 应用ID
     * @Description: 查询所有数据库中应用相关的数据
     **/
    public TreeMap<String, String> query(String appId,boolean exportCustomTables) {
        TreeMap<String, String> map = queryLocalDb(appId);
        Map<String, String> formMap = formClient.query(appId,exportCustomTables);
        Map<String, String> bpmMap = bpmClient.query(appId);
        Map<String, String> portalMap = portalClient.query(appId);
        map.putAll(formMap);
        map.putAll(bpmMap);
        map.putAll(portalMap);
        try {
            Map<String, String> ureportMap = ureportClient.query(appId);
            map.putAll(ureportMap);
        } catch (Exception e) {
            //如果报表服务没启动，则跳过
            log.info("报表服务没启动，不导出报表");
        }
        return map;
    }

    /**
     * @param appId 应用ID
     * @Description: 查询本数据库中应用相关的数据
     **/
    private TreeMap<String, String> queryLocalDb(String appId) {
        TreeMap<String, String> map = new TreeMap<String, String>();
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
            jsonObject.put("dataSource", SYSTEM_DS);
            jsonObject.put("tableName", tableName);
            jsonObject.put("tableDesc", tableDesc);
            jsonObject.put("pk", pkName);
            jsonObject.put("data", data);
            String key = KEY_PREFIX + i + "." + tableName;
            map.put(key, JSONObject.toJSONStringWithDateFormat(jsonObject,"yyyy-MM-dd HH:mm:ss"));
            i++;
        }
        return map;
    }

    /**
     * @param appId 应用ID
     * @return boolean 删除成功返回true
     * @Description: 删除应用及应用相关的数据
     * @Author: Elwin ZHANG  @Date: 2021/6/25 11:20
     **/
    @GlobalTransactional
    public boolean delete(String appId) {
        SysApp app = sysAppMapper.selectById(appId);
        //参数不正确
        if (app == null) {
            return false;
        }
        //外部应用，直接删除表记录即可
        if(app.getAppType()>1){
            sysAppMapper.deleteById(app.getAppId());
            return true;
        }
        //删除其他库的相关数据
        formClient.delete(appId);
        bpmClient.delete(appId);
        portalClient.delete(appId);
        //报表服务如果没启动则不报错
        try {
            ureportClient.delete(appId);
        } catch (RuntimeException e) {
            if (e.getMessage().indexOf("ClientException: Load balancer does not have available server for client") > 0) {
                log.info("ureport服务未启动,未删除与应用相关的报表");
            } else {
                throw e;
            }
        }
        //删除system库中应用相关的数据
        delDataOfApp(appId);
        return true;
    }

    /**
    * @Description: 导入/安装 应用
    * @param redisKey 缓存的数据KEY前缀
     * @param  tenantId 导入到的租户ID
     * @param   overrides 相同ID的记录是否覆盖
    * @return com.redxun.common.base.entity.JsonResult
    * @Author: Elwin ZHANG  @Date: 2021/7/27 16:06
    **/
    //@GlobalTransactional(timeoutMills = 1000*60*10)
    @Transactional
    public  JsonResult install( String redisKey,  String tenantId, String overrides ,String treeId){
        HashMap<String,Object> params=new LinkedHashMap<>();
        params.put("tenantId",tenantId);
        params.put("overrides",overrides);
        JSONArray formArray =  (JSONArray)redisRepository.get(redisKey + FORM_DS);
        JSONArray systemArray = (JSONArray)redisRepository.get(redisKey + SYSTEM_DS);
        JSONArray bpmArray = (JSONArray)redisRepository.get(redisKey + BPM_DS);
        JSONArray portalArray = (JSONArray)redisRepository.get(redisKey + PORTAL_DS);
        JSONArray ureportArray = (JSONArray)redisRepository.get(redisKey + UREPORT_DS);
        JsonResult resultLast=JsonResult.Success("恭喜，应用导入成功！");
        resultLast.setShow(false);
        if(ureportArray!=null && ureportArray.size()>0){
            params.put("data",ureportArray);
            JsonResult result=ureportClient.install(JSONObject.toJSONString(params));
            if(!result.isSuccess()){
                throw new RuntimeException(result.getMessage());
            }
        }
        if(systemArray!=null && systemArray.size()>0){
            params.put("data",systemArray);
            params.put("treeId",treeId);
            JsonResult result=install(params);
            if(!result.isSuccess()){
                throw new RuntimeException(result.getMessage());
            }
        }
        if(formArray!=null && formArray.size()>0){
            params.put("data",formArray);
            JsonResult result=formClient.install(JSONObject.toJSONString(params));
            if(!result.isSuccess()){
                throw new RuntimeException(result.getMessage());
            }
            resultLast.setData(result.getData());
        }
        if(bpmArray!=null && bpmArray.size()>0){
            params.put("data",bpmArray);
            JsonResult result=bpmClient.install(JSONObject.toJSONString(params));
            if(!result.isSuccess()){
                throw new RuntimeException(result.getMessage());
            }
        }
        if(portalArray!=null && portalArray.size()>0){
            params.put("data",portalArray);
            JsonResult result= portalClient.install(JSONObject.toJSONString(params));
            if(!result.isSuccess()){
                throw new RuntimeException(result.getMessage());
            }
        }
        return resultLast;
    }

    /**
     * @Description: 导入本数据库中应用相关的数据
     * @param data 应用相关数据
     **/
    private JsonResult install(HashMap<String,Object>  data){
        try {
            DEFAULT_DB_TYPE=DbUtil.getDbType("");
            String tenantId = (String) data.get("tenantId");
            String overrides = (String) data.get("overrides");
            String treeId= (String) data.get("treeId");
            JSONArray arrData = (JSONArray) data.get("data");
            HashMap logMap=new HashMap();
            //循环处理每一张表
            for (Object obj : arrData) {
                JSONObject tableObj = (JSONObject) JSONObject.toJSON(obj);
                List list= importTableData(tableObj, tenantId, overrides,treeId);
                if(list.size()>0){
                    String tableName=tableObj.getString("tableName");
                    logMap.put(tableName,list);
                }
            }
            if(logMap.size()>0){
                String detail="System库被覆盖的记录：" + JSONObject.toJSONString(logMap);
                LogContext.put(Audit.DETAIL, detail);
            }

        }catch (Exception e){
            return JsonResult.Fail("导入应用系统数据失败：" + e.getMessage());
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
    private List importTableData(JSONObject tableObj, String tenantId, String overrides,String treeId){
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
            if(TABLE_APP.equals(tableName)){
                row.put("CATEGORY_ID_",treeId);
                writeActionLog(pkId,SysAppActionLog.TYPE_IMPORT,"");
            }
            //检查记录是否存在
            String sql ="select *  from " + tableName + " where " + pkName + " = '" + pkId + "'";
            List oldData=commonDao.query(sql);
            if(oldData!=null && oldData.size()>0){
                if(overrides.indexOf("'" + tableName + "'")>0 || TABLE_APP.equals(tableName)){
                    //更新记录
                    updateRecord(tableName,pkName,row);
                    logDetail.add(oldData);
                }
            }else{
                //新增记录
                insertRecord(tableName,row);
                //应用导入时，增加默认应用管理权限
                if(TABLE_APP.equals(tableName) ){
                    sysAppManagerService.AddManager4CurUser(pkId);
                }
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
    * @Description:  写应用操作日志
    * @param appId 应用ID
     * @param  type 操作类型
     * @param  content 操作内容
    * @Author: Elwin ZHANG  @Date: 2021/8/13 9:43
    **/
    public void writeActionLog(String  appId,int type,String content){
        SysAppActionLog log=new SysAppActionLog();
        log.setAppId(appId);
        log.setType(type);
        log.setTitle(log.getTypeName(type));
        log.setContent(content);
        log.setId(IdGenerator.getIdStr());
        sysAppActionLogMapper.insert(log);
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
     * @param jsonArray 应用压缩包里的JSON文件数组
     * @param redisKey  要将文件数组缓存在redis中的key
     * @return com.redxun.common.base.entity.JsonResult
     * @Description: 校验上传的应用数据
     * @Author: Elwin ZHANG  @Date: 2021/7/19 18:12
     **/
    public JsonResult importCheck(JSONArray jsonArray, String redisKey) {
        String message = "";
        List<JSONObject> tableChecks = new ArrayList<JSONObject>();
        JsonResult result = JsonResult.Success();
        boolean isNewApp = false;
        String appName="";
        JSONArray formArray = new JSONArray();
        JSONArray systemArray = new JSONArray();
        JSONArray bpmArray = new JSONArray();
        JSONArray portalArray = new JSONArray();
        JSONArray ureportArray = new JSONArray();
        try {
            for (Object obj : jsonArray) {
                JSONObject tableObj = (JSONObject) obj;
                String dataSource = tableObj.getString("dataSource");
                //根据数据源分流到各个微服务
                switch (dataSource) {
                    case SYSTEM_DS:
                        systemArray.add(tableObj);
                        break;
                    case PORTAL_DS:
                        portalArray.add(tableObj);
                        break;
                    case BPM_DS:
                        bpmArray.add(tableObj);
                        break;
                    case UREPORT_DS:
                        ureportArray.add(tableObj);
                        break;
                    default:
                        formArray.add(tableObj);
                }
                String tableName = tableObj.getString("tableName");
                //判断是否存在相同的应用ID
                if (TABLE_APP.equals(tableName)) {
                    JSONObject rowObj = tableObj.getJSONArray("data").getJSONObject(0);
                    String appId = rowObj.getString("APP_ID_");
                    String appCode = rowObj.getString("CLIENT_CODE_");
                    appName= rowObj.getString("CLIENT_NAME_");
                    SysApp app = sysAppMapper.selectById(appId);
                    if (app == null) {
                        isNewApp = true;
                        QueryWrapper wrapper = new QueryWrapper();
                        wrapper.eq("CLIENT_CODE_", appCode);
                        app = sysAppMapper.selectOne(wrapper);
                        if (app != null) {
                            return JsonResult.Fail("校验失败：系统中存与压缩包中应用ID不同但应用编码相同的应用!");
                        }
                    } else {
                        if (!appCode.equals(app.getClientCode())) {
                            return JsonResult.Fail("校验失败：系统中存与压缩包中应用ID相同而应用编码不同的应用!");
                        }
                    }
                }
            }
            //检查各个数据库相关的表
            if(systemArray.size()>0) {
                List<JSONObject> checks =  checkTables(systemArray);
                tableChecks.addAll(checks);
            }
            if(ureportArray.size()>0){
                JsonResult outResult=ureportClient.importCheck(ureportArray.toArray());
                System.out.println("1." +outResult.getData());
                addResult2List(tableChecks,outResult);
            }
            if(portalArray.size()>0){
                JsonResult outResult=portalClient.importCheck(portalArray.toArray());
                System.out.println("2." + outResult.getData());
                addResult2List(tableChecks,outResult);
            }
            if(bpmArray.size()>0){
                JsonResult outResult=bpmClient.importCheck(bpmArray.toArray());
                System.out.println("3." + outResult.getData());
                addResult2List(tableChecks,outResult);
            }
            if(formArray.size()>0){
                JsonResult outResult=formClient.importCheck(formArray.toArray());
               System.out.println("4." + outResult.getData());
                addResult2List(tableChecks,outResult);
            }
        } catch (Exception e) {
            return JsonResult.Fail("校验失败：" + e.getMessage());
        }
        tableChecks.sort((JSONObject o1, JSONObject o2)->o2.getLong(REPEAT_QTY).compareTo(o1.getLong(REPEAT_QTY)));
        result.setData(tableChecks);
        if(isNewApp){
            message="应用【" + appName + "】将进行首次安装，校验如下：";
        }else{
            message="应用【" + appName + "】将进行升级安装，校验如下：";
        }
        result.setShow(false);
        result.setMessage(message);
        //分开缓存文件内容
        redisRepository.setExpire(redisKey + FORM_DS, formArray,CACHE_TIME);
        redisRepository.setExpire(redisKey + SYSTEM_DS, systemArray,CACHE_TIME);
        redisRepository.setExpire(redisKey + PORTAL_DS, portalArray,CACHE_TIME);
        redisRepository.setExpire(redisKey + UREPORT_DS, ureportArray,CACHE_TIME);
        redisRepository.setExpire(redisKey + BPM_DS, bpmArray,CACHE_TIME);
        return result;
    }

    /**
    * @Description: 将其他微服务返回的信息合并到同一个list中
    * @param checks 检查的结果List
     * @param result 其他微服务返回的结果
    * @Author: Elwin ZHANG  @Date: 2021/7/23 17:37
    **/
    private void addResult2List(List<JSONObject> checks,JsonResult result){
        Object data=result.getData();
        if (data==null){
            return;
        }
        JSONArray arr=(JSONArray)JSONObject.toJSON( data);
        if(arr==null || arr.size()==0){
            return;
        }
        checks.addAll(arr.toJavaList(JSONObject.class));
    }
    /**
     * @param array    表信息数组
     * @return java.util.List<com.alibaba.fastjson.JSONObject>
     * @Description: 校验将要导入的表信息
     * @Author: Elwin ZHANG  @Date: 2021/7/20 11:39
     **/
    public List<JSONObject>  checkTables(JSONArray array) {
        List<JSONObject> list = new ArrayList<>();
        for (Object obj : array) {
            JSONObject tableObj = (JSONObject) obj;
            String tableName = tableObj.getString("tableName");
            if (TABLE_APP.equals(tableName)) {
                continue;
            }
            String tableDesc = tableObj.getString("tableDesc");
            String pkName = tableObj.getString("pk");
            JSONArray data = tableObj.getJSONArray("data");
            JSONObject info = new JSONObject();
            info.put("tableName",tableName);
            info.put("tableDesc", tableDesc);
            info.put("totalQty", data.size());
            long repeatQty = getRepeatQty(data, tableName, pkName);
            info.put(REPEAT_QTY, repeatQty);
            list.add(info);
        }
        return list;
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
            JSONObject rowObj = (JSONObject)JSONObject.toJSON(obj);
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
     * @param appId 应用ID
     * @Description: 删除当前数据库（Jpaas_System）中的某应用关联数据
     * @Author: Elwin ZHANG  @Date: 2021/6/25 13:59
     **/
    public void delDataOfApp(String appId) {
        HashMap logMap=new HashMap();
        //删除关联表的数据
        for (int i = appTables.length - 1; i >= 0; i--) {
            String[] tableProp = appTables[i].split("@");
            String tableName = tableProp[0];
            String sql="select  * FROM " + tableName + " WHERE APP_ID_='" + appId + "'";
            List oldData=commonDao.query(sql);
            if(oldData!=null && oldData.size()>0) {
                sql = "DELETE FROM " + tableName + " WHERE APP_ID_='" + appId + "'";
                commonDao.execute(sql);
                logMap.put(tableName,oldData);
            }
        }
        if(logMap.size()>0){
            String detail="System库删除的记录：" + JSONObject.toJSONString(logMap);
            LogContext.put(Audit.DETAIL, detail);
        }
        //删除应用授权
        sysAppManagerService.deleteByAppId(appId);
    }

    /**
     * 获取所有应用列表
     *
     * @return
     */
    public List<SysApp> getAllApps() {
        //获取状态为启用的应用
        return sysAppMapper.getAllByStatus();
    }

    /**
     * @param appId 应用ID
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     * @Description: 通过应用ID查找应用，如果应用ID为空则显示系统内置的应用
     * @Author: Elwin ZHANG  @Date: 2021/5/20 16:32
     **/
    public List<SysApp> getById(String appId) {
        return sysAppMapper.getById(appId);
    }

    /**
     * 根据ID集合获取应用列表
     *
     * @param appIds,如：1,2,3,4
     * @return
     */
    public List<SysApp> getAppsByIds(String appIds) {
        return sysAppMapper.getAppsByIds(appIds);
    }

    public JsonResult saveOrUpdateSysApp(SysApp sysApp) {
        String clientId = appProperties.getClientId();
        String appId=sysApp.getAppId();
        this.save(sysApp);
        ClientDetails clientDetails = appConfig.getByApp();
        // 写入redis缓存
        redisRepository.set(clientRedisKey(clientId), clientDetails);
        return JsonResult.Success("操作成功");
    }


    public boolean isExist(SysApp ent) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("CLIENT_CODE_", ent.getClientCode());
        if (StringUtils.isNotEmpty(ent.getAppId())) {
            queryWrapper.ne("APP_ID_", ent.getAppId());
        }
        int count = sysAppMapper.selectCount(queryWrapper);

        return count > 0;
    }

    /**
     * 获取非单页面应用
     *
     * @return
     */
    public List<SysApp> getApps(String tenantId, String appId) {
        return sysAppMapper.getApps(tenantId, appId);
    }

    /**
     * @param appId 应用ID
     * @return boolean 系统内置应用为真，其他为假
     * @Description: 是否是系统内置的应用
     * @Author: Elwin ZHANG  @Date: 2021/5/20 14:33
     **/
    public boolean isFixedApp(String appId) {
        //不传应用ID，默认为内置应用
        if (StringUtils.isEmpty(appId) || "undefined".equals(appId) || "0".equals(appId)) {
            return true;
        }
        SysApp app = sysAppMapper.selectById(appId);
        if (app == null) {
            return false;
        }
        if (app.getAppType() == SysApp.TYPE_FIXED) {
            return true;
        }
        return false;
    }

    /**
    * @Description: 获取某个分类下的应用,参数值为-1或空时查询全部分类
    * @param categoryId  应用分类Id
     * @param  key  搜索关键字
    * @return java.util.List<com.redxun.system.core.entity.SysApp>
    * @Author: Elwin ZHANG  @Date: 2021/8/30 10:04
    **/
    public List<SysApp>getByCategory(String categoryId,String key){
        QueryWrapper wapper = new QueryWrapper();
        wapper.eq("APP_TYPE_",1);
        if(StringUtils.isNotEmpty(categoryId) && (!"-1".equals(categoryId))){
            wapper.eq("CATEGORY_ID_",categoryId);
        }
        String tenantId= ContextUtil.getCurrentTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId="1";
        }
        wapper.eq("TENANT_ID_",tenantId);
        if(StringUtils.isNotEmpty(key)){
            wapper.like("CLIENT_NAME_",key);
        }

        String sql=getAppAuthFilter();
        //当前用户的应用授权过滤
        if(sql.length()>0){
            wapper.apply(sql);
        }
        wapper.orderByAsc("SN_");
        return  sysAppMapper.selectList(wapper);
    }

    /**
    *  功能：获取当前用户的应用授权过滤SQL条件
    * @author  Elwin ZHANG
    * @date 2022/2/25 16:36
    **/
    public String getAppAuthFilter(){
        JPaasUser curUser=(JPaasUser)ContextUtil.getCurrentUser();
        //管理员不用过滤
        if(curUser.isAdmin()){
            return "";
        }
        String userId=curUser.getUserId();
        List<String>groupIds=curUser.getRoles();
        String sql=" APP_ID_ IN (SELECT APP_ID_ FROM sys_app_manager WHERE  (USER_OR_GROUP_ID_ = '" + userId + "' AND IS_GROUP_=0) ";
        //再按用户组过滤
        if(groupIds!=null && groupIds.size()>0){
            sql += " OR ( USER_OR_GROUP_ID_ IN (";
            int cnt=groupIds.size();
            for(int i=0;i<cnt;i++){
                if(i<cnt-1){
                    sql += "'" +groupIds.get(i) + "',";
                }else {
                    sql += "'" +groupIds.get(i) + "')";
                }
            }
            sql += " AND IS_GROUP_=1 )) ";
        }
        return sql;
    }

    /**
     * @Description: 获取各分类下当前用户应用的数量
     * @Author: Elwin ZHANG  @Date: 2021/8/30 10:00
     **/
    public List<HashMap> getCountByCategory(){
        String tenantId= ContextUtil.getCurrentTenantId();
        if(StringUtils.isEmpty(tenantId)){
            tenantId="1";
        }
        String authSql=getAppAuthFilter();
        return  sysAppMapper.getCountByCategory(tenantId,authSql);
    }

    /**
     * 根据租户获取APPP
     *
     * @param tenantId
     * @return
     */
    public List<SysApp> getAppByTenant(String tenantId,String deleted) {
        return sysAppMapper.getAppByTenant(tenantId,deleted);
    }

    public List<SysApp> getAppsByIdsAndType(String appIds, int appType) {
        return  sysAppMapper.getAppsByIdsAndType(appIds,appType);
    }




}


