package com.redxun.form.core.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.api.org.IOrgService;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.script.NoMethodException;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.constvar.ConstVarContext;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.DBMetaImpl;
import com.redxun.dto.form.DataResult;
import com.redxun.dto.form.FormParams;
import com.redxun.dto.form.FormSolutionDto;
import com.redxun.feign.BpmInstClient;
import com.redxun.feign.OsGroupClient;
import com.redxun.feign.bpm.BpmClient;
import com.redxun.feign.bpm.entity.BpmInst;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.datahandler.IDataHandler;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.listener.FormulaSetting;
import com.redxun.form.core.listener.FormulaSettingContext;
import com.redxun.form.core.listener.ICustomFormDataHandler;
import com.redxun.form.core.mapper.FormSolutionMapper;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import groovy.lang.Script;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * [表单方案]业务服务类
 *@author ray
 */
@Service
@Slf4j
public class FormSolutionServiceImpl extends SuperServiceImpl<FormSolutionMapper, FormSolution> implements BaseService<FormSolution> {

    private final static String BEFORE_SAVE = "beforeSave";
    private final static String AFTER_SAVE = "afterSave";
    private final static String CUSTOM_HANDLER = "customHandler";

    private final static String ACTION_SAVE="save";

    private final static String ACTION_STARTFLOW="startFlow";

    public  final static String REGION_FORMSOLUTION="formsol_";


    @Resource
    private FormSolutionMapper formSolutionMapper;
    @Resource
    private FormTableFormulaServiceImpl formTableFormulaService;
    @Resource
    FormPcServiceImpl formPcServiceImpl;
    @Resource
    FormBoEntityServiceImpl formBoEntityService;
    @Autowired
    private IDataHandler dataHandler;
    @Autowired
    private GroovyEngine groovyEngine;
    @Resource
    private ConstVarContext constVarContext;
    @Resource
    private BpmClient bpmClient;
    @Resource
    private FormBoEntityServiceImpl boEntityService;

    @Resource
    private FormDataService formDataService;

    @Autowired
    private FormPermissionServiceImpl formPermissionServiceImpl;
    @Autowired
    private FormBoDefServiceImpl formBoDefService;

    @Resource
    DBMetaImpl dbMetaImpl;

    @Resource
    JdbcTemplate jdbcTemplate;
    @Resource
    IOrgService orgService;
    @Resource
    private BpmInstClient bpmInstClient;

    @Resource
    FormMobileServiceImpl formMobileServiceImpl;
    @Resource
    OsGroupClient osGroupClient;

    @Resource
    FormPermissionServiceImpl formPermissionService;
    @Resource
    FormEntRelationServiceImpl formEntRelationService;



    @Override
    public BaseDao<FormSolution> getRepository() {
        return formSolutionMapper;
    }

    public String getCacheKey(String alias){
        return REGION_FORMSOLUTION +alias;
    }

    /**
     * 根据别名查询。
     * @param alias
     * @return
     */
    public FormSolution getByAlias(String alias){
        String key=getCacheKey(alias);
        Object obj= CacheUtil.get(REGION_FORMSOLUTION,key);
        if(obj!=null){
            return (FormSolution)obj;
        }

        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",alias);
        FormSolution solution= formSolutionMapper.selectOne(queryWrapper);
        if(solution!=null) {
            //设置缓存。
            CacheUtil.set(REGION_FORMSOLUTION, key, solution);
        }
        return  solution;

    }

    /**
     * 判断方案是否存在。
     * @param solution
     * @return
     */
    public boolean isExist(FormSolution solution){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ALIAS_",solution.getAlias());
        if(StringUtils.isNotEmpty( solution.getId())){
            queryWrapper.ne("ID_",solution.getId());
        }
        int count=formSolutionMapper.selectCount(queryWrapper);

        return count>0;
    }

    /**
     * 表单方案数据处理。
     * <pre>
     *      1.执行前置脚本。
     *      2.设置表间公式。
     *      3.处理数据保存
     *      4.处理流程
     *      5.执行后置脚本
     * </pre>
     *
     * @param jsonObject {
     *      setting:{alias:"表单方案名称",action:"动作 startFlow:启动流程,save:保存数据"},
     *      //表单数据。
     *      data:{}
     * }
     * @return
     */
    @GlobalTransactional
    @Transactional
    public JsonResult handData(JSONObject jsonObject) {
        if(jsonObject.containsKey(Audit.IS_LOG)) {
            LogContext.put(Audit.IS_LOG,jsonObject.getBoolean(Audit.IS_LOG)? MBoolean.YES.name():MBoolean.NO.name());
        }
        LogContext.put(Audit.BUS_TYPE,Audit.BUS_TYPE_FORM);
        JSONObject setting = jsonObject.getJSONObject("setting");
        JSONObject data = jsonObject.getJSONObject("data");
        String alias = setting.getString("alias");
        FormSolution formSolution = this.getByAlias(alias);
        //操作有两个 startFlow:启动流程,save:保存草稿
        String action = setting.getString("action");
        String opinion = setting.getString("opinion");
        String instId=data.getString(FormSolution.INST_ID);


        FormPc formPc = formPcServiceImpl.get(formSolution.getFormId());
        //1.执行前置脚本
        String javaCode = formSolution.getJavaCode();
        Script script = null;
        if (StringUtils.isNotEmpty(javaCode)) {
            script = groovyEngine.getScript(javaCode);
        }
        JsonResult preResult = handPreScript(script, javaCode, data);
        if (!preResult.isSuccess()) {
            return preResult;
        }

        //2.设置表间公式。
        handFormula(formSolution,action);

        //3.处理数据保存。
        String handler=formSolution.getDataHandler();
        DataResult result=null;

        //添加脚本自己处理数据。
        if(StringUtils.isNotEmpty(javaCode) && javaCode.indexOf(CUSTOM_HANDLER)!=-1) {
            JsonResult  customResult = handCustomScript(script, javaCode, data);
            if(!customResult.isSuccess()){
                return customResult;
            }
        }
        else if(StringUtils.isEmpty(handler)){
            FormBoEntity boEntity= boEntityService.getByDefId(formPc.getBodefId(),true);
            //给FormBoAttr赋值formsetting和 datasetting
            FormHelper.injectAttrJson(boEntity,formPc.getFormSettings(),formPc.getDataSetting());
            result = dataHandler.save(data, formPc.getAlias(),boEntity,jsonObject.containsKey(Audit.IS_LOG));
        }
        else {
            ICustomFormDataHandler dataHandler = SpringUtil.getBean(formSolution.getDataHandler());

            result = dataHandler.save(formSolution.getBodefId(),data,jsonObject.containsKey(Audit.IS_LOG));
        }

        //4.处理流程
        JSONObject boDataJson=new JSONObject();
        boDataJson.put(formPc.getBoDefAlias(),data);

        //5.执行后置脚本
        handAfterScript(script, javaCode, data, result);

        String message="保存数据成功!";
        String defId=getDefIdByDefMapping(formSolution.getFlowDefMapping(),data);

        boolean canStartFlow=canStartFlow(formSolution,data);
        //启动流程。
        if(canStartFlow) {
            JsonResult rtnFlow= handFlow(defId,instId, result,action,formSolution,boDataJson);
            if (rtnFlow.isSuccess()) {
                message = "save".equals(action) ? "保存草稿成功!" : "启动流程成功!";
            } else {
                MessageUtil.triggerException(rtnFlow.getMessage(),(String)rtnFlow.getData());
            }
        }
        JsonResult rtn = JsonResult.Success(message);
        result.setSolutionAlias(alias);
        rtn.setData(result);

        return rtn;
    }


