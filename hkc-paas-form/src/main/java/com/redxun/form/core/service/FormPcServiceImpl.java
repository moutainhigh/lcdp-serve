package com.redxun.form.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.Tree;
import com.redxun.common.utils.ContextUtil;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;
import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.OperatorContext;
import com.redxun.dto.form.DataResult;
import com.redxun.form.bo.entity.*;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.TableUtil;
import com.redxun.form.core.datahandler.impl.DbDataHandler;
import com.redxun.form.core.entity.*;
import com.redxun.form.core.listener.FormulaSetting;
import com.redxun.form.core.listener.FormulaSettingContext;
import com.redxun.form.core.mapper.FormPcMapper;
import com.redxun.form.util.FormCacheHandler;
import com.redxun.form.util.FormUtil;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
* [????????????]???????????????
*/
@Service
public class FormPcServiceImpl extends SuperServiceImpl<FormPcMapper, FormPc> implements BaseService<FormPc> {

    private final static String CTLTYPE = "ctltype";

    @Resource
    private FormPcMapper formPcMapper;
    @Resource
    private FormMobileServiceImpl formMobileServiceImpl;
    @Resource
    private FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Resource
    private FormBoDefServiceImpl formBoDefServiceImpl;
    @Resource
    private FormDefPermissionServiceImpl formDefPermissionServiceImpl;
    @Resource
    private FormTableFormulaServiceImpl formTableFormulaService;
    @Resource
    private FormSolutionServiceImpl formSolutionServiceImpl;
    @Resource
    private TableUtil tableUtil;
    @Resource
    CommonDao commonDao;
    @Resource
    DbDataHandler dbDataHandler;
    @Resource
    private FormBoEntityServiceImpl boEntityService;

    @Resource
    private  FormDataService formDataService;
    @Resource
    private FormPcHistoryServiceImpl formPcHistoryService;
    @Resource
    private FormChangeLogServiceImpl formChangeLogService;

    @Override
    public BaseDao<FormPc> getRepository() {
        return formPcMapper;
    }


    /**
     *  ???????????????????????????
     * @param formJson
     * @return
     */
    public boolean checkUniqueValue(JSONObject formJson){
        String alias=formJson.getString("alias");
        String keyName=formJson.getString("keyName");
        String keyValue=formJson.getString("keyValue");
        String pk=formJson.getString("pk");
        FormPc formPc= this.getByAlias(alias);
        //?????????????????????????????????
        if(BeanUtil.isEmpty(formPc)){
            return true;
        }
        //??????????????????????????????
        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(formPc.getBodefId());
        if(BeanUtil.isEmpty(boEntity)){
            return true;
        }
        List<FormBoAttr> boAttrList =boEntity.getBoAttrList();
        String  formKey = "";
        for (FormBoAttr attr:boAttrList) {
            if(keyName.equals(attr.getName())){
                formKey = attr.getFieldName();
                break;
            }
        }
        return dbDataHandler.checkUniqueValue(formKey,keyValue,pk,boEntity);
    }

    /**
     * ????????????????????????Id)?????????????????????????????????
     * @param filter
     * @return
     */
    public IPage<FormPc> searchOrderByType(QueryFilter filter){
        Map<String, Object> params = PageHelper.constructParams(filter);
        params.put("containType", FormPc.FORM_TYPE_SEL_DEV);
        IPage<FormPc> page= formPcMapper.query(filter.getPage(),params);
        return page;
    }

    /**
     *  ??????????????????ID????????????????????????
     * @param bodefId
     * @return
     */
    public List<FormPc> getByBoDefIdMainVersion(String bodefId){
        QueryWrapper queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("BODEF_ID_",bodefId);
        queryWrapper.eq("MAIN_","1");
        return formPcMapper.selectList(queryWrapper);
    }

    /**
     * 1. ???????????????
     * 2. ??????Bo ??? bo??????
     * 3. ????????????
     * 4. ???????????????????????? ????????????????????????????????????????????? ????????????????????????
     * @param json
     */
    public JsonResult saveForm(JSONObject json,StringBuilder sb){
        FormPc formPc= json.toJavaObject(FormPc.class);
        if(StringUtils.isEmpty(formPc.getType())){
            formPc.setType(FormPc.FORM_TYPE_ONLINE_DESIGN);
        }
        //????????????
        FormCacheHandler.clearForm(formPc.getAlias());

        formPc.setAlias(formPc.getAlias().trim());
        boolean isExist=isExist(formPc.getAlias(),formPc.getId());
        if(isExist){
            sb.append("????????????("+formPc.getAlias()+")?????????!");
            return JsonResult.Fail("?????????????????????!");
        }
        String appId=formDataService.getAppIdByTreeId(formPc.getCategoryId());
        formPc.setAppId(appId);
        FormBoEntity boEntity= parseHtml( formPc);
        boEntity.setAppId(appId);
        String dsAlias=json.getString("dsAlias");
        String dsName=json.getString("dsName");
        if(StringUtils.isNotEmpty(dsAlias) && StringUtils.isNotEmpty(dsAlias)){
            JSONObject datasource=new JSONObject();
            datasource.put("dsAlias",dsAlias);
            datasource.put("dsName",dsName);
            formPc.setDatasource(datasource.toJSONString());
        }
        String remark=json.getString("remark");
        boolean isAdd=saveForm(formPc,boEntity,remark);
        sb.append(formPc.getName() +"("+formPc.getId()+")");
        return JsonResult.Success("??????????????????!").setData(formPc.getId());
    }

    /**
     * ??????????????????????????????
     * <pre>
     *     ?????????????????????????????????????????????????????????????????????
     * </pre>
     * @param json
     * @return
     */
    public JsonResult validForm(JSONObject json) throws Exception {
        String dsAlias=json.getString("dsAlias");
        String dsName=json.getString("dsName");
        boolean gendb=json.getBoolean("gendb");
        FormPc formPc= json.toJavaObject(FormPc.class);
        boolean isExist=isExist(formPc.getAlias(),formPc.getId());
        StringBuilder sb=new StringBuilder();
        if(isExist){
            sb.append("????????????("+formPc.getAlias()+")?????????!");
            LogContext.put(Audit.DETAIL,sb.toString());
            return JsonResult.Fail("?????????????????????!");
        }

        FormBoEntity boEntity=parseHtml(formPc);
        boEntity.setGendb(gendb?1:0);
        if(StringUtils.isNotEmpty(formPc.getBodefId())){
            FormBoEntity oldBoEntity=formBoEntityServiceImpl.getByDefId(formPc.getBodefId(),false);
            if(StringUtils.isNotEmpty(oldBoEntity.getId())){
                dsAlias=oldBoEntity.getDsAlias();
                dsName=oldBoEntity.getDsName();
            }
        }
        dsAlias = StringUtils.isNotEmpty(dsAlias)?dsAlias : DataSourceUtil.LOCAL;
        dsName = StringUtils.isNotEmpty(dsName)?dsName : "??????";
        boEntity.setDsAlias(dsAlias);
        boEntity.setDsName(dsName);
        if(StringUtils.isNotEmpty( formPc.getId())){
            FormPc orignForm=formPcMapper.selectById(formPc.getId());
            String boDefId=orignForm.getBodefId();
            if(StringUtils.isNotEmpty(boDefId)){
                FormBoEntity orignEnt= formBoEntityServiceImpl.getByDefId(boDefId,true);
                boEntity.setOriginEnt(orignEnt);

                FormHelper.injectAttrJson(orignEnt,orignForm.getFormSettings(),orignForm.getDataSetting());
            }
        }

        formBoEntityServiceImpl.handEntity(boEntity);
        List<FormBoEntity> boEntityList=new ArrayList<>();
        boEntityList.add(boEntity);
        boEntityList.addAll(boEntity.getBoEntityList());

        List<AlterSql> delaySqlList= handTable(boEntity,true,true,false);
        // ?????????????????????JSON?????????
        boEntity.setBoEntityList(new ArrayList<>());
        boEntity.setOriginEnt(null);


        Map<String, List<FormBoEntity>> listMap = boEntityList.stream().collect(Collectors.groupingBy(p -> p.getType()));
        Map<String,Object> map=new HashMap<>();

        map.put("json",listMap);
        map.put("sqlList",delaySqlList);

        JsonResult jsonResult=  JsonResult.Success("??????????????????!");
        jsonResult.setData(map);

        sb.append("????????????"+formPc.getName()+"("+formPc.getAlias()+")????????????!");

        LogContext.put(Audit.DETAIL,sb.toString());

        return  jsonResult;
    }

