package com.redxun.form.core.controller;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.datasource.DataSourceConstant;
import com.redxun.db.CommonDao;
import com.redxun.dto.sys.SysAppDto;
import com.redxun.form.core.entity.FormDataSourceDef;
import com.redxun.form.core.entity.FormDatasourceShare;
import com.redxun.form.core.service.FormDataSourceDefServiceImpl;
import com.redxun.form.core.service.FormDatasourceShareServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/form/core/formDataSourceDef")
@ClassDefine(title = "数据源定义管理",alias = "formDataSourceDefController",path = "/form/core/formDataSourceDef",packages = "core",packageName = "表单管理")
@Api(tags = "数据源定义管理")
public class FormDataSourceDefController extends BaseController<FormDataSourceDef> {


    @Autowired
    FormDataSourceDefServiceImpl formDataSourceDefServiceImpl;
    @Resource
    FormDatasourceShareServiceImpl formDatasourceShareService;

    @Autowired
    private ConfigService configService;
    @Resource
    private CommonDao commonDao;



    @Override
    public BaseService getBaseService() {
        return formDataSourceDefServiceImpl;
    }

    @Override
    public String getComment() {
        return "数据源定义管理";
    }

    @PostMapping("/executeSql")
    public JsonResult executeSql(@RequestParam(value="alias") String alias,@RequestParam(value="sql") String sql,@RequestBody Map<String, Object> resultMap){
        JsonResult result=JsonResult.Success("执行成功！");
        try {
            commonDao.execute(alias,sql,resultMap);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
        return result;
    }
    /**
     * 测试连接
     * @param alias
     * @return
     */
    @MethodDefine(title = "获取数据源", path = "/getDataByAliasAndSql", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "alias"),@ParamDefine(title = "SQL", varName = "sql")})
    @ApiOperation(value="获取数据源", notes="获取数据源")
    @GetMapping("/getDataByAliasAndSql")
    public List getDataByAliasAndSql(@RequestParam(value="alias") String alias,@RequestParam(value="sql") String sql){
        List result = commonDao.query(alias, sql);
        return result;
    }

    /**
     * 测试连接
     * @param pkId
     * @return
     */
    @MethodDefine(title = "测试数据源连接", path = "/testConnect", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "主键", varName = "pkId")})
    @ApiOperation(value="测试连接", notes="测试数据源连接")
    @PostMapping("/testConnect")
    public JsonResult testConnect(@RequestParam(value="pkId") String pkId) throws Exception{
        FormDataSourceDef formDataSourceDef= formDataSourceDefServiceImpl.get(pkId);
        return formDataSourceDefServiceImpl.testConnect(formDataSourceDef,true);
    }


    /**
     * 测试连接
     * @param dataSourceDef
     * @return
     */
    @MethodDefine(title = "测试数据源连接", path = "/testConnect", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据源主体", varName = "dataSourceDef")})
    @ApiOperation(value="测试连接", notes="测试数据源连接")
    @PostMapping("/testConnect2")
    public JsonResult testConnect2(@RequestBody FormDataSourceDef dataSourceDef) throws Exception{
        return formDataSourceDefServiceImpl.testConnect(dataSourceDef,true);
    }

    @PostMapping("/getPassword")
    public JsonResult getPassword(@RequestBody JSONObject config) {
        String password = config.getString("password");
        boolean encrypt = config.getBoolean("encrypt");
        try {
            if(StringUtils.isNotEmpty(password)) {
                if (encrypt) {
                    password = ConfigTools.encrypt(password);
                } else {
                    password = ConfigTools.decrypt(password);
                }
            }
        } catch (Exception e) {
        }
        return new JsonResult(true,password,"");
    }

    /**
    * @Description: 应用导入数据源,需要特殊处理
    * @param entity 数据源对象
     * @param isInsert 是否新增对象
    * @return com.redxun.common.base.entity.JsonResult
    * @Author: Elwin ZHANG  @Date: 2021/8/3 17:25
    **/
    public JsonResult importDataSource(FormDataSourceDef entity,boolean isInsert) throws NacosException {
        boolean isExist = formDataSourceDefServiceImpl.isExist(entity);
        if (isExist) {
            return JsonResult.Success();
        }
        //只有允许才校验数据库连接。
        if (MBoolean.TRUE_LOWER.val.equals(entity.getEnable())) {
            JsonResult validResult = formDataSourceDefServiceImpl.testConnect(entity,false);
            if (!validResult.isSuccess()) {
                return validResult;
            }
        }
        BaseDao<FormDataSourceDef> mapper=formDataSourceDefServiceImpl.getRepository();
        if(isInsert){
            mapper.insert(entity);
        }else {
            mapper.updateById(entity);
        }
        String config = configService.getConfig(DataSourceConstant.DATASOURCE_CONFIG , DataSourceConstant.DEFAULT_GROUP, 0L);
        if( StringUtils.isEmpty(config)){
            config="{}";
        }
        JSONObject conf = JSONObject.parseObject(config);
        JSONObject json= buildDatasourceJson(entity);

        if (MBoolean.TRUE_LOWER.val.equals(entity.getEnable())) {
            conf.put(entity.getAlias(), json);
        } else {
            if (conf.containsKey(entity.getAlias())) {
                conf.remove(entity.getAlias());
            }
        }
        JSONObject jsonTmp=new JSONObject();
        jsonTmp.put(entity.getAlias(), json);
        //发布全部的数据源
        configService.publishConfig(DataSourceConstant.DATASOURCE_CONFIG, DataSourceConstant.DEFAULT_GROUP, conf.toJSONString());
        //发布临时数据源
        configService.publishConfig(DataSourceConstant.DATASOURCE_TEMP, DataSourceConstant.DEFAULT_GROUP, jsonTmp.toJSONString());

        return JsonResult.Success();
    }
    @Override
    @AuditLog(operation = "保存自定义数据源")
    @PostMapping({"/save"})
    public JsonResult save(@ApiParam @RequestBody FormDataSourceDef entity, BindingResult validationResult) throws Exception {
        boolean isExist = formDataSourceDefServiceImpl.isExist(entity);
        if (isExist) {
            return JsonResult.Fail("数据源别名系统中已存在!");
        }
        //只有允许才校验数据库连接。
        if (MBoolean.TRUE_LOWER.val.equals(entity.getEnable())) {
            JsonResult validResult = formDataSourceDefServiceImpl.testConnect(entity,false);
            if (!validResult.isSuccess()) {
                return validResult;
            }
        }
        //获取之前的变更序号
        if(BeanUtil.isNotEmpty(entity.getId())){
            FormDataSourceDef formDataSourceDef = formDataSourceDefServiceImpl.get(entity.getId());
            entity.setChangeSn(formDataSourceDef.getChangeSn());
        }
        JsonResult jsonResult = super.save(entity,validationResult);
        String config = configService.getConfig(DataSourceConstant.DATASOURCE_CONFIG , DataSourceConstant.DEFAULT_GROUP, 0L);
        if( StringUtils.isEmpty(config)){
            config="{}";
        }
        JSONObject conf = JSONObject.parseObject(config);
        JSONObject json= buildDatasourceJson(entity);

        if (MBoolean.TRUE_LOWER.val.equals(entity.getEnable())) {
            conf.put(entity.getAlias(), json);
        } else {
            if (conf.containsKey(entity.getAlias())) {
                conf.remove(entity.getAlias());
            }
        }
        JSONObject jsonTmp=new JSONObject();
        jsonTmp.put(entity.getAlias(), json);
        //发布全部的数据源
        configService.publishConfig(DataSourceConstant.DATASOURCE_CONFIG, DataSourceConstant.DEFAULT_GROUP, conf.toJSONString());
        //发布临时数据源
        configService.publishConfig(DataSourceConstant.DATASOURCE_TEMP, DataSourceConstant.DEFAULT_GROUP, jsonTmp.toJSONString());

        return jsonResult;
    }

    @PostMapping(value = "del")
    @AuditLog(operation = "删除数据源定义")
    @Override
    public JsonResult del(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new JsonResult(false, "");
        }
        String[] aryId = ids.split(",");
        List<String> list = Arrays.asList(aryId);
        try {
            String config = configService.getConfig(DataSourceConstant.DATASOURCE_CONFIG, DataSourceConstant.DEFAULT_GROUP, 0L);
            JSONObject conf=null;
            if(StringUtils.isEmpty(config)){
                config="{}";
            }
            conf = JSONObject.parseObject(config);

            JSONObject tmpDataSource=new JSONObject();

            for (String id : list) {
                FormDataSourceDef entity = formDataSourceDefServiceImpl.get(id);
                if (conf.containsKey(entity.getAlias())) {
                    conf.remove(entity.getAlias());
                }
                entity.setEnable(MBoolean.FALSE_LOWER.val);
                JSONObject json= buildDatasourceJson(entity);

                tmpDataSource.put(entity.getAlias(),json);
                //删除共享记录
                formDatasourceShareService.deleteByDataSourceId(id);
            }
            String tmpJson=tmpDataSource.toJSONString();

            configService.publishConfig(DataSourceConstant.DATASOURCE_TEMP, DataSourceConstant.DEFAULT_GROUP, tmpJson);
            configService.publishConfig(DataSourceConstant.DATASOURCE_CONFIG, DataSourceConstant.DEFAULT_GROUP, conf.toJSONString());
        } catch (Exception e) {
            MessageUtil.triggerException("删除数据源定义失败!", ExceptionUtil.getExceptionMessage(e));
        }
        return super.del(ids);
    }

    private JSONObject buildDatasourceJson(FormDataSourceDef entity){
        JSONObject json = new JSONObject();
        json.put("appName",entity.getAppName());
        json.put("setting",entity.getSetting());
        json.put("enable",entity.getEnable());
        return json;
    }

    @MethodDefine(title = "获取数据源共享给哪些应用", path = "/getShareApps", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "数据源ID", varName = "dsId")})
    @ApiOperation(value="获取数据源共享给哪些应用", notes="获取该数据源共享给哪些应用")
    @GetMapping("/getShareApps")
    public JsonResult  getShareApps(@ApiParam @RequestParam(value="dsId") String dsId){
        if(StringUtils.isEmpty(dsId)){
            return  JsonResult.Fail("参数不正确");
        }
        JsonResult result=JsonResult.Success();
        result.setShow(false);
        Object data=formDatasourceShareService.getByDataSourceId(dsId);
        result.setData(data);
        return  result;
    }

    @MethodDefine(title = "保存数据源共享结果", path = "/saveShareApps", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "共享给的应用列表", varName = "apps"),@ParamDefine(title = "数据源ID", varName = "dsId")})
    @ApiOperation(value="保存数据源共享结果", notes="保存数据源共享结果")
    @PostMapping ("/saveShareApps")
    @Transactional
    public JsonResult saveShareApps(@ApiParam @RequestBody List<SysAppDto> apps, @ApiParam @RequestParam(value="dsId") String dsId){
        if(StringUtils.isEmpty(dsId)){
            return JsonResult.Fail("参数不正确！");
        }
         FormDataSourceDef dataSourceDef= formDataSourceDefServiceImpl.getById(dsId);
        if(dataSourceDef==null){
            return JsonResult.Fail("参数不正确！");
        }
        //先删除旧的记录
        formDatasourceShareService.deleteByDataSourceId(dsId);
        //再插入新的关联记录
        if(apps==null || apps.size()==0){
            return  JsonResult.Success("取消共享完成！");
        }
        List<FormDatasourceShare> listShare=new ArrayList<>();
        for (SysAppDto app : apps ) {
            FormDatasourceShare share=new FormDatasourceShare();
            share.setShareId(IdGenerator.getIdStr());
            share.setAppId(app.getAppId());
            share.setAppName(app.getClientName());
            share.setDsId(dsId);
            listShare.add(share);
        }
        String ownerAppId=dataSourceDef.getAppId();
        formDatasourceShareService.batchInsert(listShare);
        return  JsonResult.Success("共享成功！");
    }
    /**
    * @Description: 应用里面需要处理数据源共享
    * @Author: Elwin ZHANG  @Date: 2021/12/29 9:48
    **/
    @Override
    protected void handleFilter(QueryFilter filter) {
        Map<String, QueryParam> params=filter.getQueryParams();
        QueryParam paramShare=params.get("SHARE");
        QueryParam paramAppId=params.get("APP_ID_");
        //不需要查询共享,先退出
        if(paramShare==null || paramAppId==null ){
            super.handleFilter(filter);
            return;
        }
        String appId=paramAppId.getValue().toString();
        //重新构造查询条件
        paramShare.setFieldName("ID_");
        paramShare.setFieldType("I");
        paramShare.setOpType("IN");
        String sql=" SELECT DS_ID_ AS ID_ FROM form_datasource_share WHERE APP_ID_='" + appId +
                "' UNION SELECT ID_ from form_datasource_def  WHERE APP_ID_='" + appId + "' ";
        paramShare.setValue(sql);
        paramAppId.setFieldName("1");
        paramAppId.setValue("1");
        super.handleFilter(filter);
    }

    /**
     * 根据AppName获取数据源
     * @param appName
     * @return
     */
    @MethodDefine(title = "根据AppName获取数据源", path = "/getAllByAppName", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "alias"),@ParamDefine(title = "appName", varName = "appName")})
    @ApiOperation(value="根据AppName获取数据源", notes="根据AppName获取数据源")
    @GetMapping("/getAllByAppName")
    public List getAllByAppName(@RequestParam(value="appName") String appName){
        QueryFilter queryFilter=new QueryFilter();
        if(StringUtils.isNotEmpty(appName)){
            queryFilter.addParam("Q_APP_NAME__S_LK", appName);
        }
        List list = formDataSourceDefServiceImpl.queryList(queryFilter);
        return list;
    }

    /**
     * 获取租户使用的数据源
     * @return
     */
    @MethodDefine(title = "获取租户使用的数据源", path = "/getInstDatasource", method = HttpMethodConstants.GET)
    @ApiOperation(value="获取租户使用的数据源", notes="获取租户使用的数据源")
    @GetMapping("/getInstDatasource")
    public JsonResult getInstDatasource(){
        List<FormDataSourceDef> list = formDataSourceDefServiceImpl.getInstDatasource();
        return new JsonResult(true,list,"获取成功!").setShow(false);
    }
}