    /**
     * 是否能启动流程。
     * <pre>
     *     1.首先判断数据中是否有流程实例。
     *      如果存在流程实例，如果流程的状态为草稿，表示可以启动流程，否则不能启动流程。
     *     2.如果存在流程定义ID，可以启动流程。
     * </pre>
     * @param formSolution
     * @param data
     * @return
     */
    private Boolean canStartFlow(FormSolution  formSolution,JSONObject data){
        String instId = data.getString(FormSolution.INST_ID);
        String instStatus = data.getString(FormSolution.INST_STATUS);

        if(StringUtils.isNotEmpty(instId)){
            if(FormSolution.INST_STATUS_DRAFT.equals(instStatus)){
                return true;
            }
            return false;
        }
        String defId=getDefIdByDefMapping(formSolution.getFlowDefMapping(),data);
        if(StringUtils.isNotEmpty(defId)){
            return true;
        }
        return false;

    }

    /**
     * 获取满足条件的流程定义ID
     * <pre>
     *     defMapping:流程定义配置
     *     [{
     *         config:{
     *             mode:"easy,hard",
     *             //简单模式配置。
     *             easyTableData:[{
     *                 formField:"字段",
     *                 logic:"逻辑",
     *                 op:"比较",
     *                 dataType:"数据类型",
     *                 condition:"值"
     *             }],
     *             //高级模式需要使用这个。
     *             javaCode:""
     *         }
     *     }],
     *     data:表单数据
     * </pre>
     *
     * @param defMapping
     * @param data
     * @return
     */
    public String getDefIdByDefMapping(String defMapping,JSONObject data){
        if(StringUtils.isEmpty(defMapping)){
            return null;
        }
        JSONArray defAry=JSONArray.parseArray((defMapping));
        for(Object obj:defAry){
            JSONObject defJson=(JSONObject)obj;
            JSONObject config=defJson.getJSONObject("config");
            String mode=config.getString("mode");
            String setting="";
            //配置模式
            if("easy".equals(mode)){
                setting=getEasyScript(config,data);
            }
            //高级模式
            else if("hard".equals(mode)){
                setting=config.getString("javaCode");
            }
            if(StringUtils.isEmpty(setting)){
                return defJson.getString("defId");
            }
            Object flag=groovyEngine.executeScripts(setting,data.getInnerMap());
            if(flag instanceof Boolean && (Boolean)flag) {
                return defJson.getString("defId");
            }
        }
        return null;
    }

    /**
     * 获取配置的脚本。
     * @param config
     * @param data
     * @return
     */
    private String getEasyScript(JSONObject config,JSONObject data){
        String setting="";
        JSONArray easyTableData=config.getJSONArray("easyTableData");
        int idx=-1;
        for(int i=0;i<easyTableData.size();i++){
            JSONObject easyField=easyTableData.getJSONObject(i);
            String formField=easyField.getString("formField");
            if(!data.containsKey(formField)){
                continue;
            }
            String logic=(String)easyField.getOrDefault("logic","AND");
            String op=(String)easyField.getOrDefault("op","==");
            String type=easyField.getString("dataType");
            String value=easyField.getString("condition");
            if(!"Number".equals(type) && !"number".equals(type) && !"int".equals(type)){
                value = "\""+value+"\"";
            }
            idx++;
            if(idx==0){
                setting += getSettingByOp(formField,op,value);
                continue;
            }
            if("AND".equals(logic)) {
                setting += " && "+getSettingByOp(formField,op,value);
            }else if("OR".equals(logic)){
                setting += " || "+getSettingByOp(formField,op,value);
            }
        }
        return setting;
    }

    private String getSettingByOp(String key,String op,String value){
        if("include".equals(op)){
            return key+".indexOf("+value+")!=-1";
        }else if("notInclude".equals(op)){
            return key+".indexOf("+value+")==-1";
        }
        return key + op + value;
    }


    /**
     * 处理完成任务报错。
     * @param ex
     */
    public void handException(Exception ex){
        String title="表单数据保存失败!";
        String message= ExceptionUtil.getExceptionMessage(ex);
        //网关出错
        if(message.indexOf("No outgoing sequence")!=-1){
            title="网关没有符合条件的路径，请检查网关条件配置!";
        }
        String tmp= MessageUtil.getTitle();
        if(StringUtils.isNotEmpty(tmp)) {
            title=tmp;
        }
        MessageUtil.triggerException(title,message);

    }

    /**
     * 处理表间公式。
     * @param formSolution
     * @param action
     */
    private void handFormula(FormSolution formSolution,String action){
        String formulas=formSolution.getFormulas();
        if(StringUtils.isEmpty(formulas)) {
            return;
        }
        List<FormTableFormula> formTableFormulas = new ArrayList<>();
        String[] ids = formulas.split(",");
        for (int i = 0; i < ids.length; i++) {
            FormTableFormula formTableFormula=formTableFormulaService.getById(ids[i]);

            if(formTableFormula==null){
                continue;
            }
            if(MBoolean.YES.name().equals(formTableFormula.getEnabled())){
                formTableFormulas.add(formTableFormula);
            }

        }


        FormulaSetting formulaSetting = new FormulaSetting();
        formulaSetting.setFormulaList(formTableFormulas);

        formulaSetting.getExtParams().put("op",action);

        formulaSetting.getExtParams().put("mode", FormulaSetting.FORM);
        FormulaSettingContext.setFormulaSetting(formulaSetting);
    }

    /**
     * 处理流程。
     * <pre>
     *     1.处理流程
     *      包括草稿和启动。
     *     2.更新自定义表的数据，主要是更新流程实例和数据的状态。
     * </pre>
     * @param defId
     * @param result
     * @param action
     * @param formSolution
     */
    private JsonResult handFlow(String defId,String instId, DataResult result,String action,FormSolution formSolution,JSONObject boDataJson){
        //启动流程。
        if(StringUtils.isEmpty(defId)){
            return JsonResult.Fail();
        }
        //保存草稿
        JSONObject cmd=new JSONObject();
        cmd.put("defId",defId);
        if(StringUtils.isNotEmpty(instId)){
            cmd.put("instId",instId);
        }
        cmd.put("busKey",result.getPk());
        cmd.put("boAlias",result.getBoAlias());
        cmd.put("boDataJson",boDataJson.toJSONString());
        cmd.put("formSolutionAlias",formSolution.getAlias());

        JsonResult<BpmInst> rtn=null;
        if(ACTION_SAVE.equals(action)){
           rtn= bpmClient.saveDraft(cmd);
        }
        //启动流程
        else{
            rtn =bpmClient.startProcess(cmd);
        }
        //更新数据
        if(rtn!=null &&  rtn.isSuccess()){

            Map instMap= (Map) rtn.getData();
            instId= (String) instMap.get("instId");
            String status= (String) instMap.get("status");

            //result.setStatus(status);
            //result.setInstId(instId);

            formDataService.updDataByStatus(result.getBoAlias(),
                    instId,
                    status,
                    result.getPk());
        }

        return rtn;
    }