    /**
     * ???????????????
     * <pre>
     *     1.????????????
     *     2.??????BO
     *     3.?????????????????????
     * </pre>
     * @param json
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult genForm(JSONObject json) throws Exception {
        String dsAlias=json.getString("dsAlias");
        String dsName=json.getString("dsName");
        boolean gendb=json.getBoolean("gendb");
        boolean delField=json.getBooleanValue("delField");
        boolean delTable=json.getBooleanValue("delTable");
        String genMode=json.getString("genMode");
        FormPc formPc= json.toJavaObject(FormPc.class);
        String appId=formDataService.getAppIdByTreeId(formPc.getCategoryId());
        formPc.setAppId(appId);
        //???????????????
        FormCacheHandler.clearForm(formPc.getAlias());

        if(StringUtils.isEmpty( formPc.getType())){
            formPc.setType(FormPc.FORM_TYPE_ONLINE_DESIGN);
        }
        //??????????????????,??????????????????easyform
        if(FormPc.FORM_TYPE_EASY_DESIGN.equals(formPc.getType())){
            genMode=FormBoEntity.GENMODE_EASYFORM;
        }

        if(StringUtils.isNotEmpty(dsAlias) && StringUtils.isNotEmpty(dsAlias)){
            JSONObject datasource=new JSONObject();
            datasource.put("dsAlias",dsAlias);
            datasource.put("dsName",dsName);
            formPc.setDatasource(datasource.toJSONString());
        }

        StringBuilder sb=new StringBuilder();

        boolean isExist=isExist(formPc.getAlias(),formPc.getId());
        if(isExist){
            sb.append("????????????["+formPc.getAlias()+"]?????????");
            return JsonResult.Fail("?????????????????????!");
        }
        if(StringUtils.isEmpty(formPc.getId())){
            LogContext.put(Audit.ACTION,Audit.ACTION_ADD);
            sb.append("????????????,");
        }
        else{
            LogContext.put(Audit.ACTION,Audit.ACTION_UPD);
            sb.append("????????????,");
        }

        //?????????????????????
        formPc.setTemplateTemp("");
        formPc.setJavascriptTemp("");
        formPc.setMetadataTemp("");

        String boDefId="";
        FormBoEntity boEntity=parseHtml(formPc);
        boEntity.setGendb(gendb?1:0);
        boEntity.setIsMain(1);
        boEntity.setAppId(appId);
        boEntity.setGenMode(StringUtils.isNotEmpty(genMode)?genMode:FormBoEntity.GENMODE_FORM);
        String isTenant=json.getString("isTenant");
        boEntity.setIsTenant(isTenant);
        if(StringUtils.isNotEmpty(formPc.getBodefId())){
            FormBoEntity oldBoEntity=formBoEntityServiceImpl.getByDefId(formPc.getBodefId(),false);
            if(StringUtils.isNotEmpty(oldBoEntity.getId())){
                dsAlias=oldBoEntity.getDsAlias();
                dsName=oldBoEntity.getDsName();
            }
        }
        dsAlias = StringUtils.isNotEmpty(dsAlias)?dsAlias : DataSourceUtil.LOCAL;
        dsName = StringUtils.isNotEmpty(dsName)?dsName : "??????";
        boEntity.setDsAlias(dsAlias);
        boEntity.setDsName(dsName);

        if(StringUtils.isNotEmpty( formPc.getId())){
            FormPc orignForm=formPcMapper.selectById(formPc.getId());
            String formSettings = orignForm.getFormSettings();
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isNotEmpty(formSettings)){
                jsonObject= JSONObject.parseObject(formSettings).getJSONObject("main");
            }
            boDefId=orignForm.getBodefId();
            if(StringUtils.isNotEmpty(boDefId)){
                FormBoEntity orignEnt= formBoEntityServiceImpl.getByDefId(boDefId,true);
                if(BeanUtil.isNotEmpty(jsonObject)){
                    List<FormBoAttr> boAttrList = orignEnt.getBoAttrList();
                    for (FormBoAttr formBoAttr : boAttrList) {
                        formBoAttr.setFormJson(jsonObject.getJSONObject(formBoAttr.getName()));
                    }
                }
                boEntity.setOriginEnt(orignEnt);
                //??????FormJson
                FormHelper.injectAttrJson(orignEnt,orignForm.getFormSettings(),orignForm.getDataSetting());
            }
        }
        formBoEntityServiceImpl.handEntity(boEntity);
        //?????????????????????
        JsonResult result=canExecute(boEntity);
        if(!result.isSuccess()){
            return result;
        }

        // 1. ??????BO
        FormBoDef boDef= formBoDefServiceImpl.saveBo(boEntity,boDefId,delField,delTable);
        // 2. ????????????
        formPc.setBodefId(boDef.getId());
        formPc.setBoDefAlias(boDef.getAlias());
        String remark=json.getString("remark");
        saveForm(formPc,boEntity,remark);
        // 3. ???????????????
        if(gendb){
            List<AlterSql> alterSqls = handTable(boEntity, delField, delTable, true);
        }

        //4.??????????????????
        boolean coverAppJs=json.getBooleanValue("coverAppJs");
        genMobileForm(formPc,coverAppJs);

        //?????????????????????isGenerateTable??????
        if(gendb){
            FormSolution formSolution = formSolutionServiceImpl.getByAlias(formPc.getAlias());
            if(formSolution != null){
                formSolution.setIsGenerateTable(1);
                formSolutionServiceImpl.update(formSolution);
            }
        }


        sb.append( formPc.getName() +"("+formPc.getId()+")");

        LogContext.put(Audit.PK,formPc.getId());
        LogContext.put(Audit.DETAIL,sb.toString());

        result= JsonResult.Success("??????????????????!");
        result.setData(formPc.getId());
        return result;
    }

    /**
     * ???????????????????????????
     * <pre>
     *
     * </pre>
     * @param formPc
     * @param coverAppJs ????????????app??????js
     * @return
     */
    private void genMobileForm(FormPc formPc,boolean coverAppJs){
        String boDefId=formPc.getBodefId();
        String alias=formPc.getAlias();
        String javascript=FormPc.FORM_TYPE_EASY_DESIGN.equals(formPc.getType())?formPc.getMobileJavascript():formPc.getJavascript();
        List<FormMobile> formMobiles=formMobileServiceImpl.getByBoDefId(boDefId,alias);
        if(formMobiles.size()>0){
            for(FormMobile formMobile:formMobiles){
                String formHtml = formMobile.getFormHtml();
                if(StringUtils.isNotEmpty(formHtml)){
                    JSONObject jsonObject = JSONObject.parseObject(formHtml);
                    List attrList = jsonObject.getObject("boAttrList", List.class);
                    List childrenList = jsonObject.getObject("childrens",List.class);

                    FormBoEntity boEntity= formBoEntityServiceImpl.getByDefId(boDefId,true);
                    List<FormBoAttr> boAttrList=formBoEntityServiceImpl.delRedundantFields(boEntity.getBoAttrList());
                    List<Tree> childrens=boEntity.getChildren();
                    String opinionDef = formPc.getOpinionDef();
                    //????????????
                    JSONArray opinionAttrs=new JSONArray();
                    if(StringUtils.isNotEmpty(opinionDef)){
                        opinionAttrs= JSON.parseArray(opinionDef);
                    }
                    if(opinionAttrs.size()>0){
                        for (int i = 0; i < opinionAttrs.size(); i++) {
                            JSONObject jsonObj = opinionAttrs.getJSONObject(i);
                            FormBoAttr opinionAttr=new FormBoAttr();
                            opinionAttr.setName(jsonObj.getString("name"));
                            opinionAttr.setComment(jsonObj.getString("comment"));
                            opinionAttr.setControl("rx-opinion");
                            boAttrList.add(opinionAttr);
                        }
                    }
                    //????????????
                    JSONArray mainBoAttrList = getUpdBoAttrList(boAttrList, attrList);
                    //?????? 6.6?????????????????????childrens??? 6.6?????????????????????boAttrList
                    if(BeanUtil.isNotEmpty(childrenList) && childrenList.size()>0){
                        jsonObject.put("childrens",getUpdChildrens(childrens, childrenList));
                    }else {
                        JSONArray subBoAttrList= getUpdChildrens(childrens, attrList);
                        mainBoAttrList.addAll(subBoAttrList);
                        JSONArray newBoAttrs=new JSONArray();
                        //???????????????????????????
                        for (int i = 0; i < attrList.size(); i++) {
                            JSONObject attr = (JSONObject) attrList.get(i);
                            for (int j = 0; j < mainBoAttrList.size(); j++) {
                                JSONObject jsonobj= mainBoAttrList.getJSONObject(j);
                                if(StringUtils.isEmpty(jsonobj.getString("alias")) && StringUtils.isEmpty(attr.getString("alias"))) {
                                    if(attr.getString("name").equals(jsonobj.getString("name"))){
                                        newBoAttrs.add(jsonobj);
                                        break;
                                    }
                                }else {
                                    if(attr.getString("alias").equals(jsonobj.getString("alias"))){
                                        newBoAttrs.add(jsonobj);
                                        break;
                                    }
                                }

                            }
                        }
                        mainBoAttrList=newBoAttrs;
                    }
                    jsonObject.put("boAttrList",mainBoAttrList);
                    formMobile.setFormHtml(jsonObject.toJSONString());
                }
                formMobile.setMetadata(formPc.getMetadata());
                if(coverAppJs){
                    formMobile.setScript(javascript);
                }else {
                    if(StringUtils.isNotEmpty(javascript)){
                        String oldScript = formMobile.getScript();
                        JSONObject extJs=JSON.parseObject(javascript).getJSONObject("extJs");
                        JSONObject oldScriptObj = JSON.parseObject(oldScript);
                        oldScriptObj.put("extJs",extJs);
                        formMobile.setScript(oldScriptObj.toJSONString());
                    }
                }
                formMobile.setAppId(formPc.getAppId());
                formMobileServiceImpl.update(formMobile);
            }
        }else{
            String buttonDef="[{\"name\":\"??????\",\"isPreDef\":true,\"type\":\"save\",\"method\":\"save\",\"setting\":\"\",\"needConfig\":false}]";
            String id = IdGenerator.getIdStr();
            FormMobile formMobile=new FormMobile();
            formMobile.setId(id);
            formMobile.setAppId(formPc.getAppId());
            formMobile.setType(FormPc.FORM_TYPE_EASY_DESIGN.equals(formPc.getType())?FormMobile.TYPE_EASY:FormMobile.TYPE_NORMAL);
            formMobile.setAlias(formPc.getAlias());
            formMobile.setBodefAlias(formPc.getAlias());
            formMobile.setScript(javascript);
            formMobile.setMetadata(formPc.getMetadata());
            formMobile.setName(formPc.getName());
            formMobile.setBodefId(boDefId);
            formMobile.setButtonDef(buttonDef);
            JSONObject jsonObject=new JSONObject();
            FormBoEntity boEntity= formBoEntityServiceImpl.getByDefId(boDefId,true);
            List<FormBoAttr> boAttrList=formBoEntityServiceImpl.delRedundantFields(boEntity.getBoAttrList());
            List<Tree> childrens=boEntity.getChildren();
            String opinionDef = formPc.getOpinionDef();
            //????????????
            JSONArray opinionAttrs=new JSONArray();
            if(StringUtils.isNotEmpty(opinionDef)){
                opinionAttrs= JSON.parseArray(opinionDef);
            }
            if(opinionAttrs.size()>0){
                for (int i = 0; i < opinionAttrs.size(); i++) {
                    JSONObject jsonObj = opinionAttrs.getJSONObject(i);
                    FormBoAttr opinionAttr=new FormBoAttr();
                    opinionAttr.setName(jsonObj.getString("name"));
                    opinionAttr.setComment(jsonObj.getString("comment"));
                    opinionAttr.setControl("rx-opinion");
                    boAttrList.add(opinionAttr);
                }
            }
            //????????????
            jsonObject.put("boAttrList",getMobileBoAttrList(boAttrList));
            //??????
            jsonObject.put("childrens",getMobileChildrens(childrens));
            formMobile.setCategoryId(formPc.getCategoryId());
            formMobile.setFormHtml(jsonObject.toJSONString());
            formMobile.setFormPcAlias(formPc.getAlias());
            formMobileServiceImpl.save(formMobile);
        }

    }

