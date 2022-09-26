package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserGroupConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.ext.ActNode;
import com.redxun.bpm.core.dto.NodeUsersDto;
import com.redxun.bpm.core.dto.ScriptParam;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.BpmDefExpImpService;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.TaskExecutorService;
import com.redxun.bpm.script.ScriptConfig;
import com.redxun.bpm.script.cls.ScriptClass;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.AlterSql;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.form.FormClient;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowNode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmDef")
@ClassDefine(title = "流程定义",alias = "bpmDefController",path = "/bpm/core/bpmDef",packages = "core",packageName = "流程管理")
@Api(tags = "流程定义")
public class BpmDefController extends BaseController<BpmDef> {

    @Resource
    BpmDefService bpmDefService;
    @Resource
    ActRepService actRepService;
    @Resource
    BpmDefExpImpService bpmDefExpImpService;

    @Resource
    ScriptConfig scriptConfig;
    @Resource
    TaskExecutorService taskExecutorService;

    @Resource
    IOrgService orgService;



    @Override
    public BaseService getBaseService() {
        return bpmDefService;
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        //增加分类过滤
         QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","BPM","def.read");
        //增加查询只查主版本的信息
        filter.addQueryParam("Q_IS_MAIN__S_EQ", BpmDef.IS_MAIN);
        super.handleFilter(filter);
    }

