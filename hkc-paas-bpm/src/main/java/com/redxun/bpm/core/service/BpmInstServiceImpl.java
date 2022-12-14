package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.*;
import com.redxun.bpm.activiti.processhandler.ProcessHandlerExecutor;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmCheckFileMapper;
import com.redxun.bpm.core.mapper.BpmInstMapper;
import com.redxun.bpm.core.mapper.BpmTaskMapper;
import com.redxun.bpm.core.mapper.BpmTaskUserMapper;
import com.redxun.bpm.feign.FormClient;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.bpm.util.VarExpressUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.dboperator.model.Column;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.BpmInstDataDto;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
* [????????????]???????????????
 * @author csx
 *
*/
@Slf4j
@Service
public class BpmInstServiceImpl extends SuperServiceImpl<BpmInstMapper, BpmInst> implements BaseService<BpmInst> {

    @Resource
    private BpmInstMapper bpmInstMapper;
    @Resource
    BpmInstLogServiceImpl bpmInstLogService;
    @Resource
    RuntimeService runtimeService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmTaskMapper bpmTaskMapper;
    @Resource
    BpmTransferServiceImpl bpmTransferService;
    @Resource
    FormDataService formDataService;
    @Resource
    GroovyEngine groovyEngine;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    BpmTaskSkipService taskSkipService;
    @Resource
    ProcessHandlerExecutor processHandlerExecutor;
    @Resource
    ProcessScriptEngine processScriptEngine;
    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    BpmInstDataServiceImpl  bpmInstDataServiceImpl;
    @Resource
    BpmTaskUserMapper bpmTaskUserMapper;
    @Resource
    BpmSignDataServiceImpl bpmSignDataService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    HistoryService historyService;
    @Resource
    ActInstService  actInstService;
    @Resource
    BpmInstRouterServiceImpl  bpmInstRouterService;
    @Resource
    BpmInstCpServiceImpl  bpmInstCpService;
    @Resource
    BpmInstCcServiceImpl  bpmInstCcService;
    @Resource
    BpmRemindInstServiceImpl  bpmRemindInstService;
    @Resource
    BpmRemindHistoryServiceImpl  bpmRemindHistoryService;
    @Resource
    BpmCheckFileMapper bpmCheckFileMapper;
    @Resource
    BpmInstMsgServiceImpl bpmInstMsgService;
    @Resource
    FormClient formClient;
    @Resource
    IOrgService orgService;
    @Resource
    ActRepService actRepService;
    @Resource
    BpmInstTrackedServiceImpl bpmInstTrackedService;
    @Resource
    BpmTakeBackService bpmTakeBackService;
    @Resource
    BpmInstPermissionServiceImpl bpmInstPermissionService;
    @Resource
    BpmTaskUserServiceImpl bpmTaskUserService;

    @Override
    public BaseDao<BpmInst> getRepository() {
        return bpmInstMapper;
    }


    /**
     * ???????????????????????????????????????
     * @param filter
     * @return
     */
    public IPage<BpmInst> getByUserId(QueryFilter filter){

        Map<String,Object> params= PageHelper.constructParams(filter);

        return bpmInstMapper.getByUserId(filter.getPage(),params);
    }

    /**
     * ???????????????????????????
     * @param boAlias
     * @param instId
     * @param pk
     */
    private void handBpmInstData(String boAlias,String instId,String pk){
        if(StringUtils.isEmpty(boAlias) ){
            return;
        }
        //????????????
        Integer rtn= bpmInstDataServiceImpl.getCountByInstId(instId,boAlias);
        if(rtn>0) {
            return;
        }
        BpmInstData bpmInstData=new BpmInstData();
        bpmInstData.setId(IdGenerator.getIdStr());
        bpmInstData.setBodefAlias(boAlias);
        bpmInstData.setInstId(instId);
        bpmInstData.setPk(pk);
        bpmInstDataServiceImpl.insert(bpmInstData);
    }

    private void handBusinessData(BpmInst bpmInst,ProcessStartCmd cmd,ProcessConfig processConfig,String operate){
        if(StringUtils.isNotEmpty(cmd.getBusKey())){
            bpmInst.setBusKey(cmd.getBusKey());
            //??????fieldJson???
            TaskFieldConfig fieldConfig= processConfig.getTaskFields();
            //??????JSON?????????
            handInstJson(fieldConfig,bpmInst,cmd);
            handBpmInstData(cmd.getBoAlias(),bpmInst.getInstId(),cmd.getBusKey());
        }else{
            JSONObject formData= cmd.getFormData();
            Set<String> keys = formData.keySet();
            for(String key:keys){
                JSONObject json=formData.getJSONObject(key);
                json.put(FormBoEntityDto.FIELD_INST,bpmInst.getInstId());
                json.put(FormBoEntityDto.FIELD_INST_STATUS_,BpmInstStatus.SUBMIT.name());
            }
            List<BpmInstData> bpmInstData = formDataService.handFormData(cmd, processConfig.getDataSetting(),operate);
            //??????fieldJson???
            TaskFieldConfig fieldConfig= processConfig.getTaskFields();
            //??????JSON?????????
            handInstJson(fieldConfig,bpmInst,cmd);

            if(BeanUtil.isNotEmpty(bpmInstData) && bpmInstData.size()>=1){
                bpmInst.setBusKey(bpmInstData.get(0).getPk());
            }
        }
    }

    public void handInstJson(TaskFieldConfig fieldConfig ,BpmInst bpmInst,IExecutionCmd cmd){
        if(fieldConfig==null ||
                StringUtils.isEmpty( fieldConfig.getBoAlias()) ||
                BeanUtil.isEmpty( fieldConfig.getFields())){
            return;
        }
        JSONObject json= cmd.getBoDataMap().getJSONObject(fieldConfig.getBoAlias());
        if(json==null){
            return;
        }

        JSONArray jsonAry=new JSONArray();
        List<TaskField> fields= fieldConfig.getFields();
        for(TaskField field:fields){
            JSONObject fieldJson=new JSONObject();
            fieldJson.put("label",field.getLabel());

            if(field.getIsSingle()==1){
                String val= json.getString( field.getValue());
                fieldJson.put("val",val);
            }
            else{
                JSONObject tmpJson= json.getJSONObject( field.getValue());
                if(BeanUtil.isNotEmpty(tmpJson)){
                    String label= tmpJson.getString("label");
                    fieldJson.put("val",label);
                }
            }
            jsonAry.add(fieldJson);
        }

        bpmInst.setFieldJson(jsonAry.toJSONString());

    }


    public void handStartException(Exception ex){
        String title="??????????????????!";
        String message=ExceptionUtil.getExceptionMessage(ex);
        //????????????
        if(message.indexOf("No outgoing sequence")!=-1){
            title="???????????????????????????????????????????????????????????????!";
        }
        String tmp=MessageUtil.getTitle();
        if(StringUtils.isNotEmpty(tmp)) {
            title=tmp;
        }
        MessageUtil.triggerException(title,message);

    }