    /**
     * 批量删除表单数据。
     * @param jsonObject {id:"主键数据，使用逗号分隔",alias:"表单方案名称"}
     * @return
     */
    @GlobalTransactional
    public JsonResult removeData(JSONObject jsonObject) {
        if(jsonObject.containsKey(Audit.IS_LOG)) {
            LogContext.put(Audit.IS_LOG,jsonObject.getBoolean(Audit.IS_LOG)? MBoolean.YES.name():MBoolean.NO.name());
        }
        LogContext.put(Audit.ACTION,"removeById");
        LogContext.put(Audit.BUS_TYPE,Audit.BUS_TYPE_FORM);
        String id = jsonObject.getString("id");
        String[] deleteIds = id.split(",");
        String alias = jsonObject.getString("alias");
        //绑定了删除约束 是否级联删除关联数据
        Boolean cascade = jsonObject.getBoolean("cascade");
        FormSolution formSolution = this.getByAlias(alias);
        JSONObject detailJson=new JSONObject();
        detailJson.put("alias",alias);
        detailJson.put("data",new JSONArray());
        //处理表间公式
        handFormula(formSolution,"del");
        ArrayList deleteds=new ArrayList();
        FormPc formPc = formPcServiceImpl.get(formSolution.getFormId());
        FormBoEntity boEntity= boEntityService.getByDefId(formPc.getBodefId(),false);
        String pkField=getPkField(boEntity);
        for (int i = 0; i < deleteIds.length; i++) {
            String pkId = deleteIds[i];

            //删除前判断流程状态，只有状态为空、草稿、作废时才可以删除   Elwin 2021-12-3
            JSONObject record=getData(formPc.getAlias(), pkId,null);
            String instStatus = record.getString("INST_STATUS_");
            if(StringUtils.isNotEmpty(instStatus) && !"CANCEL".equals(instStatus) && !"DRAFTED".equals(instStatus)){
                continue;
            }
            deleteds.add(pkId);
            if (StringUtils.isNotEmpty(formSolution.getDataHandler())) {
                ICustomFormDataHandler handler = SpringUtil.getBean(formSolution.getDataHandler());
                //填充删除数据
                detailJson.getJSONArray("data").add(handler.getByPk(pkId,formPc.getBodefId()));
                JsonResult result = handler.delById(pkId);
                if (!result.isSuccess()) {
                    return JsonResult.Fail(result.getMessage());
                }else {
                    //获取需要关联删除的实体
                    if(BeanUtil.isNotEmpty(cascade) && cascade){
                        formEntRelationService.delRelationData(boEntity,record);
                    }
                }
            } else {
                detailJson.getJSONArray("data").add(dataHandler.getById(formPc.getAlias(),pkId));
                JsonResult result = dataHandler.removeById(formPc.getAlias(), pkId);
                if (!result.isSuccess()) {
                    return JsonResult.Fail(result.getMessage());
                }else {
                    //获取需要关联删除的实体
                    if(BeanUtil.isNotEmpty(cascade) && cascade){
                        formEntRelationService.delRelationData(boEntity,record);
                    }
                }
            }
        }
        if(deleteds.size()==0){
            return JsonResult.Fail("流程审批中或审批完成的记录不能被删除！");
        }

        return JsonResult.Success("删除数据成功!");
    }

    /**
     * 处理前置代码。
     * @param script
     * @param javaCode
     * @param formData
     * @return
     */
    private JsonResult handPreScript(Script script, String javaCode, JSONObject formData) {
        JsonResult result = JsonResult.Success();
        if (script == null || javaCode.indexOf(BEFORE_SAVE) == -1) {
            return result;
        }
        try {
            JsonResult obj = (JsonResult) script.invokeMethod(BEFORE_SAVE, formData);
            if (obj != null && obj instanceof JsonResult && ! obj.isSuccess()) {
                result.setMessage(obj.getMessage());
                result.setSuccess(false);
            }
        }
        catch (NoMethodException ex){
            log.error("没有指定beforeSave方法");
        }
        return  result;
    }


    private JsonResult handCustomScript(Script script, String javaCode, JSONObject formData) {
        JsonResult result = JsonResult.Success();
        try {
            JsonResult obj = (JsonResult) script.invokeMethod(CUSTOM_HANDLER, formData);
            if (obj != null && obj instanceof JsonResult && ! obj.isSuccess()) {
                result.setMessage(obj.getMessage());
                result.setSuccess(false);
            }
        }
        catch (Exception ex){
            result=JsonResult.Fail("处理自定义脚本出错");
            result.setData(ExceptionUtil.getExceptionMessage(ex));
        }
        return  result;
    }

    /**
     * 执行后置脚本。
     * @param script
     * @param javaCode
     * @param formData
     * @param saveResult
     */
    private void handAfterScript(Script script, String javaCode, JSONObject formData, DataResult saveResult){
        if(script==null || javaCode.indexOf(AFTER_SAVE)==-1){
            return;
        }

        AfterParams params = new AfterParams();
        params.setJsonData(formData);
        params.setDataResult(saveResult);
        script.invokeMethod(AFTER_SAVE, params);
    }

    @GlobalTransactional
    public JsonResult rowsSave(JSONObject jsonObject) throws Exception {
        String alias = jsonObject.getString("alias");
        JSONArray rows = jsonObject.getJSONArray("rows");
        FormSolution formSolution = this.getByAlias(alias);
        batchRows(formSolution.getBodefId(), rows);
        return JsonResult.Success("保存行数据成功!");
    }

    /**
     * 批量保存行
     *
     * @param boDefId
     * @param rowArr
     */
    public void batchRows(String boDefId, JSONArray rowArr) throws Exception {
        FormBoEntity sysBoEnt = formBoEntityService.getMainEntByDefId(boDefId);
        if (StringUtils.isNotEmpty(sysBoEnt.getDsAlias())) {
            DataSourceContextHolder.setDataSource(sysBoEnt.getDsAlias());
        }
        Table mainTable = dbMetaImpl.getModelByName(sysBoEnt.getTableName());
        Map<String, Column> colsMap = new HashMap(mainTable.getColumnList().size());
        for (Column col : mainTable.getColumnList()) {
            colsMap.put(col.getFieldName(), col);
        }
        for (int i = 0; i < rowArr.size(); i++) {
            JSONObject row = rowArr.getJSONObject(i);
            if(row.containsKey("isNew") && row.getBoolean("isNew")){
                row.remove(getPkField(sysBoEnt));
            }
            doSaveRow(row, sysBoEnt, mainTable, colsMap);
        }
        DataSourceContextHolder.setDefaultDataSource();
    }


