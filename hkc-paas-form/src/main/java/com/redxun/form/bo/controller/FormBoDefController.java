package com.redxun.form.bo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
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
import com.redxun.common.utils.*;
import com.redxun.dto.form.FormBoAttrDto;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.dto.form.FormConst;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.*;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.bo.service.FormBoRelationServiceImpl;
import com.redxun.form.core.datahandler.IDataHandler;
import com.redxun.form.core.datahandler.impl.DbDataHandler;
import com.redxun.form.core.entity.FormDefPermission;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.service.FormDataService;
import com.redxun.form.core.service.FormDefPermissionServiceImpl;
import com.redxun.form.core.service.FormMobileServiceImpl;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/form/bo/formBoDef")
@ClassDefine(title = "????????????",alias = "formBoDefController",path = "/form/bo/formBoDef",packages = "bo",packageName = "??????????????????")
@Api(tags = "????????????")
public class FormBoDefController extends BaseController<FormBoDef> {

    @Autowired
    FormBoDefServiceImpl formBoDefServiceImpl;
    @Autowired
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Autowired
    FormPcServiceImpl formPcServiceImpl;
    @Autowired
    FormMobileServiceImpl formMobileService;
    @Autowired
    FormDefPermissionServiceImpl formDefPermissionServiceImpl;
    @Autowired
    FormBoRelationServiceImpl formBoRelationService;
    @Autowired
    private IDataHandler dataHandler;
    @Resource
    private FormExOrImportHandler formExOrImportHandler;
    @Resource
    SystemClient systemClient;
    @Resource
    private FormDataService formDataService;

    @Override
    public BaseService getBaseService() {
        return formBoDefServiceImpl;
    }

    @Override
    public String getComment() {
        return "????????????";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
        super.handleFilter(filter);
    }