    /**
     * 复制流程定义
     * @param bpmDef
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "复制流程定义", path = "/copyDef", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义", varName = "bpmDef")})
    @ApiOperation("复制流程定义")
    @AuditLog(operation = "复制流程定义")
    @PostMapping("/copyDef")
    public JsonResult copyDef(@RequestBody BpmDef bpmDef) throws Exception{
        boolean rtn=bpmDefService.isExistKey(bpmDef.getKey());
        if(rtn){
            return new JsonResult<>(false, "指定的KEY名称已经被使用!");
        }
        try {
            bpmDefService.copyNew(bpmDef.getDefId(), bpmDef.getKey(), bpmDef.getName(),bpmDef.getDeploy());
        } catch (Exception e) {
            MessageUtil.triggerException("复制流程定义失败!",ExceptionUtil.getExceptionMessage(e));
        }

        return new JsonResult<>(true, "复制成功");
    }

    /**
     * 流程设计导入
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "流程设计导入", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation("导入接口")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String treeId=request.getParameter("treeId");
        String formPcTreeId=request.getParameter("formPcTreeId");
        List<AlterSql> delaySqlList = bpmDefExpImpService.importBpmDefs(file,treeId,formPcTreeId);
        return JsonResult.Success().setData(delaySqlList).setMessage("导入成功");
    }


    /**
     * 流程设计导出
     */
    @MethodDefine(title = "流程设计导出", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defIds")})
    @ApiOperation("流程设计导出")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "defIds") String defIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(defIds)){
            throw new Exception("导出失败，请选择要导出的记录。");
        }
        List<BpmDefExp> defJsons=bpmDefExpImpService.exportBpmDef(defIds);
        //把每个记录实体转成JSON文件存储
        Map<String,String> map=new HashMap<>();
        for (BpmDefExp def:defJsons) {
            String fileName =def.getBpmDefName()+".json";
            String defStr = JSONObject.toJSONString(def);
            map.put(fileName,defStr);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Bpm-Def" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }

    /**
     * 按条件查询可供对外启动的流程
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "按条件查询可供对外启动的流程", path = "/getAllForStart", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="按条件查询可供对外启动的流程", notes="按条件查询可供对外启动的流程")
    @PostMapping(value="/getAllForStart")
    public JsonPageResult getAllForStart(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        filter.addQueryParam("Q_status__S_EQ",BpmDef.STATUS_DEPLOYED);
        //增加分类过滤
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","BPM","start.read");
        //增加查询只查主版本的信息
        filter.addQueryParam("Q_IS_MAIN__S_EQ", BpmDef.IS_MAIN);

        WebUtil.handFilter(filter,ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage page= bpmDefService.query(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @Override
    public String getComment() {
        return "流程定义";
    }


    @MethodDefine(title = "根据主版本号获取其下所有的流程定义版本", path = "/getVersions", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "mainDefId")})
    @ApiOperation("根据主版本号获取其下所有的流程定义版本")
    @GetMapping("getVersions")
    public JsonResult getVersions(@ApiParam @RequestParam(value = "mainDefId") String mainDefId) {
        List<BpmDef> list= bpmDefService.getAllVersionsByMainDefId(mainDefId);
        return JsonResult.getSuccessResult(list);
    }

    @MethodDefine(title = "根据当前流程定义获取同定义下的所有版本", path = "/getVersionsByDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("根据当前流程定义获取同定义下的所有版本")
    @GetMapping("getVersionsByDefId")
    public JsonResult getVersionsByDefId(@ApiParam @RequestParam("defId") String defId) {
        BpmDef bpmDef=bpmDefService.get(defId);
        if(bpmDef==null){
            return JsonResult.getSuccessResult(new ArrayList<>());
        }
        List<BpmDef> list= bpmDefService.getAllVersionsByMainDefId(bpmDef.getMainDefId());
        return JsonResult.getSuccessResult(list);
    }

    @MethodDefine(title = "设置主版本", path = "/setMainVersion", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("设置主版本")
    @GetMapping("setMainVersion")
    public JsonResult setMainVersion(@ApiParam @RequestParam(value = "defId") String defId){
        BpmDef bpmDef = bpmDefService.get(defId);
        bpmDefService.updateMainDefId(bpmDef,bpmDef.getMainDefId());
        bpmDef.setIsMain(BpmDef.IS_MAIN);
        bpmDef.setMainDefId(bpmDef.getDefId());
        bpmDefService.update(bpmDef);
        return JsonResult.Success("设置主版本成功").setShow(false);
    }

    @MethodDefine(title = "根据流程定义Id作废流程", path = "/discard", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation(value="流程定义作废", notes="根据流程定义Id作废流程")
    @PostMapping("discard")
    public JsonResult discard(@RequestParam("defId") String defId){
        BpmDef bpmDef=bpmDefService.get(defId);
        if(bpmDef!=null){
            bpmDef.setStatus(BpmDef.STATUS_INVALID);
            bpmDefService.update(bpmDef);
        }
        return new JsonResult(true,"成功作废流程定义");
    }

    @MethodDefine(title = "流程定义作废后恢复", path = "/recover", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation(value="流程定义作废后恢复")
    @PostMapping("recover")
    public JsonResult recover(@RequestParam("defId") String defId){
        BpmDef bpmDef=bpmDefService.get(defId);
        if(bpmDef!=null && StringUtils.isNotEmpty(bpmDef.getActDefId())){
            bpmDef.setStatus(BpmDef.STATUS_DEPLOYED);
            bpmDefService.update(bpmDef);
        }
        return new JsonResult(true,"成功恢复流程定义");
    }

    @ApiOperation(value="删除流程定义", notes="根据实体Id删除实体信息,parameters is {ids:'1,2'}")
    @PostMapping("del")
    @Override
    public JsonResult del(@ApiParam @RequestParam("ids") String ids) {
        String[]idArr=ids.split("[,]");
        for(String id:idArr){
            BpmDef bpmDef=bpmDefService.get(id);
            if(bpmDef!=null && StringUtils.isNotEmpty(bpmDef.getActDepId())){
                bpmDefService.delCacadeFlow(bpmDef);
            }else{
                bpmDefService.delete(id);
            }
        }
        JsonResult result=new JsonResult(true,"");
        result.setMessage("成功执行删除操作！");
        return result;
    }

    @MethodDefine(title = "通过定义ID获取到流程定义Xml", path = "/getBpmnXmlFromDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("通过定义ID获取到流程定义Xml")
    @GetMapping("getBpmnXmlFromDefId")
    public String getBpmnXmlFromDefId(@ApiParam @RequestParam("defId") String defId){
        BpmDef bpmDef=bpmDefService.get(defId);
        if(bpmDef==null){
            return "";
        }
        return bpmDef.getDesignXml();
    }

    @MethodDefine(title = "通过定义ID获取到流程所有的节点", path = "/getNodesByDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("通过定义ID获取到流程所有的节点")
    @GetMapping("getNodesByDefId")
    public  List<ActNode> getNodesByDefId(String defId){
        List<ActNode> nodes = bpmDefService.getActNodeByDefId(defId);
        return nodes;
    }

    @MethodDefine(title = "获取所有的脚本类", path = "/getScriptClasses", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "脚本参数", varName = "scriptParam")})
    @ApiOperation("获取所有的脚本类")
    @PostMapping("getScriptClasses")
    public List<ScriptClass> getScriptClasses(@RequestBody ScriptParam scriptParam){
        return scriptConfig.getScriptClasses(scriptParam.getBoAlias(),scriptParam.getVarConfigs());
    }

    @MethodDefine(title = "清除流程运行的数据", path = "/clearInstByDefId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("清除流程运行的数据")
    @PostMapping("clearInstByDefId")
    public JsonResult  clearInstByDefId(@RequestParam(value = "defId") String defId ){
        JsonResult jsonResult=JsonResult.Success("清除流程运行成功!");
        try{
            bpmDefService.clearInstByDefId(defId);
        }
        catch (Exception ex){
            MessageUtil.triggerException("清除流程数据失败!",ExceptionUtil.getExceptionMessage(ex));
        }
        return  jsonResult;

    }

    @MethodDefine(title = "根据Key获取流程定义", path = "/getBydefKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义Key", varName = "defKey")})
    @ApiOperation(value = "根据Key获取流程定义")
    @GetMapping("getBydefKey")
    public BpmDef getBydefKey(@RequestParam(value="defKey") String defKey){
        return bpmDefService.getMainByKey(defKey);
    }

    @MethodDefine(title = "根据Key获取流程定义配置", path = "/getProcessConfigByKey", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程定义Key", varName = "defKey")})
    @ApiOperation(value = "根据Key获取流程定义配置")
    @GetMapping("getProcessConfigByKey")
    public ProcessConfig getProcessConfigByKey(@RequestParam(value="defKey") String defKey){
        BpmDef bpmDef=bpmDefService.getMainByKey(defKey);
        return bpmDefService.getProcessConfig(bpmDef.getActDefId());
    }

    @GetMapping("getBoDefsByDefKey")
    public String getBoDefsByDefKey(@RequestParam(value="defKey") String defKey){
        BpmDef bpmDef=bpmDefService.getMainByKey(defKey);
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmDef.getActDefId());
        if(processConfig!=null){
            return processConfig.getBoDefs().getValue();
        }
        return null;
    }

    /**
     * 返回节点和节点的用户数据。
     * [{
     *     nodeId:"",
     *     name:"",
     *     users:[{id:"",name:""}]
     * }]
     * @param defId
     * @param formData
     * @return
     */
    @MethodDefine(title = "获取开始节点必选用户节点与可选的用户节点", path = "/getStartUserNodes", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation("获取开始节点必选用户节点与可选的用户节点")
    @PostMapping("getStartUserNodes")
    public List<JSONObject> getStartUserNodes(@ApiParam("defId") @RequestParam(value = "defId") String defId,@RequestBody JSONObject formData){
        BpmDef bpmDef=bpmDefService.get(defId);
        String actDefId=bpmDef.getActDefId();

        ProcessConfig processConfig=bpmDefService.getProcessConfig(actDefId);

        List<JSONObject> list=new ArrayList<>();
        List<String> startReqNodes = processConfig.getStartReqNodes();
        if(BeanUtil.isEmpty(startReqNodes)){
            return list;
        }

        //表单数据
        JSONObject boDataMap= bpmDefService.getBoDataMap( formData);
        IExecutionCmd cmd=new ProcessStartCmd();
        cmd.setBoDataMap(boDataMap);
        ProcessHandleUtil.setProcessCmd(cmd);
        Map<String,FlowNode> nodeMap=actRepService.getFlowNodes(actDefId);

        for(String nodeId:processConfig.getStartReqNodes()){
            FlowNode flowNode=nodeMap.get(nodeId);
            if(flowNode==null){
                continue;
            }

            JSONObject nodeJson=new JSONObject();
            nodeJson.put("nodeId",flowNode.getId());
            nodeJson.put("name",flowNode.getName());
            List<JSONObject> users=getNodeUser( actDefId, nodeId);
            nodeJson.put("users",users);

            list.add(nodeJson);
        }
        ProcessHandleUtil.clearProcessCmd();
        return list;
    }




    /**
     * 返回节点的用户
     * @param actDefId
     * @param nodeId
     * @return
     */
    private List<JSONObject> getNodeUser(String actDefId,String nodeId){
        UserTaskConfig taskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);
        List<UserGroupConfig> userConfigs = taskConfig.getUserConfigs();
        Map<String,Object> vars=new HashMap<>();
        Set<TaskExecutor> executors = taskExecutorService.getTaskExecutors(userConfigs, vars);
        List<JSONObject> users=new ArrayList<>();
        for(TaskExecutor executor : executors){
            if(TaskExecutor.TYPE_USER.equals( executor.getType())){
                JSONObject userDto=new JSONObject();
                userDto.put("id",executor.getId());
                userDto.put("name",executor.getName());
                users.add(userDto);
            }
            else{
                List<OsUserDto> userDtos = orgService.getByGroupId(executor.getId());
                for(OsUserDto userDto:userDtos){
                    JSONObject user=new JSONObject();
                    user.put("id",userDto.getUserId());
                    user.put("name",userDto.getFullName());
                    users.add(user);
                }
            }
        }
        return users;

    }



    @MethodDefine(title = "获取第一个任务之后的人员", path = "/getFlowNodesExecutors", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义ID", varName = "defId")})
    @ApiOperation(value = "获取第一个任务之后的人员")
    @PostMapping ("getFlowNodesExecutors")
    public List<NodeUsersDto> getFlowNodesExecutors(@ApiParam("defId") @RequestParam(value = "defId") String defId,@RequestBody JSONObject formData){
        try{
            //表单数据
            JSONObject boDataMap= bpmDefService. getBoDataMap(formData);
            IExecutionCmd cmd=new ProcessStartCmd();
            cmd.setBoDataMap(boDataMap);
            ProcessHandleUtil.setProcessCmd(cmd);
            BpmDef bpmDef=bpmDefService.get(defId);
            List<NodeUsersDto> usersDtos= bpmDefService.getFlowNodesExecutors(bpmDef.getActDefId());
            return usersDtos;
        }
        finally {
            ProcessHandleUtil.clearProcessCmd();
        }

    }

    @MethodDefine(title = "根据人员配置获取执行人", path = "/getExecutorByConfig", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "人员配置", varName = "userConfigs")})
    @ApiOperation(value = "根据人员配置获取执行人")
    @PostMapping ("getExecutorByConfig")
    public Set<TaskExecutor> getExecutorByConfig(@RequestBody List<UserGroupConfig> userConfigs){
        Map<String,Object> vars=new HashMap<>();
        Set<TaskExecutor> executors = taskExecutorService.getTaskExecutors(userConfigs, vars);
        return executors;
    }
}