    private void doSaveRow(JSONObject row, FormBoEntity sysBoEnt, Table mainTable, Map<String, Column> colsMap) {
        String pkField = getPkField(sysBoEnt);
        boolean isContainRefId = false;
        Iterator<String> fieldIt = row.keySet().iterator();
        StringBuffer sqlBuffer = new StringBuffer("");
        StringBuffer valBuffer = new StringBuffer("");
        boolean isInsert = false;
        String pkId = row.getString(pkField);
        if (pkId == null) {
            isInsert = true;
            sqlBuffer.append("insert into " + mainTable.getTableName()).append("(");
        } else {
            sqlBuffer.append("update " + mainTable.getTableName());
        }
        List<Object> params = new ArrayList<>();
        int cn = 0;
        while (fieldIt.hasNext()) {
            String field = fieldIt.next();
            if ("REF_ID_".equals(field)) {
                isContainRefId = true;
            }
            if("PARENT_ID_".equals(field)){
                //防止出现重复parent_id_
                continue;
            }
            if (StringUtils.isNotEmpty(field) && !field.startsWith("_")) {
                Column fieldCol = colsMap.get(field.toUpperCase());
                if (fieldCol == null) {
                    continue;
                }
                Object val = null;
                if ("number".equals(fieldCol.getColumnType())) {
                    val = row.getDouble(field);
                } else if ("date".equals(fieldCol.getColumnType())) {
                    val = row.getDate(field);
                } else {
                    val = row.getString(field);
                }
                boolean flag = setColumnValue(sqlBuffer, valBuffer, params, sysBoEnt, isInsert, field, val, cn);
                if (flag) {
                    cn++;
                }
            }
        }
        saveRow(row, sqlBuffer, valBuffer, params, sysBoEnt, isInsert, pkId, isContainRefId, cn);
    }

    private void saveRow(JSONObject row, StringBuffer sqlBuffer, StringBuffer valBuffer, List<Object> params, FormBoEntity sysBoEnt, boolean isInsert, String pkId, boolean isContainRefId, int cn) {
        String pkField = getPkField(sysBoEnt);
        String fkField = getFkField(sysBoEnt);
        String parentField = getParentField(sysBoEnt);
        Map<String, String> idMap = new HashMap<>(SysConstant.INIT_CAPACITY_16);
        String id = row.getString(pkField);
        IUser curUser = ContextUtil.getCurrentUser();
        if (!isInsert) {
            if (cn == 0) {
                sqlBuffer.append(" set ");
            }
            if (!sysBoEnt.external()) {
                sqlBuffer.append("," + FormBoEntity.FIELD_UPDATE_TIME).append("=?");
                params.add(new Date());
            }
            sqlBuffer.append(" where ").append(pkField).append("=?");
            params.add(pkId);
            jdbcTemplate.update(sqlBuffer.toString(), params.toArray());
        } else {
            if (cn > 0) {
                sqlBuffer.append(",");
                valBuffer.append(",");
            }
            sqlBuffer.append(pkField);
            //1. 插入主键ID
            valBuffer.append("?");
            String nPkId = IdGenerator.getIdStr();
            params.add(nPkId);
            idMap.put(id, nPkId);
            if (!isContainRefId) {
                //2 . 插入外键
                String pField = fkField;
                String parentId = row.getString(parentField);
                String pId = idMap.get(parentId);
                if (StringUtils.isNotEmpty(sysBoEnt.getParentField())) {
                    pField = sysBoEnt.getParentField();
                    if (StringUtils.isEmpty(pId)) {
                        pId = row.getString(pField);
                    }
                }
                sqlBuffer.append("," + pField);
                valBuffer.append(",?");
                if (StringUtils.isEmpty(parentId)) {
                    params.add("0");
                } else {
                    params.add(pId);
                }
            }
            if (!sysBoEnt.external()) {
                //3.插入用户ID
                sqlBuffer.append(",").append(FormBoEntity.FIELD_CREATE_BY);
                valBuffer.append(",?");
                params.add(curUser.getUserId());
                //4.组ID
                sqlBuffer.append(",").append(FormBoEntity.FIELD_CREATE_DEP);
                valBuffer.append(",?");
                params.add(curUser.getDeptId());
                //5.创建时间
                sqlBuffer.append(",").append(FormBoEntity.FIELD_CREATE_TIME);
                valBuffer.append(",?");
                params.add(new Date());
                //6.租户ID
                sqlBuffer.append(",").append(FormBoEntity.FIELD_TENANT);
                valBuffer.append(",?");
                params.add(curUser.getTenantId());
            }
            sqlBuffer.append(") values(").append(valBuffer.toString()).append(")");
            jdbcTemplate.update(sqlBuffer.toString(), params.toArray());
        }
    }

    private boolean setColumnValue(StringBuffer sqlBuffer, StringBuffer valBuffer, List<Object> params, FormBoEntity formBoEntity, boolean isInsert, String field, Object
            val, int cn) {
        String pkField = getPkField(formBoEntity);
        String fkField = getFkField(formBoEntity);
        if (isInsert) {
            if (field.equals(fkField)) {
                return false;
            }
            if (cn > 0) {
                sqlBuffer.append(",");
                valBuffer.append(",");
            }
            sqlBuffer.append(field);
            valBuffer.append("?");
            params.add(val);
            return true;
        } else if (!field.equals(pkField)) {
            if (cn > 0) {
                sqlBuffer.append(",");
            } else {
                sqlBuffer.append(" set ");
            }
            sqlBuffer.append(field).append("=?");
            params.add(val);
            return true;
        }
        return false;
    }


    private String getParentField(FormBoEntity boEntity) {
        if (StringUtils.isNotEmpty(boEntity.getParentField())) {
            return boEntity.getParentField();
        }
        return FormBoEntity.FIELD_PARENTID;
    }

    private String getFkField(FormBoEntity boEntity) {
        if (boEntity.getBoRelation() != null && StringUtils.isNotEmpty(boEntity.getBoRelation().getFkField())) {
            return boEntity.getBoRelation().getFkField();
        }
        return FormBoEntity.FIELD_FK;
    }

    /**
     * 获取参数Map对象。
     * @param noPkAry
     * [
     *  {
     *           name:"参数名",
     *           valueSource:"值来源",
     *           valueDef:"值定义",
     *           dateType:"数据类型"
     *       }
     * ]
     * @return 返回键值对，数据格式为 Map ,数据为键值对。
     *
     */
    public Map<String,Object> getParams(JSONArray noPkAry){

        Map<String,Object> params=new HashMap<>();
        for(int i=0;i<noPkAry.size();i++){
            JSONObject json=noPkAry.getJSONObject(i);
            Object val= getValue( json);
            if(val==null) {
                continue;
            }
            params.put(json.getString("name"), val);
        }
        return  params;
    }

