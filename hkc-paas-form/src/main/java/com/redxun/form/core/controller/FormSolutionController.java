package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.cache.CacheUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.dto.form.FormSolutionDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormResult;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.form.core.service.FormSolutionServiceImpl;
import com.redxun.form.core.service.FormTableFormulaServiceImpl;
import com.redxun.form.util.FormExOrImportHandler;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.controller.IExport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/form/core/formSolution")
@ClassDefine(title = "????????????",alias = "formSolutionController",path = "/form/core/formSolution",packages = "core",packageName = "????????????")
@Api(tags = "????????????")
public class FormSolutionController extends BaseController<FormSolution> {

    @Autowired
    FormSolutionServiceImpl formSolutionService;
    @Autowired
    FormPcServiceImpl formPcServiceImpl;
    @Resource
    private FormExOrImportHandler formExOrImportHandler;
    @Autowired
    SystemClient systemClient;
    @Resource
    FormTableFormulaServiceImpl formTableFormulaService;

    @Resource
    FormBoEntityServiceImpl formBoEntityServiceImpl;

    @Override
    protected IExport getExport() {
        return formExOrImportHandler;
    }

    @Override
    public BaseService getBaseService() {
        return formSolutionService;
    }

    @Override
    public String getComment() {
        return "????????????";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","FORM","read");

        super.handleFilter(filter);
    }

    private void removeCache(String key){
        CacheUtil.remove(FormSolutionServiceImpl.REGION_FORMSOLUTION,formSolutionService.getCacheKey(key));
    }


    protected JsonResult beforeSave(FormSolution ent) {
        boolean isExist=formSolutionService.isExist(ent);
        if(isExist){
            return JsonResult.Fail("?????????????????????!");
        }
        //???IS_GENERATE_TABLE_???????????????
        String formId=ent.getFormId();
        FormPc pc=formPcServiceImpl.getById(formId);
        if("GENBYBO".equals(pc.getType())){
            ent.setIsGenerateTable(1);
        }else{
            FormBoEntity formBoEntity = formBoEntityServiceImpl.getByDefId(pc.getBodefId(),false);
            if(null != formBoEntity &&  null != formBoEntity.getGendb()  && formBoEntity.getGendb().intValue() == 0){
                ent.setIsGenerateTable(0);
            }else {
                ent.setIsGenerateTable(1);
            }

        }
        //????????????
        removeCache(ent.getAlias());

        return JsonResult.Success();
    }