    @Override
    protected JsonResult beforeSave(FormBoDef ent) {
        boolean isExist= formBoDefServiceImpl.isExist(ent);
        if(isExist){
            return JsonResult.Fail("?????????????????????!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "????????????ID??????Bo??????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????ID", varName = "pkId")})
    @ApiOperation("????????????ID??????Bo??????")
    @GetMapping("/getById")
    public FormBoDef getById(@RequestParam(value="pkId")String pkId){
        return formBoDefServiceImpl.get(pkId);
    }

    /**
     * ????????????Bo??????
     * @param sysBoDefJson
     * @return
     */
    @MethodDefine(title = "????????????Bo??????", path = "/importFormBoDef", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????JSON", varName = "sysBoDefJson")})
    @ApiOperation(value="????????????Bo??????")
    @PostMapping("importFormBoDef")
    public List<AlterSql> importFormBoDef(@RequestBody  JSONObject sysBoDefJson) {
        String treeId = sysBoDefJson.getString("treeId");
        String appId=formDataService.getAppIdByTreeId(treeId);
        JSONArray formBoDefs = sysBoDefJson.getJSONArray("sysBoDefs");
        List<AlterSql> delaySqlList =new ArrayList<>();
        for(Object boDef:formBoDefs){
            String boDefStr = JSONObject.toJSONString(boDef);
            FormBoDef def = JSONObject.parseObject(boDefStr,FormBoDef.class);
            def.setAppId(appId);
            def.setTreeId(treeId);
            String boDefId=def.getId();
            FormBoEntity boEntity = def.getFormBoEntity();
            boEntity.setTreeId(treeId);
            boEntity.setAppId(appId);
            try {
                FormBoEntity orignEnt= formBoEntityServiceImpl.getByDefId(boDefId,true);
                boEntity.setOriginEnt(orignEnt);
                formBoEntityServiceImpl.handEntity(boEntity);
                List<AlterSql>  sqls=formPcServiceImpl.handTable(boEntity,true,true,true);
                delaySqlList.addAll(sqls);
                formExOrImportHandler.addBoDef(def,"");
            }catch (Exception e){
                log.error("--FormBoDefController.importFormBoDef is error---:"+e.getMessage());
            }
        }
        return delaySqlList;
    }

    /**
     * ??????????????????bo
     * @param alias
     * @return
     */
    @MethodDefine(title = "??????????????????bo", path = "/getFormBoDefByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "alias")})
    @ApiOperation(value="??????????????????bo")
    @GetMapping("/getFormBoDefByAlias")
    public FormBoDef getFormBoDefByAlias(@RequestParam(value="alias") String alias){
        FormBoDef boDef= formBoDefServiceImpl.getByAlias(alias);
        if(BeanUtil.isEmpty(boDef)){
            return boDef;
        }
        try {
            String bodefId = boDef.getId();
            List<FormBoRelation> relations=formBoRelationService.getByBoDefId(bodefId);
            FormBoEntity boEnt=formBoEntityServiceImpl.getByDefId(bodefId,true);
            boDef.setRelations(relations);
            boDef.setFormBoEntity(boEnt);
        }catch (Exception e){
            log.error("--FormBoDefController.getFormBoDefByAlias is error---:"+e.getMessage());
        }
        return  boDef;
    }

    /**
     * ??????Bo???????????????????????????Json??????
     * @param pkId
     * @return
     */
    @MethodDefine(title = "??????Bo??????Id???????????????Json??????", path = "/getBoJson", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo??????Id", varName = "pkId")})
    @ApiOperation(value="??????Bo??????Id???????????????Json??????")
    @GetMapping("/getBoJson")
    public JsonResult getBoJson(@RequestParam(value="pkId") String pkId){
        FormBoEntity boEntity= formBoEntityServiceImpl.getByDefId(pkId,true);
        boEntity=formBoEntityServiceImpl.getFormBoAttrByEnt(boEntity);
        FormBoDef boDef= formBoDefServiceImpl.getById(pkId);
        JSONObject json=new JSONObject();
        json.put("boEnt", boEntity);
        json.put("hasGen", boDef.getSupportDb());
        return new JsonResult(true,json,"????????????!").setShow(false);
    }

    /**
     * ??????????????????ID???????????????????????????
     * @param pkId
     * @return
     */
    @MethodDefine(title = "??????????????????ID???????????????????????????", path = "/getBoDef", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo??????Id", varName = "pkId")})
    @ApiOperation(value="??????????????????ID???????????????????????????")
    @GetMapping("/getBoDef")
    public JsonResult getBoEnt(@RequestParam(value="pkId") String pkId){
        FormBoEntity boEntity= formBoEntityServiceImpl.getByDefId(pkId,true);
        FormBoDef boDef= formBoDefServiceImpl.getById(pkId);
        boDef.setFormBoEntity(boEntity);
        //?????????????????????
        formBoDefServiceImpl.getSunBoRelation(boDef);
        return new JsonResult(true,boDef,"????????????!").setShow(false);
    }

    @MethodDefine(title = "??????JSON??????", path = "/saveBoDef", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "JSON??????", varName = "formBoDef")})
    @ApiOperation(value="??????JSON??????")
    @PostMapping("/saveBoDef")
    public JsonResult saveBoEnt(@RequestBody FormBoDef formBoDef, BindingResult validResult) throws Exception {
        if (StringUtils.isEmpty(formBoDef.getId())) {
            formBoDef.setGenType(FormBoDef.GEN_TYPE_DIRECT);
        }
        JsonResult jsonResult = save(formBoDef,validResult);
        formBoDefServiceImpl.saveBoRelation(formBoDef);
        return jsonResult;
    }

    @MethodDefine(title = "??????????????????????????????", path = "/savePermission", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "JSON??????", varName = "json")})
    @ApiOperation(value = "??????????????????????????????")
    @PostMapping("/savePermission")
    public JsonResult savePermission(@RequestBody JSONObject json) {
        JsonResult result = null;
        try {
            result = formBoDefServiceImpl.savePermission(json);
        } catch (Exception e) {
            result = JsonResult.Fail("???????????????");
        }
        return result;
    }

    @MethodDefine(title = "??????????????????JSON??????", path = "/getJsonData", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo??????ID", varName = "pkId")})
    @ApiOperation(value = "??????????????????JSON??????")
    @GetMapping("/getJsonData")
    public JsonResult getJsonData(@RequestParam(value = "pkId") String pkId) {
        JSONObject data = new JSONObject();
        FormBoDef boDef = formBoDefServiceImpl.get(pkId);
        if (dataHandler != null) {
            data = dataHandler.getInitDataByBoAlias(boDef.getAlias());
        }
        return new JsonResult(true, data, "???????????????").setShow(false);
    }

    @MethodDefine(title = "??????????????????", path = "/getRelForm", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo??????ID", varName = "pkId")})
    @ApiOperation(value="??????????????????")
    @GetMapping("/getRelForm")
    public JsonResult getRelForm(@RequestParam(value="pkId") String pkId) {
        JSONObject data = new JSONObject();
        //pc??????
        List<FormPc> formViews = formPcServiceImpl.getByBoDefId(pkId);
        //????????????
        List<FormMobile> formMobiles = formMobileService.getByBoDefId(pkId,null);
        //????????????
        Map<String, FormDefPermission> formDefPermissions = formDefPermissionServiceImpl.getByBoDefIdMap(pkId);

        //pc??????
        JSONArray formAry = new JSONArray();
        for (FormPc view : formViews) {
            JSONObject obj = new JSONObject();
            obj.put("id", view.getId());
            obj.put("boDefId", view.getBodefId());
            obj.put("name", view.getName());
            obj.put("alias", view.getAlias());
            FormDefPermission formDefPermission = formDefPermissions.get(view.getId());
            if (BeanUtil.isNotEmpty(formDefPermission)) {
                obj.put("level", formDefPermission.getLevel());
                String permission = formDefPermission.getPermission();
                if (StringUtils.isNotEmpty(permission)) {
                    JSONObject json = JSONObject.parseObject(permission);
                    obj.put("permission", json.getString("key"));
                    obj.put("permission_name", json.getString("label"));
                }
            }else{
                obj.put("level",1);
                obj.put("permission","everyone");
                obj.put("permission_name","?????????");
            }
            formAry.add(obj);
        }
        data.put("pc",formAry);
        //????????????
        JSONArray mobileAry = new JSONArray();
        for (FormMobile view : formMobiles) {
            JSONObject obj = new JSONObject();
            obj.put("id", view.getId());
            obj.put("boDefId", view.getBodefId());
            obj.put("name", view.getName());
            obj.put("alias", view.getAlias());
            FormDefPermission formDefPermission = formDefPermissions.get(view.getId());
            if (BeanUtil.isNotEmpty(formDefPermission)) {
                obj.put("level", formDefPermission.getLevel());
                String permission = formDefPermission.getPermission();
                if (StringUtils.isNotEmpty(permission)) {
                    JSONObject json = JSONObject.parseObject(permission);
                    obj.put("permission", json.getString("key"));
                    obj.put("permission_name", json.getString("label"));
                }
            }else{
                obj.put("level",1);
                obj.put("permission","everyone");
                obj.put("permission_name","?????????");
            }
            mobileAry.add(obj);
        }
        data.put("mobile",mobileAry);
        return new JsonResult(true,data,"???????????????").setShow(false);
    }

    @MethodDefine(title = "??????????????????", path = "/removeAttr", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????ID", varName = "attrId")})
    @ApiOperation(value="??????????????????")
    @GetMapping("/removeAttr")
    public JsonResult removeAttr(@RequestParam(value="attrId")String attrId){
        JsonResult rtn=new JsonResult(true, "???????????????!");
        try{
            formBoEntityServiceImpl.removeAttr(attrId);
        } catch (Exception e) {
            rtn = new JsonResult(false, "???????????????!");
        }

        return rtn;
    }

    @MethodDefine(title = "?????????????????????????????????", path = "/getBoDefConstruct", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value="?????????????????????????????????")
    @GetMapping("/getBoDefConstruct")
    public JsonResult getBoDefConstruct(HttpServletRequest request) {
        String boDefId = RequestUtil.getString(request, "boDefId");
        Boolean isMain = RequestUtil.getBoolean(request, "isMain", false);
        String entName = RequestUtil.getString(request, "entName", null);
        JSONArray jsonAry = formBoDefServiceImpl.getBoDefConstruct(boDefId, isMain, entName);
        return JsonResult.getSuccessResult(jsonAry).setShow(false);
    }

    @MethodDefine(title = "??????Bo??????ID??????????????????????????????", path = "/getBosByDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo??????ID", varName = "boDefId")})
    @ApiOperation(value="??????Bo??????ID??????????????????????????????")
    @GetMapping("/getBosByDefId")
    public JsonResult getBosByDefId(@RequestParam(value = "boDefId") String boDefId) {
        FormBoEntity boEnt = formBoEntityServiceImpl.getByDefId(boDefId, true);
        JSONArray jsonAry = new JSONArray();
        JSONObject mainTable = new JSONObject();
        mainTable.put("value", boEnt.getAlias());
        mainTable.put("label", "??????-" + boEnt.getName());
        mainTable.put("isMain", "yes");
        jsonAry.add(mainTable);
        List<FormBoEntity> boEnts = boEnt.getBoEntityList();
        for (FormBoEntity ent : boEnts) {
            JSONObject subTable = new JSONObject();
            subTable.put("value", ent.getAlias());
            subTable.put("label", "??????-" + ent.getName());
            subTable.put("isMain", "no");
            jsonAry.add(subTable);
        }
        return JsonResult.getSuccessResult(jsonAry).setShow(false);
    }

    @ApiOperation(value="??????Bo??????ID??????????????????????????????")
    private List<FormBoAttrDto> genItems(List<FormBoAttr> boAttrs){
        List<FormBoAttrDto> list=new ArrayList<>();
        for(FormBoAttr att:boAttrs){
            FormBoAttrDto dto=new FormBoAttrDto();
            try {
                BeanUtil.copyNotNullProperties(dto, att);
            }catch (Exception e){
                e.printStackTrace();
            }
            list.add(dto);
        }
        return list;
    }



    @MethodDefine(title = "??????boDefId???????????????JSON??????", path = "/getDataByBoDef", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "boDefId", varName = "boDefId"),@ParamDefine(title = "pk", varName = "pk")})
    @ApiOperation(value="??????boDefId???????????????JSON??????")
    @GetMapping("/getDataByBoDef")
    public JSONObject getDataByBoDef(@RequestParam(value = "boDefId")String boDefId,@RequestParam(value = "pk")String pk){
        DbDataHandler handler= SpringUtil.getBean(DbDataHandler.class);
        FormBoDef formBoDef = formBoDefServiceImpl.get(boDefId);
        JSONObject json=  handler.getDataById(formBoDef.getAlias(), pk);
        JSONObject returnJson = new JSONObject();
        //main
        JSONObject main=new JSONObject();
        JSONObject sub=new JSONObject();
        for(String key :json.keySet()){
            if(!key.startsWith(FormConst.SUB_PRE)) {
                main.put(key, json.get(key));
            }
            else{
                sub.put(key.replaceFirst(FormConst.SUB_PRE, ""), json.get(key));
            }

        }
        returnJson.put("main", main);
        returnJson.put("sub", sub);
        return returnJson;
    }


    /**
     * ??????bo????????????????????????????????????
     * {boAlias1:pk1,boAlias2:pk2}
     * @param keyPkJson
     * @return
     */
    @MethodDefine(title = "??????boDef?????????????????????JSON??????", path = "/getByBpmInstData",
            method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "bpminstdata????????????", varName = "keyPkJson")})
    @ApiOperation(value="??????boDefId???????????????JSON??????")
    @PostMapping("/getByBpmInstData")
    public  JSONObject getByBpmInstData(@RequestBody   JSONObject keyPkJson){

        JSONObject rtnJson=new JSONObject();
        DbDataHandler handler= SpringUtil.getBean(DbDataHandler.class);
        Set<String> keys = keyPkJson.keySet();
        for(String alias :keys){
            JSONObject json = handler.getDataById(alias, keyPkJson.getString(alias));
            rtnJson.put(alias,json);
        }
        return rtnJson;
    }

