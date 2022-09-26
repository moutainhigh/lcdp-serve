package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.*;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.ext.FormStatus.FormStatusContext;
import com.redxun.bpm.core.ext.FormStatus.IFormStatus;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.constvar.ConstVarContext;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.form.*;
import com.redxun.feign.form.FormClient;
import com.redxun.feign.sys.SystemClient;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormDataService {

    @Autowired
    private BpmInstDataServiceImpl bpmInstDataService;
    @Autowired
    private BpmDefService bpmDefService;
    @Autowired
    BpmInstServiceImpl bpmInstService;
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private FormClient formClient;
    @Autowired
    private com.redxun.bpm.feign.FormClient bpmFormClient;
    @Autowired
    private ConstVarContext constVarContext;
    @Autowired
    SystemClient systemClient;
    @Autowired
    GroovyEngine groovyEngine;
    @Autowired
    private BpmInstRouterServiceImpl bpmInstRouterService;
    @Autowired
    private RuntimeService runtimeService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Autowired
    BpmInstCcServiceImpl bpmInstCcService;

    /**
     * 根据实例ID获取Bo别名和主键的映射。
     * @param instId
     * @return
     */
    private Map<String,String > getInstMap(String instId){
        Map<String,String> instMap=new HashMap<>();
        if(StringUtils.isEmpty(instId)){
            return  instMap;
        }
        List<BpmInstData>  bpmInstDatas;
        BpmInstRouter bpmInstRouter = bpmInstRouterService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInstRouter)){
            bpmInstDatas=bpmInstDataService.getByArchiveLog(instId,bpmInstRouter.getTableId());
        }else {
            bpmInstDatas=bpmInstDataService.getByInstId(instId);
        }

        instMap=bpmInstDatas.stream().collect(Collectors.toMap(p->p.getBodefAlias(), p -> p.getPk()));
        return  instMap;
    }


    /**
     * 获取表单数据。
     * @param dataSetting       业务数据设定。
     * @param boDefAlias        BO定义别名
     * @param formConfig        表单配置
     * @param bpmInst           流程实例ID
     * @param nodeId            节点ID
     * @param isMobile          是否手机表单
     * @param defaultWrite      默认可以编辑（如果可编辑可以不管授权设定)
     * @return
     */
    public JsonResult getByInstId(DataSetting dataSetting, String boDefAlias, FormConfig formConfig,
                                  BpmInst bpmInst,String nodeId, String isMobile, boolean defaultWrite, String pk){
        Map<String,String> instMap=new HashMap<>();
        if(BeanUtil.isNotEmpty(bpmInst)){
            try {
                if (!BpmInstStatus.CANCEL.name().equals(bpmInst.getStatus()) && !BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus()) && !BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) && !BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
                    String mainActInstId = bpmInst.getParentActInstId();
                    if (StringUtils.isNotEmpty(mainActInstId)) {
                        BpmInst mainBpmInst = bpmInstService.getByActInstId(mainActInstId);
                        instMap.putAll(getInstMap(mainBpmInst.getInstId()));
                    }
                }
            }catch (Exception e) {
            }
            instMap.putAll(getInstMap(bpmInst.getInstId()));
        }

        List<Form> forms=null;
        boolean ismobile= MBoolean.YES.name().equals(isMobile);
        if(BeanUtil.isNotEmpty(bpmInst) && StringUtils.isNotEmpty(bpmInst.getFormSolutionAlias())){
            forms=new ArrayList<>();
            FormSolutionDto formSolution=bpmFormClient.getFormSolutionByAlias(bpmInst.getFormSolutionAlias());
            Form form=new Form();
            form.setAlias(formSolution.getFormAlias());
            form.setName(formSolution.getFormName());
            form.setBoAlias(formSolution.getBoAlias());
            String permission="";
            if (formConfig != null) {
                permission = formConfig.getFormpc().get(0).getPermission();
            }
            if(StringUtils.isEmpty(permission)){
                permission=formSolution.getPermission();
            }
            form.setPermission(permission);
            forms.add(form);
        }else {
            if (formConfig != null) {
                if(ismobile){
                    forms=formConfig.getMobile();
                    //流程中手机表单不能配置权限，要以取PC表单配置的权限 Elwin 2021-12-2
                    String permission= formConfig.getFormpc().get(0).getPermission();
                    forms.get(0).setPermission(permission);
                }else {
                    forms = formConfig.getFormpc();
                }

            }
        }

        List<FormParams> formParams=listFormParams(instMap,boDefAlias, forms,defaultWrite,pk);
        if(bpmInst!=null) {
            //发起人
            String startUserId = bpmInst.getCreateBy();
            //获取当前人的审批历史
            String curUserId = ContextUtil.getCurrentUserId();
            Set<String> approveNodes = new HashSet<>();
            List<BpmCheckHistory> checkHistories = bpmCheckHistoryService.getByInstUser(bpmInst.getInstId(), curUserId);
            if (BeanUtil.isNotEmpty(checkHistories)) {
                for (BpmCheckHistory bpmCheckHistory : checkHistories) {
                    approveNodes.add(bpmCheckHistory.getNodeId());
                }
            }
            //获取抄送人
            Set<String> ccUserIds = new HashSet<>();
            List<BpmInstCc> bpmInstCcList = bpmInstCcService.getByInstId(bpmInst.getInstId());
            if (BeanUtil.isNotEmpty(bpmInstCcList)) {
                for (BpmInstCc bpmInstCc : bpmInstCcList) {
                    BpmInstCp bpmInstCp = bpmInstCc.getInstCp();
                    if (BeanUtil.isNotEmpty(bpmInstCp)) {
                        ccUserIds.add(bpmInstCp.getUserId());
                    }
                }
            }
            for(FormParams formParam:formParams){
                formParam.setNodeId(nodeId);
                formParam.setStartUserId(startUserId);
                formParam.setApproveNodes(StringUtils.join(approveNodes,","));
                formParam.setCcUserIds(StringUtils.join(ccUserIds,","));
            }
        }
        if(BeanUtil.isEmpty(formParams)){
            return JsonResult.Fail("没有设置表单");
        }
        JsonResult result= new JsonResult();

        if(ismobile){
            result=formClient.getMobileByAliasAndPermisson(formParams);
        }else{
            result=formClient.getByAliasAndPermisson(formParams);
        }

        List<BpmView> viewList= (List<BpmView>) result.getData();
        //数据处理
        handDataSetting(viewList,dataSetting);

        result.setData(viewList);

        return  result;
    }

    /**
     * 构建请求参数。
     * @param instMap       流程实例MAP
     * @param boDefAlias    BO定义别名
     * @param forms         表单列表
     * @param defaultWrite  默认可写
     * @return
     */
    private List<FormParams> listFormParams(Map<String,String> instMap,
                                           String boDefAlias,
                                           List<Form> forms,
                                           boolean defaultWrite,String pk){
        List<FormParams> formParams=new ArrayList<>();
        if(BeanUtil.isNotEmpty(forms)){
            for(Form form: forms){
                FormParams params=new FormParams();
                params.setAlias(form.getAlias());
                String boAlias = form.getBoAlias();
                if(StringUtils.isEmpty(boAlias)){
                    boAlias=boDefAlias;
                }
                params.setBoAlias(boAlias);
                params.setPermission(form.getPermission());
                if(instMap.containsKey(boAlias)){
                    params.setPk(instMap.get(boAlias));
                }
                if(form.getReadOnly()){
                    params.setReadOnly(true);
                }
                /**
                 * 如果传入的主键不为空，那么使用主键
                 */
                if(StringUtils.isNotEmpty(pk)){
                    params.setPk(pk);
                }
                params.setDefaultWrite(defaultWrite);
                formParams.add(params);
            }
        }else{
            String[] aryBoAlias=boDefAlias.split(",");
            for(String alias:aryBoAlias){
                FormParams params=new FormParams();
                params.setBoAlias(alias);
                params.setPk("");

                if(instMap.containsKey(alias)){
                    params.setPk(instMap.get(alias));
                }
                /**
                 * 如果传入的主键不为空，那么使用主键
                 */
                if(StringUtils.isNotEmpty(pk)){
                    params.setPk(pk);
                }
                params.setDefaultWrite(defaultWrite);
                formParams.add(params);
            }
        }
        return formParams;
    }

    /**
     * 将表单数据映射转化为 bo 和表单数据 的映射。
     * @param formData
     * @return
     */
    public JSONObject handBySetting(JSONObject formData,DataSetting dataSetting){
        Set<String> set=formData.keySet();
        String keys= StringUtils.join(set);
        //{alias:{boAlias:"",entMap:{entName:"main"}}}
        String boJson=formClient.getAliasByFormAlias(keys);

        JSONObject jsonMap= JSONObject.parseObject(boJson);
        JSONObject rtn=new JSONObject();

        boolean isDataSetting=false;
        List<EntSetting> entSettings=null;
        Map<String, EntSetting> nodeMap=null;
        String saveScript="";
        if(dataSetting!=null){
            entSettings = dataSetting.getFieldSetting();
            saveScript=dataSetting.getSaveScript();
            if(entSettings!=null){
                nodeMap = entSettings.stream().collect(Collectors.toMap(p->p.getAlias() , p -> p));
                isDataSetting=true;
            }
        }

        for(Map.Entry<String, Object> ent :formData.entrySet()){
            String form=ent.getKey();
            JSONObject formJson=(JSONObject)ent.getValue();
            //{alias:{boAlias:"",entMap:{aaa:"main"}}}
            JSONObject boJsonEnt=jsonMap.getJSONObject(form);
            String boAlias=boJsonEnt.getString("boAlias");

            JSONObject entMap=boJsonEnt.getJSONObject("entMap");
            Set<Map.Entry<String, Object>> ents =  entMap.entrySet();
            //entMap
            if(isDataSetting){
                for( Map.Entry<String, Object> entry:ents){
                    String alias=entry.getKey();
                    //main,onotomany,onetone
                    String type= (String) entry.getValue();

                    EntSetting entSetting=nodeMap.get(alias);
                    handDataBySetting(entSetting,(JSONObject)ent.getValue(),type,false);
                }
            }

            rtn.put(boAlias,formJson);

            //执行保存脚本。
            if(StringUtils.isNotEmpty(saveScript)){
                groovyEngine.executeScripts(saveScript,rtn);
            }

        }
        return  rtn;
    }

    /**
     * 根据流程实例获取表单数据。
     * @param instId
     * @return
     */
    public JSONObject getDataByInstId(String instId){
        List<BpmInstData> bpmInstDataList= bpmInstDataService.getByInstId(instId);
        if(BeanUtil.isEmpty(bpmInstDataList)){
            return null;
        }
        //{boAlias1:pk1,boAlias2:pk2}
        JSONObject keyPkMap=new JSONObject();
        for(BpmInstData instData:bpmInstDataList){
            keyPkMap.put(instData.getBodefAlias(),instData.getPk());
        }
        //jsonObject 数据结构 {boAlias:json}
        JSONObject jsonObject= formClient.getByBpmInstData(keyPkMap);
        return  jsonObject;
    }


    /**
     * 处理表单数据。
     * @param cmd
     */
    public List<BpmInstData> handFormData(IExecutionCmd cmd, DataSetting dataSetting,String operate){
        //如果表单数据为空，那么重新在数据库获取数据，并把这个数据放到boDataMap中。
        if(BeanUtil.isEmpty( cmd.getFormData())){
            BpmInst bpmInst= (BpmInst) cmd.getTransientVar(BpmConst.BPM_INST);

            JSONObject jsonObject=getDataByInstId(bpmInst.getInstId());
            if(jsonObject!=null){
                cmd.setBoDataMap(jsonObject);
            }

            return null;
        }
        //将表单数据转换成BO映射进行存储。
        JSONObject boDataMap= handBySetting(cmd.getFormData(),dataSetting);
        cmd.setBoDataMap(boDataMap);

        /**
         * 获取公式。
         */
        String formulas=getFormulas(cmd);

        /**
         * 设置表单状态
         */
        setFormStatus(cmd);

        JSONObject data=new JSONObject();
        data.put("formData",cmd.getFormData());
        data.put("formulas",formulas);
        data.put("op",operate);
        data.put("opinion",cmd.getCheckType());



        JsonResult<List<DataResult>> result= formClient.saveFormData(data);
        if(!result.isSuccess()){
            MessageUtil.triggerException("表单数据保存出错!",result.getMessage());
            return Collections.EMPTY_LIST;
        }
        List<DataResult> dataResults=result.getData();
        List<BpmInstData> bpmInstDataList=new ArrayList<>();
        for(DataResult dataResult:dataResults){
            BpmInstData bpmInstData=getByDataResult(dataResult,operate);
            if(bpmInstData==null){
                continue;
            }

            int count=bpmInstDataService.getCountByInstId(cmd.getInstId(),bpmInstData.getBodefAlias());

            if(count>0){
                MessageUtil.triggerException("表单数据重复提交","表单数据重复提交,请检查表单提交的数据!");
            }

            bpmInstData.setInstId(cmd.getInstId());
            bpmInstDataList.add(bpmInstData);
            bpmInstDataService.insert(bpmInstData);
        }
        return  bpmInstDataList;
    }

    /**
     * 获取表间公式。
     * @param cmd
     * @return
     */
    private String getFormulas(IExecutionCmd cmd){
        String formulas="";
        ProcessNextCmd nextCmd=null;
        if(cmd instanceof ProcessNextCmd){
            nextCmd=(ProcessNextCmd) cmd;
        }
        String defId=cmd.getDefId();
        //在任务审批中保存时
        if(StringUtils.isEmpty(defId) && nextCmd!=null && StringUtils.isNotEmpty(nextCmd.getTaskId())){
            BpmTask bpmTask=bpmTaskService.get(nextCmd.getTaskId());
            defId=bpmTask.getDefId();
        }else if(StringUtils.isEmpty(defId) && StringUtils.isNotEmpty(cmd.getInstId())){//在流程实例明细中保存
            BpmInst bpmInst=bpmInstService.get(cmd.getInstId());
            defId=bpmInst.getDefId();
        }
        BpmDef bpmDef=bpmDefService.get(defId);
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmDef.getActDefId());
        if(cmd instanceof ProcessNextCmd){
            BpmTask task=bpmTaskService.get(nextCmd.getTaskId());
            NodeConfig nodeConfig=bpmDefService.getNodeConfig(bpmDef.getActDefId(),task.getKey());
            if(nodeConfig instanceof UserTaskConfig){
                //节点表间公式
                UserTaskConfig userTaskConfig=(UserTaskConfig)nodeConfig;
                formulas=userTaskConfig.getTableFormula().getValue();
            }
        }
        //全局表间公式
        if(StringUtils.isEmpty(formulas)){
            formulas=processConfig.getTableFormula().getValue();
        }
        return formulas;
    }

    private BpmInstData getByDataResult(DataResult result,String operate){
        if(DataResult.ACTION_UPD.equals( result.getAction()) && !ProcessStartCmd.OPERATE_LIVE.equals(operate)) {
            return  null;
        }
        BpmInstData bpmInstData=new BpmInstData();
        bpmInstData.setId(IdGenerator.getIdStr());
        bpmInstData.setBodefAlias(result.getBoAlias());
        bpmInstData.setPk(result.getPk());
        return bpmInstData;
    }

    public void handDataSetting(List<BpmView> list, DataSetting dataSetting){
        if(dataSetting==null){
            return;
        }
        List<EntSetting> entSettings = dataSetting.getFieldSetting();
        if(BeanUtil.isEmpty(entSettings)){
            return;
        }
        //实体名称对应EntSetting
        Map<String, EntSetting> nodeMap = entSettings.stream().collect(Collectors.toMap(p->p.getAlias() , p -> p));

        JSONObject rtn=new JSONObject();

        for (BpmView bpmView:list){
            JSONObject json= bpmView.getData();
            Map<String,String> entMap= bpmView.getEntMap();
            Set<Map.Entry<String, String>> ents = entMap.entrySet();
            for( Map.Entry<String, String> ent:ents){
                String key=ent.getKey();
                String val=ent.getValue();
                EntSetting entSetting=nodeMap.get(key);
                handDataBySetting(entSetting,json,val,true);
            }
            rtn.put(bpmView.getBoAlias(),json);
        }
        String initScript=dataSetting.getInitScript();
        if(StringUtils.isNotEmpty(initScript)){
            groovyEngine.executeScripts(initScript,rtn);
        }
    }

    private void  handDataBySetting(EntSetting entSetting,JSONObject json,String type,boolean isInit){
        if(entSetting==null){
            return;
        }
        List<AttrSetting> attrData = entSetting.getAttrData();

        JSONObject curJson=null;
        if(BoRelation.RELATION_MAIN.equals(type)){
            curJson=json;
        }
        else if(BoRelation.RELATION_ONETOONE.equals(type)){
            curJson=json.getJSONObject(FormConst.SUB_PRE +entSetting.getAlias());
        }
        else {
            curJson=json.getJSONObject(FormConst.INITDATA ).getJSONObject(entSetting.getAlias());
        }

        for(AttrSetting setting:attrData){
            FieldSetting fieldSetting =(isInit)? setting.getInitSet():setting.getSaveSet();
            handDataByFieldSetting(setting.getAlias(), fieldSetting,curJson,json);
        }

    }

    /**
     *  var valTypeList = [{id: 'constant', text: '常量'}, {id: 'script', text: '脚本'}, {
     *         id: 'manual',
     *         text: '固定值'
     *     }, {id: 'opinion', text: '审批意见'}, {id: 'sysSeqId', text: '系统流水号'}];
     * @param fieldSetting
     * @param json
     */
    private void handDataByFieldSetting(String alias, FieldSetting fieldSetting,JSONObject curJson,JSONObject json){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        String type=fieldSetting.getValType();
        String value=fieldSetting.getVal();
        if(StringUtils.isEmpty(value) && !"opinion".equals(type)){
            return;
        }

        String val="";
        switch (type){
            case "constant":
                Object obj=constVarContext.getValByKey(value,new HashMap<>());
                if(obj!=null) {
                    val = String.valueOf(obj);
                }
                break;
            case "script":
                val=getByScript(value,json);
                break;
            case "opinion":
                val=cmd.getOpinion();
                break;
            case "manual":
                val=fieldSetting.getVal();
                break;
            case "sysSeqId":
                val=systemClient.genSeqNo(value);
                break;
        }
        curJson.put(alias,val);

    }


    private String getByScript(String script, JSONObject json){
        Map<String,Object> params=new HashMap<>();
        params.put("data",json);
        return (String) groovyEngine.executeScripts(script,params);
    }


    /**
     *
     * @param cmd
     */
    private void setFormStatus(IExecutionCmd cmd){
        JSONObject formData = cmd.getFormData();
        if(BeanUtil.isEmpty(formData)){
            return;
        }
        String formStatus ="";
        AbstractNodeConfig config=null;
        //取节点配置
        UserTaskConfig userTaskConfig = (UserTaskConfig) cmd.getTransientVar(BpmConst.USERTASK_CONFIG);
        if(BeanUtil.isEmpty(userTaskConfig) || StringUtils.isEmpty(userTaskConfig.getFormStatus()) ){
            //取全局配置
            ProcessConfig processConfig= (ProcessConfig) cmd.getTransientVar(BpmConst.PROCESS_CONFIG);
             if(BeanUtil.isEmpty(processConfig)){
                 return;
             }
            formStatus=processConfig.getFormStatus();
            config=processConfig;
        }else {
            config=userTaskConfig;
            formStatus = userTaskConfig.getFormStatus();
        }
        if(StringUtils.isEmpty(formStatus)){
            formStatus="default";
        }
        IFormStatus iFormStatus = FormStatusContext.getConditionByType(formStatus);
        String status = iFormStatus.getStatus(config, cmd);
        Set<String> keys = formData.keySet();
        for(String key:keys){
            JSONObject json=formData.getJSONObject(key);
            json.put(FormBoEntityDto.FIELD_INST_STATUS_,status);
        }
    }
}