    /**
     * ???????????????????????????
     * <pre>
     *
     * </pre>
     * @param boEntity
     * @return
     */
    private JsonResult canExecute(FormBoEntity boEntity){
        if(boEntity.getGendb()==0) {
            return  JsonResult.Success();
        }
        JsonResult result=isTableExists(boEntity);
        return  result;
    }

    /**
     * ???????????????????????????????????????
     * <pre>
     *     ???????????????????????????????????????????????????
     * </pre>
     * @param boEntity
     * @return
     */
    private JsonResult isTableExists(FormBoEntity boEntity){
        ITableOperator tableOperator= OperatorContext.getByDsAlias(boEntity.getDsAlias());

        if(boEntity.getType().equals(TableUtil.OP_ADD)){
            boolean rtn= tableOperator.isTableExist(boEntity.getTableName());
            if(rtn){
                return JsonResult.Fail("??????"+boEntity.getTableName() + "?????????????????????");
            }
        }
        for(FormBoEntity subEnt:boEntity.getBoEntityList()){
            if(subEnt.getType().equals(TableUtil.OP_ADD)){
                boolean rtn= tableOperator.isTableExist(subEnt.getTableName());
                if(rtn){
                    return JsonResult.Fail("??????"+subEnt.getTableName() + "?????????????????????");
                }
            }
        }

        return  JsonResult.Success();
    }


