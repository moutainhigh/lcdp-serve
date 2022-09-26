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
@ClassDefine(title = "表单方案",alias = "formSolutionController",path = "/form/core/formSolution",packages = "core",packageName = "表单管理")
@Api(tags = "表单方案")
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
        return "表单方案";
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
            return JsonResult.Fail("表单方案已存在!");
        }
        //写IS_GENERATE_TABLE_字段默认值
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
        //清理缓存
        removeCache(ent.getAlias());

        return JsonResult.Success();
    }


    /**
     * 表单方案导入
     * @param request
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "表单方案导入", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @PostMapping("/doImport")
    @AuditLog(operation = "批量导入")
    @ApiOperation("批量导入")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的表单方案压缩包");
        }
        String treeId=request.getParameter("treeId");
        List<AlterSql> delaySqlList = formExOrImportHandler.importFormSulotionZip(file,treeId);
        return JsonResult.Success().setData(delaySqlList).setMessage("导入成功");
    }

    @MethodDefine(title = "根据单据方案别名获取树形数据", path = "/getTreeByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据方案别名", varName = "alias"),@ParamDefine(title = "主键", varName = "pk")})
    @ApiOperation("根据单据方案别名获取树形数据")
    @GetMapping(value = "getTreeByAlias")
    public JSONObject getTreeByAlias(@RequestParam(value="alias")String alias,
                                     @RequestParam(value = "pk",required = false)String pk){
        return formSolutionService.getTreeByAlias(alias,pk);
    }

    /**
     * 根据单据方案别名与主键获取单据解决方案相关数据，包括模板，字段、按钮权限等
     * @param alias
     * @param pk
     * @param jsonParams {param1:val1,param2:val2}
     * @return
     */
    @MethodDefine(title = "根据单据方案别名与主键获取单据解决方案相关数据", path = "/getByAlias", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据方案别名", varName = "alias"),@ParamDefine(title = "主键", varName = "pk"),@ParamDefine(title = "配置", varName = "setting")})
    @ApiOperation("根据单据方案别名与主键获取单据解决方案相关数据，包括模板，字段、按钮权限等")
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

    @MethodDefine(title = "根据表单数据获取按钮集合", path = "/getButtonsByFormJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "表单方案别名", varName = "formSolutionAlias"),@ParamDefine(title = "表单数据", varName = "formJson")})
    @ApiOperation("根据表单数据获取按钮集合")
    @PostMapping(value = "getButtonsByFormJson")
    public FormResult getButtonsByFormJson(@RequestParam(value = "formSolutionAlias")String formSolutionAlias,@RequestBody JSONObject formJson){
        FormResult formResult=formSolutionService.getButtonsByFormJson(formSolutionAlias,formJson);
        return formResult;
    }


    /**
     * 根据别名获取
     * @param alias
     * @param pk
     * @param jsonParams
     * @return
     */
    @MethodDefine(title = "通过别名获取单据", path = "/getMobileByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据方案别名", varName = "alias"),@ParamDefine(title = "主键", varName = "pk"),
                    @ParamDefine(title = "传入的参数映射", varName = "jsonParams")})
    @ApiOperation("通过别名获取单据")
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

    @MethodDefine(title = "获取满足条件的流程定义ID", path = "/getFlowDefId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "参数配置", varName = "params")})
    @ApiOperation("获取满足条件的流程定义ID")
    @PostMapping(value = "getFlowDefId")
    public JsonResult getFlowDefId(@RequestBody JSONObject params){
        String flowDefMapping=params.getString("flowDefMapping");
        JSONObject data=params.getJSONObject("formJson");
        String flowDefId=formSolutionService.getDefIdByDefMapping(flowDefMapping,data);
        return JsonResult.getSuccessResult(flowDefId,"获取成功").setShow(false);
    }

    @GetMapping(value = "getFormSolutionByAlias")
    public FormSolutionDto getFormSolutionByAlias(@RequestParam(value = "alias")String alias){
        return formSolutionService.getFormSolutionByAlias(alias);
    }
    /**
     * 保存单据数据
     * <pre>
     *     1.有配置流程的情况。
     *      1.点击启动流程
     *          将保存的数据主键和流程定义一起提交。
     *      2.点击保存的时候
     *          调用启动草稿的方式。
     *
     *     2.没有配置流程的情况
     *
     * </pre>
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "保存单据数据", path = "/saveForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据数据", varName = "jsonObject")})
    @ApiOperation("保存单据数据")
    @AuditLog(operation = "保存单据数据")
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
     * 按Id删除
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "按Id删除", path = "/removeById", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "单据数据", varName = "jsonObject")})
    @ApiOperation("按Id删除")
    @AuditLog(operation = "删除单据数据")
    @PostMapping(value = "removeById")
    public JsonResult removeById(@RequestBody JSONObject jsonObject) {
        JsonResult jsonResult = formSolutionService.removeData(jsonObject);
        return jsonResult;

    }

    /**
     * 行保存
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "保存多行数据", path = "/rowsSave", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "行数据", varName = "jsonObject")})
    @ApiOperation("保存多行数据")
    @PostMapping(value = "rowsSave")
    public JsonResult rowsSave(@RequestBody JSONObject jsonObject) {
        try {
            JsonResult jsonResult = formSolutionService.rowsSave(jsonObject);
            return jsonResult;
        } catch (Exception ex) {
            JsonResult rtn = JsonResult.Fail("保存行数据失败!");
            rtn.setException(ex);
            return rtn;
        }
    }

    /**
     * 通过别名获取列表字段
     * @param formAlias
     * @return
     */
    @MethodDefine(title = "通过别名获取列表字段", path = "/getListFields", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据方案别名", varName = "formAlias")})
    @ApiOperation("通过别名获取单据")
    @GetMapping(value = "getListFields")
    public JsonResult getListFields(@RequestParam(value = "formAlias")String formAlias ){
        JsonResult jsonResult=formSolutionService.getListFields(formAlias);
        return jsonResult;
    }

    /**
     * 通过别名获取业务实体
     * @param formAlias
     * @return
     */
    @MethodDefine(title = "通过别名获取业务实体", path = "/getBoEnts", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据方案别名", varName = "formAlias")})
    @ApiOperation("通过别名获取业务实体")
    @GetMapping(value = "getBoEnts")
    public JsonResult getBoEnts(@RequestParam(value = "formAlias")String formAlias ){
        JsonResult jsonResult=formSolutionService.getBoEnts(formAlias);
        return jsonResult;
    }



    protected JsonResult beforeRemove(List<String> list) {
        for(String id:list){
            FormSolution solution=formSolutionService.get(id);
            //缓存清理
            removeCache(solution.getAlias());
        }
        return super.beforeRemove(list);
    }

    @MethodDefine(title = "根据方案别名获取表单字段的权限", path = "/getPermission", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据方案别名", varName = "alias")})
    @ApiOperation("根据方案别名获取表单字段的权限")
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

    @MethodDefine(title = "根据表单方案按钮配置，获取数据",path = "/getValueByCustomFormConfig",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "参数配置",varName = "params")})
    @ApiOperation("根据表单方案按钮配置，获取数据")
    @PostMapping(value = "getValueByCustomFormConfig")
    public JSONObject getValueByCustomFormConfig(@RequestBody JSONObject params){
        JSONObject formJson=params.getJSONObject("formJson");
        JSONArray formConfig=params.getJSONArray("formConfig");
        return formTableFormulaService.getFormDataByFormConfig(formJson,formConfig);
    }

    @MethodDefine(title = "根据表单方案id获取Bo实体",path = "/getBoEntityBySolId",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "表单方案ID",varName = "solutionId")})
    @ApiOperation("根据表单方案id获取Bo实体")
    @GetMapping(value = "getBoEntityBySolId")
    public JsonResult getBoEntityBySolId(@RequestParam(value = "solutionId") String solutionId ){
        JsonResult jsonResult=new JsonResult().setShow(false).setMessage("获取成功!");
        FormSolution formSolution = formSolutionService.getByAlias(solutionId);
        if(BeanUtil.isNotEmpty(formSolution)){
            String bodefId = formSolution.getBodefId();
            FormBoEntity formBoEntity = formBoEntityServiceImpl.getByDefId(bodefId, false);
            jsonResult.setData(formBoEntity);
        }else {
            jsonResult.setMessage("表单方案为空!");
        }
        return  jsonResult;
    }


    @MethodDefine(title = "根据单据方案别名获取树形下一级数据", path = "/getNodesTreeByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "单据方案别名", varName = "alias"),@ParamDefine(title = "主键", varName = "pk")})
    @ApiOperation("根据单据方案别名获取树形下一级数据")
    @GetMapping(value = "getNodesTreeByAlias")
    public List<JSONObject> getNodesTreeByAlias(@RequestParam(value="alias")String alias,
                                     @RequestParam(value = "pk",required = false)String pk){
        return formSolutionService.getNodesTreeByAlias(alias,pk);
    }
}
