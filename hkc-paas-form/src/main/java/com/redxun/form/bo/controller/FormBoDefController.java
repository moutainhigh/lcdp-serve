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
@ClassDefine(title = "业务模型",alias = "formBoDefController",path = "/form/bo/formBoDef",packages = "bo",packageName = "业务模型管理")
@Api(tags = "业务模型")
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
        return "业务模型";
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
            return JsonResult.Fail("业务模型已存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "根据主键ID获取Bo定义", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "主键ID", varName = "pkId")})
    @ApiOperation("根据主键ID获取Bo定义")
    @GetMapping("/getById")
    public FormBoDef getById(@RequestParam(value="pkId")String pkId){
        return formBoDefServiceImpl.get(pkId);
    }

    /**
     * 导入单据Bo定义
     * @param sysBoDefJson
     * @return
     */
    @MethodDefine(title = "导入单据Bo定义", path = "/importFormBoDef", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据实体JSON", varName = "sysBoDefJson")})
    @ApiOperation(value="导入单据Bo定义")
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
     * 备份业务模型bo
     * @param alias
     * @return
     */
    @MethodDefine(title = "备份业务模型bo", path = "/getFormBoDefByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "alias")})
    @ApiOperation(value="备份业务模型bo")
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
     * 根据Bo主键获取实体定义的Json结构
     * @param pkId
     * @return
     */
    @MethodDefine(title = "根据Bo定义Id获取实体的Json结构", path = "/getBoJson", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义Id", varName = "pkId")})
    @ApiOperation(value="根据Bo定义Id获取实体的Json结构")
    @GetMapping("/getBoJson")
    public JsonResult getBoJson(@RequestParam(value="pkId") String pkId){
        FormBoEntity boEntity= formBoEntityServiceImpl.getByDefId(pkId,true);
        boEntity=formBoEntityServiceImpl.getFormBoAttrByEnt(boEntity);
        FormBoDef boDef= formBoDefServiceImpl.getById(pkId);
        JSONObject json=new JSONObject();
        json.put("boEnt", boEntity);
        json.put("hasGen", boDef.getSupportDb());
        return new JsonResult(true,json,"获取成功!").setShow(false);
    }

    /**
     * 根据业务定义ID获取业务定义的结构
     * @param pkId
     * @return
     */
    @MethodDefine(title = "根据业务定义ID获取业务定义的结构", path = "/getBoDef", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义Id", varName = "pkId")})
    @ApiOperation(value="根据业务定义ID获取业务定义的结构")
    @GetMapping("/getBoDef")
    public JsonResult getBoEnt(@RequestParam(value="pkId") String pkId){
        FormBoEntity boEntity= formBoEntityServiceImpl.getByDefId(pkId,true);
        FormBoDef boDef= formBoDefServiceImpl.getById(pkId);
        boDef.setFormBoEntity(boEntity);
        //获取孙实体关系
        formBoDefServiceImpl.getSunBoRelation(boDef);
        return new JsonResult(true,boDef,"获取成功!").setShow(false);
    }

    @MethodDefine(title = "实体JSON数据", path = "/saveBoDef", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "JSON数据", varName = "formBoDef")})
    @ApiOperation(value="实体JSON数据")
    @PostMapping("/saveBoDef")
    public JsonResult saveBoEnt(@RequestBody FormBoDef formBoDef, BindingResult validResult) throws Exception {
        if (StringUtils.isEmpty(formBoDef.getId())) {
            formBoDef.setGenType(FormBoDef.GEN_TYPE_DIRECT);
        }
        JsonResult jsonResult = save(formBoDef,validResult);
        formBoDefServiceImpl.saveBoRelation(formBoDef);
        return jsonResult;
    }

    @MethodDefine(title = "保存业务模型表单权限", path = "/savePermission", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "JSON数据", varName = "json")})
    @ApiOperation(value = "保存业务模型表单权限")
    @PostMapping("/savePermission")
    public JsonResult savePermission(@RequestBody JSONObject json) {
        JsonResult result = null;
        try {
            result = formBoDefServiceImpl.savePermission(json);
        } catch (Exception e) {
            result = JsonResult.Fail("保存失败！");
        }
        return result;
    }

    @MethodDefine(title = "获取业务模型JSON数据", path = "/getJsonData", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义ID", varName = "pkId")})
    @ApiOperation(value = "获取业务模型JSON数据")
    @GetMapping("/getJsonData")
    public JsonResult getJsonData(@RequestParam(value = "pkId") String pkId) {
        JSONObject data = new JSONObject();
        FormBoDef boDef = formBoDefServiceImpl.get(pkId);
        if (dataHandler != null) {
            data = dataHandler.getInitDataByBoAlias(boDef.getAlias());
        }
        return new JsonResult(true, data, "获取成功！").setShow(false);
    }

    @MethodDefine(title = "获取关联表单", path = "/getRelForm", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义ID", varName = "pkId")})
    @ApiOperation(value="获取关联表单")
    @GetMapping("/getRelForm")
    public JsonResult getRelForm(@RequestParam(value="pkId") String pkId) {
        JSONObject data = new JSONObject();
        //pc表单
        List<FormPc> formViews = formPcServiceImpl.getByBoDefId(pkId);
        //手机表单
        List<FormMobile> formMobiles = formMobileService.getByBoDefId(pkId,null);
        //权限属性
        Map<String, FormDefPermission> formDefPermissions = formDefPermissionServiceImpl.getByBoDefIdMap(pkId);

        //pc表单
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
                obj.put("permission_name","所有人");
            }
            formAry.add(obj);
        }
        data.put("pc",formAry);
        //手机表单
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
                obj.put("permission_name","所有人");
            }
            mobileAry.add(obj);
        }
        data.put("mobile",mobileAry);
        return new JsonResult(true,data,"获取成功！").setShow(false);
    }

    @MethodDefine(title = "删除实体属性", path = "/removeAttr", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实体属性ID", varName = "attrId")})
    @ApiOperation(value="删除实体属性")
    @GetMapping("/removeAttr")
    public JsonResult removeAttr(@RequestParam(value="attrId")String attrId){
        JsonResult rtn=new JsonResult(true, "删除列成功!");
        try{
            formBoEntityServiceImpl.removeAttr(attrId);
        } catch (Exception e) {
            rtn = new JsonResult(false, "删除列失败!");
        }

        return rtn;
    }

    @MethodDefine(title = "获取业务定义数据的结构", path = "/getBoDefConstruct", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "请求数据", varName = "request")})
    @ApiOperation(value="获取业务定义数据的结构")
    @GetMapping("/getBoDefConstruct")
    public JsonResult getBoDefConstruct(HttpServletRequest request) {
        String boDefId = RequestUtil.getString(request, "boDefId");
        Boolean isMain = RequestUtil.getBoolean(request, "isMain", false);
        String entName = RequestUtil.getString(request, "entName", null);
        JSONArray jsonAry = formBoDefServiceImpl.getBoDefConstruct(boDefId, isMain, entName);
        return JsonResult.getSuccessResult(jsonAry).setShow(false);
    }

    @MethodDefine(title = "根据Bo定义ID获取业务实体定义描述", path = "/getBosByDefId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义ID", varName = "boDefId")})
    @ApiOperation(value="根据Bo定义ID获取业务实体定义描述")
    @GetMapping("/getBosByDefId")
    public JsonResult getBosByDefId(@RequestParam(value = "boDefId") String boDefId) {
        FormBoEntity boEnt = formBoEntityServiceImpl.getByDefId(boDefId, true);
        JSONArray jsonAry = new JSONArray();
        JSONObject mainTable = new JSONObject();
        mainTable.put("value", boEnt.getAlias());
        mainTable.put("label", "主表-" + boEnt.getName());
        mainTable.put("isMain", "yes");
        jsonAry.add(mainTable);
        List<FormBoEntity> boEnts = boEnt.getBoEntityList();
        for (FormBoEntity ent : boEnts) {
            JSONObject subTable = new JSONObject();
            subTable.put("value", ent.getAlias());
            subTable.put("label", "从表-" + ent.getName());
            subTable.put("isMain", "no");
            jsonAry.add(subTable);
        }
        return JsonResult.getSuccessResult(jsonAry).setShow(false);
    }

    @ApiOperation(value="根据Bo定义ID获取业务实体定义描述")
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



    @MethodDefine(title = "根据boDefId和主键获取JSON数据", path = "/getDataByBoDef", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "boDefId", varName = "boDefId"),@ParamDefine(title = "pk", varName = "pk")})
    @ApiOperation(value="根据boDefId和主键获取JSON数据")
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
     * 根据bo别名和主键获取业务数据。
     * {boAlias1:pk1,boAlias2:pk2}
     * @param keyPkJson
     * @return
     */
    @MethodDefine(title = "根据boDef别名和主键获取JSON数据", path = "/getByBpmInstData",
            method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "bpminstdata列表数据", varName = "keyPkJson")})
    @ApiOperation(value="根据boDefId和主键获取JSON数据")
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

    @MethodDefine(title = "根据Bo别名获取单据实体", path = "/getBoFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义别名", varName = "boAlias")})
    @ApiOperation(value="根据Bo别名获取单据实体")
    @GetMapping("/getBoFields")
    public FormBoEntityDto getBoFields(@RequestParam(value = "boAlias") String boAlias) {
        FormBoDef formBoDef=formBoDefServiceImpl.getByAlias(boAlias);
        if(formBoDef==null){
            return null;
        }

        FormBoEntityDto formBoEntityDto=getByBoDefId(formBoDef.getId());
        return formBoEntityDto;
    }

    @MethodDefine(title = "根据Bo别名获取单据实体", path = "/getBoFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "Bo定义别名", varName = "boAlias")})
    @ApiOperation(value="根据Bo别名获取单据实体")
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


    @MethodDefine(title = "根据BoDefId获取单据实体", path = "/getBoIdFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "boDefId", varName = "boDefId")})
    @ApiOperation(value="根据BoDefId获取单据实体")
    @GetMapping("/getBoIdFields")
    public FormBoEntityDto getBoIdFields(@RequestParam(value = "boDefId") String boDefId) {
        if(StringUtils.isEmpty(boDefId)){
            return null;
        }
        FormBoEntityDto formBoEntityDto=getByBoDefId(boDefId);
        return  formBoEntityDto;
    }

    @ApiOperation(value = "删除业务模型")
    @PostMapping(value = "del")
    @Override
    public JsonResult del(@RequestParam(value = "ids") String ids){
        JsonResult jsonResult=new JsonResult();
        if (StringUtils.isEmpty(ids)) {
            return jsonResult.setSuccess(false).setMessage("删除失败!");
        }
        formBoDefServiceImpl.delBoDef(ids);
        return jsonResult.setSuccess(true).setMessage("删除成功!");
    }

    @MethodDefine(title = "根据BO别名获取BODEFID", path = "/getIdByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "boAlias", varName = "boAlias")})
    @ApiOperation(value="根据BO别名获取BODEFID")
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

    @MethodDefine(title = "根据条件查询包含平台表单数据记录", path = "/getByTenant", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "getByTenant")})
    @ApiOperation(value="根据条件查询包含平台表单数据记录", notes="根据条件查询包含平台表单数据记录")
    @PostMapping(value="/getByTenant")
    public JsonPageResult getByTenant(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult=JsonPageResult.getSuccess("返回数据成功!");
        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String url=request.getRequestURI();

            long start=System.currentTimeMillis();
            String currentTenantId= ContextUtil.getCurrentTenantId();
            QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
            filter.addQueryParam("Q_TENANT_ID__S_EQ",currentTenantId);
            //逻辑删除
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