    /**
     * ????????????????????????????????????
     * @param boEntity
     * @param removeField
     * @param removeTable
     * @return
     * @throws Exception
     */
    public List<AlterSql> handTable(FormBoEntity boEntity,boolean removeField,boolean removeTable,boolean excuteSql) throws Exception {
        if(boEntity.getGendb()==0){
            return  new ArrayList<>();
        }
        formBoEntityServiceImpl.injectMetaData4Attrs(boEntity);
        ITableOperator tableOperator= OperatorContext.getByDsAlias(boEntity.getDsAlias());
        //????????????
        List<AlterSql> list=new ArrayList<>();
        if(boEntity.getType().equals(TableUtil.OP_ADD) && !boEntity.external()){
            List<AlterSql> alterSqls=tableUtil.getCreateSqlByBoEnt(boEntity,tableOperator);
            list.addAll(alterSqls);
        }
        else{
            List<AlterSql> alterSqls=tableUtil.getAlterSql(boEntity,removeField);
            list.addAll(alterSqls);
        }

        //????????????
        List<FormBoEntity> entityList= boEntity.getBoEntityList();
        for(FormBoEntity ent:entityList){
            switch (ent.getType()){
                case TableUtil.OP_ADD:
                    List<AlterSql> alterSqls=tableUtil.getCreateSqlByBoEnt(ent,tableOperator);
                    list.addAll(alterSqls);
                    break;
                case  TableUtil.OP_UPD:
                    alterSqls=tableUtil.getAlterSql(ent,removeField);
                    list.addAll(alterSqls);
                    break;
                case  TableUtil.OP_DEL:
                    if(removeTable){
                        alterSqls=tableUtil.getDropSqlByBoEnt(ent,tableOperator);
                        list.addAll(alterSqls);
                    }
                    break;
                default:
                    break;
            }
        }
        List<AlterSql> delayList= list.stream().filter(p->p.isDelayExecute()).collect(Collectors.toList());

        List<AlterSql> sqlList= list.stream().filter(p->!p.isDelayExecute()).collect(Collectors.toList());

        if(excuteSql){
            //???????????????sql
            for(AlterSql alterSql:sqlList){
                if("1".equals(boEntity.getIsTenant())){
                    createFormChangeLog(boEntity,alterSql);
                }
                commonDao.execute(boEntity.getDsAlias(),alterSql.getSql());
            }
            //????????????sql
            for(AlterSql delaySql:delayList){
                if("1".equals(boEntity.getIsTenant())){
                    createFormChangeLog(boEntity,delaySql);
                }
            }
        }
        return delayList;

    }


    /**
     * ????????????????????????
     * @param formPc
     * @param formBoEntity
     * @param remark ??????
     * @return
     */
    private boolean saveForm(FormPc formPc,FormBoEntity formBoEntity,String remark){
        //???????????????????????????
        handForm( formPc, formBoEntity);
        boolean isAdd=true;
        //??????
        if(StringUtils.isEmpty(formPc.getId())) {
            String id = IdGenerator.getIdStr();
            formPc.setId(id);
            formPc.setVersion(1);
            formPc.setDeployed(0);
            formPc.setMain(1);
            formPc.setBoDefAlias(formBoEntity.getAlias());
            formPcMapper.insert(formPc);
            //??????????????????????????????(??????????????????)
            JSONObject permission = new JSONObject();
            permission.put("key", FormPermission.PERMISSION_EVERYONE);
            permission.put("label", "?????????");
            formDefPermissionServiceImpl.insert(new FormDefPermission().setFormId(formPc.getId()).setBoDefId(formPc.getBodefId())
                    .setAppId(formPc.getAppId()).setPermission(permission.toJSONString()));
        }
        else{
            isAdd=false;
            FormPc oldForm=formPcMapper.getById(formPc.getId());
            formPc.setCopyed(oldForm.getCopyed());
            formPcMapper.updateById(formPc);
            //??????????????????
            formPcHistoryService.insertFormPcHistory(oldForm,remark);
        }
        return isAdd;
    }

    /**
     * ????????????????????????????????????????????????
     * @param formPc
     * @param formBoEntity
     */
    private void handForm(FormPc formPc,FormBoEntity formBoEntity){

        JSONObject formJson=new JSONObject();
        JSONObject dataJson=new JSONObject();
        JSONObject tableButtons=new JSONObject();



        handEnt( formJson, dataJson,"main",formBoEntity);

        for(FormBoEntity subEnt:formBoEntity.getBoEntityList()){
            handEnt( formJson, dataJson,subEnt.getAlias(),subEnt);
            if(FormPc.FORM_TYPE_EASY_DESIGN.equals( formPc.getType()) ||
                    FormBoRelation.RELATION_ONETOMANY.equals( subEnt.getRelationType())){
                if(StringUtils.isNotEmpty( subEnt.getButtons())){
                    tableButtons.put(subEnt.getAlias(),JSONArray.parseArray( subEnt.getButtons()));
                }
            }
        }

        //???????????????????????????formSettings
        if(!FormPc.FORM_TYPE_EASY_DESIGN.equals( formPc.getType())){
            formPc.setFormSettings(formJson.toJSONString());
        }


        formPc.setDataSetting(dataJson.toJSONString());
        formPc.setTableButtonDef(tableButtons.toJSONString());
    }

    /**
     * ?????????????????????????????????
     * @param formJson
     * @param dataJson
     * @param key
     * @param formBoEntity
     */
    private void handEnt(JSONObject formJson,JSONObject dataJson,String key,FormBoEntity formBoEntity){
        List<FormBoAttr> list= formBoEntity.getBoAttrList();
        JSONObject data=new JSONObject();
        JSONObject form=new JSONObject();
        for(FormBoAttr attr:list){
            JSONObject dataSetting=attr.getDataJson();
            JSONObject formSetting=attr.getFormJson();
            if(BeanUtil.isNotEmpty(dataSetting)){
                data.put(attr.getName(),dataSetting);
            }
            if(BeanUtil.isNotEmpty(formSetting)){
                form.put(attr.getName(),formSetting);
            }
        }
        formJson.put(key,form);
        dataJson.put(key,data);
    }


    /**
     * ????????????
     * @param formPc
     * @return
     */
    private FormBoEntity parseHtml(FormPc formPc) {
        if (FormPc.FORM_TYPE_EASY_DESIGN.equals(formPc.getType())) {
            return parseEasyHtml(formPc);
        }
        return parseNormalHtml(formPc);
    }