    /**
     * 获取主键字段
     * @param boEntity
     * @return
     */
    private String getPkField(FormBoEntity boEntity) {
        if (boEntity.getBoRelation() != null && StringUtils.isNotEmpty(boEntity.getBoRelation().getPkField())) {
            return boEntity.getBoRelation().getPkField();
        }
        if (StringUtils.isNotEmpty(boEntity.getIdField())) {
            return boEntity.getIdField();
        }
        return FormBoEntity.FIELD_PK;
    }

    /**
     * {
     *     name:"参数名",
     *     valueSource:"值来源",
     *     valueDef:"值定义",
     *     dateType:"数据类型"
     * }
     * valSource:
     *  script:{
     *      value:"脚本"
     *  }
     *  fixedVar："固定值"
     *  constantVar:"常量"
     *  param:"传入参数"
     *
     * @param jsonObject
     * @return
     */
    private  Object getValue( JSONObject jsonObject){
        String valSource=(String)jsonObject.get("valueSource");
        String valueDef="";
        if("script".equals(valSource)) {
            try {
                //处理不是JSON数据的情况
                JSONObject json = jsonObject.getJSONObject("valueDef");
                valueDef = json.getString("value");
            } catch (Exception e) {
                valueDef=jsonObject.getString("valueDef");
            }
        }else {
            valueDef=jsonObject.getString("valueDef");
        }
        String dateType=(String)jsonObject.get("dateType");
        Map<String,Object> vars=new HashMap<String, Object>();
        Object val=null;
        if("fixedVar".equals(valSource)){   //固定值
            val=valueDef;
        }else if("script".equals(valSource)){    //脚本
            val=(String)groovyEngine.executeScripts(valueDef,vars);
        }else if("constantVar".equals(valSource)){
            val=constVarContext.getValByKey(valueDef,vars);
        }
        if(val==null) {
            return null;
        }
        if("number".equals(dateType)){
            val=Long.parseLong(val.toString());
        }

        return val;
    }

    public JSONObject getTreeByAlias(String alias,String parentId) {
        JSONObject jsonObject=new JSONObject();
        FormSolution setting=getByAlias(alias);
        String field= setting.getDisplayFields();
        String boDefId=setting.getBodefId();
        FormBoEntity ent=formBoEntityService.getByDefId(boDefId,true) ;

        //如果加载模式为懒加载且没有输入父ID设置父ID为0
        if(setting.getLoadMode().intValue()==1 && StringUtils.isEmpty(parentId)  ){
            parentId="0";
        }
        FormPc formPc= formPcServiceImpl.get(setting.getFormId());
        List<JSONObject> list=dataHandler.getData(formPc.getAlias(), parentId);
        String parentField=getParentField(ent);
        String pkField=getPkField(ent);
        jsonObject.put("pkField",pkField);
        jsonObject.put("parentField",parentField);
        for(JSONObject json:list){
            String val=json.getString(field);
            json.put("text", val);
            if(ent.external()){
                String pid=json.getString(parentField);
                String id=json.getString(pkField);
                json.put(parentField, pid);
                json.put(pkField, id);
            }
        }
        //添加根节点。
        if(StringUtils.isEmpty(parentId) ||  "0".equals(parentId)){
            JSONObject root=new JSONObject();
            root.put(pkField, "0");

            root.put(parentField, "-1");
            root.put("text", setting.getName());
            list.add(0, root);
        }
        jsonObject.put("data",list);

        return jsonObject;
    }