    /**
     * ??????????????????
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "??????????????????", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "request")})
    @PostMapping("/doImport")
    @AuditLog(operation = "????????????")
    @ApiOperation("????????????")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("???????????????????????????????????????");
        }
        String treeId=request.getParameter("treeId");
        List<AlterSql> delaySqlList = formExOrImportHandler.importFormSulotionZip(file,treeId);
        return JsonResult.Success().setData(delaySqlList).setMessage("????????????");
    }

    @MethodDefine(title = "??????????????????????????????????????????", path = "/getTreeByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "alias"),@ParamDefine(title = "??????", varName = "pk")})
    @ApiOperation("??????????????????????????????????????????")
    @GetMapping(value = "getTreeByAlias")
    public JSONObject getTreeByAlias(@RequestParam(value="alias")String alias,
                                     @RequestParam(value = "pk",required = false)String pk){
        return formSolutionService.getTreeByAlias(alias,pk);
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * @param alias
     * @param pk
     * @param jsonParams {param1:val1,param2:val2}
     * @return
     */
    @MethodDefine(title = "?????????????????????????????????????????????????????????????????????", path = "/getByAlias", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????", varName = "alias"),@ParamDefine(title = "??????", varName = "pk"),@ParamDefine(title = "??????", varName = "setting")})
    @ApiOperation("???????????????????????????????????????????????????????????????????????????????????????????????????????????????")
    @PostMapping(value = "getByAlias")
    public FormResult getByAlias(@RequestParam(value = "alias")String alias,
                                 @RequestParam(value = "pk",required = false)String pk,
                                 @RequestBody(required = false) JSONObject jsonParams){
        String params="";
        if(jsonParams!=null){
            params=jsonParams.toJSONString();
        }
        FormResult formResult=formSolutionService.getFormData(alias,pk,params);

        return formResult;
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getButtonsByFormJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????", varName = "formSolutionAlias"),@ParamDefine(title = "????????????", varName = "formJson")})
    @ApiOperation("????????????????????????????????????")
    @PostMapping(value = "getButtonsByFormJson")
    public FormResult getButtonsByFormJson(@RequestParam(value = "formSolutionAlias")String formSolutionAlias,@RequestBody JSONObject formJson){
        FormResult formResult=formSolutionService.getButtonsByFormJson(formSolutionAlias,formJson);
        return formResult;
    }


    /**
     * ??????????????????
     * @param alias
     * @param pk
     * @param jsonParams
     * @return
     */
    @MethodDefine(title = "????????????????????????", path = "/getMobileByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "alias"),@ParamDefine(title = "??????", varName = "pk"),
                    @ParamDefine(title = "?????????????????????", varName = "jsonParams")})
    @ApiOperation("????????????????????????")
    @GetMapping(value = "getMobileByAlias")
    public FormResult getMobileByAlias(@RequestParam(value = "alias")String alias,
                                       @RequestParam(value = "pk",required = false)String pk ,
                                       @RequestBody(required = false) JSONObject jsonParams){
        String params="";
        if(jsonParams!=null){
            params=jsonParams.toJSONString();
        }
        FormResult formResult=formSolutionService.getMobileData(alias,pk,params);
        return formResult;
    }

    @MethodDefine(title = "?????????????????????????????????ID", path = "/getFlowDefId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "params")})
    @ApiOperation("?????????????????????????????????ID")
    @PostMapping(value = "getFlowDefId")
    public JsonResult getFlowDefId(@RequestBody JSONObject params){
        String flowDefMapping=params.getString("flowDefMapping");
        JSONObject data=params.getJSONObject("formJson");
        String flowDefId=formSolutionService.getDefIdByDefMapping(flowDefMapping,data);
        return JsonResult.getSuccessResult(flowDefId,"????????????").setShow(false);
    }

    @GetMapping(value = "getFormSolutionByAlias")
    public FormSolutionDto getFormSolutionByAlias(@RequestParam(value = "alias")String alias){
        return formSolutionService.getFormSolutionByAlias(alias);
    }
    /**
     * ??????????????????
     * <pre>
     *     1.???????????????????????????
     *      1.??????????????????
     *          ??????????????????????????????????????????????????????
     *      2.?????????????????????
     *          ??????????????????????????????
     *
     *     2.???????????????????????????
     *
     * </pre>
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "??????????????????", path = "/saveForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "jsonObject")})
    @ApiOperation("??????????????????")
    @AuditLog(operation = "??????????????????")
    @PostMapping(value = "saveForm")
    public JsonResult saveForm(@RequestBody JSONObject jsonObject){
        try{
            JsonResult jsonResult = formSolutionService.handData(jsonObject);
            return jsonResult;
        }
        catch (Exception ex){
            formSolutionService.handException(ex);
            return null;
        }
    }

    /**
     * ???Id??????
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "???Id??????", path = "/removeById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "jsonObject")})
    @ApiOperation("???Id??????")
    @AuditLog(operation = "??????????????????")
    @PostMapping(value = "removeById")
    public JsonResult removeById(@RequestBody JSONObject jsonObject) {
        JsonResult jsonResult = formSolutionService.removeData(jsonObject);
        return jsonResult;

    }

    /**
     * ?????????
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "??????????????????", path = "/rowsSave", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "?????????", varName = "jsonObject")})
    @ApiOperation("??????????????????")
    @PostMapping(value = "rowsSave")
    public JsonResult rowsSave(@RequestBody JSONObject jsonObject) {
        try {
            JsonResult jsonResult = formSolutionService.rowsSave(jsonObject);
            return jsonResult;
        } catch (Exception ex) {
            JsonResult rtn = JsonResult.Fail("?????????????????????!");
            rtn.setException(ex);
            return rtn;
        }
    }

    /**
     * ??????????????????????????????
     * @param formAlias
     * @return
     */
    @MethodDefine(title = "??????????????????????????????", path = "/getListFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "formAlias")})
    @ApiOperation("????????????????????????")
    @GetMapping(value = "getListFields")
    public JsonResult getListFields(@RequestParam(value = "formAlias")String formAlias ){
        JsonResult jsonResult=formSolutionService.getListFields(formAlias);
        return jsonResult;
    }

    /**
     * ??????????????????????????????
     * @param formAlias
     * @return
     */
    @MethodDefine(title = "??????????????????????????????", path = "/getBoEnts", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "formAlias")})
    @ApiOperation("??????????????????????????????")
    @GetMapping(value = "getBoEnts")
    public JsonResult getBoEnts(@RequestParam(value = "formAlias")String formAlias ){
        JsonResult jsonResult=formSolutionService.getBoEnts(formAlias);
        return jsonResult;
    }



    protected JsonResult beforeRemove(List<String> list) {
        for(String id:list){
            FormSolution solution=formSolutionService.get(id);
            //????????????
            removeCache(solution.getAlias());
        }
        return super.beforeRemove(list);
    }

    @MethodDefine(title = "?????????????????????????????????????????????", path = "/getPermission", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "alias")})
    @ApiOperation("?????????????????????????????????????????????")
    @GetMapping(value = "getPermission")
    public JsonResult getPermission(@RequestParam(value = "alias")String alias ){
        JsonResult jsonResult=formSolutionService.getPermission(alias);
        return jsonResult;
    }

    @PostMapping(value = "validPreByButton")
    public JsonResult validPreByButton(@RequestBody JSONObject jsonObject){
        String config=jsonObject.getString("config");
        if(StringUtils.isEmpty(config)){
            return JsonResult.Success();
        }
        JSONObject formJson=jsonObject.getJSONObject("formJson");
        JsonResult result=formSolutionService.validPreByButton(config,formJson);
        return result;
    }

    @PostMapping(value = "validPreByButtonPk")
    public JsonResult validPreByButtonPk(@RequestBody JSONObject jsonObject){
        String config=jsonObject.getString("config");
        String formAlias=jsonObject.getString("formAlias");
        String pkIds=jsonObject.getString("pkIds");
        if(StringUtils.isEmpty(config) || StringUtils.isEmpty(formAlias)){
            return JsonResult.Success();
        }
        JsonResult result=formSolutionService.validPreByButtonPk(config,formAlias,pkIds);
        return result;
    }

    @MethodDefine(title = "?????????????????????????????????????????????",path = "/getValueByCustomFormConfig",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "params")})
    @ApiOperation("?????????????????????????????????????????????")
    @PostMapping(value = "getValueByCustomFormConfig")
    public JSONObject getValueByCustomFormConfig(@RequestBody JSONObject params){
        JSONObject formJson=params.getJSONObject("formJson");
        JSONArray formConfig=params.getJSONArray("formConfig");
        return formTableFormulaService.getFormDataByFormConfig(formJson,formConfig);
    }

    @MethodDefine(title = "??????????????????id??????Bo??????",path = "/getBoEntityBySolId",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????ID",varName = "solutionId")})
    @ApiOperation("??????????????????id??????Bo??????")
    @GetMapping(value = "getBoEntityBySolId")
    public JsonResult getBoEntityBySolId(@RequestParam(value = "solutionId") String solutionId ){
        JsonResult jsonResult=new JsonResult().setShow(false).setMessage("????????????!");
        FormSolution formSolution = formSolutionService.getByAlias(solutionId);
        if(BeanUtil.isNotEmpty(formSolution)){
            String bodefId = formSolution.getBodefId();
            FormBoEntity formBoEntity = formBoEntityServiceImpl.getByDefId(bodefId, false);
            jsonResult.setData(formBoEntity);
        }else {
            jsonResult.setMessage("??????????????????!");
        }
        return  jsonResult;
    }


    @MethodDefine(title = "???????????????????????????????????????????????????", path = "/getNodesTreeByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "alias"),@ParamDefine(title = "??????", varName = "pk")})
    @ApiOperation("???????????????????????????????????????????????????")
    @GetMapping(value = "getNodesTreeByAlias")
    public List<JSONObject> getNodesTreeByAlias(@RequestParam(value="alias")String alias,
                                     @RequestParam(value = "pk",required = false)String pk){
        return formSolutionService.getNodesTreeByAlias(alias,pk);
    }
}
