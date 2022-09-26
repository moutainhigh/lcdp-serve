package com.redxun.bpm.core.service;

import cn.hutool.json.XML;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.form.IFormService;
import com.redxun.bpm.activiti.ActivitiDeployService;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.entity.BpmDefExp;
import com.redxun.bpm.core.mapper.BpmDefMapper;
import com.redxun.bpm.flow.entity.ActGeBytearray;
import com.redxun.bpm.flow.entity.ActReDeployment;
import com.redxun.bpm.flow.entity.ActReProcdef;
import com.redxun.bpm.flow.service.ActGeBytearrayServiceImpl;
import com.redxun.bpm.flow.service.ActReDeploymentServiceImpl;
import com.redxun.bpm.flow.service.ActReProcdefServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.form.AlterSql;
import com.redxun.dto.form.FormMobileDto;
import com.redxun.dto.form.FormPcDto;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 流程导入导出SERVICE。
 */
@Slf4j
@Service
public class BpmDefExpImpService {

    @Resource
    IFormService formService;

    @Resource
    private BpmDefMapper bpmDefMapper;

    @Resource
    ActReProcdefServiceImpl actReProcdefService;
    @Resource
    ActReDeploymentServiceImpl actReDeploymentService;

    @Resource
    BpmDefService bpmDefService;

    @Autowired
    ActivitiDeployService activitiDeployService;

    @Autowired
    ActGeBytearrayServiceImpl actGeBytearrayService;

    @Resource
    AppDataServiceImpl appDataService;
    /**
     * 读取压缩包中的流程设计文件
     * @param file
     * @return
     */
    private List<BpmDefExp> readZipFile(MultipartFile file){
        List<BpmDefExp> bpmDefExpList = new ArrayList<>();
        try{
            InputStream is = file.getInputStream();
            // 转化为Zip的输入流
            ZipArchiveInputStream zipIs = new ZipArchiveInputStream(is, "UTF-8");
            while ((zipIs.getNextZipEntry()) != null) {// 读取Zip中的每个文件
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(zipIs, baos);
                String bpmDefStr = baos.toString("UTF-8");
                BpmDefExp def = JSONObject.parseObject(bpmDefStr,BpmDefExp.class);
                if(BeanUtil.isNotEmpty(def)){
                    bpmDefExpList.add(def);
                }
            }
            zipIs.close();
        }catch (Exception e){
            log.error("---BpmDefController.doImport is error =="+e.getMessage());
        }
        return bpmDefExpList;
    }



    /**
     * 流程设计导入
     * @param file
     * @param treeId
     */
    public List<AlterSql> importBpmDefs(MultipartFile file, String treeId, String formPcTreeId){

        StringBuilder sb=new StringBuilder();
        sb.append("导入流程定义:");

        List<AlterSql> delaySqlList = new ArrayList<>();
        List<BpmDefExp> bpmDefExpList = readZipFile(file);
        String appId=appDataService.getAppIdByTreeId(treeId);
        for (BpmDefExp def:bpmDefExpList) {
            BpmDef bpmDef=def.getBpmDef();
            bpmDef.setAppId(appId);
            sb.append(bpmDef.getName() +"("+bpmDef.getDefId()+"),");

            // 1处理流程定义 yes
            importBpmDef(def, treeId,appId);
            //导入电脑/手机表单
            List<AlterSql> sqls = importFormView(def, formPcTreeId);
            delaySqlList.addAll(sqls);
        }
        sb.append("到分类:" +treeId +",表单分类:" +  formPcTreeId);
        LogContext.put(Audit.DETAIL,sb.toString());

        return delaySqlList;
    }

    /**
     * 导入电脑/手机表单配置
     * @param def
     * @throws Exception
     */
    private List<AlterSql> importFormView(BpmDefExp def,String treeId){
        List<AlterSql> delaySqlList = new ArrayList<>();
        Set<FormPcDto> bpmFormViews = def.getBpmFormPcViews();
        if(BeanUtil.isNotEmpty(bpmFormViews)){
            formService.importFormPc(bpmFormViews,treeId);
        }
        Set<FormMobileDto> mobileForms = def.getMobileForms();
        if(BeanUtil.isNotEmpty(mobileForms)){

            formService.importMobileForm(mobileForms,treeId);
        }
        JSONArray sysBoDefs = def.getSysBoDefs();
        if(BeanUtil.isNotEmpty(sysBoDefs)){
            JSONObject sysBoDefJson= new JSONObject();
            sysBoDefJson.put("treeId",treeId);
            sysBoDefJson.put("sysBoDefs",sysBoDefs);
            delaySqlList=formService.importFormBoDef(sysBoDefJson);
        }
        return delaySqlList;
    }


