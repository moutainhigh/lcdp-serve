package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.*;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.dto.bpm.BpmInstDto;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.feign.org.UserClient;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmInst")
@ClassDefine(title = "流程实例",alias = "bpmInstController",path = "/bpm/core/bpmInst",packages = "core",packageName = "流程管理")
@Api(tags = "流程实例")
public class BpmInstController extends BaseController<BpmInst> {

    private static List<String> typeList=new ArrayList<>();

    private static String TYPE_NEWDOC="newDoc";
    private static String TYPE_DRAFT="draft";
    private static String TYPE_OPENDOC="openDoc";

    static  {
        typeList.add(TYPE_NEWDOC);
        typeList.add(TYPE_DRAFT);
        typeList.add(TYPE_OPENDOC);

    }

    @Autowired
    BpmInstServiceImpl bpmInstService;
    @Autowired
    BpmDefService bpmDefService;
    @Autowired
    BpmInstDataServiceImpl bpmInstDataService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    UserClient userClient;

    @Resource
    RuntimeService runtimeService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    ActRepService actRepService;

    @Resource
    TaskExecutorService taskExecutorService;

    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    ActInstService actInstService;
    @Resource
    BpmInterposeSeviceImpl bpmInterposeSevice;
    @Resource
    MessageService messageService;
    @Resource
    BpmTakeBackService bpmTakeBackService;


    @Override
    public BaseService getBaseService() {
        return bpmInstService;
    }

    @Override
    public String getComment() {
        return "流程实例";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        //增加分类过滤
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","i.TREE_ID_","BPM","inst.read");

        filter.addParam(CommonConstant.TENANT_PREFIX,"i.");
        filter.addParam(CommonConstant.COMPANY_PREFIX,"i.");
        filter.addParam(CommonConstant.DELETED_PREFIX,"i.");

        super.handleFilter(filter);
    }

