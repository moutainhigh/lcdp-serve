package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.activiti.config.BpmNodeTypeEnums;
import com.redxun.bpm.activiti.ext.FlowDefCache;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.DeployModel;
import com.redxun.bpm.core.mapper.BpmTaskMapper;
import com.redxun.bpm.core.service.AppDataServiceImpl;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.ConfigCacheUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.Dom4jUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 流程建模类
 * @author  csx
 */
@Slf4j
@RestController
@RequestMapping("/bpm/modeler")
@ClassDefine(title = "流程建模",alias = "bpmModelerController",path = "/bpm/modeler",packages = "core",packageName = "流程管理")
@Api(tags = "流程建模")
public class BpmModelerController {

    @Autowired
    BpmDefService bpmDefService;
    @Resource
    AppDataServiceImpl appDataService;
    @Autowired
    BpmTaskMapper bpmTaskMapper;


    private static Map<String,String> deployMap=new HashMap<>();
    static {
        deployMap.put(DeployModel.ACTION_DEPLOY,"发布新版本");
        deployMap.put(DeployModel.ACTION_SAVE,"发布新版本");
        deployMap.put(DeployModel.ACTION_MODIFY,"修改流程定义");
    }

    @MethodDefine(title = "验证流程定义", path = "/check", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义", varName = "bpmDef")})
    @ApiOperation(value="验证流程定义")
    @AuditLog(operation = "验证流程定义")
    @PostMapping(value="/check")
    public JsonResult check(@ApiParam @RequestBody BpmDef bpmDef, @ApiParam @RequestParam(value = "action") String action){
        modifyXml( bpmDef,DeployModel.ACTION_SAVE);
        //除保存以外的操作，对流程配置进行设置
        if(!DeployModel.ACTION_SAVE.equals(action)){
            List<String> errorList=new ArrayList<>();
            String message = validNodeTask(bpmDef);
            if(!"".equals(message)){
                errorList.add(message);
                return JsonResult.getFailResult(errorList,"验证失败！").setShow(false);
            }
        }
        JsonResult result= bpmDefService.handCheck(bpmDef);
        String detail="验证流程定义:" + bpmDef.getName() ;
        LogContext.put(Audit.DETAIL,detail);
        return result;
    }

    @MethodDefine(title = "流程定义保存、发布、修改", path = "/deploy", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "发布数据", varName = "deployModel")})
    @ApiOperation(value = "流程定义保存、发布、修改")
    @AuditLog(operation = "验证流程定义")
    @PostMapping(value = "/deploy")
    public JsonResult<BpmDef> deploy(@ApiParam @RequestBody DeployModel deployModel){
        String opMsg="";
        BpmDef bpmDef=deployModel.getBpmDef();
        String action=deployModel.getAction();
        //根据XML整理extConfs
        modifyExtConfs(bpmDef);
        //除保存以外的操作，对流程配置进行设置
        if(!DeployModel.ACTION_SAVE.equals(action)){
            bpmDef.setDesignXml(bpmDef.getDesignXmlTemp());
            bpmDef.setExtConfs(bpmDef.getExtConfsTemp());
        }
        //替换ExtConfs中的流程定义扩展属性等
        String newExtConfs=handleExtJson(bpmDef.getExtConfs(),bpmDef.getKey(),deployModel.getAction());
        bpmDef.setExtConfs(newExtConfs);
        //修改流程定义中的XML
        modifyXml( bpmDef,action);


        String detail="对流程定义:" + bpmDef.getName() ;
        String actionStr=deployMap.get(action);
        try{
            String actDefId=bpmDef.getActDefId();
            //清理配置缓存。
            if(StringUtils.isNotEmpty(actDefId)) {
                ConfigCacheUtil.remove(actDefId);
                FlowDefCache.removeByDefId(actDefId);
            }

            if(DeployModel.ACTION_DEPLOY.equals(action)){//发布版本
                opMsg=handDeploy( bpmDef);
            }else if (DeployModel.ACTION_SAVE.equals(action)) {//不发布汉程定义
                opMsg=handSave(bpmDef);
            } else {
                opMsg=handModify(bpmDef);
            }

            JsonResult<BpmDef> result=new JsonResult<BpmDef>(true,opMsg);
            result.setShow(false);
            result.setData(bpmDef);

            detail+=actionStr +"成功";
            LogContext.put(Audit.DETAIL,detail);

            return result;
        } catch (Exception ex){
            MessageUtil.triggerException(actionStr + "失败!",ExceptionUtil.getExceptionMessage(ex),false);

            return null;
        }

    }

    /**
     * 根据XML整理extConfs。
     * <pre>
     *     1.删除extConfs多余数据。
     * </pre>
     * @param bpmDef
     */
    private void modifyExtConfs(BpmDef bpmDef){
        JSONObject newExtConfs = new JSONObject();

        String designXml = bpmDef.getDesignXmlTemp();

        List<String> process =getAllSequenceFlows(designXml);
        //开始节点
        process.add("Process");

        JSONObject oldExtConfs = JSONObject.parseObject(bpmDef.getExtConfsTemp());
        Iterator iter = oldExtConfs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key  =(String)entry.getKey();
            for(int i=0;i<process.size();i++) {
                if(process.get(i).equals(key) || key.indexOf("StartEvent_")==0){
                    newExtConfs.put(key,oldExtConfs.getJSONObject(key));
                    break;
                }
            }
        }
        bpmDef.setExtConfs(newExtConfs.toJSONString());
    }

    /**
     * 根据XML获取所有节点、网关名称
     */
    private List<String> getAllSequenceFlows(String designXml){
        Set<String> flows= new HashSet<>();
        List<String> returnFlows= new ArrayList<>();
        if(StringUtils.isEmpty(designXml)){
            return returnFlows;
        }
        Document doc = Dom4jUtil.stringToDocument(designXml);
        XPath xPath = doc.createXPath("bpmn:definitions/bpmn:process/bpmn:sequenceFlow");
        List list = xPath.selectNodes(doc);

        for(int i=0;i<list.size();i++) {
            Element el = (Element) list.get(i);
            Attribute idAttr=el.attribute("targetRef");
            flows.add(idAttr.getValue());
        }
        returnFlows = new ArrayList<>(flows);
        return returnFlows;
    }

    /**
     * 修改流程定义XML数据。
     * <pre>
     *     1.支持服务器节点任务。
     *     2.支持子流程的多实例任务。
     * </pre>
     * @param bpmDef
     */
    private void modifyXml(BpmDef bpmDef,String action){

        String designXml = bpmDef.getDesignXml();
        if(DeployModel.ACTION_SAVE.equals(action)) {
            designXml = bpmDef.getDesignXmlTemp();
        }

        Document doc = Dom4jUtil.stringToDocument(designXml);

        Map map = new HashMap();
        map.put("bpmn","http://www.omg.org/spec/BPMN/20100524/MODEL");
        map.put("activiti","http://activiti.org/bpmn");
        // 修改流程定义的Key与名称
        bpmDefService.handleProcessName(doc,map,bpmDef.getKey(),bpmDef.getName());
         // 创建解析路径，就是在普通的解析路径前加上map里的key值
        handSerivce(doc,map);
        //处理多实例子流程
        handSubProcessMulti(doc,map);
        if(!DeployModel.ACTION_SAVE.equals(action)) {
            bpmDef.setDesignXml(doc.asXML());
        }

    }

    /**
     *  多实例子流程处理。
        <multiInstanceLoopCharacteristics isSequential="true" activiti:collection="${counterSignService.getUsers(execution)}">
            <completionCondition>${counterSignService.isComplete(execution)}</completionCondition>
        </multiInstanceLoopCharacteristics>

     */
    private void handSubProcessMulti(Document doc,Map map){
        XPath xPath = doc.createXPath("//bpmn:subProcess/bpmn:multiInstanceLoopCharacteristics");
        xPath.setNamespaceURIs(map);

        List list = xPath.selectNodes(doc);

        for(int i=0;i<list.size();i++){
            Element el= (Element) list.get(i);
            boolean hasAttr= hasAttr(el,"isSequential");
            if(!hasAttr){
                el.addAttribute("isSequential","false");
            }

            boolean collection= hasAttr(el,"activiti:collection");
            if(!collection){
                el.addAttribute("activiti:collection","${subProcessService.getExcutors(execution)}");
            }

            if(!el.hasContent()){
                Element elCondition=el.addElement("bpmn:completionCondition");
                elCondition.addText("${subProcessService.isComplete(execution)}");
            }
        }
    }

    /**
     * 替换流程定义中的extConfs相关的流程定义key等
     * @param extConfs
     * @param bpmDefKey
     * @param action
     * @return
     */
    private String handleExtJson(String extConfs,String bpmDefKey,String action){
        //替换扩展配置中的id与key
        JSONObject extConfigObj =(JSONObject)JSONObject.parseObject(extConfs);
        if(extConfigObj==null){
            return "";
        }
        /**
         * 获取流程定义扩展属性
         */
        JSONObject processObj= extConfigObj.getJSONObject(BpmDef.PROCESS_NODE_PRE);
        processObj.put("id",bpmDefKey);
        processObj.put("key",bpmDefKey);
        processObj.remove("extConfs");
        processObj.remove("designXml");

        if(DeployModel.ACTION_DEPLOY.equals(action)){
            processObj.put("status",BpmDef.STATUS_DEPLOYED);
        }
        return extConfigObj.toJSONString();
    }

    /**
     * 处理service任务。
     * @param doc
     * @param map
     */
    private void handSerivce(Document doc,Map map){
        XPath xPath = doc.createXPath("//bpmn:serviceTask");
        xPath.setNamespaceURIs(map);

        List list = xPath.selectNodes(doc);
        for(int i=0;i<list.size();i++){
            Element el= (Element) list.get(i);
            boolean hasAttr= hasAttr(el,"activiti:delegateExpression");
            if(!hasAttr){
                el.addAttribute("activiti:delegateExpression","${rxServiceTask}");
            }

            if(!el.hasContent()){
                Element elCondition=el.addElement("completionCondition");
                el.addText("${counterSignService.isComplete(execution)}");
            }

        }

    }


    /**
     * 判断元素是否有某个属性。
     * @param el
     * @param attrName
     * @return
     */
    private static boolean hasAttr(Element el,String attrName){
        List attributes = el.attributes();
        for(int i=0;i<  attributes.size();i++){
            Attribute attr= (Attribute) attributes.get(i);
            if( attr.getQualifiedName().equals(attrName)){
                return  true;
            }
        }
        return  false;
    }

    public static void main(String[] args) {
        String xml= FileUtil.readFile("d:/temp/subtask.xml");
        Document doc = Dom4jUtil.stringToDocument(xml);

        HashMap map = new HashMap();
        map.put("bpmn","http://www.omg.org/spec/BPMN/20100524/MODEL");
        map.put("activiti","http://activiti.org/bpmn");
        // 创建解析路径，就是在普通的解析路径前加上map里的key值
        XPath xPath = doc.createXPath("//bpmn:subProcess/bpmn:multiInstanceLoopCharacteristics");
        xPath.setNamespaceURIs(map);

        List list = xPath.selectNodes(doc);

        for(int i=0;i<list.size();i++){
            Element el= (Element) list.get(i);
            boolean hasAttr= hasAttr(el,"isSequential");
            if(!hasAttr){
                el.addAttribute("isSequential","false");
            }

            boolean collection= hasAttr(el,"activiti:collection");
            if(!collection){
                el.addAttribute("activiti:collection","${subProcessService.getExcutors(execution)}");
            }

            if(!el.hasContent()){
                Element elCondition=el.addElement("completionCondition");
                elCondition.addText("${subProcessService.isComplete(execution)}");
            }
        }


    }



    private String handSave(BpmDef bpmDef){
        String opMsg="";
        if(StringUtils.isEmpty(bpmDef.getDefId())){ //第一次保存草稿
            bpmDef.setDefId(IdGenerator.getIdStr());
            bpmDef.setActDefId(null);
            bpmDef.setActDepId(null);
            bpmDef.setIsMain(MBoolean.YES.name());
            bpmDef.setVersion(1);
            bpmDef.setStatus(BpmDef.STATUS_DRAFT);
            bpmDef.setMainDefId(bpmDef.getDefId());
            String appId=appDataService.getAppIdByTreeId(bpmDef.getTreeId());
            bpmDef.setAppId(appId);
            bpmDefService.insert(bpmDef);
            opMsg="成功保存流程定义！";
        }else{//保存更新
            BpmDef tmpDef=bpmDefService.get(bpmDef.getDefId());
            try {
                //不拷贝以下信息
                bpmDef.setStatus(null);
                bpmDef.setMainDefId(null);
                bpmDef.setVersion(null);
                bpmDef.setIsMain(null);

                BeanUtil.copyNotNullProperties(tmpDef,bpmDef);
                bpmDefService.update(tmpDef);
                bpmDef=tmpDef;
                opMsg="成功更新流程定义！";
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  opMsg;
    }

    private String  handDeploy(BpmDef bpmDef){
        String opMsg="";
        // 发布新版本  修改流程定义
        if(StringUtils.isNotEmpty(bpmDef.getActDepId()) && StringUtils.isNotEmpty(bpmDef.getDefId())){
            //复制新的程定义
            BpmDef oldToNewDef=bpmDefService.get(bpmDef.getDefId());
            //旧的
            bpmDefService.doDeployNewVersion(oldToNewDef,bpmDef);
            opMsg="成功发布了新版流程定义！";
        }else{
            String appId=appDataService.getAppIdByTreeId(bpmDef.getTreeId());
            bpmDef.setAppId(appId);
            bpmDefService.doDeployNew(bpmDef);
            opMsg="成功发布了流程定义！";
        }
        return opMsg;
    }

    private String handModify(BpmDef bpmDef) throws UnsupportedEncodingException {
        String defId=bpmDef.getDefId();
        if(StringUtils.isEmpty(defId)){
            return  "流程定义还没有保存!";
        }
        BpmDef originDef=bpmDefService.getById(defId);
        if(StringUtils.isEmpty(originDef.getActDepId())){
            return  "流程定义还没有发布!";
        }
        bpmDefService.doModify(bpmDef);

        return  "成功修改流程定义!";
    }

    /**
     * 验证节点任务
     * @param bpmDef
     * @return
     */
    private String validNodeTask(BpmDef bpmDef) {
        //新建的定义不需要验证
        if(StringUtils.isNotEmpty(bpmDef.getDefId())){
            String extConfs = bpmDef.getExtConfs();
            String extConfsTemp = bpmDef.getExtConfsTemp();
            if(StringUtils.isNotEmpty(extConfs)){
                JSONObject extConfsObj = JSONObject.parseObject(extConfs);
                JSONObject extConfsTempObj = JSONObject.parseObject(extConfsTemp);
                Set<String> extConfsKeys = extConfsObj.keySet();
                Set<String> extConfsTempKeys = extConfsTempObj.keySet();
                for (String extConfsKey : extConfsKeys) {
                    //标记是否被删除
                    boolean falg=true;
                    for (String extConfsTempKey : extConfsTempKeys) {
                        //存在则表示未被删除
                        if(extConfsKey.equals(extConfsTempKey)){
                            falg=false;
                            break;
                        }
                    }
                    JSONObject nodeConfig = extConfsObj.getJSONObject(extConfsKey);
                    //被删除且是用户任务节点 需要判断是否当前节点
                    if(falg && BpmNodeTypeEnums.USER_TASK.getTypeName().equals(nodeConfig.getString("nodeType"))){
                        QueryWrapper wrapper= new QueryWrapper<BpmTask>();
                        wrapper.eq("KEY_",extConfsKey );
                        wrapper.eq("DEF_ID_", bpmDef.getDefId());
                        List list = bpmTaskMapper.selectList(wrapper);
                        //有任务则不允许删除当前的节点
                        if(BeanUtil.isNotEmpty(list) && list.size()>0){
                            String nodeName=nodeConfig.getString("name");
                            //节点名称为空则取节点key
                            if(StringUtils.isEmpty(nodeName)){
                                nodeName=extConfsKey;
                            }
                            return "节点"+nodeName+"有正在运行的任务,无法删除!";
                        }
                    }
                }
            }
        }
        return  "";
    }

}
