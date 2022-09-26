package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.ActivitiDeployService;
import com.redxun.bpm.activiti.config.AbstractNodeConfig;
import com.redxun.bpm.activiti.config.NodeConfig;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.ext.ActNode;
import com.redxun.bpm.activiti.ext.FlowDefCache;
import com.redxun.bpm.activiti.utils.BpmNodeConfigUtil;
import com.redxun.bpm.core.dto.NodeUsersDto;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.mapper.BpmDefMapper;
import com.redxun.bpm.valid.ExclusiveGatewayValidator;
import com.redxun.bpm.valid.ServiceTaskValidator;
import com.redxun.bpm.valid.UserTaskValidator;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.Dom4jUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.feign.form.FormClient;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.validation.ProcessValidatorImpl;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.ValidatorSet;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
* [流程定义]业务服务类
*/
@Service
@Slf4j
public class BpmDefService extends SuperServiceImpl<BpmDefMapper, BpmDef> implements BaseService<BpmDef> {

    @Resource
    private BpmDefMapper bpmDefMapper;
    @Autowired
    ActivitiDeployService activitiDeployService;
    @Autowired
    RepositoryService repositoryService;
    @Resource
    ActRepService actRepService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    FormClient formClient;



    @Override
    public BaseDao<BpmDef> getRepository() {
        return bpmDefMapper;
    }

    /**
     * 根据流程ID删除流程定义。
     * @param bpmDef
     */
    @Transactional
    public void delCacadeFlow(BpmDef bpmDef){
        FlowDefCache.removeByDefId(bpmDef.getActDefId());
        try{
            //清除运行时数据。
            clearInstByDefId(bpmDef.getDefId());
            //删除发布的定义
            repositoryService.deleteDeployment(bpmDef.getActDepId(),true);
        }catch (Exception e){
            log.error(ExceptionUtil.getExceptionMessage(e));
        }
        delete(bpmDef.getDefId());
    }

    /**
     * 根据流程定义ID清除流程实例任务。
     * <pre>
     *     1.根据流程定义ID获取流程实例.
     *     2.遍历实例进行删除。
     * </pre>
     * @param defId
     */
    @Transactional
    public void clearInstByDefId(String defId){
        BpmDef bpmDef=bpmDefMapper.selectById(defId);

        List<BpmDef> bpmDefs=this.getByKey(bpmDef.getKey());
        for(BpmDef def:bpmDefs){
            List<BpmInst> insts = bpmInstService.getByDefId(def.getDefId());
            StringBuilder sb=new StringBuilder();
            for(BpmInst bpmInst:insts){
                bpmInstService.deleteByInstId(bpmInst.getInstId(),sb);
            }
        }
        String detail="清除流程定义"+bpmDef.getName() +"("+bpmDef.getDefId()+")的运行数据!";
        LogContext.put(Audit.DETAIL,detail);
    }



    /**
     * 根据Key获得主流程版本
     * @param key
     * @return
     */
    public BpmDef getMainByKey(String key){
        return bpmDefMapper.getMainByKey(key);
    }

    /**
     * 获得当前主定义的最大版本数
     * @param key
     * @return
     */
    public Integer getMaxVersion(String key){
        Integer max= bpmDefMapper.getMaxVersion(key);
        if(max==null){
            max=0;
        }
        return max;
    }