    @MethodDefine(title = "??????Bo????????????????????????", path = "/getBoFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo????????????", varName = "boAlias")})
    @ApiOperation(value="??????Bo????????????????????????")
    @GetMapping("/getBoFields")
    public FormBoEntityDto getBoFields(@RequestParam(value = "boAlias") String boAlias) {
        FormBoDef formBoDef=formBoDefServiceImpl.getByAlias(boAlias);
        if(formBoDef==null){
            return null;
        }

        FormBoEntityDto formBoEntityDto=getByBoDefId(formBoDef.getId());
        return formBoEntityDto;
    }

    @MethodDefine(title = "??????Bo????????????????????????", path = "/getBoFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo????????????", varName = "boAlias")})
    @ApiOperation(value="??????Bo????????????????????????")
    @GetMapping("/getEntByBoAlias")
    public Map<String,FormBoEntityDto> getEntByBoAlias(@RequestParam(value = "boAlias") String boAlias) {

        Map<String,FormBoEntityDto> map=new HashMap<>();
        String[] aliasAry=boAlias.split(",");
        for(String alias:aliasAry){
            FormBoDef formBoDef=formBoDefServiceImpl.getByAlias(alias);
            if(formBoDef==null){
                continue;
            }

            FormBoEntityDto formBoEntityDto=getByBoDefId(formBoDef.getId());
            map.put(alias,formBoEntityDto);
        }
        return map;
    }

    private FormBoEntityDto getByBoDefId(String boDefId){
        FormBoEntity boEnt = formBoEntityServiceImpl.getByDefId(boDefId, true);
        FormBoEntityDto formBoEntityDto=new FormBoEntityDto();
        try {
            BeanUtil.copyNotNullProperties(formBoEntityDto, boEnt);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        formBoEntityDto.setRelationType(boEnt.getBoRelation().getType());

        List<FormBoAttrDto> attrs=genItems(boEnt.getBoAttrList());
        formBoEntityDto.setBoAttrs(attrs);

        List<FormBoEntity> boEnts = boEnt.getBoEntityList();
        List<FormBoEntityDto> subBoList=new ArrayList<>();
        for (FormBoEntity ent : boEnts) {
            FormBoEntityDto subDto=new FormBoEntityDto();
            try {
                BeanUtil.copyNotNullProperties(subDto, ent);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            subDto.setRelationType(ent.getBoRelation().getType());
            List<FormBoAttrDto> subAttrs=genItems(ent.getBoAttrList());
            subDto.setBoAttrs(subAttrs);
            subBoList.add(subDto);
        }
        formBoEntityDto.setSubBoList(subBoList);
        return formBoEntityDto;
    }


    @MethodDefine(title = "??????BoDefId??????????????????", path = "/getBoIdFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "boDefId", varName = "boDefId")})
    @ApiOperation(value="??????BoDefId??????????????????")
    @GetMapping("/getBoIdFields")
    public FormBoEntityDto getBoIdFields(@RequestParam(value = "boDefId") String boDefId) {
        if(StringUtils.isEmpty(boDefId)){
            return null;
        }
        FormBoEntityDto formBoEntityDto=getByBoDefId(boDefId);
        return  formBoEntityDto;
    }

    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "del")
    @Override
    public JsonResult del(@RequestParam(value = "ids") String ids){
        JsonResult jsonResult=new JsonResult();
        if (StringUtils.isEmpty(ids)) {
            return jsonResult.setSuccess(false).setMessage("????????????!");
        }
        formBoDefServiceImpl.delBoDef(ids);
        return jsonResult.setSuccess(true).setMessage("????????????!");
    }

    @MethodDefine(title = "??????BO????????????BODEFID", path = "/getIdByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "boAlias", varName = "boAlias")})
    @ApiOperation(value="??????BO????????????BODEFID")
    @GetMapping("/getIdByAlias")
    public JSONObject getIdByAlias(@RequestParam(value = "boAlias") String boAlias) {
        if(StringUtils.isEmpty(boAlias)){
            return new JSONObject();
        }
        List<String> list=new ArrayList<>();
        String[] aliasAry=boAlias.split(",");
        for(String alias:aliasAry){
            FormBoDef boDef= formBoDefServiceImpl.getByAlias(alias);
            if(boDef!=null){
                list.add(boDef.getId());
            }

        }
        String  boListId= StringUtils.join(list,",");
        JSONObject rtn=new JSONObject();
        rtn.put("boDefId",boListId);
        return rtn;
    }

    @MethodDefine(title = "????????????????????????????????????????????????", path = "/getByTenant", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "getByTenant")})
    @ApiOperation(value="????????????????????????????????????????????????", notes="????????????????????????????????????????????????")
    @PostMapping(value="/getByTenant")
    public JsonPageResult getByTenant(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");
        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String url=request.getRequestURI();

            long start=System.currentTimeMillis();
            String currentTenantId= ContextUtil.getCurrentTenantId();
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            filter.addQueryParam("Q_TENANT_ID__S_EQ",currentTenantId);
            //????????????
            if (DbLogicDelete.getLogicDelete()) {
                filter.addQueryParam("Q_DELETED__S_EQ","0");
            }
            QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
            IPage page= getBaseService().query(filter);
            handlePage(page);
            jsonResult.setPageData(page);

            logger.info("url:" +url +",escape time:" + (System.currentTimeMillis()-start) +"ms");
        }catch (Exception ex){
            jsonResult.setSuccess(false);
            logger.error(ExceptionUtil.getExceptionMessage(ex));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }
}
