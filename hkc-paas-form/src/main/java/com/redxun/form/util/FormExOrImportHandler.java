package com.redxun.form.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.MessageUtil;
import com.redxun.form.bo.entity.*;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.FormBoRelationServiceImpl;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.service.*;
import com.redxun.util.SysUtil;
import com.redxun.web.controller.IExport;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FormExOrImportHandler implements IExport {



    public  static final String FORM_SETTING="formSetting";
    public  static final String FORM_RIGHT="formRight";
    public  static final String FORM_FORMLA_MAPPINGS="formulaMappings";
    public  static final String FORM_FORMLAS="bpmTableFormulas";

    public  static final String FORM_PCVIEWS="formPcViews";
    public  static final String FORM_MOBILES="formMobiles";

    public  static final String SYS_BODEFS="sysBoDefs";
    public  static final String FORM_BO_ENTITY="formBoEntity";
    public  static final String DATASOURCE_ALIAS="dsAlias";

    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    FormPcServiceImpl formPcServiceImpl;
    @Autowired
    FormMobileServiceImpl formMobileService;
    @Autowired
    FormBoEntityServiceImpl boEntityService;
    @Autowired
    FormPermissionServiceImpl formPermissionServiceImpl;
    @Autowired
    FormTableFormulaServiceImpl formTableFormulaService;
    @Autowired
    FormBoDefServiceImpl formBoDefService;
    @Autowired
    FormBoRelationServiceImpl formBoRelationService;
    @Autowired
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Autowired
    FormDataSourceDefServiceImpl formDataSourceDefServiceImpl;
    @Autowired
    FormBoDefServiceImpl formBoDefServiceImpl;
    @Resource
    private FormBoAttrServiceImpl formBoAttrServiceImpl;
    @Resource
    private  FormDataService formDataService;

    /**
     * ??????????????????
     * @param file
     * @param treeId
     */
    @GlobalTransactional
    public List<AlterSql> importFormSulotionZip(MultipartFile file, String treeId){
        List<AlterSql> delaySqlList = new ArrayList<>();
        JSONArray formSultionArry  = readZipFile(file);

        String appId=formDataService.getAppIdByTreeId(treeId);
        for (Object obj:formSultionArry) {
            JSONObject sulotionObj = (JSONObject)obj;
            //???????????????????????????
            if(!isExistDataSource(sulotionObj)){
                MessageUtil.triggerException("?????????????????????????????????,???????????????????????????????????????","");
            }
            //??????????????????
            importFormSulotion(sulotionObj,treeId,appId);
            //??????????????????-??????????????????
            importFormPermission(sulotionObj.getJSONObject(FORM_RIGHT),appId);
            //??????????????????
            importTableFormulas(sulotionObj.getJSONArray(FORM_FORMLAS),appId);
            //4.?????????????????????
            //????????????/????????????
            List<AlterSql> sqls = importFormView(sulotionObj,treeId,appId);
            delaySqlList.addAll(sqls);
        }
        return delaySqlList;
    }

    /*
     * ?????????????????????????????????????????????
     * Elwin ZHANG 2021-4-7
     * @param jsonArray JSON??????
     */
    private boolean isExistDataSource(JSONObject sulotionObj){
        try{
            JSONObject boDef =(JSONObject)sulotionObj.getJSONArray(SYS_BODEFS).get(0);
            String dsAlias=boDef.getJSONObject(FORM_BO_ENTITY).getString(DATASOURCE_ALIAS);
            //???????????????????????????
            if("LOCAL".equals(dsAlias)){
                return  true;
            }
            return formDataSourceDefServiceImpl.isExistByAlias(dsAlias);
        }
        catch(Exception e){
            return  false;
        }
    }

   /*
     * ????????????/??????????????????
     * @param sulotionObj
     * @throws Exception
     */
    private List<AlterSql> importFormView(JSONObject sulotionObj, String treeId,String appId){
        //pc??????
        JSONArray formPcViews = sulotionObj.getJSONArray(FORM_PCVIEWS);
        importFormPcs(formPcViews,treeId,appId);
        //????????????
        JSONArray formMobiles = sulotionObj.getJSONArray(FORM_MOBILES);
        importFormMobiles(formMobiles,treeId,appId);
        //BO??????
        JSONArray sysBoDefs = sulotionObj.getJSONArray(SYS_BODEFS);
        List<AlterSql> delaySqlList =importFormBoDefs(sysBoDefs,treeId,appId);
        if(BeanUtil.isEmpty(delaySqlList)){
            delaySqlList = new ArrayList<>();
        }
        return delaySqlList;
    }




    public List<AlterSql> importFormBoDefs(JSONArray formBoDefs, String treeId,String appId) {
        List<AlterSql> delaySqlList =new ArrayList<>();
        for (int i = 0; i < formBoDefs.size(); i++) {
            JSONObject boDef = formBoDefs.getJSONObject(i);
            FormBoDef def=handBoDef(boDef);
            def.setAppId(appId);
            String boDefId=def.getId();
            FormBoEntity boEntity = def.getFormBoEntity();
            try {
                boEntity.setAppId(appId);
                FormBoEntity orignEnt= formBoEntityServiceImpl.getByDefId(boDefId,true);
                boEntity.setOriginEnt(orignEnt);
                formBoEntityServiceImpl.handEntity(boEntity);
                List<AlterSql>  sqls=formPcServiceImpl.handTable(boEntity,true,true,true);
                delaySqlList.addAll(sqls);
                addBoDef(def,treeId);
            }catch (Exception e){
                log.error("--FormExOrImportHandler.importFormBoDef is error---:"+e.getMessage());
                MessageUtil.triggerException("????????????",e.getMessage());
            }
        }
        return delaySqlList;
    }

    /**
     *  ??????FormBoEntity????????? ??????FormBoDef?????????
     */
    public FormBoDef handBoDef(JSONObject boDef){
        FormBoDef formBoDef = JSONObject.toJavaObject(boDef, FormBoDef.class);
        JSONObject formBoEntityObj = boDef.getJSONObject("formBoEntity");
        FormBoEntity formBoEntity = JSONObject.toJavaObject(formBoEntityObj, FormBoEntity.class);
        formBoDef.setFormBoEntity(formBoEntity);
        return formBoDef;
    }

    /**
     * ???bo???????????????
     * @param boDef
     */
    public void addBoDef(FormBoDef boDef,String treeId){
        if(StringUtils.isEmpty(treeId)){
            treeId=boDef.getTreeId();
        }
        String boDefId =boDef.getId();
        boDef.setTreeId(treeId);
        formBoDefServiceImpl.delete(boDefId);
        formBoDefServiceImpl.insert(boDef);

        List<FormBoRelation>  boRelations=boDef.getRelations();
        String appId=boDef.getAppId();
        for(FormBoRelation relation:boRelations){
            relation.setAppId(appId);
            //???????????????????????????????????????????????? ????????????ENT_ID?????????
            formBoRelationService.delByEntityId(relation.getEntId());
            //formBoRelationService.delete(relation.getId()); elwin notes at 2021-4-8
            formBoRelationService.insert(relation);
        }
        addBoEnt(boDef.getFormBoEntity(),treeId);
    }

    /**
     * ??????BO?????????
     * @param boEnt
     */
    public void addBoEnt(FormBoEntity boEnt,String treeId){
        boEnt.setTreeId(treeId);
        addEnt(boEnt);
        String appId=boEnt.getAppId();
        List<FormBoEntity> ents= boEnt.getBoEntityList();
        for(FormBoEntity ent:ents){
            ent.setTreeId(treeId);
            ent.setAppId(appId);
            addEnt(ent);
        }
    }

    private void addEnt(FormBoEntity ent){
        formBoEntityServiceImpl.delete(ent.getId());
        formBoEntityServiceImpl.insert(ent);
        String appId=ent.getAppId();
        for(FormBoAttr attr:ent.getBoAttrList()){
            attr.setAppId(appId);
            formBoAttrServiceImpl.delete(attr.getId());
            formBoAttrServiceImpl.insert(attr);
        }
    }

    public void  importFormMobiles(JSONArray formMobiles, String treeId,String appId) {
        for (Object formMobileObj:formMobiles) {
            JSONObject formMobileJson = (JSONObject)formMobileObj;
            FormMobile formMobile =JSONObject.toJavaObject(formMobileJson,FormMobile.class);
            String formMobileId = formMobile.getId();
            formMobileService.delete(formMobileId);
            formMobile.setCategoryId(treeId);
            formMobile.setAppId(appId);
            formMobileService.insert(formMobile);
        }
    }

    public void  importFormPcs(JSONArray formPcViews, String treeId,String appId) {
        for (Object fprmPcObj:formPcViews) {
            JSONObject fprmPcObjJson = (JSONObject)fprmPcObj;
            FormPc formPc =JSONObject.toJavaObject(fprmPcObjJson,FormPc.class);
            formPc.setAppId(appId);
            String formPcId = formPc.getId();
            formPcServiceImpl.delete(formPcId);
            formPc.setCategoryId(treeId);
            formPcServiceImpl.insert(formPc);
        }
    }


    /**
     * ??????????????????
     * @param bpmTableFormulas
     */
    private void importTableFormulas(JSONArray bpmTableFormulas,String appId){
        //3.????????????
        for (Object obj:bpmTableFormulas) {
            JSONObject formulaJson = (JSONObject)obj;
            FormTableFormula formula =JSONObject.toJavaObject(formulaJson,FormTableFormula.class);

            String formulaId = formula.getId();
            if(StringUtils.isEmpty(formulaId)){
                continue;
            }
            formula.setAppId(appId);
            FormTableFormula oldRecord=formTableFormulaService.get(formulaId);
            formTableFormulaService.delete(formulaId);
            formTableFormulaService.insert(formula);
        }
    }

    /**
     * ??????????????????-??????????????????
     * @param formRightJson
     */
    private void importFormPermission(JSONObject formRightJson,String appId){
        String id = formRightJson.getString("id");
        if (StringUtils.isEmpty(id)){
            return;
        }
        String formRightStr = formRightJson.toJSONString();
        FormPermission permission =JSONObject.parseObject(formRightStr,FormPermission.class);
        permission.setAppId(appId);
        FormPermission oldRecord=formPermissionServiceImpl.get(permission.getId());
        formPermissionServiceImpl.delete(id);
        formPermissionServiceImpl.insert(permission);
    }

    /**
     * ??????????????????
     * @param sulotionObj
     */
    private void importFormSulotion(JSONObject sulotionObj, String treeId,String appId){
        JSONObject formSetting = sulotionObj.getJSONObject(FORM_SETTING);
        if(BeanUtil.isNotEmpty(formSetting)){
            String formSettingStr = formSetting.toJSONString();
            FormSolution formSolution=JSONObject.parseObject(formSettingStr,FormSolution.class);
            formSolution.setCategoryId(treeId);
            formSolution.setAppId(appId);
            String id = formSolution.getId();
            FormSolution oldSolution = formSolutionService.get(id);
            if(BeanUtil.isNotEmpty(oldSolution)){
                    formSolutionService.update(formSolution);
            }else {
                formSolutionService.insert(formSolution);
            }
        }
    }

    /**
     * ???????????????????????????????????????
     * @param file
     * @return
     */
    public static JSONArray readZipFile(MultipartFile file){
        JSONArray formSultionArry = new JSONArray();
        try{
            InputStream is = file.getInputStream();
            // ?????????Zip????????????
            ZipArchiveInputStream zipIs = new ZipArchiveInputStream(is, "UTF-8");
            while ((zipIs.getNextZipEntry()) != null) {// ??????Zip??????????????????
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(zipIs, baos);
                String sulotionStr = baos.toString("UTF-8");
                JSONObject sulotionObj = JSON.parseObject(sulotionStr);
                formSultionArry.add(sulotionObj);
            }
            zipIs.close();
        }catch (Exception e){
            log.error("---FormExOrImportHandler.readZipFile is error =="+e.getMessage());
        }
        return formSultionArry;
    }

    /**
     * ??????????????????ID??????
     * @param id ????????????ID
     * @return
     */
    @Override
    public JSONObject doExportById(String id , StringBuilder sb ){
        JSONObject json = new JSONObject();
        FormSolution formSolution=formSolutionService.get(id);

        sb.append(formSolution.getName() +"("+id+"),");

        //1.????????????
        json.put(FORM_SETTING,formSolution);

        //2.????????????-??????????????????
        FormPermission permission= formPermissionServiceImpl.getByconfigId(FormPermission.TYPE_FORM_SOLUTION,id);
        if(BeanUtil.isEmpty(permission)){
            permission =new FormPermission();
        }
        json.put(FORM_RIGHT,permission);

        //3.????????????
        List<FormTableFormula> bpmTableFormulas=new ArrayList<>();
        String formulas = formSolution.getFormulas();
        if(StringUtils.isNotEmpty(formulas)){
            String[] formulaList = formulas.split("[,]");
            for (String formulaId:formulaList) {
                FormTableFormula formula = formTableFormulaService.get(formulaId);
                bpmTableFormulas.add(formula);
            }
        }
        json.put(FORM_FORMLAS,bpmTableFormulas);

        //4.?????????????????????
        exportByBoDefId(formSolution.getBodefId(),json);

        return json;
    }

    /**
     * ??????boDefId ????????????????????????
     * <pre>
     *     1.pc??????
     *     2.????????????
     *     3.bo??????
     *     4.bo??????
     *     5.bo??????
     * </pre>
     * @param boDefId
     * @param json
     */
    public void exportByBoDefId(String boDefId,JSONObject json){
        /**
         * pc??????
         */
        //1. ????????????
        List<FormPc> formPcViews = formPcServiceImpl.getByBoDefIdMainVersion(boDefId);
        if(BeanUtil.isEmpty(formPcViews)){
            formPcViews=new ArrayList<>();
        }
        json.put(FORM_PCVIEWS,formPcViews);

        /**
         * moble??????
         */
        //2. ??????????????????
       List<FormMobile> formMobiles= formMobileService.getByBoDefId(boDefId,null);
        if(BeanUtil.isEmpty(formMobiles)){
            formMobiles=new ArrayList<>();
        }
        json.put(FORM_MOBILES,formMobiles);
        //??????BO??????
        exportBoDefByAlias(boDefId,json);
    }

    private void exportBoDefByAlias(String boDefId,JSONObject json){
        JSONArray boDefs = new JSONArray();
        if(StringUtils.isNotEmpty(boDefId)){
            String[] boDefIds = boDefId.split(",");
            for(String id:boDefIds){
                FormBoDef boDef= formBoDefService.getById(id);
                List<FormBoRelation> relations=formBoRelationService.getByBoDefId(id);
                FormBoEntity boEnt=formBoEntityServiceImpl.getByDefId(id,true);
                boDef.setRelations(relations);
                boDef.setFormBoEntity(boEnt);
                if(BeanUtil.isNotEmpty(boDef)){
                    boDefs.add(boDef);
                }
            }
        }
        json.put(SYS_BODEFS,boDefs);
    }
}