    /**
     * 发布新版本流程定义
     * @param bpmDef
     * @return
     */
    @Transactional
    public BpmDef doDeployNew(BpmDef bpmDef){
        Deployment deployment = activitiDeployService.deploy(bpmDef.getKey(), bpmDef.getDesignXml());
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).latestVersion().singleResult();
        if(processDefinition!=null){
            bpmDef.setActDefId(processDefinition.getId());
        }
        bpmDef.setActDepId(deployment.getId());
        bpmDef.setStatus(BpmDef.STATUS_DEPLOYED);
        //新建流程定义
        if(StringUtils.isEmpty(bpmDef.getDefId())) {
            bpmDef.setIsMain("YES");
            bpmDef.setVersion(1);
            bpmDef.setDefId(IdGenerator.getIdStr());
            bpmDef.setMainDefId(bpmDef.getDefId());
            insert(bpmDef);
            //更新流程定义中的扩展属性定义
            doCopyBpmDefProsAndUpdate(bpmDef);
        }else{//更新流程定义
          update(bpmDef);
        }
        return bpmDef;
    }

    /**
     * 发布流程定义。
     * @param bpmDef
     * @return
     * @throws UnsupportedEncodingException
     */
    public BpmDef doModify(BpmDef bpmDef) throws UnsupportedEncodingException {
        FlowDefCache.removeByDefId(bpmDef.getActDefId());
        actRepService.writeDefXml(bpmDef.getActDepId(),bpmDef.getDesignXml());
        update(bpmDef);
        return bpmDef;
    }


    /**
     * 发布新版流程定义
     * @param oldBpmDef
     * @param newBpmDef
     */
    public void doDeployNewVersion(BpmDef oldBpmDef, BpmDef  newBpmDef){
        String oldDefId=oldBpmDef.getDefId();
        //换成新的
        newBpmDef.setDefId(IdGenerator.getIdStr());
        //更新为主版本主键Id
        newBpmDef.setMainDefId(newBpmDef.getDefId());
        newBpmDef.setIsMain(BpmDef.IS_MAIN);
        newBpmDef.setStatus(BpmDef.STATUS_DEPLOYED);
        //发布Activiti的流程定义
        Deployment deployment = activitiDeployService.deploy(newBpmDef.getKey(), newBpmDef.getDesignXml());
        newBpmDef.setActDepId(deployment.getId());
        ProcessDefinition processDefinition=repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).latestVersion().singleResult();
        //获取流程定义Id
        if(processDefinition!=null){
            newBpmDef.setActDefId(processDefinition.getId());
        }
        Integer maxVersion=getMaxVersion(newBpmDef.getKey());
        //升级流程定义版本
        newBpmDef.setVersion(maxVersion+1);
        //更新主版本号
        updateMainDefId(newBpmDef,oldDefId);
        //插入新流程
        insert(newBpmDef);
        //更新流程定义中的扩展属性定义
        doCopyBpmDefProsAndUpdate(newBpmDef);
    }

    /**
     * 更新流程定义中的extPros中的流程属性部分，避免版本变更时，里面存储的值还是保留旧版本的相关值。
     * @param bpmDef
     */
    private void doCopyBpmDefProsAndUpdate(BpmDef bpmDef){
        //同时更新一下
        JSONObject nodePros= JSONObject.parseObject(bpmDef.getExtConfs());

        JSONObject processPros=nodePros.getJSONObject(BpmDef.PROCESS_NODE_PRE);

        if(processPros!=null){
            JSONObject objPros= (JSONObject) JSONObject.toJSON(bpmDef);
            if(objPros!=null){
                Iterator it=objPros.keySet().iterator();
                while (it.hasNext()){
                    String key=(String)it.next();
                    if(! "designXml".equalsIgnoreCase(key)
                            && !"extConfs" .equalsIgnoreCase(key)){
                        processPros.put(key,objPros.getString(key));
                    }
                }
            }
            bpmDef.setExtConfs(nodePros.toJSONString());
        }

        update(bpmDef);
    }

    public List<BpmDef> getAllVersionsByMainDefId(String mainDefId){
        return bpmDefMapper.getAllVersionsByMainDefId(mainDefId);
    }

    public void updateMainDefId(BpmDef newBpmDef,String oldDefId){
        Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
        params.put("newMainDefId",newBpmDef.getDefId());
        params.put("oldMainDefId",oldDefId);
        params.put("isMain",BpmDef.IS_NOT_MAIN);
        bpmDefMapper.updateMainDefIdIsMain(params);
    }

    /**
     * 传入流程节点的外部扩展节点属性配置
     * @param extJsons
     * @return
     */
    public Map<String,NodeConfig> getNodeConfigs(String extJsons){
        Map<String,NodeConfig> nodeConfigMap=new HashMap<>();
        JSONObject configs=JSONObject.parseObject(extJsons);

        Iterator<String> its=(Iterator<String>)configs.keySet().iterator();
        while(its.hasNext()){
            String key= its.next();
            JSONObject conf=configs.getJSONObject(key);
            String nodeType=conf.getString("nodeType");
            Class<? extends NodeConfig> configClass=BpmNodeConfigUtil.getNodeConfigInstClass(nodeType);
            if(configClass!=null){
                NodeConfig configInst=configs.getObject(key,configClass);
                if(configInst!=null){
                    nodeConfigMap.put(key,configInst);
                }
            }
        }
        return nodeConfigMap;
    }

    /**
     * 获取流程节点的节点配置，优先从缓存中获取，若没有则从数据库中读取
     * @param actDefId
     * @param nodeId
     * @return
     */
    public NodeConfig getNodeConfig(String actDefId,String nodeId){

        if(StringUtils.isEmpty(actDefId) || StringUtils.isEmpty(nodeId)){
            return null;
        }
        Map<String,NodeConfig> mapConfig=getConfigByActDefId(actDefId);
        return mapConfig.get(nodeId);
    }

    /**
     * 根据流程引擎定义ID获取流程定义对象。
     * @param actDefId
     * @return
     */
    public BpmDef getByActDefId(String actDefId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ACT_DEF_ID_" ,actDefId);
        return this.bpmDefMapper.selectOne(wrapper);
    }

    public BpmDef getSimpleByActDefId(String actDefId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ACT_DEF_ID_" ,actDefId);
        wrapper.select(new String[]{
                "DEF_ID_","TREE_ID_","NAME_","KEY_","DESCP_" ,
                "ACT_DEF_ID_","ACT_DEP_ID_" ,
                "STATUS_" ,"VERSION_" ,"IS_MAIN_" , "MAIN_DEF_ID_",
                "BO_DEF_IDS_" ,"ICON_" ,"COLOR_"});

        return this.bpmDefMapper.selectOne(wrapper);
    }

    /**
     * 根据流程定义获取节点定义。
     * @param actDefId
     * @return
     */
    public Map<String,NodeConfig> getConfigByActDefId(String actDefId){
        Map<String,NodeConfig> mapConfig=ConfigCacheUtil.getCache(actDefId);
        if(BeanUtil.isNotEmpty(mapConfig)){
            return mapConfig;
        }
        BpmDef bpmDef=getByActDefId(actDefId);
        mapConfig=getNodeConfigs(bpmDef.getExtConfs());
        ConfigCacheUtil.add(actDefId,mapConfig);
        return  mapConfig;
    }

    /**
     * 获取流程节点。
     * @param defId
     * @return
     */
    public List<ActNode> getActNodeByDefId(String defId){
        BpmDef bpmDef=get(defId);
        Map<String,NodeConfig> map= getConfigByActDefId(bpmDef.getActDefId());
        Map<String, FlowNode> flowNodes = actRepService.getFlowNodes(bpmDef.getActDefId());
        List<ActNode> list=new ArrayList<>();
        for(Map.Entry<String, FlowNode> ent : flowNodes.entrySet()){
            String id=ent.getKey();
            AbstractNodeConfig nodeConfig= (AbstractNodeConfig) map.get(id);
            if(nodeConfig==null){
                continue;
            }
            ActNode actNode=new ActNode();
            actNode.setNodeId(nodeConfig.getKey());
            actNode.setName(nodeConfig.getName());
            actNode.setOrderNo(nodeConfig.getOrderNo());
            actNode.setNodeType(nodeConfig.getNodeType());
            list.add(actNode);
        }
        List<ActNode> nodes= list.stream().sorted(Comparator.comparing(p->p.getOrderNo())).collect(Collectors.toList());
        return nodes;
    }


    /**
     * 根据流程定义ID获取
     * @param actDefId
     * @return
     */
    public ProcessConfig getProcessConfig(String actDefId){
        if(StringUtils.isEmpty(actDefId)){
            return null;
        }
        Map<String,NodeConfig> mapConfig=getConfigByActDefId(actDefId);
        for (NodeConfig entry:mapConfig.values()){
            if(entry instanceof ProcessConfig){
                return (ProcessConfig) entry;
            }
        }
        return null;
    }

    private JSONObject getProcessAttr(JSONObject nodeProp){
        for(String key : nodeProp.keySet()){
            JSONObject attr=nodeProp.getJSONObject(key);
            if("bpmn:Process".equals(attr.getString("nodeType"))){
                return attr;
            }
        }
        return null;
    }

    /**
     * 检查流程定义的配置。
     * @param extConf
     * @return
     */
    public List<String> handXmlCheck(String extConf){
        List<String> errorList=new ArrayList<>();
        JSONObject json = JSONObject.parseObject(extConf);
        JSONObject processAttr = getProcessAttr(json);
        JSONObject globalForm=processAttr.getJSONObject("globalForm");
        if(!globalForm.containsKey("formpc") || BeanUtil.isEmpty(globalForm.getJSONArray("formpc"))){
            if(!processAttr.getJSONObject("startForm").containsKey("formpc") || BeanUtil.isEmpty(processAttr.getJSONObject("startForm").getJSONArray("formpc"))) {
                errorList.add("开始表单没有配置");
            }
        }
        int count=0;
        for(String key : json.keySet()){
            JSONObject attr = json.getJSONObject(key);
            String name = StringUtils.isEmpty(attr.getString("name"))?key:attr.getString("name");
            if("bpmn:UserTask".equals(attr.getString("nodeType"))){
                //用户节点
                count++;
                if(!globalForm.containsKey("formpc") || BeanUtil.isEmpty(globalForm.getJSONArray("formpc"))) {
                    if (!attr.getJSONObject("form").containsKey("formpc") || BeanUtil.isEmpty(attr.getJSONObject("form").getJSONArray("formpc"))) {
                        errorList.add("用户节点【" + name + "】没有配置表单或全局表单没有配置");
                    }
                }
                if(BeanUtil.isEmpty(attr.getJSONArray("userConfigs"))){
                    errorList.add("用户节点【"+name+"】没有配置执行人");
                }
            }
        }
        if(count==0){
            errorList.add("请至少配置一个用户节点");
        }
        return errorList;
    }

    /**
     * 检查BPMN定义
     * @param bpmDef
     * @return
     */
    public JsonResult handCheck(BpmDef bpmDef){
        List<String> errorList=new ArrayList<>();
        try {
            //配置错误信息
            errorList.addAll(handXmlCheck(bpmDef.getExtConfsTemp()));
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStream is = new ByteArrayInputStream(bpmDef.getDesignXmlTemp().getBytes());
            InputStreamReader in = new InputStreamReader(is, "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
            //创建默认模型校验器
            ProcessValidatorImpl processValidator = new ProcessValidatorImpl();
            processValidator.addValidatorSet(getValidatorSet());
            List<ValidationError> errors= processValidator.validate(bpmnModel);

            if(errors.size()>0){
                for(ValidationError ve:errors){
                    errorList.add(ve.getDefaultDescription());
                }
                return JsonResult.getFailResult(errorList,"验证失败！").setShow(false);
            }
        }catch (Exception e){
            errorList.add(e.getMessage());
            return JsonResult.getFailResult(errorList,"验证失败！").setShow(false);
        }
        return JsonResult.Success("验证通过！").setShow(false);
    }

    private ValidatorSet getValidatorSet(){
        ValidatorSet validatorSet = new ValidatorSet("流程图验证");
        validatorSet.addValidator(new UserTaskValidator());
        validatorSet.addValidator(new ServiceTaskValidator());
        validatorSet.addValidator(new ExclusiveGatewayValidator());
        return validatorSet;
    }

    public boolean isExistKey(String key) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",key);
        Integer rtn=bpmDefMapper.selectCount(queryWrapper);
        return rtn>0;
    }

    /**
     * 修正流程定义的版本
     * @param doc
     * @param map
     * @param processName
     * @param processCode
     */
    public void handleProcessName(Document doc,Map map,String processCode,String processName){
        XPath xPath = doc.createXPath("bpmn:definitions/bpmn:process");
        xPath.setNamespaceURIs(map);
        List list = xPath.selectNodes(doc);
        String  processId=processCode;
        String oldProcessId=null;
        for(int i=0;i<list.size();i++) {
            Element el = (Element) list.get(i);
            Attribute idAttr=el.attribute("id");
            oldProcessId=idAttr.getValue();
            idAttr.setValue(processId);
            Attribute nameAttr=el.attribute("name");
            if(nameAttr==null){
                el.addAttribute("name",processName);
            }
            else{
                nameAttr.setValue(processName);
            }

        }

        if(StringUtils.isNotEmpty(oldProcessId)) {
            XPath xPath2 = doc.createXPath("bpmn:definitions/bpmn:collaboration/bpmn:participant[@processRef='" + oldProcessId + "']");
            xPath2.setNamespaceURIs(map);
            List list2 = xPath2.selectNodes(doc);
            for (int i = 0; i < list2.size(); i++) {
                Element el = (Element) list2.get(i);
                Attribute processRefAttr=el.attribute("processRef");
                processRefAttr.setValue(processCode);
            }
        }
    }



    /**
     * 复制新版的流程定义
     * @param defId
     * @param key
     * @param name
     * @param deploy
     */
    public void copyNew(String defId, String key, String name, Boolean deploy) {
        StringBuilder sb=new StringBuilder();
        sb.append("原流程定义为:");
        //获取旧值
        BpmDef orgBpmDef = get(defId);

        sb.append(orgBpmDef.getName() +"("+orgBpmDef.getDefId()+")");

        //拷至新对象
        BpmDef bpmDef=new BpmDef();
        try {
            BeanUtil.copyNotNullProperties(bpmDef, orgBpmDef);
        }catch (Exception e){
            e.printStackTrace();
        }

        //替换扩展配置中的id与key
        JSONObject extConfigObj =(JSONObject)JSONObject.parseObject(bpmDef.getExtConfs());
        JSONObject processObj= extConfigObj.getJSONObject(BpmDef.PROCESS_NODE_PRE);

        processObj.put("id",key);
        processObj.put("key",key);
        processObj.put("name",name);
        processObj.remove("extConfs");
        processObj.remove("designXml");

        if(deploy){
            processObj.put("status",BpmDef.STATUS_DEPLOYED);
        }else {
            processObj.put("status",BpmDef.STATUS_DRAFT);
        }
        bpmDef.setExtConfs(extConfigObj.toJSONString());

        Document doc=Dom4jUtil.stringToDocument(bpmDef.getDesignXml());

        Map map = new HashMap();
        map.put("bpmn","http://www.omg.org/spec/BPMN/20100524/MODEL");
        map.put("activiti","http://activiti.org/bpmn");
        bpmDef.setDefId(null);
        bpmDef.setKey(key);
        bpmDef.setName(name);
        //替换流程定义中的Id
        handleProcessName(doc,map,key,name);
        bpmDef.setDesignXml(doc.asXML());

        if(deploy){//发布新版
            doDeployNew(bpmDef);
        }else{ // 插入新的记录
            bpmDef.setDefId(IdGenerator.getIdStr());
            bpmDef.setIsMain(MBoolean.YES.name());
            bpmDef.setVersion(1);
            bpmDef.setStatus(BpmDef.STATUS_DRAFT);
            bpmDef.setMainDefId(bpmDef.getDefId());
            bpmDef.setActDefId(null);
            bpmDef.setActDepId(null);
            insert(bpmDef);
        }
        if(deploy){
            sb.append(",发布新的流程定义为:" +bpmDef.getName() +"("+bpmDef.getDefId()+")");
        }
        else{
            sb.append(",新的流程定义(未发布)为:" +bpmDef.getName() +"("+bpmDef.getDefId()+")");
        }
        LogContext.put(Audit.DETAIL,sb.toString());

    }

    /**
     * 根据键获取流程定义列表。
     * @param key
     * @return
     */
    public  List<BpmDef> getByKey(String key){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("KEY_",key);
        List list = bpmDefMapper.selectList(wrapper);
        return list;
    }


    /**
     * 获取流程节点执行人员。
     * @param actDefId  流程定义ID
     * @return
     */
    public List<NodeUsersDto> getFlowNodesExecutors(String actDefId){
        FlowNode firstNode = actRepService.getFirstUserTaskNode(actDefId);
        Set<FlowNode> outNodes = actRepService.getOutcomeNodes(actDefId, firstNode.getId());
        Map<String,Object> vars=new HashMap<>();
        List<NodeUsersDto> nodeUsersDtoList = bpmTaskService.getNodeUsers(actDefId,outNodes,vars);
        return nodeUsersDtoList;
    }


    /**
     * 根据表单数据，将数据转换成 {boAlias:{表单JSON数据}}
     * <pre>
     *     返回数据格式:
     *     {表单别名:{业务模型别名:"",entMap:{实体名:"实体类型"}}
     * </pre>
     * @param formData
     * @return
     */
    public JSONObject getBoDataMap(JSONObject formData){
        Set<String> set=formData.keySet();
        String keys= StringUtils.join(set);
        //{alias:{boAlias:"",entMap:{entName:"main"}}}
        //{表单别名:{业务模型别名:"",entMap:{实体名:"实体类型"}}
        String boJsonStr=formClient.getAliasByFormAlias(keys);
        JSONObject boJson=JSONObject.parseObject(boJsonStr);
        JSONObject boDataMap=new JSONObject();
        for(String key:formData.keySet()){
            JSONObject boAliasJson=boJson.getJSONObject(key);
            String boAlias=boAliasJson.getString("boAlias");
            boDataMap.put(boAlias,formData.getJSONObject(key));
        }
        return boDataMap;
    }

}