    private void importBpmDef(BpmDefExp def, String treeId,String appId){
        String bpmDefId =def.getBpmDefId();
        BpmDef bpmDef = def.getBpmDef();
        bpmDef.setTreeId(treeId);
        bpmDef.setAppId(appId);
        BpmDef olDbpmDef = bpmDefMapper.selectById(bpmDefId);
        if(BeanUtil.isNotEmpty(olDbpmDef)){
            bpmDefService.update(bpmDef);
        }else {
            bpmDefService.insert(bpmDef);
        }

        ActReProcdef actProcDef = def.getActProcDef();
        if(BeanUtil.isNotEmpty(actProcDef)){
            ActReProcdef oldProcDef =  actReProcdefService.get(actProcDef.getId());
            if(BeanUtil.isNotEmpty(oldProcDef)){
                actReProcdefService.update(actProcDef);
            }else {
                actReProcdefService.insert(actProcDef);
            }
        }

        ActGeBytearray byteArray =def.getByteArrays();
        if(BeanUtil.isNotEmpty(byteArray)){
            actGeBytearrayService.removeById(byteArray.getId());
        }

        ActReDeployment actDeployMent =def.getActDeployMent();
        if(BeanUtil.isNotEmpty(actDeployMent)){
            actReDeploymentService.removeById(actDeployMent.getId());
            actReDeploymentService.insert(actDeployMent);
        }

        if(BeanUtil.isNotEmpty(byteArray)){
            actGeBytearrayService.insert(byteArray);
        }
    }



    /**
     *  流程设计导出
     * @param defIds
     * @return
     */
    public List<BpmDefExp> exportBpmDef(String defIds){
        StringBuilder sb=new StringBuilder();

        sb.append("导出流程定义为:");

        List<BpmDefExp> bpmDefExps = new ArrayList<>();
        String[] ids = defIds.split("[,]");
        for (String defId:ids) {
            BpmDefExp bpmDefExp = new BpmDefExp();
            bpmDefExp.setBpmDefId(defId);
            BpmDef bpmDef = bpmDefMapper.selectById(defId);

            sb.append(bpmDef.getName() +"("+ bpmDef.getDefId() +")");

            bpmDefExp.setBpmDef(bpmDef);
            bpmDefExp.setBpmDefName(bpmDef.getName());

            String  actDefId=bpmDef.getActDefId();

            if(BpmDef.STATUS_DEPLOYED.equals(bpmDef.getStatus())){
                bpmDefExp.setDeployed(true);
            }

            if(StringUtils.isNotEmpty(actDefId)){
                ActReProcdef actProcDef = actReProcdefService.get(actDefId);
                bpmDefExp.setActProcDef(actProcDef);
            }

            String  actDepId=bpmDef.getActDepId();
            if(StringUtils.isNotEmpty(actDepId)){
                ActGeBytearray byteArray =actGeBytearrayService.getByDeployId(actDepId);
                bpmDefExp.setByteArrays(byteArray);
                ActReDeployment actDeployMent =actReDeploymentService.get(actDepId);
                bpmDefExp.setActDeployMent(actDeployMent);
            }
            bpmDefExps.add(bpmDefExp);

            // 加入表单配置
            getFormPcsAndMobileForms(bpmDef,bpmDefExp);
        }
        //日志
        LogContext.put(Audit.DETAIL,sb.toString());
        return bpmDefExps;
    }


    /**
     *  加入表单配置
     * @param bpmDef
     * @param bpmDefExp
     */
    private void getFormPcsAndMobileForms(BpmDef bpmDef,BpmDefExp bpmDefExp){
        String processKey = bpmDef.getKey();
        //扩展配置
        String extConfs= bpmDef.getExtConfs();

        JSONObject extConfsJson= JSONObject.parseObject(extConfs);
        //全局节点
        JSONObject process = extConfsJson.getJSONObject(BpmDef.PROCESS_NODE_PRE);

        Set<String> pcFormAlias = new HashSet<>();
        Set<String> mobleFormAlias = new HashSet<>();

        //全局表单
        JSONObject globalForm = process.getJSONObject("globalForm");
        getAliasList(pcFormAlias,mobleFormAlias,globalForm);
        //开始表单
        JSONObject startForm = process.getJSONObject("startForm");
        getAliasList(pcFormAlias,mobleFormAlias,startForm);
        //明细表单
        JSONObject detailForm = process.getJSONObject("detailForm");
        getAliasList(pcFormAlias,mobleFormAlias,detailForm);


        List<String> userTasks = new ArrayList<>();
        //设计XML
        String designXml = bpmDef.getDesignXml();
        if (StringUtils.isNotEmpty(designXml)){
            userTasks =getUserTaskId(designXml);
        }
        for (String userTask:userTasks) {
            JSONObject userTaskJson = extConfsJson.getJSONObject(userTask);
            if(BeanUtil.isEmpty(userTaskJson)){
                continue;
            }
            JSONObject form = userTaskJson.getJSONObject("form");
            getAliasList(pcFormAlias,mobleFormAlias,form);
        }
        // 加入表单配置
        exportByAlias(pcFormAlias,mobleFormAlias,bpmDefExp);

        //表单
        JSONObject boDefsJson = process.getJSONObject("boDefs");
        String boDefId = boDefsJson.getString("value");
        //导出BO定义
        exportBoDefByAlias(boDefId,bpmDefExp);
    }