    private FormBoEntity parseEasyHtml(FormPc formPc){
        JSONObject metaJson=JSONObject.parseObject(formPc.getMetadata());
        JSONArray pclist=metaJson.getJSONArray("pclist");
        JSONArray mainAry=new JSONArray();
        for(Object obj:pclist){
            JSONObject json=(JSONObject)obj;
            mainAry.addAll(json.getJSONArray("list"));
        }
        FormBoEntity mainEnt = FormUtil.parseList(mainAry,"");
        mainEnt.setName(formPc.getName()).setAlias(formPc.getAlias())
                .setGenMode(FormBoEntity.GENMODE_EASYFORM)
                .setTreeId(formPc.getCategoryId());
        //??????????????????
        for(FormBoEntity subEnt:mainEnt.getBoEntityList()){
            subEnt.setTreeId(mainEnt.getTreeId());
        }
        if(StringUtils.isEmpty(formPc.getPkId())){
            mainEnt.setVersionField(FormBoEntity.FIELD_UPDATE_VERSION);
        }
        formPc.setButtonDef(mainEnt.getButtons());
        return mainEnt;
    }

    private FormBoEntity parseNormalHtml(FormPc formPc){
        String template=formPc.getTemplate();
        JSONArray ary= JSONArray.parseArray(template);
        String content="";
        for(int i=0;i<ary.size();i++){
            JSONObject json=ary.getJSONObject(i);
            content+=json.getString("content");
        }
        String metadata=formPc.getMetadata();
        Document doc= Jsoup.parse(content);

        JSONObject metaJson=JSONObject.parseObject(metadata);

        FormBoEntity mainEnt=new FormBoEntity();
        mainEnt.setGenMode(FormBoEntity.GENMODE_FORM);
        mainEnt.setAlias(formPc.getAlias())
                .setName(formPc.getName()).setTreeId(formPc.getCategoryId());
        if(StringUtils.isEmpty(formPc.getPkId())){
            mainEnt.setVersionField(FormBoEntity.FIELD_UPDATE_VERSION);
        }
        if(metaJson!=null&&metaJson.size()>0){
            //???????????????
            Elements els =doc.select("[type=\"main\"]");
            for(Iterator<Element> it = els.iterator(); it.hasNext();) {
                Element el=it.next();
                String id=el.id();
                JSONObject fieldJson=metaJson.getJSONObject(id);
                if(fieldJson!=null&&fieldJson.size()>0){
                    FormBoAttr attr=getAttr(el,fieldJson);
                    mainEnt.addAttr(attr);
                }
            }

            //????????????
            Elements tables =doc.select("[ctltype=\"rx-table\"]");
            for(Iterator<Element> it = tables.iterator(); it.hasNext();) {
                Element table=it.next();
                FormBoEntity subBoEnt= handTable(table,metaJson);
                subBoEnt.setGenMode(FormBoEntity.GENMODE_FORM);
                subBoEnt.setTreeId(formPc.getCategoryId());
                if(StringUtils.isEmpty(formPc.getPkId())){
                    subBoEnt.setVersionField(FormBoEntity.FIELD_UPDATE_VERSION);
                }
                mainEnt.addBoEnt(subBoEnt);
            }

            Elements onetooneTables =doc.select("[ctltype=\"rx-table-onetoone\"]");
            for(Iterator<Element> it = onetooneTables.iterator(); it.hasNext();) {
                Element table=it.next();
                FormBoEntity subBoEnt= handOneToOneTable(table,metaJson);
                subBoEnt.setTreeId(mainEnt.getTreeId());
                mainEnt.addBoEnt(subBoEnt);
            }
        }
        JSONArray aryBtn=new JSONArray();
        //????????????
        Elements buttons =doc.select("[ctltype=\"rx-form-button\"]");
        for(Iterator<Element> it = buttons.iterator(); it.hasNext();) {
            Element btn=it.next();
            String alias=btn.attr("alias");
            JSONObject btnJson=new JSONObject();
            btnJson.put("alias",alias);
            btnJson.put("name",btn.text());
            aryBtn.add(btnJson);
        }

        JSONArray aryTab=new JSONArray();
        JSONObject mainTab=new JSONObject();
        mainTab.put("name","??????TAB");
        JSONArray tabChild=new JSONArray();
        mainTab.put("children",tabChild);
        for(int i=0;i<ary.size();i++){
            JSONObject json=ary.getJSONObject(i);
            JSONObject tabJson=new JSONObject();
            tabJson.put("name",json.getString("title"));
            tabChild.add(tabJson);
        }
        aryTab.add(mainTab);
        //????????????
        Elements tabs =doc.select("[ctltype=\"rx-tabs\"]");
        for(Iterator<Element> it = tabs.iterator(); it.hasNext();) {
            Element btn=it.next();
            String name=btn.attr("id");
            String tabsData=btn.attr("tabsdata");
            JSONObject tabJson=new JSONObject();
            tabJson.put("name",name);
            tabJson.put("children",JSONArray.parseArray(tabsData));
            aryTab.add(tabJson);
        }

        //????????????
        JSONArray opinionBtn=new JSONArray();
        Elements opinions =doc.select("[ctltype=\"rx-opinion\"]");
        for(Iterator<Element> it = opinions.iterator(); it.hasNext();) {
            Element opinion=it.next();
            String comment=opinion.attr("comment");
            String name=opinion.attr("name");
            JSONObject opinionJson=new JSONObject();
            opinionJson.put("comment",comment);
            opinionJson.put("name",name);
            opinionBtn.add(opinionJson);
        }

        formPc.setButtonDef(aryBtn.toJSONString());
        formPc.setTabDef(aryTab.toJSONString());
        formPc.setOpinionDef(opinionBtn.toJSONString());

        return mainEnt;
    }

    /**
     * ???????????????
     * @param table
     * @param metaJson
     * @return
     */
    private  FormBoEntity  handTable(Element table,JSONObject metaJson){
        String id=table.id();
        JSONObject tableJson=metaJson.getJSONObject(id);
        FormBoEntity ent=new FormBoEntity();
        ent.setName(tableJson.getString("comment").trim());
        ent.setAlias(tableJson.getString("name").trim());
        ent.setRelationType(FormBoRelation.RELATION_ONETOMANY);
        JSONArray ary=tableJson.getJSONArray("fields");
        JSONArray buttons=tableJson.getJSONArray("buttons");

        if(BeanUtil.isNotEmpty(buttons)){
            for(int i=0;i<buttons.size();i++){
                JSONObject json=buttons.getJSONObject(i);
                json.remove("conf");
                json.remove("mode");
                json.remove("idx_");
            }
            ent.setButtons(buttons.toJSONString());
        }

        Elements  fileds= table.select(".rx-table [type=\"onetomany\"]");
        if(fileds.size()==0){
            //???????????????????????????
            fileds=table.select(".rx-table-dialog [type=\"onetomany\"]");
        }
        Map<String, JSONObject>   fieldMap= ary.stream().collect(Collectors.toMap(i->{
            JSONObject json=(JSONObject)i;
            return json.getString("id");} ,
                p->{JSONObject json=(JSONObject)p;
                return json;}));

        for(Iterator<Element> it= fileds.iterator();it.hasNext();){
            Element fieldEl=it.next();
            String fieldId=fieldEl.id();
            JSONObject fieldJson= fieldMap.get(fieldId);

            FormBoAttr fieldAttr=getAttr(fieldEl,fieldJson);
            ent.addAttr(fieldAttr);
        }

        return  ent;

    }