    /**
     * ????????????
     * @param cmd
     * @return
     */
    @GlobalTransactional
    @Transactional
    public BpmInst doStartProcess(ProcessStartCmd cmd, BpmDef bpmDef,String operate) throws Exception {
        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(cmd);
        //???????????????????????????
        BpmInst bpmInst = initBpmInst(cmd,bpmDef);
        //?????????????????????
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmDef.getActDefId());
        //??????????????????
        if(cmd.getSystemHand()){
            handBusinessData(bpmInst,cmd,processConfig,operate);
        }
        //?????????????????????????????????
        processHandlerExecutor.handStartBeforeHandler(processConfig,bpmInst);
        //?????????????????????????????????
        handBpmInst( bpmInst, cmd, bpmDef, processConfig);
        //?????????????????????
        handVars(cmd,  bpmInst,processConfig,true);
        //??????Activiti??????????????????
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(bpmDef.getActDefId(),bpmInst.getBusKey(), cmd.getVars());
        bpmInst.setActInstId(processInstance.getId());
        //??????Act??????????????????????????????
        runtimeService.setVariable(processInstance.getId(),BpmInstVars.ACT_INST_ID.getKey(),processInstance.getId());

        //????????????????????????????????????????????????
        processHandlerExecutor.handStartAfterHandler(processConfig,bpmInst);

        //??????????????????
        saveBpmInst(cmd,bpmInst,true);
        //?????????????????????
        handSubVars(cmd,bpmInst);
        //????????????????????????
        handFirstJump(cmd,processConfig,bpmInst);

