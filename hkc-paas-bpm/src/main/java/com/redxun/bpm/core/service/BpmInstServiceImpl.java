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
* [流程实例]业务服务类
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
     * 获取用户个人启动的流程实例
     * @param filter
     * @return
     */
    public IPage<BpmInst> getByUserId(QueryFilter filter){

        Map<String,Object> params= PageHelper.constructParams(filter);

        return bpmInstMapper.getByUserId(filter.getPage(),params);
    }

    /**
     * 添加流程实例数据。
     * @param boAlias
     * @param instId
     * @param pk
     */
    private void handBpmInstData(String boAlias,String instId,String pk){
        if(StringUtils.isEmpty(boAlias) ){
            return;
        }
        //已经插入
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
            //处理fieldJson。
            TaskFieldConfig fieldConfig= processConfig.getTaskFields();
            //处理JSON字段。
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
            //处理fieldJson。
            TaskFieldConfig fieldConfig= processConfig.getTaskFields();
            //处理JSON字段。
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
        String title="启动流程失败!";
        String message=ExceptionUtil.getExceptionMessage(ex);
        //网关出错
        if(message.indexOf("No outgoing sequence")!=-1){
            title="网关没有符合条件的路径，请检查网关条件配置!";
        }
        String tmp=MessageUtil.getTitle();
        if(StringUtils.isNotEmpty(tmp)) {
            title=tmp;
        }
        MessageUtil.triggerException(title,message);

    }

    /**
     * 启动流程
     * @param cmd
     * @return
     */
    @GlobalTransactional
    @Transactional
    public BpmInst doStartProcess(ProcessStartCmd cmd, BpmDef bpmDef,String operate) throws Exception {
        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(cmd);
        //初始流程实例数据。
        BpmInst bpmInst = initBpmInst(cmd,bpmDef);
        //获取流程组配置
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmDef.getActDefId());
        //处理表单数据
        if(cmd.getSystemHand()){
            handBusinessData(bpmInst,cmd,processConfig,operate);
        }
        //在启动时执行前置处理器
        processHandlerExecutor.handStartBeforeHandler(processConfig,bpmInst);
        //处理流程实例数据保存。
        handBpmInst( bpmInst, cmd, bpmDef, processConfig);
        //处理流程变量。
        handVars(cmd,  bpmInst,processConfig,true);
        //调用Activiti引擎启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(bpmDef.getActDefId(),bpmInst.getBusKey(), cmd.getVars());
        bpmInst.setActInstId(processInstance.getId());
        //更新Act流程实例至流程变量中
        runtimeService.setVariable(processInstance.getId(),BpmInstVars.ACT_INST_ID.getKey(),processInstance.getId());

        //在流程启动完成时处理后置处理器。
        processHandlerExecutor.handStartAfterHandler(processConfig,bpmInst);

        //保存流程实例
        saveBpmInst(cmd,bpmInst,true);
        //处理子流程变量
        handSubVars(cmd,bpmInst);
        //跳过第一个节点。
        handFirstJump(cmd,processConfig,bpmInst);

        //任务跳过
        IExecutionCmd nextCmd=ProcessHandleUtil.getProcessCmd();
        taskSkipService.handSkipTask(nextCmd);
        //加入流程实例权限表
        bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, "", null);
        bpmInstPermissionService.createTaskInfo(bpmInst,nextCmd);
        // 添加跟踪。
        addTracked(bpmInst);
        return  bpmInst;
    }

    /**
     * 添加跟踪。
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
     * 保存流程实例。
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
     * 初始流程实例。
     * @param cmd
     * @return
     */
    private BpmInst initBpmInst(ProcessStartCmd cmd,BpmDef bpmDef) {
        String busKey=cmd.getBusKey();
        cmd.setCheckType(TaskOptionType.AGREE.name());
        //直接启动。
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
        //设置billType
        if(StringUtils.isNotEmpty(cmd.getBillType())){
            bpmInst.setBillType(cmd.getBillType());
        }else{
            bpmInst.setBillType(processConfig.getBoDefs().getText());
        }

        bpmInst.setActDefId(bpmDef.getActDefId())
                .setDefId(bpmDef.getDefId())
                .setDefCode(bpmDef.getKey())
                .setBillNo(bpmInst.getBusKey())//billNo通过流水号获取
                .setVersion(bpmDef.getVersion());//TODO 单独生成一个流水单号
        bpmInst.setIsTest(BpmDef.FORMAL_YES.equals(bpmDef.getFormal()) ? MBoolean.NO.name() : MBoolean.YES.name());
        //根据提交过来的方式来生成流程标题
        if(StringUtils.isNotEmpty(cmd.getSubject())){
            bpmInst.setSubject(cmd.getSubject());
        }else{
            //从流程实例的配置中取流程
            String subject=getSubject(processConfig,bpmDef.getName(),cmd);
            bpmInst.setSubject(subject);
        }
        if(BpmDef.FORMAL_NO.equals(bpmDef.getFormal())){
            bpmInst.setSubject("[测试]"+bpmInst.getSubject());
        }

    }

    /**
     * 跳过审批任务的第一个节点
     * @param processConfig
     * @param bpmInst
     */
    private void handFirstJump(ProcessStartCmd cmd,ProcessConfig processConfig,BpmInst bpmInst){
        //获取第一个节点
        FlowNode firstNode = actRepService.getFirstUserTaskNode(bpmInst.getActDefId());
        //是否为第一个节点
        boolean isFirstNode=false;
        if(StringUtils.isNotEmpty(cmd.getDestNodeId())){
            isFirstNode = firstNode.getId().equals(cmd.getDestNodeId());
        }

        //跳过第一个节点
        if(BeanUtil.isNotEmpty(processConfig.getStartNodeOptions()) && (processConfig.getStartNodeOptions().contains("skipFirstNode") || StringUtils.isNotEmpty(cmd.getDestNodeId()))){

            //DestNodeId不为空且为第一个节点
            if(StringUtils.isNotEmpty(cmd.getDestNodeId()) && isFirstNode){
                return;
            }

            ProcessNextCmd nextCmd=new ProcessNextCmd();
            ProcessHandleUtil.setProcessCmd(nextCmd);

            String opinion=cmd.getOpinion();
            if(StringUtils.isEmpty(opinion)){
                opinion="发起流程事项审批";
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
            //选择路径。
            if(StringUtils.isNotEmpty(cmd.getDestNodeId())){
                BpmTask bpmTask=tasks.get(0);
                //构造执行路径
                nextCmd.setPreNodeId(bpmTask.getKey());
                actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),cmd.getDestNodeId());

                //删除关联的任务
                bpmTaskMapper.deleteByActTaskId(bpmTask.getActTaskId());
            }
            else{
                for(BpmTask task:tasks){

                    nextCmd.setTaskId(task.getTaskId());
                    nextCmd.setInstId(bpmInst.getInstId());
                    nextCmd.setDefId(bpmInst.getDefId());
                    nextCmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,task);



                    bpmTaskService.completeTask(nextCmd);

                    //删除关联的任务
                    bpmTaskMapper.deleteByActTaskId(task.getActTaskId());
                }
            }
        }

    }

    /**
     * 处理流程变量。
     * @param cmd
     * @param bpmInst
     * @param config
     */
    public void handVars(IExecutionCmd cmd,  BpmInst bpmInst,ProcessConfig config,boolean start) {
        Map<String,Object> vars=new HashMap<String,Object>();
        //加入全局变量
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
        //加上后续节点用户Id
        if(StringUtils.isNotEmpty(cmd.getNodeUserIds())){
            vars.put(BpmInstVars.NODE_USER_IDS.getKey(),cmd.getNodeUserIds());
        }

        //处理流程变量配置。
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
            //表示从表单获取值
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
            //表示从表单获取值
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
     * 上下文数据有表单数据
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
     * 根据数据类型进行数据类型的转换。
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
            // 验证JSON格式。
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
     * 获取标题规则。
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
            //不是字段
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
                    MessageUtil.triggerException("提示信息","请检查BO别名["+boAlias+"]是否存在!");
                }
                if(ary.length==2) {
                    //单值情况
                    String val = jsonObj.getString(key);
                    sb.append(val);
                }else if(ary.length==3){
                    //双值情况
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
     * 保存草稿。
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
        //处理表单数据
        if(cmd.getSystemHand()){
            //处理表单数据
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
            //在流程启动完成时处理后置处理器。--自定义表单
            processHandlerExecutor.handStartAfterHandler(processConfig,bpmInst);
        }

        handBpmInst(bpmInst,cmd,bpmDef,processConfig);
        saveBpmInst(cmd,bpmInst,false);
        return  bpmInst;
    }

    /**
     * 根据流程实例ID获取流程变量数据。
     * @param actInstId
     * @return
     */
    public Map<String,Object> getVariables(String actInstId){
        return runtimeService.getVariables(actInstId);
    }

    /**
     * 删除流程实例时，级联删除其下的所有相关的记录，包括任务，审批历史，跳转记录等。
     * <pre>
     *     1. 删除任务用户。
     *     2. 删除任务
     *     3. 删除流程引擎运行数据。
     *     4. 删除RUNPATH
     *     5. 删除活动日志。
     *     6. 删除流程实例
     *     7. 删除会签数据
     *     8. 删除审批意见数据
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
        //将表单数据流程状态改为删除 DELETE
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
     * 批量删除流程实例
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
     * 根据实例 Id更新其状态
     * @param instId
     * @param status 状态值来自 @BpmInstStatus
     */
    @Transactional
    public void updateStatusByInstId(String instId,String status){
        bpmInstMapper.updateStatusByInstId(instId,status);
        //审批历史
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if(bpmTasks.size()>0){
            String curUserId = ContextUtil.getCurrentUserId();
            insertCheckHistory(bpmTasks.get(0),curUserId,curUserId,status,status,"","");
        }
    }

    /**
     * 新增一条审批历史
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
        //添加附件。
        bpmCheckHistoryService.addOpFiles(opFiles,bpmCheckHistory);
    }

    /**
     * 获取表单数。
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
     * 获取发起表单配置。
     * isDetail 是否为明细
     * <pre>
     *     1.先获取发起表单。
     *     2.获取不到再获取全局表单。
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
        //是否为明细配置
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
     * 解密instId
     * <pre>
     *     1.首先判断ID是否加密过，如果没有加密直接返回。
     *     2.判断是否从关联流程过来。
     *          如果是则判断是否超时，如果超时则返回错误提示。
     *     3.不是从关联过来
     *          1.判断是不是我的流程草稿。
     *          2.判断我是否有权限
     *          3.判断是不是我的待办任务。
     * </pre>
     * @param instId 流程实例ID
     * @return
     */
    public JsonResult getBpmInstDecrypt(String instId) {
        JsonResult result = new JsonResult(false);
            if (StringUtils.isEmpty(instId)) {
            return result;
        }

        //ID长度小于20表示没有加密过
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
            return JsonResult.Fail("ID传入错误,请检查你的ID!");
        }

        String[] ary = tmp.split(",");
        String instIdStr = ary[0];
        String time = ary[1];
        String userId = ContextUtil.getCurrentUserId();
        String encryptTime=SysPropertiesUtil.getString("encryptTime");
        if(StringUtils.isNotEmpty(encryptTime)) {
            long longTime = (Long.parseLong(time) + (1000 * 60 * Integer.valueOf(encryptTime)));
            if (now > longTime) {
                return JsonResult.Fail("链接访问已超时,请刷新列表重新访问!");
            }
        }

        if (ary.length == 2) {
            return doCalInstAuth(instIdStr, userId,true);
        }
        //是否为关联
        String relType = ary[2];

        //从关联过来的流程
        if (MBoolean.TRUE_LOWER.val.equals(relType)) {
            return getInstResult(instIdStr, false);
        }
        //不是关联流程过来的数据。
        return doCalInstAuth(instIdStr, userId,false);
    }

    /**
     * 计算流程实例是否有权限
     * 根据当前用户调用calInstAuth方法判断是否有权限
     * 有权限，直接返回
     * 无权限，判断该用户是否有关联用户
     * 无关联用户，直接返回
     * 有关联用户，根据关联用户调用calInstAuth方法判断是否有权限，并返回
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
     * 计算流程实例是否有权限。
     * <pre>
     *     1.判断是否为草稿。
     *     2.判断流程实例是否有权限。
     * </pre>
     * @param instIdStr
     * @param userId
     * @return
     */
    private JsonResult calInstAuth(String instIdStr,String userId,boolean relType){
        JsonResult result=new JsonResult(false);
        //1.查询是否为我的草稿
        BpmInst bpmInst=getById(instIdStr);
        if(bpmInst!=null){
            if(BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus()) && bpmInst.getCreateBy().equals(userId)) {
                return getInstResult(instIdStr, relType);
            } else if(bpmInst.getCreateBy().equals(userId)){
                return getInstResult(instIdStr, relType);
            }
        }

        //2.查询流程权限表是否存在授权
        Integer count = bpmInstPermissionService.getByInstId(instIdStr,userId);
        if(count>0){
            return getInstResult(instIdStr,relType);
        }

        //3. 检查权限是否为管理员。
        count = bpmInstPermissionService.getCountByDefCode(bpmInst.getDefCode(),userId);
        if(count>0){
            return getInstResult(instIdStr,relType);
        }

        //4.是否为流程实例待办人
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
                //审批人
                if(executor.getId().equals(userId)){
                    return getInstResult(instIdStr,relType);
                }
            }
        }
        return result;
    }

    /**
     * 审批任务判断是否有权限。
     * <pre>
     *     1.判断是否为流程管理员。
     *     2.判断是否为任务的审批人。
     * </pre>
     * @param task
     * @param userId
     * @return
     */
    public JsonResult calTaskAuth(BpmTask task,String userId){
        JsonResult result=new JsonResult(false);
        BpmDef bpmDef=bpmDefService.getById(task.getDefId());
        //1.是否为流程管理员
        Integer count=bpmInstPermissionService.getCountByDefCode(bpmDef.getKey(),userId);
        if(count>0){
            result.setSuccess(true);
            return result;
        }
        //2.是否为待办人
        Set<TaskExecutor> executors=new HashSet<>();
        executors.addAll(bpmTaskUserService.getTaskExecutors(task));
        for(TaskExecutor executor:executors){
            if(TaskExecutor.TYPE_USER.equals( executor.getType())){
                //审批人
                if(executor.getId().equals(userId)){
                    result.setSuccess(true);
                    return result;
                }
            }
        }
        result.setMessage("此任务没权限!");
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
     * 加密instId
     * <pre>
     *  加密数据为: 实例ID,用户ID,加密时间,是否为关联流程
     *  instId+","+userId+","+time+","+relType
     * </pre>
     * @param instId 流程实例ID
     * @param isRelType 是否为关联流程
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
            result.setMessage("加密失败!");
        }
        return result;
    }
    /**
     * 作废流程实例
     * @param instId
     * @param reason
     */
    @GlobalTransactional
    public void cancelProcess(String instId,String reason,String opFiles){
        BpmInst bpmInst=get(instId);
        if(bpmInst==null){
            return;
        }

        //执行脚本。
        executeScript(bpmInst);

        //更新流程实例作废状态
        bpmInstMapper.updateStatusByInstId(instId,BpmInstStatus.CANCEL.name());
        //加上实例的日志
        bpmInstLogService.addInstLog(instId,"进行流程实例的作废，作废原因为：" + reason );
        //插入作废的流程审批历史
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if (!bpmTasks.isEmpty()) {
            String curUserId = ContextUtil.getCurrentUserId();
            for (BpmTask bpmTask : bpmTasks) {
                insertCheckHistory(bpmTask, curUserId,curUserId, "CANCEL","CANCEL", reason, "");
                //增加权限。
                bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), null);
            }
        }
        if (!BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus()) &&
                !BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) &&
                !BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
            //删除流程实例
            runtimeService.deleteProcessInstance(bpmInst.getActInstId(), reason);
        }
        //删除任务
        bpmTaskMapper.deleteByInstId(instId);

        //修改表单状态
        updDataStatus(instId);
    }

    /**
     * 结束流程实例
     * @param instId
     * @param reason
     */



    @GlobalTransactional
    public void doEndProcess(String instId,String reason,String opFiles){
        BpmInst bpmInst=get(instId);
        if(bpmInst==null){
            return;
        }
        //执行脚本。
        executeScript(bpmInst);
        //更新流程实例结束状态
        bpmInstMapper.updateStatusByInstId(instId,BpmInstStatus.SUCCESS_END.name());
        //加上实例的日志
        bpmInstLogService.addInstLog(instId,"手工结束：原因" + reason );
        //插入流程审批历史
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if(bpmTasks.size()>0){
            String curUserId = ContextUtil.getCurrentUserId();

            insertCheckHistory(bpmTasks.get(0),curUserId,curUserId,BpmInstStatus.SUCCESS_END.name(),BpmInstStatus.SUCCESS_END.name(),reason,opFiles);
        }
        //删除流程实例
        runtimeService.deleteProcessInstance(bpmInst.getActInstId(),reason);
        //删除任务
        bpmTaskMapper.deleteByInstId(instId);
    }

    /**
     * 作废时执行流程脚本。
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
     * 按Act实例Id获取流程实例
     * @param actInstId
     * @return
     */
    public BpmInst getByActInstId(String actInstId){
        return bpmInstMapper.getByActInstId(actInstId);
    }


    /**
     * 检查是否允许任务执行。
     * @param userTaskConfig
     * @param formDataJson
     * @param vars
     * @return
     */
    public JsonResult getAllowApprove(UserTaskConfig userTaskConfig, JSONObject formDataJson, Map<String, Object> vars) {
        IExecutionCmd cmd = ProcessHandleUtil.getProcessCmd();
        String allowScript = userTaskConfig.getAllowScript();
        //没有配置条件直接返回成功。
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
            return JsonResult.Fail("脚本执行错误：" + e.getMessage());
        }
        return JsonResult.Fail("脚本执行错误，返回值需为true或false");
    }

    /**
     * 根据业务主键获取流程状态。
     * @param defIds 流程实例
     * @param pk    主键
     * @return
     */
    public List<BpmInst> getByBusKey(List<String> defIds,String pk){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.in("DEF_ID_",defIds);
        wrapper.eq("BUS_KEY_",pk);
        return bpmInstMapper.selectList(wrapper);
    }


    /**
     * 根据流程
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
     * 根据流程
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
     * 获取流程实例的相关数据
     * @param instId
     * @param isMobile
     * @param defaultWrite 单据的数据是否只读
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
        //获取单据的数据
        JsonResult dataResult=getFormData( instId, processConfig,isMobile,defaultWrite,true);

        BpmInstDetail detail=new BpmInstDetail();
        //设置实例主数据
        detail.setBpmInst(bpmInst);
        //设置单据的数据
        detail.setFormData(dataResult);
        //设置流程级的配置
        detail.setProcessConfig(processConfig);
        //审批意见记录
        List<BpmCheckHistory> opinionHistoryList= bpmCheckHistoryService.getOpinionNameNotEmpty(instId);
        detail.setBpmCheckHistories(opinionHistoryList);
        IUser user=ContextUtil.getCurrentUser();
        //是否已经跟踪。
        JsonResult trackResult= bpmInstTrackedService.getTracked(bpmInst.getInstId(),user.getUserId());
        detail.setTracked(trackResult.isSuccess()?"1":"0");
        //处理是否可以撤销
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


        //审批意见记录
        List<BpmCheckHistory> opinionHistoryList= bpmCheckHistoryService.getOpinionNameNotEmpty(instId);
        detail.setBpmCheckHistories(opinionHistoryList);
        IUser user=ContextUtil.getCurrentUser();
        //是否已经跟踪。
        JsonResult trackResult= bpmInstTrackedService.getTracked(bpmInst.getInstId(),user.getUserId());
        detail.setTracked(trackResult.isSuccess()?"1":"0");

        return detail;
    }

    /**
     * 获取单据的数据
     * @param instId
     * @param processConfig
     * @param isMobile
     * @param defaultWrite
     * @return
     */
    public JsonResult getFormData(String instId,ProcessConfig processConfig,String isMobile,Boolean defaultWrite,Boolean isDetail){
        FormConfig formConfig= getForms(processConfig,isDetail);

        //检查单据的配置是否为空，若不空，设置PC单据与移动端单据
        if(BeanUtil.isNotEmpty(formConfig)){
            formConfig.setFormpc(setReadOnly(formConfig.getFormpc()));
            formConfig.setMobile(setReadOnly(formConfig.getMobile()));
        }
        /**
         * 通过Feign的服务获取单据的基本信息结果
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
     * 查看流程表单明细，全部设置只读
     */
    private List<Form> setReadOnly(List<Form> forms){
        for (Form form:forms) {
            form.setReadOnly(true);
        }
        return forms;
    }
    /**
     * 更新实例状态。
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
     * 分页查询备份的流程实例
     * @param queryFilter
     * @param tableId
     * @return
     */
    public IPage queryByArchiveLog(QueryFilter queryFilter,String tableId) {
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return  bpmInstMapper.queryByArchiveLog(queryFilter.getPage(),params,tableId);
    }

    /**
     * 根据instId获取备份数据
     * @param instId
     * @param tableId
     * @return
     */
    public BpmInst getByArchiveLog(String instId, Integer tableId) {
        return  bpmInstMapper.getByArchiveLog(instId,tableId);
    }

    /**
     * 删除备份记录
     * @param instIds
     */
    public void delArchive(List<String> instIds) {
        StringBuilder sb=new StringBuilder();
        sb.append("删除归档数据:");
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
     * 流程实例复活
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
        //获取流程历史变量
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(actInstId).list();
        for (HistoricVariableInstance historicVariableInstance : list) {
            vars.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
        }
        ProcessStartCmd cmd = new ProcessStartCmd();
        JSONObject destNode = JSONObject.parseObject(destNodeUsers);
        // 目标节点
        String destNodeId = destNode.getString("nodeId");
        // 审批人
        String userIds = destNode.getString("userIds");
        // 审批组
        String groupIds = destNode.getString("groupIds");

        cmd.setOpFiles(opFiles);
        cmd.setDefId(bpmInst.getDefId());
        cmd.setDefKey(bpmInst.getDefCode());
        cmd.setActDefId(bpmInst.getActDefId());
        //获取节点与流程配置
        UserTaskConfig userTaskConfig=(UserTaskConfig)bpmDefService.getNodeConfig(bpmInst.getActDefId(),destNodeId);
        ProcessConfig processConfig= bpmDefService.getProcessConfig(bpmInst.getActDefId());
        //判断是否自定义表单
        String formType=bpmTaskService.handFormConfig(processConfig,userTaskConfig);

        /**
         * 获取表单数据。
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
        // 目标节点
        cmd.setDestNodeId(destNodeId);
        // 节点人员配置
        Map<String, LinkedHashSet<TaskExecutor>> nodeExecutors=new HashMap<>();
        LinkedHashSet<TaskExecutor> executors=new LinkedHashSet<>();
        if(StringUtils.isNotEmpty(userIds)){
            String[] userIdAry = userIds.split(",");
            for(String userId:userIdAry) {
                executors.add(TaskExecutor.getUser(orgService.getUserById(userId)));
            }
        }
        //获取节点后续人员
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

        //启动新流程
        BpmInst newInst = doStartProcess(cmd,bpmDef,"live");
        List<BpmTask> tasks = bpmTaskMapper.getByInstId(newInst.getInstId());
        //创建任务实例
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
     * 获取我的已办流程
     * @param filter
     * @return
     */
    public IPage<BpmInst> getMyApproved(QueryFilter filter){

        Map<String,Object> params= PageHelper.constructParams(filter);

        return bpmInstMapper.getMyApproved(filter.getPage(),params);
    }

    /**
     * 获取我的已办流程数
     * @param userId
     * @param tenantId
     * @return
     */
    public Integer getMyApprovedCount(String userId,String tenantId){
        return bpmInstMapper.getMyApprovedCount(userId,tenantId);
    }


    /**
     * 更新表单数据的状态。
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