    private  FormBoEntity  handOneToOneTable(Element table,JSONObject metaJson){
        String id=table.id();
        JSONObject tableJson=metaJson.getJSONObject(id);
        FormBoEntity ent=new FormBoEntity();
        ent.setName(tableJson.getString("comment"));
        ent.setAlias(tableJson.getString("name"));
        ent.setRelationType(FormBoRelation.RELATION_ONETOONE);
        JSONArray ary=tableJson.getJSONArray("fields");

        Elements  fileds= table.select("[type=\"onetoone\"]:not([ctltype=\"rx-table-onetoone\"])");
        Map<String, JSONObject>   fieldMap= ary.stream().collect(Collectors.toMap(i->{
                JSONObject json=(JSONObject)i;
                return json.getString("id");
            }
            , p->{
                JSONObject json=(JSONObject)p;return json;
            }
        ));

        for(Iterator<Element> it= fileds.iterator();it.hasNext();){
            Element fieldEl=it.next();
            String fieldId=fieldEl.id();
            JSONObject fieldJson= fieldMap.get(fieldId);

            FormBoAttr fieldAttr=getAttr(fieldEl,fieldJson);
            ent.addAttr(fieldAttr);
        }

        return  ent;

    }

    private FormBoAttr getAttr(Element el,JSONObject fieldJson){
        IAttrHandler handler=AttrHandlerContext.getAttrHandler(el.attr(CTLTYPE));
        FormBoAttr attr= handler.parse(el,fieldJson);
        return  attr;
    }

    private FormBoAttr getByAttrName(FormBoEntity boEntity,String ctlId){
        for(FormBoAttr boAttr : boEntity.getBoAttrList()){
            if(boAttr.getId().equals(ctlId)){
                return boAttr;
            }
        }
        return null;
    }



    /**
     * ?????????????????????
     * @param alias
     * @param id
     * @return
     */
    public boolean isExist(String alias,String id){
        Map<String,Object> params=new HashMap<>();
        params.put("alias",alias);
        if(StringUtils.isNotEmpty(id)){
            params.put("id",id);
        }
        Integer rtn=formPcMapper.isExist(params);
        return rtn>0;
    }

    public  FormPc getByFormId(String id){
        return formPcMapper.getById(id);
    }


    /**
     * ?????????????????????????????????
     * @param alias
     * @return
     */
    public FormPc getByAlias(String alias){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ALIAS_",alias);
        wrapper.eq("MAIN_",1);
        return formPcMapper.selectOne(wrapper);
    }

    /**
     * @Description:  ????????????ID??????????????????FormPc??????
     * @param entityId form_bo_entity????????????
     * @Author: Elwin ZHANG  @Date: 2021/8/30 15:43
     **/
    public  FormPc getByEntityId(String entityId){
        List<FormPc> list=formPcMapper.getByEntityId(entityId);
        if(list==null || list.size()==0){
            return  null;
        }
        return list.get(0) ;
    }
    /**
     * ????????????????????????????????????:
     *
     * @param json   {
     *           userinfo:{
     *               name:"",
     *               address:""
     *           }
     *       }
     *  @param  formulas ????????????ID?????????????????????
     *  @param  op      ????????????
     * @return
     */
    @Transactional
    public List<DataResult> saveData(JSONObject json,String formulas,String op,String opinion){
        if (StringUtils.isNotEmpty(formulas)) {
            List<FormTableFormula> formTableFormulas = new ArrayList<>();
            String[] ids = formulas.split(",");
            for (int i = 0; i < ids.length; i++) {
                FormTableFormula formula=formTableFormulaService.getById(ids[i]);
                if(formula==null){
                    continue;
                }
                if(MBoolean.YES.name().equals(formula.getEnabled())){
                    formTableFormulas.add(formula);
                }
            }
            FormulaSetting formulaSetting = new FormulaSetting();
            formulaSetting.setFormulaList(formTableFormulas);
            formulaSetting.getExtParams().put("op",op);
            formulaSetting.getExtParams().put("opinion",opinion);
            formulaSetting.getExtParams().put("mode", FormulaSetting.FLOW);
            FormulaSettingContext.setFormulaSetting(formulaSetting);
        }
        List<DataResult> dataResults = saveData(json);
        return dataResults;
    }

    /**
     * ??????JSON ?????????
     * @param json  ?????????????????????
     *              {
     *                 userinfo:{
     *                     name:"",
     *                     address:""
     *                 }
     *             }
     * @return
     */
    public List<DataResult> saveData(JSONObject json){
        List<DataResult> list=new ArrayList<>();
        for(Iterator<String> it= json.keySet().iterator();it.hasNext();){
            //????????????
            String key=it.next();
            //????????????
            JSONObject formData=json.getJSONObject(key);
            FormPc formPc=getByAlias(key);
            FormBoEntity boEntity = formBoEntityServiceImpl.getByDefId(formPc.getBodefId(), true);
            //???FormBoAttr??????formsetting??? datasetting
            FormHelper.injectAttrJson(boEntity,formPc.getFormSettings(),formPc.getDataSetting());
            DataResult result = dbDataHandler.save(formData,key,boEntity,false);
            list.add(result);
        }
        return list;
    }

    /**
     * ??????????????????ID????????????????????????
     * @param boDefId
     * @return
     */
    public List<FormPc> getByBoDefId(String boDefId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("BODEF_ID_",boDefId);
        return formPcMapper.selectList(wrapper);
    }