    /**
     * 获得所有查询数据列表，不传入条件时，则返回所有的记录
     * @return
     * @throws Exception
     */
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @PostMapping(value="/query")
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{

        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        Map<String, String> params = queryData.getParams();
        String tableId= params.get("tableId");
        params.remove("tableId");
        QueryFilter filter=QueryFilterBuilder.createQueryFilter(queryData);
        handleFilter(filter);
        IPage page;
        if(StringUtils.isNotEmpty(tableId)){
             page=bpmInstService.queryByArchiveLog(filter,tableId);
        }else {
             page= getBaseService().query(filter);
        }
        handlePage(page);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "触发流程往下执行", path = "/trigger", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程事件ID", varName = "executionId"),@ParamDefine(title = "跳转类型", varName = "jumpType"),@ParamDefine(title = "意见", varName = "opinion")})
    @ApiOperation("触发流程往下执行")
    @AuditLog(operation = "触发流程往下执行")
    @PostMapping("trigger")
    public JsonResult trigger(@ApiParam @RequestParam(value="executionId") String executionId,
                        @ApiParam @RequestParam(value="jumpType")String jumpType,
                        @ApiParam @RequestParam(value="opinion")String opinion){

        try{
            actInstService.trigger(executionId,jumpType,opinion);
            return JsonResult.Success("执行流程等待任务成功!");
        }
        catch (Exception ex){
            MessageUtil.triggerException("执行流程等待任务失败!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    @MethodDefine(title = "获取流程实例中的所有变量", path = "/getStatusByBusKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程实例ID", varName = "defIds"),@ParamDefine(title = "业务主键", varName = "pk")})
    @ApiOperation("获取流程实例中的所有变量")
    @GetMapping("getStatusByBusKey")
    public JsonResult getStatusByBusKey(@ApiParam @RequestParam(value="defIds") String defIds,
                                              @ApiParam @RequestParam(value="pk")String pk) {
        List<String> defIdAry=Arrays.asList(defIds.split(","));
        List<BpmInst> bpmInsts= bpmInstService.getByBusKey(defIdAry,pk);
        if(BeanUtil.isEmpty(bpmInsts)){
            return JsonResult.Success();
        }
        JsonResult result=JsonResult.Success();
        List<String> instStatus=new ArrayList<>();
        for(BpmInst bpmInst:bpmInsts){
            instStatus.add(bpmInst.getStatus());
        }
        result.setData(StringUtils.join(instStatus,","));
        return result;
    }


    /**
     * 显示流程实例中的流程变量值
     *
     * @param actInstId
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "获取流程实例中的所有变量", path = "/listVars", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "ACT流程实例ID", varName = "actInstId")})
    @ApiOperation("获取流程实例中的所有变量")
    @PostMapping("listVars")
    public List<ActVarInst> listVars(@ApiParam @RequestParam("actInstId") String actInstId)
            throws Exception {
        Map<String, Object> varMaps = runtimeService.getVariables(actInstId);
        List<ActVarInst> varInstList = new ArrayList<ActVarInst>();
        Iterator<String> varKeyIt = varMaps.keySet().iterator();
        while (varKeyIt.hasNext()) {
            String key = varKeyIt.next();
            Object val = (Object) varMaps.get(key);
            String clz = null;
            if (val != null) {
                clz = val.getClass().getName();
            }
            if (!"java.util.HashMap".equals(clz)) {
                ActVarInst inst = new ActVarInst(key, clz, val);
                varInstList.add(inst);
            }
        }
        return  varInstList;
    }

    @MethodDefine(title = "删除流程变量", path = "/delVar", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "ACT流程实例ID", varName = "actInstId"),@ParamDefine(title = "变量KEY", varName = "varKey")})
    @ApiOperation("删除流程变量")
    @PostMapping("delVar")
    public JsonResult delVar(@ApiParam @RequestParam("actInstId") String actInstId,@ApiParam @RequestParam("varKey") String varKey){
        runtimeService.removeVariable(actInstId,varKey);
        return  new JsonResult(true,"成功删除变量！");
    }


    /**
     * 保存与添加变量行值
     * @param actInstId
     * @param data
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "保存与添加变量行值", path = "/saveVarRow", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "ACT流程实例ID", varName = "actInstId"),@ParamDefine(title = "变量值", varName = "data")})
    @ApiOperation("保存与添加变量行值")
    @PostMapping("saveVarRow")
    public JsonResult saveVarRow(@ApiParam @RequestParam("actInstId") String actInstId,
                                 @ApiParam @RequestParam("data") String data) throws Exception {
        try {
            ActVarInst var = JSON.parseObject(data, ActVarInst.class);
            Object val = null;
            if ("java.util.Date".equals(var.getType())) {
                val = DateUtils.parseDate((String) var.getVal());
            } else if ("java.lang.Double".equals(var.getType())) {
                val = new Double(var.getVal().toString());
            } else {
                val = var.getVal();
            }
            runtimeService.setVariable(actInstId, var.getKey(), val);
        } catch (Exception ex) {
            MessageUtil.triggerException("保存与添加变量行值失败!",ExceptionUtil.getExceptionMessage(ex));
        }

        return new JsonResult(true, "成功保存！");
    }

    /**
     * 查询我的流程实例列表
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询我的流程实例列表", path = "/myBpmInsts", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="查询我的流程实例列表", notes="查询我的流程实例列表")
    @PostMapping(value="/myBpmInsts")
    public JsonPageResult myBpmInsts(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",userId);
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","i.TREE_ID_","BPM","inst.read");

        filter.addParam(CommonConstant.TENANT_PREFIX,"i.");
        WebUtil.handFilter(filter, ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage page= bpmInstService.getByUserId(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }



    /**
     * 查询我的已经办的流程实例
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询我的已经办的流程实例", path = "/getMyApprovedInsts", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="查询我的已经办的流程实例", notes="查询我的已经办的流程实例")
    @PostMapping(value="/getMyApprovedInsts")
    public JsonPageResult getMyApprovedInsts(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",userId);
        filter.addQueryParam("Q_TENANT_ID__S_EQ",ContextUtil.getCurrentTenantId());
        IPage page= bpmInstService.getMyApproved(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "查询我的已经办的流程实例", path = "/getMyApproved", method = HttpMethodConstants.POST,
            params = {
                    @ParamDefine(title = "流程定义KEY", varName = "defKey"),
                    @ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="查询我的已经办的流程实例", notes="查询我的已经办的流程实例")
    @PostMapping(value="/getMyApproved/{defKey}")
    public JsonPageResult getMyApproved(@PathVariable(name = "defKey")String defKey,
                                        @RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",userId);
        filter.addParam("defKey",defKey);
        IPage page= bpmInstService.getMyApproved(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }
    /**
     * 查询我的已经办的流程实例数
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询我的已经办的流程实例数", path = "/getMyApprovedInstCount", method = HttpMethodConstants.GET)
    @ApiOperation(value="查询我的已经办的流程实例数")
    @GetMapping(value="/getMyApprovedInstCount")
    public JsonResult getMyApprovedInstCount() throws Exception{
        Integer counts= bpmInstService.getMyApprovedCount(ContextUtil.getCurrentUserId(),ContextUtil.getCurrentTenantId());
        return new JsonResult(true,counts,"");
    }


    /**
     * 查询我的流程实例列表
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询我的流程实例列表", path = "/myBpmDraftInsts", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="查询我的流程实例列表", notes="查询我的流程实例列表")
    @PostMapping(value="/myBpmDraftInsts")
    public JsonPageResult myBpmDraftInsts(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",userId);
        filter.addQueryParam("Q_i.STATUS__S_EQ",BpmInstStatus.DRAFTED.name());
        IPage page= bpmInstService.getByUserId(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }




    /**
     * 流程启动
     * @param startCmd
     * @return
     */
    @MethodDefine(title = "流程启动", path = "/startProcess", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "启动参数", varName = "startCmd")})
    @ApiOperation(value = "流程启动")
    @AuditLog(operation = "流程启动")
    @PostMapping("startProcess")
    public JsonResult<BpmInst> startProcess(@ApiParam @RequestBody ProcessStartCmd startCmd) throws Exception {
        JsonResult jsonResult=handReq(startCmd);
        if(!jsonResult.isSuccess()){
            return  jsonResult;
        }
        BpmDef bpmDef= (BpmDef) jsonResult.getData();
        boolean hasRight=systemClient.findAuthRight("BPM","def.start",bpmDef.getTreeId());
        if(!hasRight){
            return JsonResult.Fail("你没有启动权限！");
        }

        String operate=startCmd.isHasPk() ? ProcessStartCmd.OPERATE_LIVE: ProcessStartCmd.OPERATE_START;
        try {
            //启动流程
            BpmInst bpmInst = bpmInstService.doStartProcess(startCmd, bpmDef, operate);
            //发送任务消息通知
            messageService.sendMsg();
            return jsonResult.setData(bpmInst)
                    .setSuccess(true).setMessage("成功启动流程！");
        }catch (Exception e){
            bpmInstService.handStartException(e);
            return null;
        }
    }



    /**
     * 处理流程发起的以获取流程定义实体
     * @param cmd
     * @return
     */
    private JsonResult handReq(ProcessStartCmd cmd){
        JsonResult jsonResult=new JsonResult();
        //检查参数
        if(StringUtils.isEmpty(cmd.getDefId())
                && StringUtils.isEmpty(cmd.getDefKey())){
            return jsonResult.setSuccess(false).setMessage("请传参数defId或defKey");
        }
        BpmDef bpmDef=null;
        if(StringUtils.isNotEmpty(cmd.getDefId())) {
            bpmDef=bpmDefService.get(cmd.getDefId());
        }else if(StringUtils.isNotEmpty(cmd.getDefKey())){
            bpmDef=bpmDefService.getMainByKey(cmd.getDefKey());
        }
        //没有找到流程定义并且流程实例为空。
        if(bpmDef==null && StringUtils.isEmpty( cmd.getInstId())){
            return jsonResult.setSuccess(false).setMessage("流程定义不存在!");
        }
        cmd.setDefId(bpmDef.getDefId());
        cmd.setActDefId(bpmDef.getActDefId());
        cmd.setDefKey(bpmDef.getKey());

        return jsonResult.setData(bpmDef);
    }

    /**
     * 保存草稿
     * @param startCmd
     * @return
     */
    @ApiOperation(value = "保存草稿")
    @AuditLog(operation = "保存草稿")
    @PostMapping("saveDraft")
    public JsonResult<BpmInst> saveDraft(@ApiParam @RequestBody ProcessStartCmd startCmd){
        JsonResult jsonResult=handReq(startCmd);

        if(!jsonResult.isSuccess()){
            return  jsonResult;
        }
        BpmDef bpmDef= (BpmDef) jsonResult.getData();
        //启动流程
        BpmInst bpmInst=bpmInstService.doSaveDraft(startCmd,bpmDef);


        return jsonResult.setData(bpmInst)
                .setShow(false)
                .setSuccess(true)
                .setMessage("成功保存草稿！");
    }


    /**
     * 根据实例ID获取实例数据。
     * @param instId
     * @return
     */
    @MethodDefine(title = "根据实例ID获取实例数据", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation(value = "根据实例ID获取实例数据")
    @GetMapping("/getById")
    public BpmInstDto getById(@ApiParam @RequestParam(value = "instId") String instId) {
        BpmInst bpmInst = bpmInstService.getById(instId);
        BpmInstDto bpmInstDto = new BpmInstDto();
        if(BeanUtil.isNotEmpty(bpmInst)) {
            BeanUtil.copyProperties(bpmInstDto, bpmInst);
        }
        return bpmInstDto;
    }

    /**
     * 根据流程实例ID获取变量数据
     * @param actInstId
     * @return
     */
    @MethodDefine(title = "根据流程实例ID获取变量数据", path = "/getVariables", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "actInstId")})
    @ApiOperation(value = "根据流程实例ID获取变量数据")
    @GetMapping("/getVariables")
    public Map<String,Object> getVariables(@ApiParam @RequestParam(value = "actInstId") String actInstId){
        return bpmInstService.getVariables(actInstId);
    }

    /**
     * 根据流程实例Id获取变量数据
     * @param instId
     * @return
     */
    @MethodDefine(title = "根据流程实例Id获取变量数据", path = "/getVariablesByInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation(value = "根据流程实例Id获取变量数据")
    @GetMapping("/getVariablesByInstId")
    public Map<String,Object> getVariablesByInstId(@ApiParam @RequestParam(value = "instId") String instId){
        BpmInst bpmInst=bpmInstService.get(instId);
        String actInstId=null;
        if(BeanUtil.isNotEmpty(bpmInst)){
            actInstId=bpmInst.getActInstId();
        }
        return bpmInstService.getVariables(actInstId);
    }
    /**
     * 更新流程实例状态
     * @param instId
     * @param status
     * @return
     */
    @MethodDefine(title = "更新流程实例状态", path = "/updateProcessStatus", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId"),@ParamDefine(title = "状态", varName = "status")})
    @ApiOperation(value = "更新流程实例状态")
    @PostMapping("updateProcessStatus")
    public JsonResult updateProcessStatus(@ApiParam @RequestParam(value="instId") String instId,@ApiParam @RequestParam(value="status") String status){
        bpmInstService.updateStatusByInstId(instId,status);
        return new JsonResult(true,"成功更新流程状态！");
    }

    /**
     * 作废流程实例
     * @param instId
     * @param reason
     * @return
     */
    @MethodDefine(title = "作废流程实例", path = "/cancelProcess", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId"),
                    @ParamDefine(title = "原因", varName = "reason"),
                    @ParamDefine(title = "附件", varName = "opFiles")})
    @ApiOperation(value = "作废流程实例")
    @PostMapping("cancelProcess")
    public JsonResult cancelProcess(@ApiParam @RequestParam(value="instId") String instId,
                                    @ApiParam @RequestParam(value="reason") String reason,
                                    @ApiParam @RequestParam(value="opFiles" ,required = false) String opFiles){
        bpmInstService.cancelProcess(instId,reason,opFiles);
        return new JsonResult(true,"成功更新流程状态！");
    }

    /**
     * 删除流程实例
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除流程实例")
    @PostMapping("del")
    @Override
    public JsonResult del(@ApiParam @RequestParam(value="ids") String ids){
        String[] idArr=ids.split("[,]");
        List<String> idList=Arrays.asList(idArr);
        bpmInstService.delete((List)idList);
        return new JsonResult(true,"删除流程实例数据成功！");
    }

    /**
     * 删除备份的流程实例
     * @param ids
     * @return
     */
    @MethodDefine(title = "删除备份的流程实例", path = "/delArchive", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID列表", varName = "ids")})
    @ApiOperation(value = "删除备份的流程实例")
    @PostMapping("delArchive")
    public JsonResult delArchive(@ApiParam @RequestParam(value="ids") String ids){
        String[] idArr=ids.split("[,]");
        List<String> idList=Arrays.asList(idArr);
        bpmInstService.delArchive(idList);
        return new JsonResult(true,"删除备份数据成功！");
    }

    @MethodDefine(title = "获取流程定义配置", path = "/getProcessConfig", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation(value = "获取流程定义配置")
    @GetMapping("getProcessConfig")
    public JsonResult getProcessConfig(@RequestParam(value="defId") String defId){
        BpmDef bpmDef=bpmDefService.getById(defId);
        JsonResult result=getProcessConfig(bpmDef);
        return result;
    }



    /**
     * 根据流程定义获取流程级的配置，返回PrcocessConfig对象至JsonResult对象中。
     * @param bpmDef
     * @return
     */
    private JsonResult getProcessConfig(BpmDef bpmDef){
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmDef.getActDefId());
        processConfig.setDefId(bpmDef.getDefId());
        List<String> startOptions= processConfig.getStartNodeOptions();
        boolean skipFirstNode= startOptions.contains("skipFirstNode");
        if(skipFirstNode){
            boolean allowSelectPath= actRepService.allowStartSelectPath(bpmDef.getActDefId());
            processConfig.setAllowSelectPath(allowSelectPath);
        }
        return JsonResult.getSuccessResult(processConfig);
    }

    @MethodDefine(title = "根据KEY获取流程定义配置", path = "/getProcessConfigByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defKey")})
    @ApiOperation(value = "根据KEY获取流程定义配置")
    @GetMapping("getProcessConfigByKey")
    public JsonResult getProcessConfigByKey(@RequestParam(value="defKey") String defKey){
        BpmDef bpmDef=bpmDefService.getMainByKey(defKey);
        JsonResult result=getProcessConfig(bpmDef);
        return result;
    }


    @MethodDefine(title = "获取流程定义配置", path = "/getInstIdProcessConfig", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程实例ID", varName = "instId")})
    @ApiOperation(value = "获取流程定义配置")
    @GetMapping("getInstIdProcessConfig")
    public JsonResult getInstIdProcessConfig(@RequestParam(value="instId") String instId){
        BpmInst bpmInst = bpmInstService.getById(instId);
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmInst.getActDefId());
        List<String> startOptions= processConfig.getStartNodeOptions();
        boolean skipFirstNode= startOptions.contains("skipFirstNode");
        if(skipFirstNode){
            boolean allowSelectPath= actRepService.allowStartSelectPath(bpmInst.getActDefId());
            processConfig.setAllowSelectPath(allowSelectPath);
        }
        return JsonResult.getSuccessResult(processConfig);
    }

    @MethodDefine(title = "根据流程定义ID返回表单数据", path = "/getViewByDefId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId"),@ParamDefine(title = "实例ID", varName = "instId"),@ParamDefine(title = "是否手机表单", varName = "isMobile")})
    @ApiOperation(value = "根据流程定义ID返回表单数据")
    @PostMapping("getViewByDefId")
    public JsonResult getViewByDefId(@RequestParam(value="defId") String defId,
                                     @RequestParam(value="instId",required = false) String instId,
                                     @RequestParam(value="isMobile",required = false) String isMobile,
                                     @RequestParam(value="pkId",required = false) String pkId){
        BpmDef bpmDef=bpmDefService.getById(defId);
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmDef.getActDefId());
        if (BeanUtil.isEmpty(processConfig)){
            return new JsonResult(false,"根据流程定义ID返回表单数据为空！");
        }
        FormConfig formConfig= getForms(processConfig);
        DataSetting dataSetting= processConfig.getDataSetting();

        BpmInst bpmInst=null;

        JSONObject json=new JSONObject();

        if(StringUtils.isNotEmpty(instId)){
            bpmInst =bpmInstService.get(instId);
            json.put("bpmInst",bpmInst);
        }
        JsonResult result= formDataService.getByInstId(dataSetting,processConfig.getBoDefs().getValue(),formConfig,bpmInst,null,isMobile,false,pkId);
        if(result.isSuccess()){
            json.put("formData",result.getData());
        }

        return result.setData(json);
    }

    /**
     * 根据流程级配置获取单据配置
     * @param processConfig
     * @return
     */
    private FormConfig getForms(ProcessConfig processConfig){
        if (BeanUtil.isEmpty(processConfig) ){
            return null;
        }
        //获取全局的配置
        FormConfig globalForm= processConfig.getGlobalForm();

        //获取开始的配置
        FormConfig config= processConfig.getStartForm();
        if(BeanUtil.isNotEmpty(config) && BeanUtil.isNotEmpty(config.getFormpc())){
            FormConfig returnConfig=null;
            try{
                returnConfig= (FormConfig) FileUtil.cloneObject(config);
            }
            catch (Exception ex){
                log.error("BpmInstController.FormConfig is error ---:"+ExceptionUtil.getExceptionMessage(ex));
            }

            List<Form> formpcs = returnConfig.getFormpc();
            List<Form> newFormPcs = new ArrayList<>();
            for (Form form:formpcs) {
                String permission = form.getPermission();
                Form newFormpc=null;
                if(StringUtils.isEmpty(permission) || "{}".endsWith(permission)){
                    newFormpc = getFormByGlobalForm(globalForm,form.getAlias());
                }
                if (BeanUtil.isNotEmpty(newFormpc)){
                    newFormPcs.add(newFormpc);
                    continue;
                }
                newFormPcs.add(form);
            }
            returnConfig.setFormpc(newFormPcs);
            return returnConfig;
        }
        if(BeanUtil.isNotEmpty(globalForm) && BeanUtil.isNotEmpty(globalForm.getFormpc())){
            return globalForm;
        }
        return null;
    }

    /**
     * 按单据别名与配置获取全局表单
     * @param globalForm
     * @param alias
     * @return
     */
    private  Form getFormByGlobalForm(FormConfig globalForm,String alias){
        Form newFormpc =null;
        if (BeanUtil.isEmpty(globalForm)){
            return newFormpc;
        }
        List<Form> formpcs = globalForm.getFormpc();
        if (BeanUtil.isEmpty(formpcs)){
            return newFormpc;
        }
        for (Form form:formpcs) {
            if (alias.equals(form.getAlias())){
                Form returmForm=null;
                try{
                    returmForm= (Form) FileUtil.cloneObject(form);
                }
                catch (Exception ex){
                    log.error("BpmInstController.getFormByGlobalForm is error ---:"+ExceptionUtil.getExceptionMessage(ex));
                }
                returmForm.setReadOnly(true);
                return returmForm;
            }
        }
        return newFormpc;
    }

    /**
     * 获取实例明细
     * @param instId
     * @return
     */
    @MethodDefine(title = "获取实例明细（编辑）", path = "/getInstDetail", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId"),@ParamDefine(title = "是否手机表单", varName = "isMobile")})
    @ApiOperation("获取实例明细（编辑）")
    @GetMapping("getInstDetail")
    public BpmInstDetail getInstDetail(@RequestParam(value = "instId") String instId,
                                       @RequestParam(value ="isMobile", required = false,defaultValue = "NO") String isMobile,
                                       @RequestParam(value ="from", required = false,defaultValue = "") String from){
        BpmInstDetail detail=bpmInstService.getInstDetail(instId,isMobile,false,from);
        return detail;
    }

    /**
     * 获取实例明细
     * @param instId
     * @return
     */
    @MethodDefine(title = "获取实例明细（只读）", path = "/getInstDetailForInterpose", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId"),@ParamDefine(title = "是否手机表单", varName = "isMobile")})
    @ApiOperation("获取实例明细（只读）")
    @GetMapping("getInstDetailForInterpose")
    public BpmInstDetail getInstDetailForInterpose(@RequestParam(value = "instId") String instId,@RequestParam(value ="isMobile",required = false,defaultValue = "NO") String isMobile) {
        BpmInstDetail detail = bpmInstService.getInstDetail(instId, isMobile, true,"");
        return detail;
    }

    /**
     * 获取所有的流程节点及节点对应的人员
     * @param actInstId
     * @return
     */
    @MethodDefine(title = "通过实例Id获取所有的任务节点执行人员列表", path = "/getTaskNodeUsers", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "actInstId")})
    @ApiOperation("通过实例Id获取所有的任务节点执行人员列表")
    @GetMapping("getTaskNodeUsers")
    public List getTaskNodeUsers(@ApiParam("actInstId") @RequestParam("actInstId") String actInstId,
                                 @ApiParam("isGetEnd") @RequestParam(value = "isGetEnd",required = false) String isGetEnd){
        List<TaskNodeUser> taskNodeUserList=new ArrayList<>();
        BpmInst bpmInst=bpmInstService.getByActInstId(actInstId);
        // 获得当前的待办任务
        List<BpmTask> curTasks = bpmTaskService.getByActInstId(actInstId);
        //取得流程的所有人工节点
        Collection<FlowNode> userNodes=null;
        if (MBoolean.YES.val.equals(isGetEnd)){
            userNodes=actRepService.getUserNodes(bpmInst.getActDefId(),true);
        }else {
            userNodes=actRepService.getUserNodes(bpmInst.getActDefId());
        }

        //取得到之前在流程任务节点中增加的所有的节点人员Id映射
        String nodeUserIds=(String)runtimeService.getVariable(actInstId,BpmInstVars.NODE_USER_IDS.getKey());
        Map<String,Object> nodeUserIdMap=null;
        if(StringUtils.isNotEmpty(nodeUserIds)){
            nodeUserIdMap = JSON.parseObject(nodeUserIds).getInnerMap();
        }

        //表单数据
        JSONObject boDataMap= getBoDataMap(bpmInst);
        IExecutionCmd cmd=new ProcessStartCmd();
        cmd.setBoDataMap(boDataMap);
        ProcessHandleUtil.setProcessCmd(cmd);

        //查找流程所有的节点，并且把审批中的、未审批的、已审批的节点的人员计算全部计算出来
        for (FlowNode flowNode : userNodes) {
            String nodeId=flowNode.getId();
            TaskNodeUser taskNodeUser = new TaskNodeUser(flowNode.getId(),flowNode.getName());
            //加入结束节点。
            if(MBoolean.YES.val.equals(isGetEnd) && flowNode instanceof EndEvent){
                taskNodeUser.setEndNode(true);
                if(StringUtils.isEmpty(taskNodeUser.getNodeText())){
                    taskNodeUser.setNodeText("结束节点");
                }
                taskNodeUserList.add(taskNodeUser);
                continue;
            }


            // 1 查找已经审批的
            handApproved(bpmInst,taskNodeUser,nodeId);
            // 2 从正在运行中的流程任务实例中获得执行人
            handRunning(curTasks,taskNodeUser,nodeId);
            // 3 从流程变量中获得其人员列表
            handInstInitUser(nodeUserIdMap,taskNodeUser,nodeId);
            //4 从流程配置文件中获取人员信息
            handConfigUser(taskNodeUser,bpmInst,nodeId);

            taskNodeUserList.add(taskNodeUser);
        }
        ProcessHandleUtil.clearProcessCmd();
        return  taskNodeUserList;
    }

    /**
     * 处理已审批的人员
     * @param bpmInst
     * @param taskNodeUser
     * @param nodeId
     */
    private void handApproved(BpmInst bpmInst, TaskNodeUser taskNodeUser,String nodeId){
        List<BpmCheckHistory> bpmCheckHistories = bpmCheckHistoryService.getByInstIdNodeId(bpmInst.getInstId(), nodeId);
        if(BeanUtil.isEmpty(bpmCheckHistories)){
            return;
        }

        List<TaskExecutor> checkExecutors = new ArrayList<TaskExecutor>();
        Set<String> userIdSet = new HashSet<String>();
        for (BpmCheckHistory history : bpmCheckHistories) {
            if (StringUtils.isEmpty(history.getHandlerId())
                    || userIdSet.contains(history.getHandlerId())) {
                continue;
            }
            userIdSet.add(history.getHandlerId());
            IUser osUser = userClient.findByUserId(history.getHandlerId());
            if (osUser != null) {
                checkExecutors.add(TaskExecutor.getUser(osUser.getUserId(), osUser.getFullName(),osUser.getAccount()));
            }
        }
        taskNodeUser.setStatus(TaskNodeUser.STATUS_FINISH);
        taskNodeUser.getCheckExecutors().addAll(checkExecutors);

    }


    /**
     * 处理正在执行的人员
     * @param curTasks
     * @param taskNodeUser
     * @param nodeId
     */
    private void handRunning(List<BpmTask> curTasks,TaskNodeUser taskNodeUser,String nodeId){
        for (BpmTask task : curTasks) {
            if (task.getKey().equals(nodeId)) {
                Collection<TaskExecutor> taskExecutors = bpmTaskService.getTaskExecutors(task.getTaskId());
                taskNodeUser.getRunExecutors().addAll(taskExecutors);
                taskNodeUser.setStatus(TaskNodeUser.STATUS_RUNING);
            }
        }
    }

    /**
     * 处理流程实例初始指定人员
     * @param nodeUserIdMap
     * @param taskNodeUser
     * @param nodeId
     */
    private void handInstInitUser(Map<String,Object> nodeUserIdMap,TaskNodeUser taskNodeUser,String nodeId ){
        if(nodeUserIdMap==null || !nodeUserIdMap.containsKey(nodeId)){
            return;
        }

        Set<TaskExecutor> operatorExecutors = new HashSet<>();
        String userIds = (String) nodeUserIdMap.get(nodeId);
        if (StringUtils.isEmpty(userIds)) {
            return;
        }
        String[] uIds = userIds.split("[,]");
        for (String uId : uIds) {
            IUser osUser = userClient.findByUserId(uId);
            operatorExecutors.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
        }
        taskNodeUser.getOperateExecutors().addAll(operatorExecutors);


    }

    /**
     * 获取配置用户。
     * @param taskNodeUser
     * @param bpmInst
     * @param nodeId
     */
    private void  handConfigUser(TaskNodeUser taskNodeUser,BpmInst bpmInst,String nodeId){
        Map<String, Object> variables = runtimeService.getVariables(bpmInst.getActInstId());
        UserTaskConfig userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmInst.getActDefId(),nodeId);
        Set<TaskExecutor> taskExecutors = taskExecutorService.getTaskExecutors(userTaskConfig.getUserConfigs(), variables);
        taskNodeUser.getConfigExecutors().addAll(taskExecutors);
    }

    /**
     * 根据表单数据，将数据转换成 {boAlias:{表单JSON数据}}
     * @param bpmInst
     * @return
     */
    private JSONObject getBoDataMap(BpmInst bpmInst){

        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmInst.getActDefId());
        //获取单据的数据
        JsonResult dataResult=bpmInstService.getFormData( bpmInst.getInstId(), processConfig,"NO",false,true);

        Object formDatas = dataResult.getData();
        String formDataStr = JSONArray.toJSONString(formDatas);
        JSONArray formDataList = JSONArray.parseArray(formDataStr);

        JSONObject boDataMap=new JSONObject();
        for (Object obj:formDataList) {
            JSONObject formData =(JSONObject)obj;
            boDataMap.put(formData.getString("boAlias"),formData.getJSONObject("data"));
        }
        return boDataMap;
    }

    /**
     * 更新当前流程实例的当前用户
     * @param actInstId
     * @param nodeId
     * @param userIds
     * @return
     */
    @MethodDefine(title = "更新当前流程实例的当前用户", path = "/updateOperatorNodeUser", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "actInstId"),@ParamDefine(title = "节点ID", varName = "nodeId"),@ParamDefine(title = "用户ID列表", varName = "userIds")})
    @ApiOperation("更新当前流程实例的当前用户")
    @PostMapping("updateOperatorNodeUser")
    public JsonResult updateOperatorNodeUser(@ApiParam("actInstId") @RequestParam("actInstId") String actInstId,
                                             @ApiParam("nodeId") @RequestParam("nodeId") String nodeId,
                                             @ApiParam("userIds") @RequestParam("userIds") String userIds){
        //取得到之前在流程任务节点中增加的所有的节点人员Id映射
        String nodeUserIds=(String)runtimeService.getVariable(actInstId,BpmInstVars.NODE_USER_IDS.getKey());
        JSONObject nodeUserJson=null;
        if(StringUtils.isNotEmpty(nodeUserIds)){
            nodeUserJson = JSON.parseObject(nodeUserIds);
        }

        if(nodeUserJson==null){
            nodeUserJson=new JSONObject();
        }

        nodeUserJson.put(nodeId,userIds);

        runtimeService.setVariable(actInstId,BpmInstVars.NODE_USER_IDS.getKey(),nodeUserJson.toJSONString());

        return new JsonResult(true,"成功设置干预的节点用户！");
    }

    /**
     * 更新运行的节点用户
     * @param actInstId
     * @param nodeId
     * @param userIds
     * @return
     */
    @MethodDefine(title = "更新当前流程执行的实例用户", path = "/updateRunningNodeUser", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "actInstId"),@ParamDefine(title = "节点ID", varName = "nodeId"),@ParamDefine(title = "用户ID列表", varName = "userIds")})
    @ApiOperation("更新当前流程执行的实例用户")
    @AuditLog(operation = "更新当前流程执行的实例用户")
    @PostMapping("updateRunningNodeUser")
    public JsonResult updateRunningNodeUser(@ApiParam("actInstId") @RequestParam("actInstId") String actInstId,
                                            @ApiParam("nodeId") @RequestParam("nodeId") String nodeId,
                                            @ApiParam("userIds") @RequestParam("userIds") String userIds){

        if(StringUtils.isEmpty(userIds)){
            return new JsonResult(false,"用户没选择");
        }
        return bpmInterposeSevice.updateRunningNodeUser(actInstId,nodeId,userIds);
    }

    @MethodDefine(title = "根据业务主键获取流程启动状态", path = "/getByBusKey", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId"),@ParamDefine(title = "业务主键", varName = "pk")})
    @ApiOperation(value = "根据业务主键获取流程启动状态")
    @PostMapping("getByBusKey")
    public JsonResult getByBusKey (@ApiParam @RequestParam(value="defId") String defId,
                                   @ApiParam @RequestParam(value="pk")String pk){
        List<String> defIdAry=Arrays.asList(defId.split(","));
        List<BpmInst> bpmInsts= bpmInstService.getByBusKey(defIdAry,pk);
        if(BeanUtil.isEmpty(bpmInsts)){
            return JsonResult.Success();
        }
        JsonResult result=JsonResult.Success();
        List<String> instStatus=new ArrayList<>();
        for(BpmInst bpmInst:bpmInsts){
            instStatus.add(bpmInst.getStatus());
        }
        result.setData(StringUtils.join(instStatus,","));
        return result;
    }

    @MethodDefine(title = "获取我创建的草稿", path = "/getMyAllDraftBpmInst", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取我创建的草稿")
    @GetMapping("getMyAllDraftBpmInst")
    public JsonResult getMyAllDraftBpmInst (){
        String userId= ContextUtil.getCurrentUserId();
        List<BpmInst> bpmInsts= bpmInstService.getMyAllDraftBpmInst(userId,BpmInstStatus.DRAFTED.name(),ContextUtil.getCurrentTenantId());
        JsonResult result=JsonResult.Success();
        result.setData(bpmInsts);
        return result;
    }

    @MethodDefine(title = "根据流程实例ID获取主键", path = "/getPkByInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程实例ID", varName = "instId")})
    @ApiOperation(value = "根据流程实例ID获取主键")
    @GetMapping("getPkByInstId")
    public JsonResult getPkByInstId (@ApiParam @RequestParam(value="instId") String instId){
        JsonResult result=JsonResult.Success();
        BpmInst bpmInst=bpmInstService.get(instId);
        if(bpmInst!=null){
            result=JsonResult.Success();
            result.setData(bpmInst.getBusKey());
        }
        else{
            result=JsonResult.Fail();
        }
        return result;
    }

    @MethodDefine(title = "获取子流程数据", path = "/getSubProcessFormData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "传递参数", varName = "data")})
    @ApiOperation(value = "获取子流程数据")
    @PostMapping("getSubProcessFormData")
    public JsonResult getSubProcessFormData(@ApiParam @RequestBody JSONObject data){
        JsonResult rtn = new JsonResult<>(true, "获取成功");
        try {
            String defKey=data.getString("defKey");
            String mainDefId=data.getString("mainDefId");
            String instId=data.getString("instId");
            JSONObject formData=data.getJSONObject("formData");
            JSONObject jsonData = new JSONObject();
            JSONObject vars=new JSONObject();
            BpmDef mainBpmDef=bpmDefService.get(mainDefId);
            BpmDef bpmDef=bpmDefService.getMainByKey(defKey);
            if(bpmDef==null){
                rtn.setSuccess(false);
                rtn.setMessage("启动的子流程不存在！");
                return rtn;
            }
            ProcessConfig processConfig = bpmDefService.getProcessConfig(mainBpmDef.getActDefId());
            List<SubProcessDefConfig> subConfigs=processConfig.getSubProcessDefs();
            for(SubProcessDefConfig sub:subConfigs){
                if(sub.getAlias().equals(defKey)){
                    JSONArray setting=sub.getConfig().getJSONArray("data");
                    jsonData.putAll(bpmInstService.parseFormData(setting,formData));
                    JSONArray varData=sub.getConfig().getJSONArray("varData");
                    vars.putAll(bpmInstService.parseVarData(varData,formData));
                }
            }
            data.put("defId",bpmDef.getDefId());
            data.put("defName",bpmDef.getName());
            data.put("vars", vars);
            data.put("formData", jsonData);
            rtn.setData(data);
            rtn.setShow(false);
        } catch (Exception ex) {
            MessageUtil.triggerException("获取子流程数据失败!",ExceptionUtil.getExceptionMessage(ex));
        }
        return rtn;
    }

    @MethodDefine(title = "复活流程", path = "/liveProcess", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "传递参数", varName = "data")})
    @ApiOperation(value = "复活流程")
    @PostMapping("liveProcess")
    public JsonResult liveProcess(@ApiParam @RequestBody JSONObject data) throws Exception{
        //实例ID
        String actInstId = data.getString("actInstId");
        //指定节点 {nodeId:nodeId,userIds:objUser.getValue(),groupIds:objGroup.getValue()}
        String destNodeUsers = data.getString("destNodeUsers");
        //意见
        String opinion = data.getString("opinion");
        //附件
        String opFiles = data.getString("opFiles");

        BpmInst bpmInst = bpmInstService.getByActInstId(actInstId);
        if (BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) || BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
            bpmInstService.doLiveProcessInstance(actInstId,destNodeUsers,opinion,opFiles);
        }else {
            return new JsonResult(true, "实例未结束,无法复活!");
        }
        return new JsonResult(true, "成功复活流程!");
    }

    @MethodDefine(title = "获取所有可指定节点", path = "/getAllNodes", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实例ID", varName = "instId")})
    @ApiOperation(value = "获取所有可指定节点")
    @GetMapping("getAllNodes")
    public List<BpmRuPath> getAllNodes(@RequestParam(value="instId")String instId){
        BpmInst inst = bpmInstService.get(instId);
        Map<String,FlowNode> nodes= actRepService.getFlowNodes(inst.getActDefId());
        List<BpmRuPath> temp=new ArrayList<BpmRuPath>();
        for(FlowNode node:nodes.values()){
            if(node instanceof UserTask) {
                BpmRuPath path = new BpmRuPath();
                path.setNodeId(node.getId());
                path.setNodeName(node.getName());
                temp.add(path);
            }
        }
        return temp;
    }


    @MethodDefine(title = "查询我启动的流程实例", path = "/getMyStart", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义KEY", varName = "defKey"),
                    @ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="查询我的流程实例列表", notes="查询我的流程实例列表")
    @PostMapping(value="/getMyStart/{defKey}")
    public JsonPageResult getMyStart(@PathVariable(name = "defKey")String defKey, @RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String userId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",userId);
        filter.addParam("defKey",defKey);
        IPage page= bpmInstService.getByUserId(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    /**
     * 根据流程实例Id获取权限列表
     * @param json
     * @return
     */
    @MethodDefine(title = "根据流程实例Id获取权限列表", path = "/getBpmInstEncryptList", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "参数", varName = "json")})
    @ApiOperation(value="根据流程实例Id获取权限列表", notes="根据流程实例Id获取权限列表")
    @PostMapping(value = "/getBpmInstEncryptList")
    public JSONArray getBpmInstEncryptList(@RequestBody JSONObject json){
        String instIds=json.getString("instIds");
        String relType=json.getString("relType");
        JSONArray resultList=new JSONArray();
        if(StringUtils.isEmpty(instIds)){
            return resultList;
        }
        String[] instIdAry=instIds.split(",");
        for(String instId:instIdAry) {
            JSONObject result=new JSONObject();
            Boolean isRelType=null;
            if(StringUtils.isNotEmpty(relType)) {
                isRelType = relType.equals("true");
            }
            JsonResult jsonResult=bpmInstService.getBpmInstEncrypt(instId,isRelType);
            result.put("grant",jsonResult.isSuccess());
            result.put("encInstId",jsonResult.getData());
            BpmInst bpmInst=bpmInstService.getById(instId);
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 根据流程实例ID进行加密操作
     * @param instId
     * @return
     */
    @MethodDefine(title = "根据流程实例ID进行加密操作", path = "/getBpmInstEncrypt", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程实例ID", varName = "instId")})
    @ApiOperation(value="根据流程实例ID进行加密操作", notes="根据流程实例ID进行加密操作")
    @PostMapping(value = "/getBpmInstEncrypt")
    public JsonResult getBpmInstEncrypt(@RequestParam(name = "instId")String instId){
        return bpmInstService.getBpmInstEncrypt(instId,null);
    }

    /**
     * 根据流程实例ID进行解密操作
     * @param instId
     * @return
     */
    @MethodDefine(title = "根据流程实例ID进行解密操作", path = "/getBpmInstDecrypt", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程实例ID", varName = "instId")})
    @ApiOperation(value="根据流程实例ID进行解密操作", notes="根据流程实例ID进行解密操作")
    @PostMapping(value = "/getBpmInstDecrypt")
    public JsonResult getBpmInstDecrypt(@RequestParam(name = "instId")String instId){
        return bpmInstService.getBpmInstDecrypt(instId);
    }

    /**
     * 根据流程实例ID获取流程实例信息
     * @param instIds
     * @return
     */
    @MethodDefine(title = "根据流程实例ID获取流程实例信息", path = "/getBpmInstInfo", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程实例ID", varName = "instIds")})
    @ApiOperation(value="根据流程实例ID获取流程实例信息", notes="根据流程实例ID获取流程实例信息")
    @PostMapping(value = "/getBpmInstInfo")
    public JSONArray getBpmInstInfo(@RequestParam(name = "instIds",required = false)String instIds,@RequestParam(name = "relType",required = false)String relType){
        JSONArray array=new JSONArray();
        if(StringUtils.isEmpty(instIds)){
            return array;
        }
        String[] instIdAry=instIds.split(",");
        for(String instId:instIdAry){
            JSONObject json=new JSONObject();
            JsonResult<JSONObject> jsonResult =bpmInstService.getBpmInstDecrypt(instId);
            if(!jsonResult.isSuccess()){
                continue;
            }
            BpmInst bpmInst=bpmInstService.getById(jsonResult.getData().getString("instId"));
            if(BeanUtil.isEmpty(bpmInst)){
                continue;
            }
            //流程名称
            String process="";
            BpmDef bpmDef=bpmDefService.getByActDefId(bpmInst.getActDefId());
            if(BeanUtil.isNotEmpty(bpmDef)){
                process=bpmDef.getName();
            }
            //如果relType不为空，那么是关联类型。
            Boolean isRelType=null;
            if(StringUtils.isNotEmpty(relType)) {
                isRelType = relType.equals("true");
            }
            //获取加密数据
            JsonResult result=bpmInstService.getBpmInstEncrypt(instId,isRelType);
            json.put("subject",bpmInst.getSubject());
            json.put("instId",instId);
            json.put("process",process);
            json.put("grant",result.isSuccess());
            json.put("encInstId",result.getData());
            array.add(json);
        }
        return array;
    }


    @MethodDefine(title = "根据流程实例ID获取任务ID", path = "/getTaskIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程实例ID", varName = "instIds")})
    @ApiOperation(value="根据流程实例ID获取任务ID", notes="根据流程实例ID获取任务ID")
    @PostMapping(value="/getTaskIds")
    public  List getTaskIds(@RequestParam(name = "instIds")String instIds) throws Exception{
        JSONArray jsonArray = JSONArray.parseArray(instIds);
        if(BeanUtil.isEmpty(jsonArray)){
            return new ArrayList<>();
        }
        List<Map<String,String>> data=new ArrayList<>();
        IUser user = ContextUtil.getCurrentUser();
        List<String> roles = user.getRoles();
        for (int i = 0; i < jsonArray.size(); i++) {
            BpmInst bpmInst = bpmInstService.get(jsonArray.get(i).toString());
            //只获取运行中或锁定的流程
            if(BeanUtil.isEmpty(bpmInst) || (!bpmInst.getStatus().equals(BpmInstStatus.RUNNING.name()) &&!bpmInst.getStatus().equals(BpmInstStatus.LOCKED.name()))){
                continue;
            }
            QueryData queryData =new QueryData();
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            filter.addQueryParam("Q_v.INST_ID__S_EQ",jsonArray.get(i).toString());
            IPage<BpmTask> tasks = bpmTaskService.getByUserId(user.getUserId(), roles,ContextUtil.getCurrentTenantId(), filter);
            if(tasks==null || tasks.getRecords().size()==0 ){
                continue;
            }

            List<BpmTask> records = tasks.getRecords();
            for(BpmTask task:records){
                Map<String,String> map=new HashMap<>();
                map.put("instId",jsonArray.get(i).toString());
                map.put("taskId",task.getTaskId());
                data.add(map);
                continue;
            }

        }
        return data;
    }


    /**
     * 根据页面类型和流程实例获取流程的状态，根据状态显示页面。
     * @param type      页面类型
     * @param id        流程实例ID或流程KEY
     * @return
     * <pre>
     *     {success:false:message:"消息"}
     *     {success:true,action:"start,startDraft,detail,task",taskId:""}
     * </pre>
     * @throws Exception
     */
    @PostMapping(value="/getInfoByDefKeyInstId")
    public  JSONObject getInfoByDefKeyInstId(@RequestParam(name = "type")String type,
                                               @RequestParam(name = "id",required = false)String id,
                                               @RequestParam(name = "action",required = false)String action) throws Exception{
        //流程定义为空，实例为空
        if(StringUtils.isEmpty(type) || StringUtils.isEmpty(id)){
            JSONObject json=new JSONObject();
            json.put("success",false);
            json.put("message","没有输入参数");
            return json;
        }

        if(!typeList.contains(type)){
            JSONObject json=new JSONObject();
            json.put("success",false);
            json.put("message","输入类型无效!");
            return json;
        }
        //当类型为新建的时候
        if(TYPE_NEWDOC.equals(type)){
            BpmDef def= bpmDefService.getMainByKey(id);
            if(def==null){
                JSONObject json=new JSONObject();
                json.put("success",false);
                json.put("message","流程KEY无效");
                return json;
            }
            JSONObject json=new JSONObject();
            json.put("success",true);
            json.put("action","start");
            return json;
        }
        //解密instId
        JsonResult result=bpmInstService.getBpmInstDecrypt(id);
        if(!result.isSuccess()){
            JSONObject json=new JSONObject();
            json.put("success",false);
            json.put("message",result.getMessage());
            return json;
        }
        JSONObject resultJson=(JSONObject)result.getData();
        id=resultJson.getString("instId");
        boolean relType=resultJson.getBoolean("relType");
        //不是启动的情况，就是流程实例ID
        BpmInst bpmInst=bpmInstService.get(id);
        if(bpmInst==null){
            JSONObject json=new JSONObject();
            json.put("success",false);
            json.put("message","流程实例ID无效");
            return json;
        }

        String status=bpmInst.getStatus();
        //草稿状态。
        if(BpmInstStatus.DRAFTED.name().equals(status)){
            JSONObject json=new JSONObject();
            json.put("success",true);
            json.put("action","startDraft");
            json.put("relType",relType);
            json.put("defKey",bpmInst.getDefCode());
            json.put("instId",id);
            return json;
        }

        JPaasUser jPaasUser= (JPaasUser) ContextUtil.getCurrentUser();
        List<BpmTask> taskList= bpmTaskService.getByInstUserId(jPaasUser,id);
        if(BpmInstStatus.CANCEL.name().equals(status) ||
                BpmInstStatus.SUPSPEND.name().equals(status) ||
                taskList.size()==0 || "detail".equals(action)){
            JSONObject json=new JSONObject();
            json.put("success",true);
            json.put("relType",relType);
            json.put("action","detail");
            json.put("instId",id);
            return json;
        }

        BpmTask task=taskList.get(0);

        JSONObject json=new JSONObject();
        json.put("success",true);
        json.put("action","task");
        json.put("relType",relType);
        json.put("taskId",task.getTaskId());
        json.put("instId",id);
        return json;
    }


    /**
     * 撤回流程.
     * <pre>
     *     1.发起人可以撤销流程重新执行。
     * </pre>
     * @param instId
     * @param from
     * @param opinion
     * @return
     */
    @MethodDefine(title = "撤回流程实例", path = "/revokeProcess", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例ID", varName = "instId"),
                    @ParamDefine(title = "来源", varName = "from"),
                    @ParamDefine(title = "原因", varName = "opinion") })
    @ApiOperation(value = "撤回流程实例")
    @PostMapping("revokeProcess")
    public JsonResult revokeProcess(@ApiParam @RequestParam(value="instId") String instId,
                                    @ApiParam @RequestParam(value="from") String from,
                                    @ApiParam @RequestParam(value="opinion") String opinion){
        JsonResult rtn = bpmTakeBackService.revoke (instId,from, opinion);
        return rtn;
    }
}