    /**
     * 根据主键获取表单数据。
     * @param alias
     * @param pk
     * @return
     */
    public JsonResult getDataByPk(String alias,String pk ){
        FormSolution formSolution=getByAlias(alias);
        FormPc formPc= formPcServiceImpl.get(formSolution.getFormId());
        String boDefId=formPc.getBodefId();

        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);
        String formAlias=formPc.getAlias();
        FormResult formResult=new FormResult();
        //子表权限
        JSONArray subtableRights = getSubTablePermission(formSolution.getId(),boEntity,formPc);
        //设置子表权限。
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);
        //获取数据
        JSONObject data=getData(formAlias,pk,new JSONArray());
        formResult.setData(data);

        JsonResult result=JsonResult.Success();
        result.setData(data);
        return result;
    }

    public String parseCacTemplate(String permission, String template, FormParams formParams){
        if(StringUtils.isNotEmpty(permission)){
            JSONArray templateAry=JSONArray.parseArray(template);
            IUser user = ContextUtil.getCurrentUser();
            Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
            JSONObject rightSetting=JSONObject.parseObject(permission);
            //TAB
            JSONObject tabList = null;
            if (!rightSetting.containsKey(FormPermission.PERMISSION_TABLIST)) {
                tabList = new JSONObject();
            } else {
                tabList = rightSetting.getJSONObject(FormPermission.PERMISSION_TABLIST);
            }
            JSONObject json=tabList.getJSONObject("表单TAB");
            if(BeanUtil.isNotEmpty(json) && btnCalcField(json,profiles,false,formParams)){
               JSONArray subJson=json.getJSONArray("children");
               JSONArray tempAry=new JSONArray();
                for(Object tempObj:templateAry) {
                    JSONObject tempJson = (JSONObject) tempObj;
                    boolean flag = false;
                    for (Object obj : subJson) {
                        JSONObject objJson = (JSONObject) obj;
                        if (objJson.getString("name").equals(tempJson.getString("title"))) {
                            flag = true;
                            if(btnCalcField(objJson, profiles, false,formParams)) {
                                tempAry.add(tempJson);
                            }
                        }
                    }
                    if (!flag) {
                        tempAry.add(tempJson);
                    }
                }
               return tempAry.toJSONString();
            }
        }
        return template;
    }

    private boolean btnCalcField(JSONObject field, Map<String, Set<String>> profileMap, boolean readonly,FormParams formParams) {
        String editAry = field.getString(FormPermission.PERMISSION_EDIT);
        boolean canEdit = formPermissionServiceImpl.hasRights(editAry, profileMap,formParams);
        //有编辑权限
        if (canEdit && !readonly) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据表单方案获取表单方案的字段权限。
     * @param alias
     * @return
     */
    public JsonResult<JSONObject> getPermission(String alias){
        FormSolution formSolution=getByAlias(alias);

        FormPc formPc= formPcServiceImpl.get(formSolution.getFormId());

        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(formSolution.getBodefId());
        //获取权限
        JSONObject permissionJson=getPermission(formSolution.getId(),boEntity,formPc);

        return JsonResult.Success().setData(permissionJson);
    }

    public JsonResult validPreByButtonPk(String config,String formAlias,String pkIds){
        JsonResult result=JsonResult.Success();
        if(StringUtils.isEmpty(pkIds)){
            result=validPreByButton(config,new JSONObject());
            return result;
        }
        for(String pkId:pkIds.split(",")){
            JSONObject formJson=dataHandler.getById(formAlias,pkId);
            result=validPreByButton(config,formJson);
            if(!result.isSuccess()){
                return result;
            }
        }
        return result;
    }

    public JsonResult validPreByButton(String config,JSONObject formJson){
        JsonResult result=JsonResult.Success();
        JSONObject json=JSONObject.parseObject(config);
        Map<String,Object> vars=new HashMap<>();
        Map<String,Object> contextData=new HashMap<>();
        contextData.put("type","form");
        IUser user=ContextUtil.getCurrentUser();
        contextData.put("curUserId",user.getUserId());
        contextData.put("curUserName",user.getFullName());
        contextData.put("account",user.getAccount());
        contextData.put("deptId",user.getDeptId());
        if(StringUtils.isNotEmpty(formJson.getString("INST_ID_")) && !"DELETE".equals(formJson.getString("INST_STATUS_"))){
            contextData.put("type","detail");
            contextData.put("instId",formJson.getString("INST_ID_"));
        }
        vars.put("context",contextData);
        vars.put("formJson",formJson.getInnerMap());
        String mode = json.getString("mode");
        String setting = "";
        if ("easy".equals(mode)) {
            JSONArray easyTableData = json.getJSONArray("easyTableData");
            int idx = -1;
            for (int i = 0; i < easyTableData.size(); i++) {
                JSONObject easyField = easyTableData.getJSONObject(i);
                String formField = easyField.getString("formField");
                if (!formJson.containsKey(formField)) {
                    continue;
                }
                formField="formJson."+formField;
                String logic = (String) easyField.getOrDefault("logic", "AND");
                String op = (String) easyField.getOrDefault("op", "==");
                String type = easyField.getString("dataType");
                String value = easyField.getString("condition");
                if (!"Number".equals(type) && !"number".equals(type) && !"int".equals(type)) {
                    value = "\"" + value + "\"";
                }
                idx++;
                if (idx == 0) {
                    setting += getSettingByOp(formField, op, value);
                    continue;
                }
                if ("AND".equals(logic)) {
                    setting += " && " + getSettingByOp(formField, op, value);
                } else if ("OR".equals(logic)) {
                    setting += " || " + getSettingByOp(formField, op, value);
                }
            }
        } else if ("hard".equals(mode)) {
            setting = json.getString("javaCode");
        }
        if (StringUtils.isEmpty(setting)) {
            return result;
        }
        Object flag = groovyEngine.executeScripts(setting, vars);
        if (flag instanceof Boolean && !(Boolean) flag) {
            String validMsg=json.getString("validMsg");
            validMsg=String.valueOf(groovyEngine.executeScripts(validMsg, vars));
            return JsonResult.Fail(validMsg).setShow(false);
        }
        return result;
    }

    /**
     * 根据表单数据获取按钮集合
     * @param formSolutionAlias
     * @param formJson
     * @return
     */
    public FormResult getButtonsByFormJson(String formSolutionAlias,JSONObject formJson){
        FormSolution formSolution=getByAlias(formSolutionAlias);
        FormResult formResult=new FormResult();
        formResult.setData(formJson);
        formResult.setButtons(parseButtons(formSolution.getButtonsSetting(),formJson));

        return formResult;
    }

    private String parseButtons(String buttonsSetting,JSONObject formJson) {
        if (StringUtils.isEmpty(buttonsSetting)) {
            return "[]";
        }
        Map<String,Object> vars=new HashMap<>();
        Map<String,Object> contextData=new HashMap<>();
        contextData.put("type","form");
        IUser user=ContextUtil.getCurrentUser();
        contextData.put("curUserId",user.getUserId());
        contextData.put("curUserName",user.getFullName());
        contextData.put("account",user.getAccount());
        contextData.put("deptId",user.getDeptId());
        if(StringUtils.isNotEmpty(formJson.getString("INST_ID_")) && !"DELETE".equals(formJson.getString("INST_STATUS_"))){
            contextData.put("type","detail");
            contextData.put("instId",formJson.getString("INST_ID_"));
        }
        vars.put("context",contextData);
        vars.put("formJson",formJson.getInnerMap());
        JSONArray buttonAry = JSONArray.parseArray((buttonsSetting));
        JSONArray tempAry = new JSONArray();
        for (Object obj : buttonAry) {
            JSONObject buttonJson = (JSONObject) obj;
            JSONObject config = buttonJson.getJSONObject("conditionConfig");
            if(config==null){
                tempAry.add(buttonJson);
                continue;
            }
            String mode = config.getString("mode");
            String setting = "";
            if ("easy".equals(mode)) {
                JSONArray easyTableData = config.getJSONArray("easyTableData");
                int idx = -1;
                for (int i = 0; i < easyTableData.size(); i++) {
                    JSONObject easyField = easyTableData.getJSONObject(i);
                    String formField = easyField.getString("formField");
                    if (!formJson.containsKey(formField)) {
                        continue;
                    }
                    formField="formJson."+formField;
                    String logic = (String) easyField.getOrDefault("logic", "AND");
                    String op = (String) easyField.getOrDefault("op", "==");
                    String type = easyField.getString("dataType");
                    String value = easyField.getString("condition");
                    if (!"Number".equals(type) && !"number".equals(type) && !"int".equals(type)) {
                        value = "\"" + value + "\"";
                    }
                    idx++;
                    if (idx == 0) {
                        setting += getSettingByOp(formField, op, value);
                        continue;
                    }
                    if ("AND".equals(logic)) {
                        setting += " && " + getSettingByOp(formField, op, value);
                    } else if ("OR".equals(logic)) {
                        setting += " || " + getSettingByOp(formField, op, value);
                    }
                }
            } else if ("hard".equals(mode)) {
                setting = config.getString("javaCode");
            }
            if (StringUtils.isEmpty(setting)) {
                tempAry.add(buttonJson);
                continue;
            }
            Object flag = groovyEngine.executeScripts(setting, vars);
            if (flag instanceof Boolean && (Boolean) flag) {
                tempAry.add(buttonJson);
                continue;
            }
        }
        return tempAry.toJSONString();
    }

    /**
     * 获取表单数据
     * @param alias     方案别名
     * @param pk        主键
     * @param  params   传入参数映射。
     *                  {param1:1,param2:val2}
     * @return
     */
    public FormResult getFormData(String alias,String pk,String params){
        FormSolution formSolution=getByAlias(alias);
        FormPc formPc= formPcServiceImpl.get(formSolution.getFormId());
        String boDefId=formPc.getBodefId();

        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);

        String formAlias=formPc.getAlias();

        FormResult formResult=new FormResult();
        formResult.setIdField(boEntity.getIdField());
        formResult.setWizard(formPc.getWizard());
        formResult.setType(formPc.getType());
        //获取模版。
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolution.getId());
        String permStr="";
        if(permission!=null){
            permStr=permission.getPermission();
        }
        formResult.setTemplate(parseCacTemplate(permStr,formPc.getTemplate(),null));
        //设置表单方案
        formResult.setFormSolution(formSolution);
        //获取权限
        JSONObject permissionJson=getPermission(formSolution.getId(),boEntity,formPc);
        formResult.setPermission(permissionJson);

        formResult.setMetadata(formPc.getMetadata());

        String script=FormPc.FORM_TYPE_EASY_DESIGN.equals(formPc.getType())?formPc.getJavascript():formPc.getJavascript();

        formResult.setScript(script);

        JSONArray pkSettingAry=new JSONArray();
        if(StringUtils.isEmpty(pk)){
            pkSettingAry= handNoPkSetting(params,formSolution.getNoPkSetting());
        }

        JSONObject data=getData(formAlias,pk,pkSettingAry);
        formResult.setData(data);
        String instId = data.getString("INST_ID_");
        String instStatus = data.getString("INST_STATUS_");
        if(StringUtils.isNotEmpty(instId) && !"DELETE".equals(instStatus)){
            JSONObject instDetail = bpmInstClient.getInstDetailForInterpose(instId,"NO");
            //如果返回包含 success ，就是出错了。
            if(instDetail!=null && !instDetail.containsKey("success")){
                formResult.setInstDetail(instDetail);
            }
        }
        //按钮设定。
        formResult.setButtons(parseButtons(formSolution.getButtonsSetting(),data));


        //子表权限
        JSONArray subtableRights = getSubTablePermission(formSolution.getId(),boEntity,formPc);
        //设置子表权限。
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);

        formResult.setAlias(formPc.getAlias());

        //是否支持启动流程。
        boolean canStartFlow= canStartFlow(formSolution,data);

        formResult.setCanStartFlow(canStartFlow);

        return formResult;
    }

    /**
     * 处理无主键状态下，外部参数传入进行绑定。
     * @param params
     * @param noPkSetting
     * @return
     */
    private JSONArray handNoPkSetting(String params, String noPkSetting){
        JSONArray pkSettingAry=new JSONArray();
        if(StringUtils.isNotEmpty(noPkSetting)) {
            pkSettingAry = JSONArray.parseArray(noPkSetting);
        }

        if(StringUtils.isNotEmpty(params)) {
            JSONObject paramsJson = JSONObject.parseObject(params);
            for (int i = 0; i < pkSettingAry.size(); i++) {
                JSONObject settingJson = pkSettingAry.getJSONObject(i);
                String name = settingJson.getString("name");
                String valueSource = settingJson.getString("valueSource");
                if ("param".equals(valueSource) && paramsJson.containsKey(name)) {
                    settingJson.put("valueSource", "fixedVar");
                    String val = paramsJson.getString(name);
                    settingJson.put("valueDef", val);
                }
            }
        }

        return pkSettingAry;
    }

    /**
     * 获取手机端数据。
     * @param alias
     * @param pk
     * @return
     */
    public  FormResult getMobileData(String alias,String pk,String params){
        FormSolution formSolution=getByAlias(alias);
        FormMobile formMobile= formMobileServiceImpl.get(formSolution.getMobileFormId());
        FormPc formPc= formPcServiceImpl.get(formSolution.getFormId());
        if(formMobile==null||formPc==null){
            return new FormResult();
        }
        String boDefId=formPc.getBodefId();
        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);

        //用PC端别名获取数据
        String formAlias=formPc.getAlias();

        FormResult formResult=new FormResult();
        formResult.setIdField(boEntity.getIdField());
        formResult.setType(formPc.getType());
        //设置表单方案
        formResult.setFormSolution(formSolution);
        //获取手机表单分组权限。
        JSONArray permissions=new JSONArray();
        String groupPermissions = formMobile.getGroupPermissions();
        if(StringUtils.isNotEmpty(groupPermissions)){
            permissions= JSON.parseArray(groupPermissions);
        }
        //获取模版。
        String formHtml = formMobile.getFormHtml();
        if(permissions.size()>0){
            formHtml = getMobileHtml(formHtml, permissions);
        }
        //获取关联列表权限。
        formHtml=getRelListPermissions(formHtml);
        formResult.setTemplate(formHtml);
        //子表权限
        JSONArray subtableRights = getSubTablePermission(formSolution.getId(),boEntity,formPc);
        //设置子表权限。
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);
        //获取数据
        String pkSetting=formSolution.getNoPkSetting();
        JSONArray pkSettingAry=new JSONArray();
        if(StringUtils.isNotEmpty(pkSetting)){
            pkSettingAry=handNoPkSetting(params,formSolution.getNoPkSetting());
        }
        JSONObject data=getData(formAlias,pk,pkSettingAry);
        formResult.setData(data);
        //获取权限
        JSONObject permissionJson=getPermission(formSolution.getId(),boEntity,formPc);
        formResult.setPermission(permissionJson);

        formResult.setScript(formMobile.getScript());
        formResult.setMetadata(formMobile.getMetadata());
        //按钮设定。
        formResult.setName(formSolution.getName());
        formResult.setButtons(formMobile.getButtonDef());
        //是否支持启动流程。
        boolean canStartFlow= canStartFlow(formSolution,data);

        formResult.setCanStartFlow(canStartFlow);
        return formResult;
    }




    /**
     * 获取表单权限。
     * @param formSolId
     * @param boEntity
     * @return
     */
    private JSONObject getPermission(String formSolId, FormBoEntity boEntity,FormPc formPc){
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolId);
        JSONObject  jsonObject=null;
        if(permission!=null){
            //1. 取得当前人的身份信息。
            Map<String, Set<String>> profiles =  orgService.getCurrentProfile();
            String str= permission.getPermission();
            JSONObject rightSetting=JSONObject.parseObject(str);
            jsonObject= formPermissionServiceImpl.calcRights(boEntity,rightSetting,profiles,formPc,false,null);
        }else {
            jsonObject= formPermissionServiceImpl.getInitPermission(boEntity,formPc);
        }

        return  jsonObject;
    }

    /**
     * 获取子表权限。
     * @param formSolId
     * @param boEntity
     * @return
     */
    private JSONArray getSubTablePermission(String formSolId, FormBoEntity boEntity, FormPc formPc){
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolId);
        //子表权限
        JSONArray subtableRights = null;
        if(permission!=null){
            String str= permission.getPermission();
            JSONObject rightSetting=JSONObject.parseObject(str);
            subtableRights = rightSetting.getJSONArray("subtableRights");
        }else {
            subtableRights = new JSONArray();
            List<FormBoEntity> boEntityList = boEntity.getBoEntityList();
            for (FormBoEntity subEnt : boEntityList) {
                JSONObject right = new JSONObject();
                right.put("alias",subEnt.getAlias());
                right.put("name",subEnt.getName());
                right.put("type","all");
                right.put("setting","");
                subtableRights.add(right);
            }
        }
        return  subtableRights;
    }

    /**
     * 获取数据。
     * @param formAlias
     * @param pk
     * @param noPkAry 无主键设置，如果没有主键就使用无主键设置。
     *  [
     *       {
     *                 name:"参数名",
     *                 valueSource:"值来源",
     *                 valueDef:"值定义",
     *                 dateType:"数据类型"
     *       }
     *  ]
     * @return
     */
    public JSONObject getData(String formAlias,String pk,JSONArray noPkAry){
        JSONObject data=null;
        //处理数据
        if(StringUtils.isNotEmpty(pk)){
            data=dataHandler.getById(formAlias,pk);
        }
        else if(BeanUtil.isNotEmpty(noPkAry)){
            Map<String, Object> params = getParams(noPkAry);
            if(BeanUtil.isNotEmpty(params)){
                data=dataHandler.getByParams(formAlias,params);
            }
        }
        // 如果数据为空
        if(data==null){
            data=dataHandler.getInitData(formAlias);
        }
        return  data;
    }

    /**
     * 根据权限显示对应的分组
     * @param html
     * @param permissions
     * @return
     */
    private String getMobileHtml(String html,JSONArray permissions){
        //当前用户信息
        Map<String, Set<String>> profiles =  orgService.getCurrentProfile();

        JSONArray newBoAttrs=new JSONArray();
        JSONObject htmlObj = JSON.parseObject(html);
        JSONArray boAttrs = htmlObj.getJSONArray("boAttrList");
        for (int i = 0; i < boAttrs.size(); i++) {
            JSONObject boAttr = boAttrs.getJSONObject(i);
            Boolean isGroup=boAttr.getBoolean("group");
            if(isGroup!=null && isGroup){
                for (int j = 0; j <permissions.size() ; j++) {
                    JSONObject permission = permissions.getJSONObject(j);
                    if(boAttr.getString("id").equals(permission.getString("id"))){
                        String value = permission.getJSONObject("permission").getString("value");
                        //所有人
                        if(FormPermission.PERMISSION_EVERYONE.equals(value)){
                            newBoAttrs.add(boAttr);
                        }
                        //无权限
                        else if(FormPermission.PERMISSION_NONE.equals(value)){
                            continue;
                        }
                        else if(FormPermission.PERMISSION_CUSTOM.equals(value)){
                            continue;
                        }
                        else {
                            JSONObject json = JSONObject.parseObject(value);
                            for (String key : profiles.keySet()) {
                                if (!json.containsKey(key)) {
                                    continue;
                                }
                                JSONObject setType = json.getJSONObject(key);
                                String val = setType.getString("values");
                                //配置为空跳过。
                                if (StringUtils.isEmpty(val)) {
                                    continue;
                                }
                                Boolean include = setType.getBoolean("include");
                                String[] aryVal = val.split(",");
                                for (int k = 0; k < aryVal.length; k++) {
                                    String id = aryVal[k];
                                    Set<String> set = profiles.get(key);
                                    if (BeanUtil.isNotEmpty(set)) {
                                        if (include) {
                                            if (!set.contains(id)) {
                                                newBoAttrs.add(boAttr);
                                            }
                                        } else {
                                            if (set.contains(id)) {
                                                newBoAttrs.add(boAttr);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                newBoAttrs.add(boAttr);
            }
        }
        htmlObj.put("boAttrList",newBoAttrs);
        return htmlObj.toString();
    }

    /**
     * 通过别名获取列表字段
     * @param formAlias
     * @return
     */
    public JsonResult getListFields(String formAlias) {
        JsonResult result = JsonResult.Success().setShow(false);
        FormSolution formSolution=formSolutionMapper.getByFormAlias(formAlias);
        FormBoEntity formBoEntity = formBoEntityService.getByDefId(formSolution.getBodefId(), true);
        List<FormBoAttr> boAttrList = formBoEntity.getBoAttrList();
        result.setData(boAttrList);
        return result;
    }

    /**
     * 通过别名获取业务实体
     * @param formAlias
     * @return
     */
    public JsonResult getBoEnts(String formAlias){
        JsonResult result = JsonResult.Success().setShow(false);
        FormSolution formSolution=formSolutionMapper.getByFormAlias(formAlias);
        List<FormBoEntity> formBoEntityList = formBoEntityService.getByDefId(formSolution.getBodefId());
        result.setData(formBoEntityList);
        return result;
    }

    public FormSolutionDto getFormSolutionByAlias(String alias) {
        FormSolution formSolution=getByAlias(alias);
        FormSolutionDto formSolutionDto = new FormSolutionDto();
        if(BeanUtil.isNotEmpty(formSolution)) {
            BeanUtil.copyProperties(formSolutionDto, formSolution);
            formSolutionDto.setBoAlias(formBoDefService.get(formSolutionDto.getBodefId()).getAlias());
            formSolutionDto.setFormAlias(formPcServiceImpl.getById(formSolutionDto.getFormId()).getAlias());
            FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolutionDto.getId());
            String permStr="";
            if(permission!=null){
                permStr=permission.getPermission();
            }
            formSolutionDto.setPermission(permStr);
        }
        return formSolutionDto;
    }

    /**
     * 获取手机表单关联列表权限
     * @param html
     * @return
     */
    private String getRelListPermissions(String html){
        //当前用户信息
        Map<String, Set<String>> profiles =  orgService.getCurrentProfile();

        JSONArray newRelLists=new JSONArray();
        JSONObject htmlObj = JSON.parseObject(html);
        JSONArray relLists = htmlObj.getJSONArray("relList");
        if(BeanUtil.isEmpty(relLists)){
            return html;
        }
        for (int i = 0; i < relLists.size(); i++) {
            JSONObject relList = relLists.getJSONObject(i);
            if(BeanUtil.isNotEmpty(relList) && BeanUtil.isNotEmpty(relList.getJSONObject("permission"))){
                String value = relList.getJSONObject("permission").getString("value");
                //所有人
                if(FormPermission.PERMISSION_EVERYONE.equals(value)){
                    newRelLists.add(relList);
                }
                //无权限
                else if(FormPermission.PERMISSION_NONE.equals(value)){
                    continue;
                }
                else if(FormPermission.PERMISSION_CUSTOM.equals(value)){
                    continue;
                }
                else {
                    JSONObject json = JSONObject.parseObject(value);
                    for (String key : profiles.keySet()) {
                        if (!json.containsKey(key)) {
                            continue;
                        }
                        JSONObject setType = json.getJSONObject(key);
                        String val = setType.getString("values");
                        //配置为空跳过。
                        if (StringUtils.isEmpty(val)) {
                            continue;
                        }
                        Boolean include = setType.getBoolean("include");
                        String[] aryVal = val.split(",");
                        for (int k = 0; k < aryVal.length; k++) {
                            String id = aryVal[k];
                            Set<String> set = profiles.get(key);
                            if (BeanUtil.isNotEmpty(set)) {
                                if (include) {
                                    if (set.contains(id)) {
                                        newRelLists.add(relList);
                                    }
                                } else {
                                    if (!set.contains(id)) {
                                        newRelLists.add(relList);
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                newRelLists.add(relList);
            }
        }
        htmlObj.put("relList",newRelLists);
        return htmlObj.toString();
    }

    public List<JSONObject> getNodesTreeByAlias(String alias,String parentId) {
        FormSolution setting=getByAlias(alias);
        String field= setting.getDisplayFields();
        String boDefId=setting.getBodefId();
        FormBoEntity ent=formBoEntityService.getByDefId(boDefId,true) ;

        //如果没有输入父ID设置父ID为0
        if(StringUtils.isEmpty(parentId)  ){
            parentId="0";
        }
        FormPc formPc= formPcServiceImpl.get(setting.getFormId());
        List<JSONObject> list=dataHandler.getData(formPc.getAlias(), parentId);
        return list;
    }
}