    public void parseMetadata(FormBoEntity boEntity,String templateAlias,String html,JSONObject metadataList){
        Document doc= Jsoup.parse(html);
        String openType="subWinOneColumn".equals(templateAlias) || "subWinTwoColumn".equals(templateAlias)?"window":"inner";
        //???????????????
        Elements els =doc.select("[type=\""+boEntity.getBoRelation().getType()+"\"]");
        String subId="";
        for(Iterator<Element> it = els.iterator(); it.hasNext();) {
            Element fieldEl=it.next();
            String id=fieldEl.attr("id");
            String type=fieldEl.attr("ctltype");
            if("rx-table".equals(type)){
                JSONObject rxGrid=JSONObject.parseObject("{\"comment\":\""+boEntity.getName()+"\",\"name\":\""+boEntity.getAlias()+"\",\"required\":false,\"mode\":\""+openType+"\",\"selMode\":\"multi\",\"width\":\"100%\",\"isTitle\":true,\"isserial\":false}");
                if("subWinOneColumn".equals(templateAlias)){
                    rxGrid.put("templateid","dOneColumn");
                }else if("subWinTwoColumn".equals(templateAlias)){
                    rxGrid.put("templateid","dTwoColumns");
                }
                JSONArray buttons=new JSONArray();
                buttons.add(JSONObject.parseObject("{\"name\":\"??????\",\"alias\":\"add\",\"type\":\"default\",\"openType\":\""+openType+"\"}"));
                buttons.add(JSONObject.parseObject("{\"name\":\"??????\",\"alias\":\"remove\",\"type\":\"default\",\"openType\":\""+openType+"\"}"));
                buttons.add(JSONObject.parseObject("{\"name\":\"??????\",\"alias\":\"up\",\"type\":\"default\",\"openType\":\""+openType+"\"}"));
                buttons.add(JSONObject.parseObject("{\"name\":\"??????\",\"alias\":\"down\",\"type\":\"default\",\"openType\":\""+openType+"\"}"));
                rxGrid.put("buttons",buttons);
                metadataList.put(id,rxGrid);
                subId=id;
            }else if("rx-table-onetoone".equals(type)){
                JSONObject rxGridOne=JSONObject.parseObject("{\"comment\":\""+boEntity.getName()+"\",\"name\":\""+boEntity.getAlias()+"\"}");
                metadataList.put(id,rxGridOne);
                subId=id;
            }
            String ctlid=fieldEl.attr("ctlid");
            String isattr=fieldEl.attr("isattr");
            if("false".equals(isattr)){
                continue;
            }
            FormBoAttr boAttr=getByAttrName(boEntity,ctlid);
            if(boAttr!=null){
                IAttrHandler attrHandler=AttrHandlerContext.getAttrHandler(type);
                if(StringUtils.isNotEmpty(subId)){
                    JSONObject rxGrid=metadataList.getJSONObject(subId);
                    Object fields=rxGrid.get("fields");
                    if(fields==null){
                        fields=new JSONArray();
                    }
                    JSONArray array=(JSONArray)fields;
                    JSONObject fieldJson=attrHandler.parseMetadata(boAttr,true);
                    //?????????????????????????????????
                    if(!"rx-ref".equals(fieldJson.getString("ctltype")) && !"rx-commonfield".equals(fieldJson.getString("ctltype"))){
                        fieldJson.put("id",id);
                        fieldJson.put("breadth",100);
                        array.add(fieldJson);
                        rxGrid.put("fields",array);
                    }
                    continue;
                }
                metadataList.put(id,attrHandler.parseMetadata(boAttr,false));
            }
        }
    }

    /**
     * ???????????????????????????
     * @param formId
     * @param genSolution ????????????????????????
     */
    public JsonResult updDeployStatus(String formId,boolean genSolution){

        JsonResult result=JsonResult.Success();
        FormPc formPc=formPcMapper.getById(formId);

        //?????????????????????
        boolean isGenenateTable = true;

        String detail="?????????:" + formPc.getName() +"(" + formPc.getAlias() +")????????????";
        if(genSolution){
            detail+=",?????????????????????";
        }
        LogContext.put(Audit.DETAIL,detail);



        int deployed= formPc.getDeployed();
        if(deployed==0){
            FormBoEntity formBoEntity= formBoEntityServiceImpl.getByDefId(formPc.getBodefId(),false);
            if(BeanUtil.isEmpty(formBoEntity)||StringUtils.isEmpty(formBoEntity.getTableName())){
                return new JsonResult().setSuccess(false).setMessage("???????????????,????????????!");
            }
            ITableOperator tableOperator= OperatorContext.getByDsAlias(formBoEntity.getDsAlias());
            boolean rtn= tableOperator.isTableExist(formBoEntity.getTableName());
            if (!rtn){
                isGenenateTable  = false;
            }
        }
        int status=(deployed==0)?1:0;
        formPc.setDeployed(status);
        formPcMapper.updateById(formPc);

        if(genSolution){
            if(StringUtils.isNotEmpty(formPc.getAlias())){
                FormSolution formSolution = formSolutionServiceImpl.getByAlias(formPc.getAlias());
                //??????????????????
                if(BeanUtil.isEmpty(formSolution)){
                    FormSolution newFormSolution=new FormSolution();
                    newFormSolution.setId(IdGenerator.getIdStr());
                    newFormSolution.setCategoryId(formPc.getCategoryId());
                    newFormSolution.setAlias(formPc.getAlias());
                    newFormSolution.setName(formPc.getName());
                    newFormSolution.setBodefId(formPc.getBodefId());
                    newFormSolution.setFormId(formPc.getId());
                    newFormSolution.setAppId(formPc.getAppId());
                    newFormSolution.setFormName(formPc.getAlias());
                    newFormSolution.setIsGenerateTable(isGenenateTable ? 1 : 0);
                    //??????????????????
                    JSONArray buttons=new JSONArray();
                    buttons.add(JSONObject.parse("{name:\"????????????\",method:\"startFlow\",style:\"primary\",idx_:1,needConfig:false,type:\"startFlow\",config:{},preDef:true,modeConfig:[\"edit\"],mode:\"edit\"}"));
                    buttons.add(JSONObject.parse("{name:\"??????\",method:\"submit\",style:\"default\",idx_:2,needConfig:false,type:\"submit\",config:{},preDef:true,modeConfig:[\"edit\"],mode:\"edit\"}"));
                    buttons.add(JSONObject.parse("{name:\"??????\",method:\"print\",style:\"default\",idx_:3,needConfig:false,type:\"print\",config:{},preDef:true,modeConfig:[\"edit\",\"detail\"],mode:\"edit,detail\"}"));
                    buttons.add(JSONObject.parse("{name:\"?????????\",method:\"flowImage\",style:\"default\",idx_:4,needConfig:false,type:\"flowImage\",config:{},preDef:true,modeConfig:[\"edit\",\"detail\"],mode:\"edit,detail\"}"));
                    newFormSolution.setButtonsSetting(buttons.toJSONString());
                    formSolutionServiceImpl.insert(newFormSolution);
                }else {
                    formSolution.setFormId(formPc.getId());
                    formSolution.setFormName(formPc.getAlias());
                    formSolution.setBodefId(formPc.getBodefId());
                    formSolution.setIsGenerateTable(isGenenateTable ? 1 : 0);
                    formSolutionServiceImpl.update(formSolution);
                }
            }
        }
        String msg=status==1?"??????????????????!":"?????????????????????!";

        result.setMessage(msg);

        return result;

    }


    /**
     * ????????????????????????????????????
     * @param alias
     * @return
     */
    public List<FormPc> getVersions(String alias){
        List list= formPcMapper.getVersions(alias);
        return list;
    }

    /**
     * ????????????????????????
     * @param formPc
     */
    public void switchMain(FormPc formPc){
        formPcMapper.updNotMain(formPc.getAlias());
        formPc.setMain(1);
        formPcMapper.updateById(formPc);
    }

    /**
     * ?????????????????????
     * <pre>
     *     1.????????????
     *     2.????????????ID
     * </pre>
     * @param formPc
     */
    public FormPc createVersion(FormPc formPc){
        Integer version= formPcMapper.getMaxVersion(formPc.getAlias());
        formPc.setId(IdGenerator.getIdStr());
        formPc.setMain(0);
        formPc.setVersion(version+1);
        formPc.setCreateTime(new Date());
        formPcMapper.insert(formPc);
        return formPc;
    }

    /**
     * ????????????
     * @param formPc
     * @param newAlias
     * @param newName
     */
    public void copyNewForm(FormPc formPc,String newAlias,String newName){
        String oldAlias = formPc.getAlias();
        formPc.setId(IdGenerator.getIdStr());
        formPc.setAlias(newAlias);
        formPc.setName(newName);
        formPc.setCreateTime(new Date());
        String curUserId = ContextUtil.getCurrentUserId();
        formPc.setCreateBy(curUserId);
        formPc.setCopyed(1);
        formPc.setUpdateTime(new Date());
        formPc.setUpdateBy(curUserId);
        formPcMapper.insert(formPc);
    }