        //????????????
        IExecutionCmd nextCmd=ProcessHandleUtil.getProcessCmd();
        taskSkipService.handSkipTask(nextCmd);
        //???????????????????????????
        bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, "", null);
        bpmInstPermissionService.createTaskInfo(bpmInst,nextCmd);
        // ???????????????
        addTracked(bpmInst);
        return  bpmInst;
    }

    /**
     * ???????????????
     * @param bpmInst
     */
    private void addTracked(BpmInst bpmInst){
        if(BpmInstStatus.RUNNING.name().equals(bpmInst.getStatus())){
            String userId=ContextUtil.getCurrentUserId();
            bpmInstTrackedService.addTracked(bpmInst.getInstId(),userId);
        }
    }

    private void handSubVars(ProcessStartCmd cmd,BpmInst bpmInst) {
        String mainActInstId = bpmInst.getParentActInstId();
        if (StringUtils.isEmpty(mainActInstId)) {
            return;
        }
        String subInstIds = (String) runtimeService.getVariable(mainActInstId, BpmInstVars.SUB_INST_IDS.getKey());
        if (StringUtils.isEmpty(subInstIds)) {
            subInstIds = bpmInst.getInstId();
        } else {
            subInstIds += "," + bpmInst.getInstId();
        }
        runtimeService.setVariable(mainActInstId, BpmInstVars.SUB_INST_IDS.getKey(), subInstIds);
    }

    /**
     * ?????????????????????
     * @param cmd
     * @param bpmInst
     * @param isStart
     */
    private void saveBpmInst(ProcessStartCmd cmd,BpmInst bpmInst,boolean isStart){
        boolean isNew= (boolean) cmd.getTransientVar("isNew");
        String status=isStart? BpmInstStatus.RUNNING.name():BpmInstStatus.DRAFTED.name();
        bpmInst.setStatus(status);
        if(isNew){
            insert(bpmInst);
        }
        else{
            update(bpmInst);
        }
    }

    /**
     * ?????????????????????
     * @param cmd
     * @return
     */
    private BpmInst initBpmInst(ProcessStartCmd cmd,BpmDef bpmDef) {
        String busKey=cmd.getBusKey();
        cmd.setCheckType(TaskOptionType.AGREE.name());
        //???????????????
        BpmInst bpmInst=null;
        if(StringUtils.isNotEmpty(busKey)){
            List<String> defIdAry=Arrays.asList(cmd.getDefId().split(","));
            List<BpmInst> bpmInsts= this.getByBusKey(defIdAry,busKey);
            if(BeanUtil.isNotEmpty(bpmInsts)){
                bpmInst=bpmInsts.get(0);
                cmd.addTransientVar("isNew",false);
                return bpmInst;
            }
        }
        String instId=cmd.getInstId();


        if(StringUtils.isEmpty(instId)){
            bpmInst=new BpmInst();
            bpmInst.setInstId(IdGenerator.getIdStr());
            bpmInst.setParentActInstId(cmd.getParentActInstId());
            bpmInst.setTreeId(bpmDef.getTreeId());
            bpmInst.setFormSolutionAlias(cmd.getFormSolutionAlias());
            if(StringUtils.isNotEmpty(busKey)){
                bpmInst.setBusKey(busKey);
            }

            cmd.setInstId(bpmInst.getInstId());
            cmd.addTransientVar("isNew",true);
        }
        else{
            bpmInst=bpmInstMapper.selectById(instId);
            cmd.addTransientVar("isNew",false);
            cmd.addTransientVar("bpmDef",bpmDef);
        }
        cmd.addTransientVar(BpmConst.BPM_INST,bpmInst);

        return bpmInst;
    }

    private void handBpmInst(BpmInst bpmInst,ProcessStartCmd cmd,BpmDef bpmDef,ProcessConfig processConfig){
        //??????billType
        if(StringUtils.isNotEmpty(cmd.getBillType())){
            bpmInst.setBillType(cmd.getBillType());
        }else{
            bpmInst.setBillType(processConfig.getBoDefs().getText());
        }

        bpmInst.setActDefId(bpmDef.getActDefId())
                .setDefId(bpmDef.getDefId())
                .setDefCode(bpmDef.getKey())
                .setBillNo(bpmInst.getBusKey())//billNo?????????????????????
                .setVersion(bpmDef.getVersion());//TODO ??????????????????????????????
        bpmInst.setIsTest(BpmDef.FORMAL_YES.equals(bpmDef.getFormal()) ? MBoolean.NO.name() : MBoolean.YES.name());
        //????????????????????????????????????????????????
        if(StringUtils.isNotEmpty(cmd.getSubject())){
            bpmInst.setSubject(cmd.getSubject());
        }else{
            //????????????????????????????????????
            String subject=getSubject(processConfig,bpmDef.getName(),cmd);
            bpmInst.setSubject(subject);
        }
        if(BpmDef.FORMAL_NO.equals(bpmDef.getFormal())){
            bpmInst.setSubject("[??????]"+bpmInst.getSubject());
        }

    }

    /**
     * ????????????????????????????????????
     * @param processConfig
     * @param bpmInst
     */
    private void handFirstJump(ProcessStartCmd cmd,ProcessConfig processConfig,BpmInst bpmInst){
        //?????????????????????
        FlowNode firstNode = actRepService.getFirstUserTaskNode(bpmInst.getActDefId());
        //????????????????????????
        boolean isFirstNode=false;
        if(StringUtils.isNotEmpty(cmd.getDestNodeId())){
            isFirstNode = firstNode.getId().equals(cmd.getDestNodeId());
        }

        //?????????????????????
        if(BeanUtil.isNotEmpty(processConfig.getStartNodeOptions()) && (processConfig.getStartNodeOptions().contains("skipFirstNode") || StringUtils.isNotEmpty(cmd.getDestNodeId()))){

            //DestNodeId??????????????????????????????
            if(StringUtils.isNotEmpty(cmd.getDestNodeId()) && isFirstNode){
                return;
            }

            ProcessNextCmd nextCmd=new ProcessNextCmd();
            ProcessHandleUtil.setProcessCmd(nextCmd);

            String opinion=cmd.getOpinion();
            if(StringUtils.isEmpty(opinion)){
                opinion="????????????????????????";
            }
            nextCmd.setOpinion(opinion);
            nextCmd.setOpFiles(cmd.getOpFiles());
            nextCmd.setRelInsts(cmd.getRelInsts());
            nextCmd.setCheckType(TaskOptionType.SKIP.name());
            nextCmd.setNodeExecutors(cmd.getNodeExecutors());
            nextCmd.setFormData(cmd.getFormData());
            nextCmd.setBoDataMap(cmd.getBoDataMap());
            nextCmd.setTransientVars(cmd.getTransientVars());

            List<BpmTask> tasks = bpmTaskMapper.getByInstId(bpmInst.getInstId());
            //???????????????
            if(StringUtils.isNotEmpty(cmd.getDestNodeId())){
                BpmTask bpmTask=tasks.get(0);
                //??????????????????
                nextCmd.setPreNodeId(bpmTask.getKey());
                actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),cmd.getDestNodeId());

                //?????????????????????
                bpmTaskMapper.deleteByActTaskId(bpmTask.getActTaskId());
            }
            else{
                for(BpmTask task:tasks){

                    nextCmd.setTaskId(task.getTaskId());
                    nextCmd.setInstId(bpmInst.getInstId());
                    nextCmd.setDefId(bpmInst.getDefId());
                    nextCmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,task);



                    bpmTaskService.completeTask(nextCmd);

                    //?????????????????????
                    bpmTaskMapper.deleteByActTaskId(task.getActTaskId());
                }
            }
        }

    }

    /**
     * ?????????????????????
     * @param cmd
     * @param bpmInst
     * @param config
     */
    public void handVars(IExecutionCmd cmd,  BpmInst bpmInst,ProcessConfig config,boolean start) {
        Map<String,Object> vars=new HashMap<String,Object>();
        //??????????????????
        vars.put(BpmInstVars.BUS_KEY.getKey(),bpmInst.getBusKey());
        vars.put(BpmInstVars.INST_ID.getKey(),bpmInst.getInstId());
        vars.put(BpmInstVars.MAIN_INST_IDS.getKey(),bpmInst.getParentActInstId());
        vars.put(BpmInstVars.DEF_ID.getKey(),bpmInst.getDefId());
        vars.put(BpmInstVars.BILL_NO.getKey(),bpmInst.getBillNo());
        if(start) {
            IUser loginUser = ContextUtil.getCurrentUser();
            vars.put(BpmInstVars.START_USER_ID.getKey(), loginUser.getUserId());
            vars.put(BpmInstVars.START_DEP_ID.getKey(), loginUser.getDeptId());
        }
        vars.put(BpmInstVars.PROCESS_SUBJECT.getKey(),bpmInst.getSubject());
        //????????????????????????Id
        if(StringUtils.isNotEmpty(cmd.getNodeUserIds())){
            vars.put(BpmInstVars.NODE_USER_IDS.getKey(),cmd.getNodeUserIds());
        }

        //???????????????????????????
        List<VariableConfig> varConfigs = config.getVarConfigs();

        Map<String,Object> configVars=  getVarsFromConfig(varConfigs,cmd);

        if(BeanUtil.isNotEmpty(configVars)){
            vars.putAll(configVars);
        }

        cmd.getVars().putAll(vars);
    }

    public JSONObject parseFormData(JSONArray setting,JSONObject formData){
        JSONObject jsonData=new JSONObject();
        for(Object obj:setting){
            JSONObject data=new JSONObject();
            data.put("formData",formData);
            data.put("setting",obj);
            jsonData.putAll(formClient.getTableFieldValueHandler(data));
        }
        return jsonData;
    }

    public JSONObject parseFormData(JSONArray setting,JSONObject formData,int index){
        JSONObject jsonData=new JSONObject();
        String mainAlias=null;
        JSONObject mainFormData=new JSONObject();
        for(Object obj:setting){
            JSONObject data=new JSONObject();
            data.put("formData",formData);
            JSONObject json=(JSONObject)obj;
            JSONObject conf=json.getJSONObject("conf");
            conf.put("index",index);
            data.put("setting",json);
            String relationType=conf.getString("relationType");
            if("main".equals(relationType)){
                mainFormData=new JSONObject();
                FormBoEntityDto mainEnt=formClient.getBoEntByTableName(conf.getString("tableName"));
                mainAlias=mainEnt.getAlias();
            }
            mainFormData.putAll(formClient.getTableFieldValueHandler(data));
            if (StringUtils.isNotEmpty(mainAlias)) {
                jsonData.put(mainAlias,mainFormData);
            }
        }
        return jsonData;
    }

    public JSONObject parseVarData(JSONArray varData,JSONObject formData){
        Map<String,Object> vars=new HashMap<>();
        for(Object var:varData){
            VariableConfig config=JSONObject.toJavaObject((JSON)var,VariableConfig.class);
            String field=config.getField();
            String expression=config.getExpression();
            //????????????????????????
            if(StringUtils.isNotEmpty(field) && field.indexOf(".")>0){
                handFormField(config,vars,formData);
            }
            else if(StringUtils.isNotEmpty(expression)){
                handExpression(config,vars,formData,new ProcessStartCmd());
            }
        }
        return new JSONObject(vars);
    }

    private Map<String,Object> getVarsFromConfig(List<VariableConfig> varConfigs, IExecutionCmd cmd){
        JSONObject formData=cmd.getBoDataMap();

        Map<String,Object> vars=new HashMap<>();
        for(VariableConfig config:varConfigs){
            String field=config.getField();
            String expression=config.getExpression();
            //????????????????????????
            if(StringUtils.isNotEmpty(field) && field.indexOf(".")>0){
                handFormField(config,vars,formData);
            }
            else if(StringUtils.isNotEmpty(expression)){
                handExpression(config,vars,formData,cmd);
            }
        }
        return vars;
    }

    private void handFormField(VariableConfig config,Map<String,Object> vars,JSONObject formData){
        String[] aryField=config.getField().split("[.]");
        String boAlias=aryField[0];
        String fieldName=aryField[1];
        if(formData==null){
           return;
        }
        Map json= (Map) formData.get(boAlias);
        if(BeanUtil.isEmpty(json)){
            return;
        }

        Object val=json.get(fieldName);
        if(BeanUtil.isNotEmpty(val)){
            Object rtnVal=handValue(val,config.getDatatype());
            vars.put(config.getKey(),rtnVal);
        }
    }

    /**
     * ??????????????????????????????
     * @param config
     * @param vars
     * @param formData
     * @param cmd
     */
    private void handExpression(VariableConfig config,Map<String,Object> vars,JSONObject formData,IExecutionCmd cmd){
        String script=config.getExpression();
        Map<String,Object> model=new HashMap<>(SysConstant.INIT_CAPACITY_4);
        model.put("cmd",cmd);
        if(BeanUtil.isNotEmpty(formData)){
            model.putAll(formData);
        }
        model.put("varUtil", VarExpressUtil.class);
        Object obj= groovyEngine.executeScripts(script,model);
        if(obj!=null){
            vars.put(config.getKey(),obj);
        }

    }

    /**
     * ????????????????????????????????????????????????
     * @param val
     * @param datatype
     * @return
     */
    private Object handValue(Object val,String datatype ){
        if(Column.COLUMN_TYPE_VARCHAR.equals(datatype)){
            if(val==null){
                return "";
            }
            String value=val.toString();
            // ??????JSON?????????
            boolean match = value.matches("(?sm)^\\{.*?\\}$");
            if(match){
                JSONObject json=JSONObject.parseObject(value);
                if(json.containsKey("value")){
                    return json.getString("value");
                }
            }
            return val.toString();
        }
        else if(Column.COLUMN_TYPE_NUMBER.equals(datatype)){
            return new BigDecimal(val.toString());
        }
        else if(Column.COLUMN_TYPE_INT.equals(datatype)){
            return   Integer.valueOf(val.toString());
        }
        return  val;
    }




    /**
     * ?????????????????????
     * @param processConfig
     * @param defTitle
     * @param cmd
     * @return
     */
    private String getSubject(ProcessConfig processConfig,String defTitle,ProcessStartCmd cmd){
        IUser user= ContextUtil.getCurrentUser();

        Map<String,Object> mapConst=new HashMap<>();
        mapConst.put("processName",defTitle);
        mapConst.put("createUser",user.getFullName());
        mapConst.put("createTime",DateUtils.getTime());
        mapConst.put("curDate",DateUtils.getDate());
        mapConst.putAll(cmd.getVars());

        JSONObject boMap=cmd.getBoDataMap();

        List<SubjectRuleItem> subjectRuleData = processConfig.getSubjectRuleData();
        StringBuilder sb=new StringBuilder();
        for(SubjectRuleItem item:subjectRuleData){
            boolean isField=item.getRuleType();
            //????????????
            if(!isField){
                sb.append(item.getRuleSplitor()) ;
                continue;
            }

            String rule=item.getRuleSplitor();
            if( rule.indexOf(".")==-1){
                sb.append(mapConst.get(rule));
            }
            else{
                String[] ary=rule.split("[.]");
                String boAlias=ary[0];
                String key=ary[1];
                JSONObject jsonObj= boMap.getJSONObject(boAlias);
                if(jsonObj==null){
                    MessageUtil.triggerException("????????????","?????????BO??????["+boAlias+"]????????????!");
                }
                if(ary.length==2) {
                    //????????????
                    String val = jsonObj.getString(key);
                    sb.append(val);
                }else if(ary.length==3){
                    //????????????
                    try {
                        String type = ary[2];
                        String val = jsonObj.getJSONObject(key).getString(type);
                        sb.append(val);
                    }catch (Exception e){
                    }
                }
            }

        }
        return  sb.toString();
    }

    /**
     * ???????????????
     * @param cmd
     * @param bpmDef
     * @return
     */
    @GlobalTransactional
    public BpmInst doSaveDraft(ProcessStartCmd cmd, BpmDef bpmDef){
        BpmInst bpmInst=initBpmInst(cmd,bpmDef);

        if(bpmDef==null){
            bpmDef= (BpmDef) cmd.getTransientVar("bpmDef");
        }

        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmDef.getActDefId());
        //??????????????????
        if(cmd.getSystemHand()){
            //??????????????????
            if(StringUtils.isNotEmpty(cmd.getBusKey())){
                bpmInst.setBusKey(cmd.getBusKey());
                handBpmInstData(cmd.getBoAlias(),bpmInst.getInstId(),cmd.getBusKey());
            }else{
                JSONObject formData= cmd.getFormData();
                Set<String> keys = formData.keySet();
                for(String key:keys){
                    JSONObject json=formData.getJSONObject(key);
                    json.put(FormBoEntityDto.FIELD_INST,bpmInst.getInstId());
                    json.put(FormBoEntityDto.FIELD_INST_STATUS_,BpmInstStatus.DRAFTED.name());
                }
                List<BpmInstData> bpmInstData = formDataService.handFormData(cmd, processConfig.getDataSetting(), "draft");
                if(BeanUtil.isNotEmpty(bpmInstData) && bpmInstData.size()==1){
                    bpmInst.setBusKey(bpmInstData.get(0).getPk());
                }
            }
        }else{
            //????????????????????????????????????????????????--???????????????
            processHandlerExecutor.handStartAfterHandler(processConfig,bpmInst);
        }

        handBpmInst(bpmInst,cmd,bpmDef,processConfig);
        saveBpmInst(cmd,bpmInst,false);
        return  bpmInst;
    }

    /**
     * ??????????????????ID???????????????????????????
     * @param actInstId
     * @return
     */
    public Map<String,Object> getVariables(String actInstId){
        return runtimeService.getVariables(actInstId);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * <pre>
     *     1. ?????????????????????
     *     2. ????????????
     *     3. ?????????????????????????????????
     *     4. ??????RUNPATH
     *     5. ?????????????????????
     *     6. ??????????????????
     *     7. ??????????????????
     *     8. ????????????????????????
     * </pre>
     * @param instId
     */
    @Transactional
    public void deleteByInstId(Serializable instId,StringBuilder sb) {

        BpmInst bpmInst=get(instId);

        if(bpmInst==null){
            return;
        }
        sb.append(bpmInst.getSubject() +",");

        String bpmInstId=bpmInst.getInstId();
        String actInstId=bpmInst.getActInstId();
        //??????????????????????????????????????? DELETE
        List<BpmInstData> bpmInstDatas = bpmInstDataServiceImpl.getByInstId((String) instId);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(bpmInstDatas));
        formClient.setFormInstStatus(jsonArray);

        bpmTaskUserMapper.deleteByInstId((String) instId);
        bpmTaskMapper.deleteByInstId(bpmInstId);
        bpmTransferService.delByInstId(bpmInstId);

        bpmRuPathService.delByInstId(bpmInstId);
        bpmInstLogService.delByInstId(bpmInstId);
        bpmInstMapper.deleteById(bpmInstId);
        bpmCheckHistoryService.delByInstId(bpmInstId);

        bpmInstDataServiceImpl.removeByInstId(bpmInstId);

        if(StringUtils.isNotEmpty(actInstId)){
            bpmSignDataService.deleteByActInstId(actInstId);
            if (!BpmInstStatus.CANCEL.name().equals(bpmInst.getStatus()) &&
                    !BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) &&
                    !BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
                runtimeService.deleteProcessInstance(actInstId, "remove by user force");
            }
        }
    }

    /**
     * ????????????????????????
     * @param ids
     */
    @Override
    public void delete(Collection<Serializable> ids) {
        StringBuilder sb=new StringBuilder();
        for(Serializable id:ids){
            deleteByInstId(id,sb);
        }
        LogContext.put(Audit.DETAIL,sb.toString());
    }

    /**
     * ???????????? Id???????????????
     * @param instId
     * @param status ??????????????? @BpmInstStatus
     */
    @Transactional
    public void updateStatusByInstId(String instId,String status){
        bpmInstMapper.updateStatusByInstId(instId,status);
        //????????????
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if(bpmTasks.size()>0){
            String curUserId = ContextUtil.getCurrentUserId();
            insertCheckHistory(bpmTasks.get(0),curUserId,curUserId,status,status,"","");
        }
    }

    /**
     * ????????????????????????
     * @param bpmTask
     * @param ownerId
     * @param handlerId
     * @param jumpType
     * @param status
     * @param remark
     */
    private void insertCheckHistory(BpmTask bpmTask,String ownerId,String handlerId,String jumpType,String status,String remark,String opFiles){
        BpmCheckHistory bpmCheckHistory=new BpmCheckHistory();
        bpmCheckHistory.setHisId(IdGenerator.getIdStr());
        bpmCheckHistory.setActDefId(bpmTask.getActDefId());
        bpmCheckHistory.setInstId(bpmTask.getInstId());
        bpmCheckHistory.setTreeId(bpmTask.getTreeId());
        bpmCheckHistory.setSubject(bpmTask.getSubject());
        bpmCheckHistory.setJumpType(jumpType);
        bpmCheckHistory.setCheckStatus(status);
        bpmCheckHistory.setNodeId(bpmTask.getKey());
        bpmCheckHistory.setNodeName(bpmTask.getName());
        bpmCheckHistory.setTaskId(bpmTask.getTaskId());
        bpmCheckHistory.setRemark(remark);
        bpmCheckHistory.setCompleteTime(new Date());
        bpmCheckHistory.setOwnerId(ownerId);
        bpmCheckHistory.setHandlerId(handlerId);
        bpmCheckHistoryService.insert(bpmCheckHistory);
        //???????????????
        bpmCheckHistoryService.addOpFiles(opFiles,bpmCheckHistory);
    }

    /**
     * ??????????????????
     * @param instId
     * @param processConfig
     * @param isMobile
     * @return
     */
    public JsonResult getFormData(String instId,ProcessConfig processConfig,String isMobile){
        FormConfig formConfig= getForms(processConfig,false);
        BpmInst bpmInst=null;
        if(StringUtils.isNotEmpty(instId)){
            bpmInst=this.get(instId);
        }
        JsonResult result= formDataService.getByInstId(processConfig.getDataSetting(), processConfig.getBoDefs().getValue(),formConfig,bpmInst,null,isMobile,false,"");
        return result;
    }

    /**
     * ???????????????????????????
     * isDetail ???????????????
     * <pre>
     *     1.????????????????????????
     *     2.????????????????????????????????????
     * </pre>
     * @param processConfig
     * @return
     */
    private FormConfig getForms(ProcessConfig processConfig,Boolean isDetail) {
        FormConfig returnConfig = null;
        if (BeanUtil.isEmpty(processConfig)) {
            return returnConfig;
        }
        FormConfig config=null;
        //?????????????????????
        if(isDetail){
            config = processConfig.getDetailForm();
        }else {
            config = processConfig.getStartForm();
        }
        if (BeanUtil.isNotEmpty(config) && BeanUtil.isNotEmpty(config.getFormpc())) {
            try {
                returnConfig = (FormConfig) FileUtil.cloneObject(config);
            } catch (Exception ex) {
                log.error("BpmInstServiceImpl.FormConfig is error ---:" + ExceptionUtil.getExceptionMessage(ex));
            }
            return returnConfig;
        }
        config = processConfig.getGlobalForm();
        if (BeanUtil.isNotEmpty(config) && BeanUtil.isNotEmpty(config.getFormpc())) {
            try {
                returnConfig = (FormConfig) FileUtil.cloneObject(config);
            } catch (Exception ex) {
                log.error("BpmInstServiceImpl.FormConfig is error ---:" + ExceptionUtil.getExceptionMessage(ex));
            }
        }
        return returnConfig;
    }
    /**
     * ??????instId
     * <pre>
     *     1.????????????ID???????????????????????????????????????????????????
     *     2.????????????????????????????????????
     *          ?????????????????????????????????????????????????????????????????????
     *     3.?????????????????????
     *          1.????????????????????????????????????
     *          2.????????????????????????
     *          3.????????????????????????????????????
     * </pre>
     * @param instId ????????????ID
     * @return
     */
    public JsonResult getBpmInstDecrypt(String instId) {
        JsonResult result = new JsonResult(false);
            if (StringUtils.isEmpty(instId)) {
            return result;
        }

        //ID????????????20?????????????????????
        if (instId.length() <= 20) {
            result.setSuccess(true);
            JSONObject json = new JSONObject();
            json.put("instId", instId);
            json.put("relType", false);
            result.setData(json);
            return result;
        }

        long now = System.currentTimeMillis();

        String tmp = "";
        try {
            tmp = EncryptUtil.decrypt(instId);
        } catch (Exception ex) {
            return JsonResult.Fail("ID????????????,???????????????ID!");
        }

        String[] ary = tmp.split(",");
        String instIdStr = ary[0];
        String time = ary[1];
        String userId = ContextUtil.getCurrentUserId();
        String encryptTime=SysPropertiesUtil.getString("encryptTime");
        if(StringUtils.isNotEmpty(encryptTime)) {
            long longTime = (Long.parseLong(time) + (1000 * 60 * Integer.valueOf(encryptTime)));
            if (now > longTime) {
                return JsonResult.Fail("?????????????????????,???????????????????????????!");
            }
        }

        if (ary.length == 2) {
            return doCalInstAuth(instIdStr, userId,true);
        }
        //???????????????
        String relType = ary[2];

        //????????????????????????
        if (MBoolean.TRUE_LOWER.val.equals(relType)) {
            return getInstResult(instIdStr, false);
        }
        //????????????????????????????????????
        return doCalInstAuth(instIdStr, userId,false);
    }

    /**
     * ?????????????????????????????????
     * ????????????????????????calInstAuth???????????????????????????
     * ????????????????????????
     * ????????????????????????????????????????????????
     * ??????????????????????????????
     * ??????????????????????????????????????????calInstAuth???????????????????????????????????????
     * @param instIdStr
     * @param userId
     * @param relType
     * @return
     */
    private JsonResult doCalInstAuth(String instIdStr,String userId,boolean relType){
        JsonResult result = calInstAuth(instIdStr, userId, relType);
        return result;
    }

    /**
     * ????????????????????????????????????
     * <pre>
     *     1.????????????????????????
     *     2.????????????????????????????????????
     * </pre>
     * @param instIdStr
     * @param userId
     * @return
     */
    private JsonResult calInstAuth(String instIdStr,String userId,boolean relType){
        JsonResult result=new JsonResult(false);
        //1.???????????????????????????
        BpmInst bpmInst=getById(instIdStr);
        if(bpmInst!=null){
            if(BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus()) && bpmInst.getCreateBy().equals(userId)) {
                return getInstResult(instIdStr, relType);
            } else if(bpmInst.getCreateBy().equals(userId)){
                return getInstResult(instIdStr, relType);
            }
        }

        //2.???????????????????????????????????????
        Integer count = bpmInstPermissionService.getByInstId(instIdStr,userId);
        if(count>0){
            return getInstResult(instIdStr,relType);
        }

        //3. ?????????????????????????????????
        count = bpmInstPermissionService.getCountByDefCode(bpmInst.getDefCode(),userId);
        if(count>0){
            return getInstResult(instIdStr,relType);
        }

        //4.??????????????????????????????
        List<BpmTask> taskList=bpmTaskService.getByInstId(instIdStr);
        List<String> taskIdList=taskList.stream().map(item->item.getTaskId()).collect(Collectors.toList());
        Integer taskCount=bpmInstPermissionService.getByInstIdAuthId(instIdStr,userId,taskIdList);
        if(taskCount>0){
            return result;
        }
        Set<TaskExecutor> executors=new HashSet<>();
        for (BpmTask task : taskList) {
            executors.addAll(bpmTaskUserService.getTaskExecutors(task));
        }
        for(TaskExecutor executor:executors){
            if(TaskExecutor.TYPE_USER.equals( executor.getType())){
                //?????????
                if(executor.getId().equals(userId)){
                    return getInstResult(instIdStr,relType);
                }
            }
        }
        return result;
    }

    /**
     * ????????????????????????????????????
     * <pre>
     *     1.?????????????????????????????????
     *     2.????????????????????????????????????
     * </pre>
     * @param task
     * @param userId
     * @return
     */
    public JsonResult calTaskAuth(BpmTask task,String userId){
        JsonResult result=new JsonResult(false);
        BpmDef bpmDef=bpmDefService.getById(task.getDefId());
        //1.????????????????????????
        Integer count=bpmInstPermissionService.getCountByDefCode(bpmDef.getKey(),userId);
        if(count>0){
            result.setSuccess(true);
            return result;
        }
        //2.??????????????????
        Set<TaskExecutor> executors=new HashSet<>();
        executors.addAll(bpmTaskUserService.getTaskExecutors(task));
        for(TaskExecutor executor:executors){
            if(TaskExecutor.TYPE_USER.equals( executor.getType())){
                //?????????
                if(executor.getId().equals(userId)){
                    result.setSuccess(true);
                    return result;
                }
            }
        }
        result.setMessage("??????????????????!");
        return result;
    }

    private JsonResult getInstResult(String instId,boolean relType){
        JsonResult result=JsonResult.Success();
        JSONObject json=new JSONObject();
        json.put("instId",instId);
        json.put("relType",relType);
        result.setData(json);
        return result;
    }

    /**
     * ??????instId
     * <pre>
     *  ???????????????: ??????ID,??????ID,????????????,?????????????????????
     *  instId+","+userId+","+time+","+relType
     * </pre>
     * @param instId ????????????ID
     * @param isRelType ?????????????????????
     * @return
     */
    public JsonResult getBpmInstEncrypt(String instId,Boolean isRelType) {
        JsonResult result =JsonResult.Success().setShow(false);
        try {
            long time = System.currentTimeMillis();
            String str=instId+","+time;
            if(isRelType!=null){
                String relType=isRelType? "true":"false";
                str+=","+relType;
            }
            result.setData(EncryptUtil.encrypt(str));

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("????????????!");
        }
        return result;
    }
    /**
     * ??????????????????
     * @param instId
     * @param reason
     */
    @GlobalTransactional
    public void cancelProcess(String instId,String reason,String opFiles){
        BpmInst bpmInst=get(instId);
        if(bpmInst==null){
            return;
        }

        //???????????????
        executeScript(bpmInst);

        //??????????????????????????????
        bpmInstMapper.updateStatusByInstId(instId,BpmInstStatus.CANCEL.name());
        //?????????????????????
        bpmInstLogService.addInstLog(instId,"????????????????????????????????????????????????" + reason );
        //?????????????????????????????????
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if (!bpmTasks.isEmpty()) {
            String curUserId = ContextUtil.getCurrentUserId();
            for (BpmTask bpmTask : bpmTasks) {
                insertCheckHistory(bpmTask, curUserId,curUserId, "CANCEL","CANCEL", reason, "");
                //???????????????
                bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), null);
            }
        }
        if (!BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus()) &&
                !BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) &&
                !BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
            //??????????????????
            runtimeService.deleteProcessInstance(bpmInst.getActInstId(), reason);
        }
        //????????????
        bpmTaskMapper.deleteByInstId(instId);

        //??????????????????
        updDataStatus(instId);
    }

    /**
     * ??????????????????
     * @param instId
     * @param reason
     */



    @GlobalTransactional
    public void doEndProcess(String instId,String reason,String opFiles){
        BpmInst bpmInst=get(instId);
        if(bpmInst==null){
            return;
        }
        //???????????????
        executeScript(bpmInst);
        //??????????????????????????????
        bpmInstMapper.updateStatusByInstId(instId,BpmInstStatus.SUCCESS_END.name());
        //?????????????????????
        bpmInstLogService.addInstLog(instId,"?????????????????????" + reason );
        //????????????????????????
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if(bpmTasks.size()>0){
            String curUserId = ContextUtil.getCurrentUserId();

            insertCheckHistory(bpmTasks.get(0),curUserId,curUserId,BpmInstStatus.SUCCESS_END.name(),BpmInstStatus.SUCCESS_END.name(),reason,opFiles);
        }
        //??????????????????
        runtimeService.deleteProcessInstance(bpmInst.getActInstId(),reason);
        //????????????
        bpmTaskMapper.deleteByInstId(instId);
    }

    /**
     * ??????????????????????????????
     * @param bpmInst
     */
    private void executeScript(BpmInst bpmInst){
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmInst.getActDefId());
        String script=processConfig.getEndProcessScript();
        if(StringUtils.isEmpty(script)) {
            return;
        }
        JsonResult formData = getFormData(bpmInst.getInstId(),processConfig,"NO");
        JSONObject formDataJson=new JSONObject();
        for(BpmView bpmView:(List<BpmView>)formData.getData()){
            formDataJson.put(bpmView.getBoAlias(),bpmView.getData());
        }

        Map<String, Object> contextData = new HashMap<>(formDataJson.size() + 2);
        if (formDataJson != null) {
            Set<Map.Entry<String, Object>> ents = formDataJson.entrySet();
            for (Map.Entry<String, Object> ent : ents) {
                contextData.put(ent.getKey(), ent.getValue());
            }
        }

        contextData.put(BpmConst.BPM_INST, bpmInst);

        contextData.put("vars", getVariables(bpmInst.getActInstId()));
        processScriptEngine.exeScript(script, contextData);

    }

    /**
     * ???Act??????Id??????????????????
     * @param actInstId
     * @return
     */
    public BpmInst getByActInstId(String actInstId){
        return bpmInstMapper.getByActInstId(actInstId);
    }


    /**
     * ?????????????????????????????????
     * @param userTaskConfig
     * @param formDataJson
     * @param vars
     * @return
     */
    public JsonResult getAllowApprove(UserTaskConfig userTaskConfig, JSONObject formDataJson, Map<String, Object> vars) {
        IExecutionCmd cmd = ProcessHandleUtil.getProcessCmd();
        String allowScript = userTaskConfig.getAllowScript();
        //???????????????????????????????????????
        if(StringUtils.isEmpty(allowScript)){
            return JsonResult.Success();
        }
        String allowTipInfo = userTaskConfig.getAllowTipInfo();
        Map<String, Object> contextData = new HashMap<>(formDataJson.size() + 2);
        if (formDataJson != null) {
            Set<Map.Entry<String, Object>> ents = formDataJson.entrySet();
            for (Map.Entry<String, Object> ent : ents) {
                contextData.put(ent.getKey(), ent.getValue());
            }
        }
        contextData.put("cmd", cmd);
        contextData.put("vars", vars);
        try {
            Object jsonResult = processScriptEngine.exeScript(allowScript, contextData);
            if (jsonResult instanceof Boolean) {
                JsonResult result = new JsonResult((Boolean) jsonResult);
                jsonResult = processScriptEngine.exeScript(allowTipInfo, contextData);
                result.setMessage((String) jsonResult);
                return result;
            }
        } catch (Exception e) {
            return JsonResult.Fail("?????????????????????" + e.getMessage());
        }
        return JsonResult.Fail("????????????????????????????????????true???false");
    }

    /**
     * ???????????????????????????????????????
     * @param defIds ????????????
     * @param pk    ??????
     * @return
     */
    public List<BpmInst> getByBusKey(List<String> defIds,String pk){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.in("DEF_ID_",defIds);
        wrapper.eq("BUS_KEY_",pk);
        return bpmInstMapper.selectList(wrapper);
    }


    /**
     * ????????????
     * @param defId
     * @return
     */
    public List<BpmInst> getByDefId(String defId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("DEF_ID_",defId);
        wrapper.eq("IS_TEST_",MBoolean.YES.name());
        List list = bpmInstMapper.selectList(wrapper);

        return  list;
    }
    /**
     * ????????????
     * @param userId
     * @param status
     * @param tenantId
     * @return
     */
    public List<BpmInst> getMyAllDraftBpmInst(String userId,String status,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("STATUS_",status);
        wrapper.eq("CREATE_BY_",userId);
        wrapper.eq("TENANT_ID_",tenantId);
        List list = bpmInstMapper.selectList(wrapper);
        return  list;
    }

    /**
     * ?????????????????????????????????
     * @param instId
     * @param isMobile
     * @param defaultWrite ???????????????????????????
     * @return
     */
    public BpmInstDetail getInstDetail( String instId,  String isMobile,Boolean defaultWrite,String from){
        BpmInst bpmInst;
        BpmInstRouter bpmInstRouter = bpmInstRouterService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInstRouter)){
            bpmInst=bpmInstMapper.getByArchiveLog(instId,bpmInstRouter.getTableId());
        }else {
            bpmInst=get(instId);
        }
        BpmDef bpmDef=bpmDefService.get(bpmInst.getDefId());
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmDef.getActDefId());
        //?????????????????????
        JsonResult dataResult=getFormData( instId, processConfig,isMobile,defaultWrite,true);

        BpmInstDetail detail=new BpmInstDetail();
        //?????????????????????
        detail.setBpmInst(bpmInst);
        //?????????????????????
        detail.setFormData(dataResult);
        //????????????????????????
        detail.setProcessConfig(processConfig);
        //??????????????????
        List<BpmCheckHistory> opinionHistoryList= bpmCheckHistoryService.getOpinionNameNotEmpty(instId);
        detail.setBpmCheckHistories(opinionHistoryList);
        IUser user=ContextUtil.getCurrentUser();
        //?????????????????????
        JsonResult trackResult= bpmInstTrackedService.getTracked(bpmInst.getInstId(),user.getUserId());
        detail.setTracked(trackResult.isSuccess()?"1":"0");
        //????????????????????????
        JsonResult result = bpmTakeBackService.canRevoke(detail.getBpmInst().getInstId(), from);
        detail.setCanRevoke(result.isSuccess());

        return detail;
    }

    public BpmInstDetail getInstDetail( String instId){
        BpmInst bpmInst;
        BpmInstRouter bpmInstRouter = bpmInstRouterService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInstRouter)){
            bpmInst=bpmInstMapper.getByArchiveLog(instId,bpmInstRouter.getTableId());
        }else {
            bpmInst=get(instId);
        }
        BpmDef bpmDef=bpmDefService.get(bpmInst.getDefId());
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmDef.getActDefId());

        JsonResult dataResult=getFormData( instId, processConfig, MBoolean.NO.name(),false,true);

        BpmInstDetail detail=new BpmInstDetail();
        detail.setBpmInst(bpmInst);

        if(dataResult.isSuccess()){
            List<BpmView> bpmViews= (List<BpmView>) dataResult.getData();
            for (BpmView view:bpmViews){
                view.setButtons(null);
                view.setMetadata(null);
                view.setPermission(null);
                view.setTemplate(null);
                view.setScript(null);
                view.setEntMap(null);
            }
            detail.setBpmViews(bpmViews);
        }


        //??????????????????
        List<BpmCheckHistory> opinionHistoryList= bpmCheckHistoryService.getOpinionNameNotEmpty(instId);
        detail.setBpmCheckHistories(opinionHistoryList);
        IUser user=ContextUtil.getCurrentUser();
        //?????????????????????
        JsonResult trackResult= bpmInstTrackedService.getTracked(bpmInst.getInstId(),user.getUserId());
        detail.setTracked(trackResult.isSuccess()?"1":"0");

        return detail;
    }

    /**
     * ?????????????????????
     * @param instId
     * @param processConfig
     * @param isMobile
     * @param defaultWrite
     * @return
     */
    public JsonResult getFormData(String instId,ProcessConfig processConfig,String isMobile,Boolean defaultWrite,Boolean isDetail){
        FormConfig formConfig= getForms(processConfig,isDetail);

        //??????????????????????????????????????????????????????PC????????????????????????
        if(BeanUtil.isNotEmpty(formConfig)){
            formConfig.setFormpc(setReadOnly(formConfig.getFormpc()));
            formConfig.setMobile(setReadOnly(formConfig.getMobile()));
        }
        /**
         * ??????Feign??????????????????????????????????????????
         */
        BpmInst bpmInst=null;
        if(StringUtils.isNotEmpty(instId)){
            bpmInst=this.get(instId);
        }
        JsonResult result= formDataService.getByInstId(processConfig.getDataSetting(),
                processConfig.getBoDefs().getValue(),
                formConfig,
                bpmInst,
                null,
                isMobile,
                defaultWrite,"");
        return result;
    }

    /**
     * ?????????????????????????????????????????????
     */
    private List<Form> setReadOnly(List<Form> forms){
        for (Form form:forms) {
            form.setReadOnly(true);
        }
        return forms;
    }
    /**
     * ?????????????????????
     * @param instId
     * @param status
     */
    public void updStatus(String instId,String status){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("INST_ID_",instId);
        wrapper.set("STATUS_",status);

        bpmInstMapper.update(null,wrapper);

    }

    /**
     * ?????????????????????????????????
     * @param queryFilter
     * @param tableId
     * @return
     */
    public IPage queryByArchiveLog(QueryFilter queryFilter,String tableId) {
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return  bpmInstMapper.queryByArchiveLog(queryFilter.getPage(),params,tableId);
    }

    /**
     * ??????instId??????????????????
     * @param instId
     * @param tableId
     * @return
     */
    public BpmInst getByArchiveLog(String instId, Integer tableId) {
        return  bpmInstMapper.getByArchiveLog(instId,tableId);
    }

    /**
     * ??????????????????
     * @param instIds
     */
    public void delArchive(List<String> instIds) {
        StringBuilder sb=new StringBuilder();
        sb.append("??????????????????:");
        for (String instId : instIds) {
            BpmInstRouter bpmInstRouter=bpmInstRouterService.get(instId);
            if(BeanUtil.isEmpty(bpmInstRouter)){
                continue;
            }

            Integer tableId = bpmInstRouter.getTableId();
            BpmInst bpmInst=getByArchiveLog(instId,tableId);

            if(bpmInst==null){
                return;
            }
            sb.append(bpmInst.getSubject() +",");

            String bpmInstId=bpmInst.getInstId();
            String actInstId=bpmInst.getActInstId();

            bpmRuPathService.delArchiveByInstId(bpmInstId,tableId);
            bpmInstLogService.delArchiveByInstId(bpmInstId,tableId);
            bpmCheckHistoryService.delArchiveByInstId(bpmInstId,tableId);
            bpmInstDataServiceImpl.delArchiveByInstId(bpmInstId,tableId);
            bpmInstCpService.delArchiveByInstId(bpmInstId,tableId);
            bpmInstCcService.delArchiveByInstId(bpmInstId,tableId);
            bpmRemindInstService.delArchiveByInstId(bpmInstId,tableId);
            bpmRemindHistoryService.delArchiveByInstId(bpmInstId,tableId);
            bpmCheckFileMapper.delArchiveByInstId(bpmInstId,tableId);
            bpmInstMsgService.delArchiveByInstId(bpmInstId,tableId);
            bpmInstMapper.delArchiveByInstId(bpmInstId,bpmInstRouter.getTableId());
            bpmInstRouterService.delete(bpmInstId);

        }
        LogContext.put(Audit.DETAIL,sb.toString());
    }

    /**
     *
     * ??????????????????
     * @param actInstId
     * @param destNodeUsers
     * @param opinion
     * @param opFiles
     * @throws Exception
     */
    public void doLiveProcessInstance(String actInstId, String destNodeUsers, String opinion, String opFiles) throws Exception{
        BpmInst bpmInst = getByActInstId(actInstId);
        BpmDef bpmDef=bpmDefService.getById(bpmInst.getDefId());
        Map<String,Object> vars = new HashMap();
        //????????????????????????
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(actInstId).list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            vars.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
        }
        ProcessStartCmd cmd = new ProcessStartCmd();
        JSONObject destNode = JSONObject.parseObject(destNodeUsers);
        // ????????????
        String destNodeId = destNode.getString("nodeId");
        // ?????????
        String userIds = destNode.getString("userIds");
        // ?????????
        String groupIds = destNode.getString("groupIds");

        cmd.setOpFiles(opFiles);
        cmd.setDefId(bpmInst.getDefId());
        cmd.setDefKey(bpmInst.getDefCode());
        cmd.setActDefId(bpmInst.getActDefId());
        //???????????????????????????
        UserTaskConfig userTaskConfig=(UserTaskConfig)bpmDefService.getNodeConfig(bpmInst.getActDefId(),destNodeId);
        ProcessConfig processConfig= bpmDefService.getProcessConfig(bpmInst.getActDefId());
        //???????????????????????????
        String formType=bpmTaskService.handFormConfig(processConfig,userTaskConfig);

        /**
         * ?????????????????????
         */
        if(!BpmTask.FORM_TYPE_SEL_DEV.equals(formType)){

            JsonResult formData= bpmTaskService.getFormData(bpmInst.getInstId(),processConfig,userTaskConfig,null);

            JSONObject formDataJson=new JSONObject();
            for(BpmView bpmView:(List<BpmView>)formData.getData()){
                formDataJson.put(bpmView.getBoAlias(),bpmView.getData());
            }
            cmd.setBoDataMap(formDataJson);
            cmd.setFormJson(formDataJson.toJSONString());
        }
        cmd.setOpinion(opinion);
        // ????????????
        cmd.setDestNodeId(destNodeId);
        // ??????????????????
        Map<String, LinkedHashSet<TaskExecutor>> nodeExecutors=new HashMap<>();
        LinkedHashSet<TaskExecutor> executors=new LinkedHashSet<>();
        if(StringUtils.isNotEmpty(userIds)){
            String[] userIdAry = userIds.split(",");
            for(String userId:userIdAry) {
                executors.add(TaskExecutor.getUser(orgService.getUserById(userId)));
            }
        }
        //????????????????????????
        if(StringUtils.isNotEmpty(groupIds)){
            String[] groupIdAry = groupIds.split(",");
            for(String groupId:groupIdAry) {
                List<OsUserDto> users=orgService.getByGroupId(groupId);
                for(OsUserDto osUserDto:users) {
                    executors.add(TaskExecutor.getUser(osUserDto));
                }
            }
        }
        nodeExecutors.put(destNodeId,executors);
        cmd.setNodeExecutors(nodeExecutors);

        cmd.setVars(vars);
        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(cmd);

        //???????????????
        BpmInst newInst = doStartProcess(cmd,bpmDef,"live");
        List<BpmTask> tasks = bpmTaskMapper.getByInstId(newInst.getInstId());
        //??????????????????
        for(BpmTask task:tasks) {
            IUser iUser=ContextUtil.getCurrentUser();
            bpmCheckHistoryService.createHistory(task, TaskOptionType.LIVE.name(), "", opinion, iUser.getUserId(), iUser.getDeptId(), opFiles,"","");
        }
        newInst.setLiveInstId(bpmInst.getInstId());
        bpmInst.setIsLive(MBoolean.YES.name());
        update(newInst);
        update(bpmInst);
    }

    /**
     * ????????????????????????
     * @param filter
     * @return
     */
    public IPage<BpmInst> getMyApproved(QueryFilter filter){

        Map<String,Object> params= PageHelper.constructParams(filter);

        return bpmInstMapper.getMyApproved(filter.getPage(),params);
    }

    /**
     * ???????????????????????????
     * @param userId
     * @param tenantId
     * @return
     */
    public Integer getMyApprovedCount(String userId,String tenantId){
        return bpmInstMapper.getMyApprovedCount(userId,tenantId);
    }


    /**
     * ??????????????????????????????
     * @param instId
     */
    private void updDataStatus(String instId)  {
        List<BpmInstData> bpmInstDataList= bpmInstDataServiceImpl.getByInstId(instId);
        List<BpmInstDataDto> dataDtoList=new ArrayList<>();
        for (BpmInstData data:bpmInstDataList) {
            BpmInstDataDto bpmInstDataDto=new BpmInstDataDto();
            bpmInstDataDto.setBodefAlias(data.getBodefAlias());
            bpmInstDataDto.setInstId(data.getInstId());
            bpmInstDataDto.setPk(data.getPk());
            bpmInstDataDto.setStatus(BpmInstStatus.CANCEL.name());
            dataDtoList.add(bpmInstDataDto);
        }
        formClient.updStatusByInstId(dataDtoList);
    }

    public Map<String,String> getFieldJsonByInstIds(List<String> instIds){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.select("FIELD_JSON_","INST_ID_");
        wrapper.in("INST_ID_",instIds);
        Map<String,String> instMap=new HashMap<>();
        List<BpmInst> list = bpmInstMapper.selectList(wrapper);
        for(BpmInst inst : list){
            instMap.put(inst.getInstId(),inst.getFieldJson());
        }
        return instMap;
    }

}


