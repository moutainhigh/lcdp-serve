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
 * [????????????]???????????????
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
     * ?????????????????????
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
            //???????????????
            CacheUtil.set(REGION_FORMSOLUTION, key, solution);
        }
        return  solution;

    }

    /**
     * ???????????????????????????
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
     * ???????????????????????????
     * <pre>
     *      1.?????????????????????
     *      2.?????????????????????
     *      3.??????????????????
     *      4.????????????
     *      5.??????????????????
     * </pre>
     *
     * @param jsonObject {
     *      setting:{alias:"??????????????????",action:"?????? startFlow:????????????,save:????????????"},
     *      //???????????????
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
        //??????????????? startFlow:????????????,save:????????????
        String action = setting.getString("action");
        String opinion = setting.getString("opinion");
        String instId=data.getString(FormSolution.INST_ID);


        FormPc formPc = formPcServiceImpl.get(formSolution.getFormId());
        //1.??????????????????
        String javaCode = formSolution.getJavaCode();
        Script script = null;
        if (StringUtils.isNotEmpty(javaCode)) {
            script = groovyEngine.getScript(javaCode);
        }
        JsonResult preResult = handPreScript(script, javaCode, data);
        if (!preResult.isSuccess()) {
            return preResult;
        }

        //2.?????????????????????
        handFormula(formSolution,action);

        //3.?????????????????????
        String handler=formSolution.getDataHandler();
        DataResult result=null;

        //?????????????????????????????????
        if(StringUtils.isNotEmpty(javaCode) && javaCode.indexOf(CUSTOM_HANDLER)!=-1) {
            JsonResult  customResult = handCustomScript(script, javaCode, data);
            if(!customResult.isSuccess()){
                return customResult;
            }
        }
        else if(StringUtils.isEmpty(handler)){
            FormBoEntity boEntity= boEntityService.getByDefId(formPc.getBodefId(),true);
            //???FormBoAttr??????formsetting??? datasetting
            FormHelper.injectAttrJson(boEntity,formPc.getFormSettings(),formPc.getDataSetting());
            result = dataHandler.save(data, formPc.getAlias(),boEntity,jsonObject.containsKey(Audit.IS_LOG));
        }
        else {
            ICustomFormDataHandler dataHandler = SpringUtil.getBean(formSolution.getDataHandler());

            result = dataHandler.save(formSolution.getBodefId(),data,jsonObject.containsKey(Audit.IS_LOG));
        }

        //4.????????????
        JSONObject boDataJson=new JSONObject();
        boDataJson.put(formPc.getBoDefAlias(),data);

        //5.??????????????????
        handAfterScript(script, javaCode, data, result);

        String message="??????????????????!";
        String defId=getDefIdByDefMapping(formSolution.getFlowDefMapping(),data);

        boolean canStartFlow=canStartFlow(formSolution,data);
        //???????????????
        if(canStartFlow) {
            JsonResult rtnFlow= handFlow(defId,instId, result,action,formSolution,boDataJson);
            if (rtnFlow.isSuccess()) {
                message = "save".equals(action) ? "??????????????????!" : "??????????????????!";
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
     * ????????????????????????
     * <pre>
     *     1.?????????????????????????????????????????????
     *      ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     *     2.????????????????????????ID????????????????????????
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
     * ?????????????????????????????????ID
     * <pre>
     *     defMapping:??????????????????
     *     [{
     *         config:{
     *             mode:"easy,hard",
     *             //?????????????????????
     *             easyTableData:[{
     *                 formField:"??????",
     *                 logic:"??????",
     *                 op:"??????",
     *                 dataType:"????????????",
     *                 condition:"???"
     *             }],
     *             //?????????????????????????????????
     *             javaCode:""
     *         }
     *     }],
     *     data:????????????
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
            //????????????
            if("easy".equals(mode)){
                setting=getEasyScript(config,data);
            }
            //????????????
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
     * ????????????????????????
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
     * ???????????????????????????
     * @param ex
     */
    public void handException(Exception ex){
        String title="????????????????????????!";
        String message= ExceptionUtil.getExceptionMessage(ex);
        //????????????
        if(message.indexOf("No outgoing sequence")!=-1){
            title="???????????????????????????????????????????????????????????????!";
        }
        String tmp= MessageUtil.getTitle();
        if(StringUtils.isNotEmpty(tmp)) {
            title=tmp;
        }
        MessageUtil.triggerException(title,message);

    }

    /**
     * ?????????????????????
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
     * ???????????????
     * <pre>
     *     1.????????????
     *      ????????????????????????
     *     2.??????????????????????????????????????????????????????????????????????????????
     * </pre>
     * @param defId
     * @param result
     * @param action
     * @param formSolution
     */
    private JsonResult handFlow(String defId,String instId, DataResult result,String action,FormSolution formSolution,JSONObject boDataJson){
        //???????????????
        if(StringUtils.isEmpty(defId)){
            return JsonResult.Fail();
        }
        //????????????
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
        //????????????
        else{
            rtn =bpmClient.startProcess(cmd);
        }
        //????????????
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
     * ???????????????????????????
     * @param jsonObject {id:"?????????????????????????????????",alias:"??????????????????"}
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
        //????????????????????? ??????????????????????????????
        Boolean cascade = jsonObject.getBoolean("cascade");
        FormSolution formSolution = this.getByAlias(alias);
        JSONObject detailJson=new JSONObject();
        detailJson.put("alias",alias);
        detailJson.put("data",new JSONArray());
        //??????????????????
        handFormula(formSolution,"del");
        ArrayList deleteds=new ArrayList();
        FormPc formPc = formPcServiceImpl.get(formSolution.getFormId());
        FormBoEntity boEntity= boEntityService.getByDefId(formPc.getBodefId(),false);
        String pkField=getPkField(boEntity);
        for (int i = 0; i < deleteIds.length; i++) {
            String pkId = deleteIds[i];

            //????????????????????????????????????????????????????????????????????????????????????   Elwin 2021-12-3
            JSONObject record=getData(formPc.getAlias(), pkId,null);
            String instStatus = record.getString("INST_STATUS_");
            if(StringUtils.isNotEmpty(instStatus) && !"CANCEL".equals(instStatus) && !"DRAFTED".equals(instStatus)){
                continue;
            }
            deleteds.add(pkId);
            if (StringUtils.isNotEmpty(formSolution.getDataHandler())) {
                ICustomFormDataHandler handler = SpringUtil.getBean(formSolution.getDataHandler());
                //??????????????????
                detailJson.getJSONArray("data").add(handler.getByPk(pkId,formPc.getBodefId()));
                JsonResult result = handler.delById(pkId);
                if (!result.isSuccess()) {
                    return JsonResult.Fail(result.getMessage());
                }else {
                    //?????????????????????????????????
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
                    //?????????????????????????????????
                    if(BeanUtil.isNotEmpty(cascade) && cascade){
                        formEntRelationService.delRelationData(boEntity,record);
                    }
                }
            }
        }
        if(deleteds.size()==0){
            return JsonResult.Fail("?????????????????????????????????????????????????????????");
        }

        return JsonResult.Success("??????????????????!");
    }

    /**
     * ?????????????????????
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
            log.error("????????????beforeSave??????");
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
            result=JsonResult.Fail("???????????????????????????");
            result.setData(ExceptionUtil.getExceptionMessage(ex));
        }
        return  result;
    }

    /**
     * ?????????????????????
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
        return JsonResult.Success("?????????????????????!");
    }

    /**
     * ???????????????
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
                //??????????????????parent_id_
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
            //1. ????????????ID
            valBuffer.append("?");
            String nPkId = IdGenerator.getIdStr();
            params.add(nPkId);
            idMap.put(id, nPkId);
            if (!isContainRefId) {
                //2 . ????????????
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
                //3.????????????ID
                sqlBuffer.append(",").append(FormBoEntity.FIELD_CREATE_BY);
                valBuffer.append(",?");
                params.add(curUser.getUserId());
                //4.???ID
                sqlBuffer.append(",").append(FormBoEntity.FIELD_CREATE_DEP);
                valBuffer.append(",?");
                params.add(curUser.getDeptId());
                //5.????????????
                sqlBuffer.append(",").append(FormBoEntity.FIELD_CREATE_TIME);
                valBuffer.append(",?");
                params.add(new Date());
                //6.??????ID
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
     * ????????????Map?????????
     * @param noPkAry
     * [
     *  {
     *           name:"?????????",
     *           valueSource:"?????????",
     *           valueDef:"?????????",
     *           dateType:"????????????"
     *       }
     * ]
     * @return ????????????????????????????????? Map ,?????????????????????
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
     * ??????????????????
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
     *     name:"?????????",
     *     valueSource:"?????????",
     *     valueDef:"?????????",
     *     dateType:"????????????"
     * }
     * valSource:
     *  script:{
     *      value:"??????"
     *  }
     *  fixedVar???"?????????"
     *  constantVar:"??????"
     *  param:"????????????"
     *
     * @param jsonObject
     * @return
     */
    private  Object getValue( JSONObject jsonObject){
        String valSource=(String)jsonObject.get("valueSource");
        String valueDef="";
        if("script".equals(valSource)) {
            try {
                //????????????JSON???????????????
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
        if("fixedVar".equals(valSource)){   //?????????
            val=valueDef;
        }else if("script".equals(valSource)){    //??????
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

        //????????????????????????????????????????????????ID?????????ID???0
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
        //??????????????????
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
     * ?????????????????????????????????
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
        //????????????
        JSONArray subtableRights = getSubTablePermission(formSolution.getId(),boEntity,formPc);
        //?????????????????????
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);
        //????????????
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
            JSONObject json=tabList.getJSONObject("??????TAB");
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
        //???????????????
        if (canEdit && !readonly) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     * @param alias
     * @return
     */
    public JsonResult<JSONObject> getPermission(String alias){
        FormSolution formSolution=getByAlias(alias);

        FormPc formPc= formPcServiceImpl.get(formSolution.getFormId());

        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(formSolution.getBodefId());
        //????????????
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
     * ????????????????????????????????????
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
     * ??????????????????
     * @param alias     ????????????
     * @param pk        ??????
     * @param  params   ?????????????????????
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
        //???????????????
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolution.getId());
        String permStr="";
        if(permission!=null){
            permStr=permission.getPermission();
        }
        formResult.setTemplate(parseCacTemplate(permStr,formPc.getTemplate(),null));
        //??????????????????
        formResult.setFormSolution(formSolution);
        //????????????
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
            //?????????????????? success ?????????????????????
            if(instDetail!=null && !instDetail.containsKey("success")){
                formResult.setInstDetail(instDetail);
            }
        }
        //???????????????
        formResult.setButtons(parseButtons(formSolution.getButtonsSetting(),data));


        //????????????
        JSONArray subtableRights = getSubTablePermission(formSolution.getId(),boEntity,formPc);
        //?????????????????????
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);

        formResult.setAlias(formPc.getAlias());

        //???????????????????????????
        boolean canStartFlow= canStartFlow(formSolution,data);

        formResult.setCanStartFlow(canStartFlow);

        return formResult;
    }

    /**
     * ????????????????????????????????????????????????????????????
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
     * ????????????????????????
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

        //???PC?????????????????????
        String formAlias=formPc.getAlias();

        FormResult formResult=new FormResult();
        formResult.setIdField(boEntity.getIdField());
        formResult.setType(formPc.getType());
        //??????????????????
        formResult.setFormSolution(formSolution);
        //?????????????????????????????????
        JSONArray permissions=new JSONArray();
        String groupPermissions = formMobile.getGroupPermissions();
        if(StringUtils.isNotEmpty(groupPermissions)){
            permissions= JSON.parseArray(groupPermissions);
        }
        //???????????????
        String formHtml = formMobile.getFormHtml();
        if(permissions.size()>0){
            formHtml = getMobileHtml(formHtml, permissions);
        }
        //???????????????????????????
        formHtml=getRelListPermissions(formHtml);
        formResult.setTemplate(formHtml);
        //????????????
        JSONArray subtableRights = getSubTablePermission(formSolution.getId(),boEntity,formPc);
        //?????????????????????
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);
        //????????????
        String pkSetting=formSolution.getNoPkSetting();
        JSONArray pkSettingAry=new JSONArray();
        if(StringUtils.isNotEmpty(pkSetting)){
            pkSettingAry=handNoPkSetting(params,formSolution.getNoPkSetting());
        }
        JSONObject data=getData(formAlias,pk,pkSettingAry);
        formResult.setData(data);
        //????????????
        JSONObject permissionJson=getPermission(formSolution.getId(),boEntity,formPc);
        formResult.setPermission(permissionJson);

        formResult.setScript(formMobile.getScript());
        formResult.setMetadata(formMobile.getMetadata());
        //???????????????
        formResult.setName(formSolution.getName());
        formResult.setButtons(formMobile.getButtonDef());
        //???????????????????????????
        boolean canStartFlow= canStartFlow(formSolution,data);

        formResult.setCanStartFlow(canStartFlow);
        return formResult;
    }




    /**
     * ?????????????????????
     * @param formSolId
     * @param boEntity
     * @return
     */
    private JSONObject getPermission(String formSolId, FormBoEntity boEntity,FormPc formPc){
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolId);
        JSONObject  jsonObject=null;
        if(permission!=null){
            //1. ?????????????????????????????????
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
     * ?????????????????????
     * @param formSolId
     * @param boEntity
     * @return
     */
    private JSONArray getSubTablePermission(String formSolId, FormBoEntity boEntity, FormPc formPc){
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,formSolId);
        //????????????
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
     * ???????????????
     * @param formAlias
     * @param pk
     * @param noPkAry ???????????????????????????????????????????????????????????????
     *  [
     *       {
     *                 name:"?????????",
     *                 valueSource:"?????????",
     *                 valueDef:"?????????",
     *                 dateType:"????????????"
     *       }
     *  ]
     * @return
     */
    public JSONObject getData(String formAlias,String pk,JSONArray noPkAry){
        JSONObject data=null;
        //????????????
        if(StringUtils.isNotEmpty(pk)){
            data=dataHandler.getById(formAlias,pk);
        }
        else if(BeanUtil.isNotEmpty(noPkAry)){
            Map<String, Object> params = getParams(noPkAry);
            if(BeanUtil.isNotEmpty(params)){
                data=dataHandler.getByParams(formAlias,params);
            }
        }
        // ??????????????????
        if(data==null){
            data=dataHandler.getInitData(formAlias);
        }
        return  data;
    }

    /**
     * ?????????????????????????????????
     * @param html
     * @param permissions
     * @return
     */
    private String getMobileHtml(String html,JSONArray permissions){
        //??????????????????
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
                        //?????????
                        if(FormPermission.PERMISSION_EVERYONE.equals(value)){
                            newBoAttrs.add(boAttr);
                        }
                        //?????????
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
                                //?????????????????????
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
     * ??????????????????????????????
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
     * ??????????????????????????????
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
     * ????????????????????????????????????
     * @param html
     * @return
     */
    private String getRelListPermissions(String html){
        //??????????????????
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
                //?????????
                if(FormPermission.PERMISSION_EVERYONE.equals(value)){
                    newRelLists.add(relList);
                }
                //?????????
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
                        //?????????????????????
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

        //?????????????????????ID?????????ID???0
        if(StringUtils.isEmpty(parentId)  ){
            parentId="0";
        }
        FormPc formPc= formPcServiceImpl.get(setting.getFormId());
        List<JSONObject> list=dataHandler.getData(formPc.getAlias(), parentId);
        return list;
    }
}