    /**
     * 1. ???????????????
     */
    public JsonResult temporaryForm(JSONObject json,StringBuilder sb){
        FormPc formPc= json.toJavaObject(FormPc.class);
        if(StringUtils.isEmpty(formPc.getType())){
            formPc.setType(FormPc.FORM_TYPE_ONLINE_DESIGN);
        }
        //????????????
        FormCacheHandler.clearForm(formPc.getAlias());

        formPc.setAlias(formPc.getAlias().trim());
        boolean isExist=isExist(formPc.getAlias(),formPc.getId());
        if(isExist){
            sb.append("????????????("+formPc.getAlias()+")?????????!");
            return JsonResult.Fail("?????????????????????!");
        }
        String appId=formDataService.getAppIdByTreeId(formPc.getCategoryId());
        formPc.setAppId(appId);
        FormBoEntity boEntity= parseHtml( formPc);
        boEntity.setAppId(appId);
        String dsAlias=json.getString("dsAlias");
        String dsName=json.getString("dsName");
        if(StringUtils.isNotEmpty(dsAlias) && StringUtils.isNotEmpty(dsAlias)){
            JSONObject datasource=new JSONObject();
            datasource.put("dsAlias",dsAlias);
            datasource.put("dsName",dsName);
            formPc.setDatasource(datasource.toJSONString());
        }
        String remark=json.getString("remark");
        //??????
        if(StringUtils.isEmpty(formPc.getId())) {
            saveForm(formPc,boEntity,remark);
        }else {
            FormPc oldForm=formPcMapper.getById(formPc.getId());
            formPc.setCopyed(oldForm.getCopyed());
            //???????????????
            formPc.setTemplateTemp(formPc.getTemplate());
            formPc.setJavascriptTemp(formPc.getJavascript());
            formPc.setMetadataTemp(formPc.getMetadata());
            //?????????????????????
            formPc.setTemplate(oldForm.getTemplate());
            formPc.setJavascript(oldForm.getJavascript());
            formPc.setMetadata(oldForm.getMetadata());
            formPc.setTableButtonDef(oldForm.getTableButtonDef());
            formPc.setFormSettings(oldForm.getFormSettings());
            formPc.setDataSetting(oldForm.getDataSetting());

            formPcMapper.updateById(formPc);
            //??????????????????
            formPcHistoryService.insertFormPcHistory(oldForm,remark);
        }
        sb.append(formPc.getName() +"("+formPc.getId()+")");
        return JsonResult.Success("??????????????????!").setData(formPc.getId());
    }


    public void createFormChangeLog(FormBoEntity boEntity,AlterSql alterSql){
        int sn=1;
        Integer maxSn = formChangeLogService.getMaxSn();
        if(BeanUtil.isNotEmpty(maxSn)){
            sn= maxSn+1;
        }
        FormChangeLog formChangeLog=new FormChangeLog();
        formChangeLog.setBoAlias(boEntity.getAlias());
        formChangeLog.setBoName(boEntity.getName());
        formChangeLog.setSql(alterSql.getSql());
        formChangeLog.setType(alterSql.getType());
        formChangeLog.setSn(sn);
        formChangeLogService.insert(formChangeLog);
    }

    /**
     * ??????????????????????????? ????????????????????????
     * @param boAttrList
     */
    private  List<Map<String,Object>> getMobileBoAttrList(List<FormBoAttr> boAttrList){
        List<Map<String,Object>> newBoAttrList=new ArrayList<>();
        if(BeanUtil.isEmpty(boAttrList)){
            return newBoAttrList;
        }
        for (FormBoAttr formBoAttr : boAttrList) {
            Map<String,Object> map=new HashMap<>();
            map.put("name",formBoAttr.getName());
            map.put("comment",formBoAttr.getComment());
            map.put("fieldName",formBoAttr.getFieldName());
            map.put("control",formBoAttr.getControl());
            map.put("isSingle",formBoAttr.getIsSingle());
            newBoAttrList.add(map);
        }
        return newBoAttrList;
    }

  /**
     * ????????????????????? ????????????????????????
     * @param childrens
     */
    private  List<Map<String,Object>> getMobileChildrens(List childrens){
        List<Map<String,Object>> newChildrens=new ArrayList<>();
        if(BeanUtil.isEmpty(childrens)){
            return newChildrens;
        }
        for (int i = 0; i < childrens.size(); i++) {
            FormBoEntity formBoEntity = (FormBoEntity) childrens.get(i);
            Map<String,Object> map=new HashMap<>();
            map.put("alias",formBoEntity.getAlias());
            map.put("name",formBoEntity.getName());
            map.put("idField",formBoEntity.getIdField());
            map.put("tableName",formBoEntity.getTableName());
            map.put("boAttrList",getMobileBoAttrList(formBoEntity.getBoAttrList()));
            map.put("boEntityList",getMobileChildrens(formBoEntity.getBoEntityList()));
            newChildrens.add(map);
        }
        return newChildrens;
    }

    /**
     * ???????????????????????????????????????
     * ???????????????????????????
     * @param boAttrs
     * @param oldBoAttrs
     */
    private  JSONArray getUpdBoAttrList(List boAttrs,List oldBoAttrs){
        JSONArray newBoAttrList=new JSONArray();
        for (int i = 0; i < oldBoAttrs.size(); i++) {
            JSONObject oldObj = (JSONObject) oldBoAttrs.get(i);
            Boolean group = oldObj.getBoolean("group");
            if(BeanUtil.isNotEmpty(group) && group){
                JSONArray list = oldObj.getJSONArray("list");
                JSONArray oldList = getUpdBoAttrList(boAttrs, list);
                oldObj.put("list",oldList);
                newBoAttrList.add(oldObj);
            }else {
                for (int j = 0; j < boAttrs.size(); j++) {
                    FormBoAttr obj = (FormBoAttr) boAttrs.get(j);
                    if(oldObj.getString("name").equals(obj.getName())){
                        newBoAttrList.add(oldObj);
                    }
                }
            }
        }
        return newBoAttrList;
    }
 /**
     * ???????????????????????????????????????
     * @param childrens
     * @param oldChildrens
     */
    private  JSONArray getUpdChildrens(List childrens,List oldChildrens){
        JSONArray newChildrens=new JSONArray();
        if(childrens==null || oldChildrens==null){
            return newChildrens;
        }
        for (int i = 0; i < oldChildrens.size(); i++) {
            JSONObject oldObj = (JSONObject) oldChildrens.get(i);
            if(StringUtils.isEmpty(oldObj.getString("alias"))) continue;
            for (int j = 0; j < childrens.size(); j++) {
                FormBoEntity obj = (FormBoEntity) childrens.get(j);
                if(oldObj.getString("name").equals(obj.getName())){
                    List<FormBoAttr> boAttrs = obj.getBoAttrList();
                    JSONArray boAttrList = oldObj.getJSONArray("boAttrList");
                    //??????
                    oldObj.put("boAttrList",getUpdBoAttrList(boAttrs,boAttrList.toJavaList(Object.class)));
                    newChildrens.add(oldObj);
                }
            }
        }
        return newChildrens;
    }
}