    /**
     * 获取bo数据。
     * @param boDefId
     * @param bpmDefExp
     */
    private void exportBoDefByAlias(String boDefId,BpmDefExp bpmDefExp){
        if(StringUtils.isNotEmpty(boDefId)){
            String[] boDefIds = boDefId.split(",");
            JSONArray boDefs = new JSONArray();
            for(String id:boDefIds){
                Object boDef= formService.getFormBoDefByAlias(id);
                if(BeanUtil.isNotEmpty(boDef)){
                    boDefs.add(boDef);
                }
            }
            bpmDefExp.setSysBoDefs(boDefs);
        }
    }

    /**
     * 获取表单配置。
     * @param pcFormAlias
     * @param mobleFormAlias
     * @param bpmDefExp
     */
    private void exportByAlias(Set<String> pcFormAlias,Set<String> mobleFormAlias,BpmDefExp bpmDefExp){
        for (String alias:pcFormAlias) {
            FormPcDto formPc = formService.getFormPcByAlias(alias);
            if (BeanUtil.isNotEmpty(formPc)){
                bpmDefExp.getBpmFormPcViews().add(formPc);
            }
        }
        for (String alias:mobleFormAlias) {
            FormMobileDto formMobile = formService.getMobleFormByAlias(alias);
            if (BeanUtil.isNotEmpty(formMobile)){
                bpmDefExp.getMobileForms().add(formMobile);
            }
        }
    }


    /**
     * 获取表单别名。
     * @param pcFormAlias
     * @param mobleFormAlias
     * @param json
     */
    private void getAliasList(Set<String> pcFormAlias,Set<String> mobleFormAlias,JSONObject json){
        if(BeanUtil.isEmpty(json)){
            return;
        }
        JSONArray formpcArray = json.getJSONArray("formpc");
        for (Object formPc:formpcArray) {
            JSONObject formPcJson = (JSONObject)formPc;
            getAlias(pcFormAlias,formPcJson);
        }
        JSONArray mobileFormArray = json.getJSONArray("mobile");
        for (Object mobile:mobileFormArray) {
            JSONObject mobileFormJson = (JSONObject)mobile;
            getAlias(mobleFormAlias,mobileFormJson);
        }
    }

    private void getAlias(Set<String> list,JSONObject json){
        String alias = json.getString("alias");
        if (StringUtils.isNotEmpty(alias)){
            list.add(alias);
        }
    }

    /**
     * 在流程定义中获取任务的ID。
     * @param designXml
     * @return
     */
    private List<String> getUserTaskId(String designXml){
        List<String> userTasks = new ArrayList<>();
        cn.hutool.json.JSONObject xmlJsonObj = XML.toJSONObject(designXml);
        cn.hutool.json.JSONObject definitions = xmlJsonObj.getJSONObject("bpmn:definitions");
        if(BeanUtil.isEmpty(definitions)){
            return userTasks;
        }
        cn.hutool.json.JSONObject bpmProcess = definitions.getJSONObject("bpmn:process");
        if(BeanUtil.isEmpty(bpmProcess)){
            return userTasks;
        }

        Object obj = bpmProcess.get("bpmn:userTask");
        if(BeanUtil.isEmpty(obj)){
            return userTasks;
        }

        if(obj instanceof cn.hutool.json.JSONArray){
            cn.hutool.json.JSONArray bpmUserTasks =(cn.hutool.json.JSONArray)obj;
            for (Object user:bpmUserTasks) {
                cn.hutool.json.JSONObject userTask =(cn.hutool.json.JSONObject)user;
                userTasks.add(userTask.getStr("id"));
            }
        }
        if(obj instanceof cn.hutool.json.JSONObject){
            cn.hutool.json.JSONObject userTask =(cn.hutool.json.JSONObject)obj;
            userTasks.add(userTask.getStr("id"));
        }
        return userTasks;
    }
}
