package com.redxun.form.core.controller;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redxun.api.org.IOrgService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.engine.FtlEngine;
import com.redxun.common.engine.FtlUtil;
import com.redxun.common.excel.EasyExcelUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.constvar.ConstVarContext;
import com.redxun.constvar.ConstVarType;
import com.redxun.dto.bpm.BpmInstDataDto;
import com.redxun.dto.form.BpmView;
import com.redxun.dto.form.DataResult;
import com.redxun.dto.form.FormParams;
import com.redxun.dto.form.FormPcDto;
import com.redxun.dto.sys.SysFileDto;
import com.redxun.feign.OsGroupClient;
import com.redxun.feign.SysFileClient;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.datahandler.impl.DbDataHandler;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.entity.FormPermission;
import com.redxun.form.core.entity.FormTemplate;
import com.redxun.form.core.service.*;
import com.redxun.form.operator.FileOperatorFactory;
import com.redxun.form.operator.IFileOperator;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/form/core/formPc")
@ClassDefine(title = "????????????",alias = "formPcController",path = "/form/core/formPc",packages = "core",packageName = "????????????")
@Api(tags = "????????????")
public class FormPcController extends BaseController<FormPc> {
    public static final  String  FORM_PRINT="print";

    @Autowired
    FormPcServiceImpl formPcServiceImpl;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DbDataHandler dataHandler;
    @Autowired
    FormBoEntityServiceImpl boEntityService;
    @Autowired
    FormBoAttrServiceImpl formBoAttrServiceImpl;
    @Autowired
    FormPermissionServiceImpl formPermissionServiceImpl;
    @Autowired
    FtlEngine freemarkEngine;
    @Autowired
    ConstVarContext constVarContext;
    @Autowired
    private OsGroupClient osGroupClient;
    @Autowired
    private FormBoDefServiceImpl formBoDefService;
    @Autowired
    private FormDefPermissionServiceImpl formDefPermissionService;
    @Autowired
    IOrgService orgService;
    @Autowired
    FormMobileServiceImpl formMobileServiceImpl;
    @Autowired
    FormTemplateServiceImpl formTemplateService;
    @Autowired
    FormDataService formDataService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    SysFileClient sysFileClient;
    @Autowired
    FileOperatorFactory fileOperatorFactory;
    @Autowired
    FormBoEntityServiceImpl formBoEntityService;
    @Autowired
    FormSolutionServiceImpl formSolutionService;


    @Override
    public BaseService getBaseService() {
        return formPcServiceImpl;
    }

    @Override
    public String getComment() {
        return "????????????";
    }

    @MethodDefine(title = "???????????????????????????", path = "/checkUniqueValue", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????JSON", varName = "formJson")})
    @ApiOperation("???????????????????????????")
    @PostMapping("checkUniqueValue")
    public boolean checkUniqueValue(@RequestBody JSONObject formJson) {
        return formPcServiceImpl.checkUniqueValue(formJson);
    }


    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","FORM","read");

        super.handleFilter(filter);
    }

    /**
     * ??????Excel
     * @param request
     * @return
     * @throws IOException
     */
    @ApiOperation("??????Excel")
    @MethodDefine(title = "??????Excel", path = "/uploadExcel", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @PostMapping("uploadExcel")
    @ResponseBody
    public JsonResult uploadExcel(MultipartHttpServletRequest request) throws IOException {
        String beginRow = request.getParameter("beginRow");
        String sheetStr = request.getParameter("sheetNo");
        Integer sheetNo;
        if("".equals(sheetStr)){
            sheetNo=0;
        }else {
            sheetNo=Integer.parseInt(sheetStr)-1;
        }
        JsonResult jsonResult = new JsonResult();
        MultiValueMap<String, MultipartFile> multiFileMap = request.getMultiFileMap();
        List<MultipartFile> files = multiFileMap.get("files");
        Iterator<MultipartFile> it = files.iterator();
        while (it.hasNext()) {
            MultipartFile multipartFile = it.next();
            try {
                List<Map<Integer, String>> data = EasyExcelUtil.readExcel(multipartFile, beginRow, sheetNo);
                if (CollectionUtils.isEmpty(data)||data.size()==0) {
                    jsonResult.setSuccess(true);
                    jsonResult.setMessage("??????????????????!");
                }
                jsonResult.setSuccess(true);
                jsonResult.setData(data);
                jsonResult.setMessage("????????????!");
            }catch (Exception e){
                jsonResult.setSuccess(false);
                jsonResult.setMessage("????????????!");
            }
        }

        return jsonResult;
    }

    @MethodDefine(title = "??????Excel??????", path = "/downloadTemplate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "fieldsConfig"),@ParamDefine(title = "?????????", varName = "comment")})
    @ApiOperation("??????Excel??????")
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(@RequestParam(value = "fieldsConfig") String fieldsConfig,
                                 @ApiParam @RequestParam(value = "comment") String comment) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        JSONObject jsonObject = JSONObject.parseObject(fieldsConfig);
        String fileName = comment + "??????????????????";
        if (jsonObject.containsKey("fileId") && StringUtils.isNotEmpty(jsonObject.getString("fileId"))) {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
            String fileId=jsonObject.getString("fileId");
            SysFileDto sysFile = sysFileClient.getByFileId(fileId);
            IFileOperator operator=fileOperatorFactory.getByType(sysFile.getFileSystem());
            InputStream inputStream=operator.getInputStream(sysFile,false,false);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(inputStream).build();
            excelWriter.finish();
        } else {
            JSONArray fields = jsonObject.getJSONArray("fields");
            JSONArray dataTemp = new JSONArray();
            JSONObject json = new JSONObject();
            List<ExcelExportEntity> gridcolumns = new ArrayList<>();
            for (Object obj : fields) {
                JSONObject attr = (JSONObject) obj;
                ExcelExportEntity gridcolumn = new ExcelExportEntity();
                gridcolumn.setName(attr.getString("comment"));
                gridcolumn.setKey(attr.getString("name"));
                gridcolumns.add(gridcolumn);
                String valmode = attr.getString("valmode");
                String ctltype = attr.getString("ctltype");
                String val = "";
                if ("double".equals(valmode)) {
                    val += "{." + gridcolumn.getKey() + ".value}/";
                    val += "{." + gridcolumn.getKey() + ".label}";
                } else if ("multi".equals(valmode)) {
                    if ("rx-address".equals(ctltype)) {
                        val += "{." + gridcolumn.getKey() + ".province_code}/";
                        val += "{." + gridcolumn.getKey() + ".province}/";
                        val += "{." + gridcolumn.getKey() + ".city_code}/";
                        val += "{." + gridcolumn.getKey() + ".city}/";
                        val += "{." + gridcolumn.getKey() + ".county_code}/";
                        val += "{." + gridcolumn.getKey() + ".county}/";
                        val += "{." + gridcolumn.getKey() + ".address}";
                    }
                } else {
                    val += "{." + gridcolumn.getKey() + "}";
                }
                json.put(attr.getString("name"), val);
            }
            dataTemp.add(json);
            ExcelUtil.exportExcel(dataTemp, fileName, "????????????", gridcolumns, fileName + ".xlsx", response);
        }
    }

    @MethodDefine(title = "??????Excel", path = "/exportExcel", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "fieldsConfig"),@ParamDefine(title = "?????????", varName = "comment"),
                    @ParamDefine(title = "????????????", varName = "subData")})
    @ApiOperation("??????Excel")
    @PostMapping("/exportExcel")
    public void exportExcel(@RequestParam String fieldsConfig,@ApiParam @RequestParam String comment,@RequestParam String subData) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        JSONObject jsonObject = JSONObject.parseObject(fieldsConfig);
        String fileId=jsonObject.getString("fileId");
        Integer sheetNo=jsonObject.getInteger("sheetNo");
        JSONArray fields = jsonObject.getJSONArray("fields");
        //??????????????????
        boolean isSn=false;
        for(Object obj:fields){
            JSONObject field=(JSONObject)obj;
            if(field.containsKey("isSn") && field.getBoolean("isSn")){
                isSn=true;
                break;
            }
        }
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName=comment+"??????????????????";
        response.setHeader("Content-disposition", "attachment;filename="+fileName+".xlsx");
        JSONArray subDataArray=new JSONArray();
        if(StringUtils.isNotEmpty(subData)){
            subDataArray=JSONArray.parseArray(subData);
        }
        List<Map<String,Object>> writeAry=new ArrayList<>();
        for (int i = 0; i < subDataArray.size(); i++) {
            JSONObject data = subDataArray.getJSONObject(i);
            JSONObject writeData=new JSONObject();
            for(Object obj:fields) {
                JSONObject field = (JSONObject) obj;
                String fieldName = field.getString("name");
                try {
                    //??????json?????????
                    Map<String,Object> jsonMap=data.getJSONObject(fieldName).getInnerMap();
                    for(String key:jsonMap.keySet()){
                        writeData.put(fieldName+"."+key,jsonMap.get(key));
                    }
                }catch (Exception e){
                    writeData.put(fieldName, data.get(fieldName));
                }
            }
            if(isSn) {
                writeData.put("sn", i + 1);
            }
            writeAry.add(writeData.getInnerMap());
        }
        SysFileDto sysFile = sysFileClient.getByFileId(fileId);
        IFileOperator operator=fileOperatorFactory.getByType(sysFile.getFileSystem());
        InputStream inputStream=operator.getInputStream(sysFile,false,false);
        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(inputStream).build();
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetNo-1).build();
        excelWriter.fill(writeAry, writeSheet);
        excelWriter.finish();
    }

    /**
     * ????????????????????????????????????????????????Id????????????
     * @return
     * @throws Exception
     */
    @ApiOperation(value="???????????????????????????", notes="??????????????????????????????")
    @PostMapping(value="/query")
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult jsonResult = JsonPageResult.getSuccess("??????????????????!");

        try {
            IPage page =null;
            QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
            if("SEL_DEV".equals(queryData.getParams().get(FormPc.CONTAI_TYPE_))){
                page = formPcServiceImpl.searchOrderByType(filter);
            }else {
                this.handleFilter(filter);
                page = this.getBaseService().query(filter);
            }
            this.handlePage(page);
            jsonResult.setPageData(page);
        } catch (Exception var5) {
            jsonResult.setSuccess(false);
            this.logger.error(ExceptionUtil.getExceptionMessage(var5));
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(var5));
        }
        return jsonResult;
    }

    @ApiOperation("??????????????????")
    @MethodDefine(title = "??????????????????", path = "/importFormPc", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????JSON", varName = "bpmFormJson")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("importFormPc")
    public void importFormPc(@RequestBody JSONObject bpmFormJson) {
        StringBuilder sb=new StringBuilder();
        sb.append("????????????,??????????????????:");

        JSONArray formPcDtos = bpmFormJson.getJSONArray("formPcDto");
        String treeId = bpmFormJson.getString("treeId");
        String appId=formDataService.getAppIdByTreeId(treeId);
        for (int i = 0; i < formPcDtos.size(); i++) {
            JSONObject formPcDtoJson = formPcDtos.getJSONObject(i);
            String fprmPcDtoStr = formPcDtoJson.toJSONString();
            try {
                FormPc formPc = JSONObject.parseObject(fprmPcDtoStr, FormPc.class);
                sb.append( formPc.getName() +"("+ formPc.getId()+"),");
                String formPcId = formPc.getId();
                formPcServiceImpl.delete(formPcId);
                formPc.setCategoryId(treeId);
                formPc.setAppId(appId);
                formPcServiceImpl.insert(formPc);

            } catch (Exception e) {
                sb.append("????????????:"+ ExceptionUtil.getExceptionMessage(e));
            }
        }
        sb.append("???????????????:" +treeId);

        LogContext.put(Audit.DETAIL,sb.toString());
    }


    @ApiOperation("??????????????????????????????")
    @MethodDefine(title = "??????????????????????????????", path = "/getFormPcByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "alias")})
    @GetMapping("getFormPcByAlias")
    public FormPcDto getByAlias(@RequestParam(value = "alias") String alias) {
        FormPcDto formMobileDto = new FormPcDto();
        FormPc formPc = formPcServiceImpl.getByAlias(alias);
        try {
            BeanUtil.copyNotNullProperties(formMobileDto, formPc);
        } catch (Exception e) {
            log.error("--FormPcController.getFormPcByAlias is error---:" + e.getMessage());
        }
        return formMobileDto;
    }

    /**
     * ????????????key??????????????????????????????/?????????????????????
     * @param boAlias
     * @return
     */
    @ApiOperation("????????????key??????????????????????????????")
    @MethodDefine(title = "????????????key??????????????????????????????", path = "/getFormPermissionByBoAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????KEY", varName = "boAlias")})
    @GetMapping("getFormPermissionByBoAlias")
    public FormPc getFormPermissionByBoAlias(@RequestParam(value = "boAlias") String boAlias) {
        FormBoDef formBoDef = formBoDefService.getByAlias(boAlias);
        if (BeanUtil.isEmpty(formBoDef)) {
            return null;
        }
        String boDefId=formBoDef.getId();
        List<FormPc> formPcs = formPcServiceImpl.getByBoDefId(boDefId);
        if (BeanUtil.isEmpty(formPcs)) {
            return null;
        }
        FormPc formPc =formPcs.get(0);
        FormBoEntity boEntity= boEntityService.getWithAttrsByDefId(boDefId);
        JSONObject permissionR =formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,"",true);
        formPc.setPermissionR(formPermissionServiceImpl.getInitPermissionObject(permissionR).toJSONString());
        JSONObject permissionW =formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,"",false);
        formPc.setPermissionW(formPermissionServiceImpl.getInitPermissionObject(permissionW).toJSONString());
        return  formPc;
    }



    @MethodDefine(title = "????????????????????????????????????????????????", path = "/getByBoAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????KEY", varName = "boAlias")})
    @ApiOperation("????????????????????????????????????????????????")
    @GetMapping("getByBoAlias")
    public List<FormPc> getByBoAlias(@RequestParam(value = "boAlias") String boAlias) {
        FormBoDef formBoDef = formBoDefService.getByAlias(boAlias);
        if (BeanUtil.isEmpty(formBoDef)) {
            return new ArrayList<>();
        }
        return formPcServiceImpl.getByBoDefId(formBoDef.getId());
    }

    @ApiOperation("??????????????????")
    @MethodDefine(title = "??????????????????", path = "/saveForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????", varName = "json")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("saveForm")
    public JsonResult saveForm(@RequestBody JSONObject json) {
        JsonResult result = null;
        StringBuilder sb=new StringBuilder();
        sb.append("??????????????????:");
        try {
            result = formPcServiceImpl.saveForm(json,sb);
            LogContext.put(Audit.DETAIL,sb.toString());
        } catch (Exception ex) {
            MessageUtil.triggerException("??????????????????!",ExceptionUtil.getExceptionMessage(ex));
        }
        result.setShow(false);
        return result;
    }

    @ApiOperation("??????????????????")
    @MethodDefine(title = "??????????????????", path = "/temporaryForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????", varName = "json")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("temporaryForm")
    public JsonResult temporaryForm(@RequestBody JSONObject json) {
        JsonResult result = null;
        StringBuilder sb=new StringBuilder();
        sb.append("??????????????????:");
        try {
            result = formPcServiceImpl.temporaryForm(json,sb);
            LogContext.put(Audit.DETAIL,sb.toString());
        } catch (Exception ex) {
            MessageUtil.triggerException("??????????????????!",ExceptionUtil.getExceptionMessage(ex));
        }
        result.setShow(false);
        return result;
    }

    @ApiOperation("????????????")
    @MethodDefine(title = "????????????", path = "/genForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @AuditLog(operation = "????????????")
    @PostMapping("genForm")
    public JsonResult genForm(@RequestBody JSONObject json) {
        JsonResult result = null;
        try {
            result = formPcServiceImpl.genForm(json);
        } catch (Exception ex) {
            MessageUtil.triggerException("??????????????????!",ExceptionUtil.getExceptionMessage(ex));
        }

        result.setShow(false);
        return result;

    }

    @ApiOperation("????????????")
    @MethodDefine(title = "????????????", path = "/validForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @PostMapping("validForm")
    public JsonResult validForm(@RequestBody JSONObject json) {
        JsonResult result = null;
        try {
            result = formPcServiceImpl.validForm(json);
        } catch (Exception ex) {
            String errMsg="??????????????????:"+ ExceptionUtil.getExceptionMessage(ex);
            result = JsonResult.Fail("??????????????????!");
            LogContext.put(Audit.DETAIL,errMsg);
            LogContext.put(Audit.STATUS,Audit.STATUS_FAIL);
            result.setData(errMsg);
        }
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "????????????????????????????????????", path = "/getById", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "pkId")})
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????????????????")
    @GetMapping("/getById")
    public JsonResult<FormPc> getById(@RequestParam(value = "pkId") String pkId) {
        FormPc formPc = formPcServiceImpl.getByFormId(pkId);
        String boDefId= formPc.getBodefId();

        if(StringUtils.isNotEmpty(boDefId)){
            FormBoEntity boEntity= formBoEntityService.getByDefId(boDefId,true);
            String idField= boEntity.getIdField();
            formPc.setPkField(idField.toUpperCase());
            //??????BO?????????(???????????????????????????)
            formPc.setFormBoEntity(boEntity);
        }
        else{
            formPc.setPkField(FormBoEntity.FIELD_PK);
        }

        JsonResult result = JsonResult.Success("get data success").setData(formPc);
        result.setShow(false);
        return result;
    }

    /**
     * [{type:"main",key:"",name:"????????????",temlate:[{alias:"alias",name:"????????????"}]},
     * {type:"sub",key:"",name:"????????????",temlate:[{alias:"alias",name:"????????????"}]}
     * {type:"sub",key:"",name:"????????????",temlate:[{alias:"alias",name:"????????????"}]}]
     *
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation("????????????????????????")
    @MethodDefine(title = "????????????????????????", path = "/getTemplatesByBoDef", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @PostMapping("/getTemplatesByBoDef")
    public JSONArray getTemplatesByBoDef(HttpServletRequest request) throws Exception {
        String boDefId = RequestUtil.getString(request, "boDefId");
        String type = RequestUtil.getString(request, "type","pc");

        FormBoEntity boEnt = boEntityService.getByDefId(boDefId, false);
        JSONArray mainAry = new JSONArray();
        JSONArray subOneAry = new JSONArray();
        JSONArray subManyAry = new JSONArray();
        List<FormTemplate> formTemplateList = formTemplateService.getAll();
        for (FormTemplate formTemplate : formTemplateList) {
            if (type.equals(formTemplate.getType())) {
                String category = formTemplate.getCategory();
                //??????????????????
                if ("main".equals(category)) {
                    mainAry.add(JSONObject.parseObject("{label:'" + formTemplate.getName() + "',value:'" + formTemplate.getAlias() + "'}"));
                }
                //???????????????????????????
                else if ("onetoone".equals(category)) {
                    subOneAry.add(JSONObject.parseObject("{label:'" + formTemplate.getName() + "',value:'" + formTemplate.getAlias() + "'}"));
                }
                //???????????????????????????
                else if ("onetomany".equals(category)) {
                    subManyAry.add(JSONObject.parseObject("{label:'" + formTemplate.getName() + "',value:'" + formTemplate.getAlias() + "'}"));
                }
            }
        }
        JSONArray rtnJson = new JSONArray();

        JSONObject mainJson = new JSONObject();
        mainJson.put("type", FormBoRelation.RELATION_MAIN);
        mainJson.put("key", boEnt.getAlias());
        mainJson.put("name", boEnt.getName());
        mainJson.put("template", mainAry);
        mainJson.put("entId", boEnt.getId());

        rtnJson.add(mainJson);

        if (BeanUtil.isNotEmpty(boEnt.getBoEntityList())) {
            for (FormBoEntity subEnt : boEnt.getBoEntityList()) {
                JSONObject subJson = new JSONObject();
                subJson.put("type", subEnt.getBoRelation().getType());
                subJson.put("key", subEnt.getAlias());
                subJson.put("name", subEnt.getName());
                subJson.put("entId", subEnt.getId());
                if (FormBoRelation.RELATION_ONETOONE.equals(subEnt.getBoRelation().getType())) {
                    subJson.put("template", subOneAry);
                } else {
                    subJson.put("template", subManyAry);
                }
                rtnJson.add(subJson);
            }
        }
        return rtnJson;
    }

    @MethodDefine(title = "??????????????????HTML", path = "/generatePrintHtml", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("??????????????????HTML")
    @PostMapping("/generatePrintHtml")
    public JsonResult generatePrintHtml(HttpServletRequest request) throws Exception {
        String boDefId = RequestUtil.getString(request, "boDefId");
        String templates = RequestUtil.getString(request, "templates");
        boolean genTab = RequestUtil.getBoolean(request, "genTab", false);
        return generateHtmlByType(boDefId,templates,genTab,"print");
    }

    @ApiOperation("??????BO????????????")
    @MethodDefine(title = "??????BO????????????", path = "/generateHtml", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @AuditLog(operation = "??????BO????????????")
    @PostMapping("/generateHtml")
    public JsonResult generateHtml(HttpServletRequest request) throws Exception {
        String boDefId = RequestUtil.getString(request, "boDefId");
        String templates = RequestUtil.getString(request, "templates");
        boolean genTab = RequestUtil.getBoolean(request, "genTab", false);
        JsonResult result= generateHtmlByType(boDefId,templates,genTab,"pc");

        String detail="??????BO??????("+ boDefId+")????????????";
        LogContext.put(Audit.DETAIL,detail);

        return result;
    }

    /**
     * ????????????????????????????????????????????????Tab???????????????????????????Html
     * @param boDefId
     * @param templates
     * @param genTab
     * @param type
     * @return
     * @throws Exception
     */
    private JsonResult generateHtmlByType(String boDefId,String templates,boolean genTab,String type) throws Exception{

        JSONObject jsonObj = JSONObject.parseObject(templates);

        FormBoEntity boEnt = boEntityService.getByBoDefIdExcludeRefFields(boDefId);
        if (BeanUtil.isEmpty(boEnt)) {
            return new JsonResult<>(true, "", "").setShow(false);
        }

        String mainAlias = jsonObj.getJSONObject("main").getString(boEnt.getAlias());

        StringBuffer sb = new StringBuffer();
        //??????????????????
        String formTemplate = formTemplateService.getByAliasAndType(mainAlias, type);
        //??????????????????
        String fieldTemplate = formTemplateService.getByAliasAndType("fieldCtrl", type);


        List<String> titleList = new ArrayList<>();
        List<String> contentList = new ArrayList<>();
        JSONObject metadataList = new JSONObject();

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("ctxPath", request.getContextPath());
        //?????????????????????
        boEntityService.removeEntRefFields(boEnt);

        model.put("ent", boEnt);
        List<ConstVarType> contextVarAry = ConstVarContext.getTypes();
        model.put("contexts", contextVarAry);

        TemplateHashModel util = FtlUtil.generateStaticModel(StringUtils.class);
        model.put("util", util);

        titleList.add(boEnt.getName());
        String mainHtml = freemarkEngine.parseByStringTemplate(model, fieldTemplate + formTemplate);
        contentList.add(mainHtml);
        formPcServiceImpl.parseMetadata(boEnt, mainAlias, mainHtml, metadataList);

        JSONObject sub = jsonObj.getJSONObject("sub");
        if (!sub.isEmpty()) {
            List<FormBoEntity> entList = boEnt.getBoEntityList();
            for (FormBoEntity ent : entList) {
                String alias = ent.getAlias();
                if (!sub.containsKey(alias)) {
                    continue;
                }
                //?????????????????????
                boEntityService.removeEntRefFields(ent);
                String template = sub.getString(alias);
                //??????????????????
                String subTemplate = formTemplateService.getByAliasAndType(template, type);
                model.put("ent", ent);

                String html = freemarkEngine.parseByStringTemplate(model, fieldTemplate + subTemplate);

                sb.append(html);

                titleList.add(ent.getName());
                contentList.add(html);
                formPcServiceImpl.parseMetadata(ent, template, html, metadataList);
            }
        }
        String tag = genTab ? FormPc.PAGE_TAG : "";
        String title = genTab ? StringUtils.join(titleList, tag) : "??????1";
        String content = StringUtils.join(contentList, tag);
        JsonResult result = new JsonResult(true);
        result.setMessage(title);
        JSONObject data = new JSONObject();
        data.put("formTab", content);
        data.put("metadata", metadataList);
        data.put("pkField",boEnt.getIdField().toUpperCase());
        result.setData(data);
        return result.setShow(false);
    }

    @ApiOperation("????????????????????????")
    @MethodDefine(title = "????????????????????????", path = "/getByCategory", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @PostMapping("/getByCategory")
    public JSONArray getByCategory(HttpServletRequest request) throws Exception {
        String category = RequestUtil.getString(request, "category");
        String type = RequestUtil.getString(request, "type");
        JSONArray templates = new JSONArray();
        //??????????????????????????????????????????
        List<FormTemplate> formTemplates = formTemplateService.getByTypeAndCategory(type, category);
        for (FormTemplate formTemplate : formTemplates) {
            templates.add(JSONObject.parseObject("{label:'" + formTemplate.getName() + "',value:'" + formTemplate.getAlias() + "'}"));
        }
        return templates;
    }

    /**
     * ?????????????????????????????????????????????
     * @param alias
     * @param type
     * @return
     */
    private String getTemplateByAlias(String alias, String type) {
        String fieldTemplate = formTemplateService.getByAliasAndType(alias, type);
        return fieldTemplate;
    }

    @MethodDefine(title = "??????????????????HTML??????", path = "/listSubTemplates", method = HttpMethodConstants.POST)
    @ApiOperation("??????????????????HTML??????")
    @PostMapping("/listSubTemplates")
    public ArrayNode listSubTemplates() throws Exception {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        org.springframework.core.io.Resource resource = new ClassPathResource("templates/view/formTemplate.xml");
        Document doc = Dom4jUtil.load(resource.getInputStream());

        List<Node> nodes = doc.getRootElement().selectNodes("/templates/subTemplate/template");
        for (Node template : nodes) {
            Element el = (Element) template;
            String id = el.attributeValue("id");
            String name = el.attributeValue("name");
            String file = el.attributeValue("file");

            ObjectNode node = objectMapper.createObjectNode();
            node.put("id", id);
            node.put("name", name);
            node.put("file", file);
            arrayNode.add(node);
        }
        return arrayNode;
    }

    /**
     * ????????????Id????????????
     * @param templateId
     * @return
     * @throws Exception
     */
    private String getTemplate(String templateId) throws Exception {

        URL url = this.getClass().getClassLoader().getResource("templates/view/" + templateId + ".html");
        String fileStr=FileUtil.readFile(url);
        return fileStr;

    }

    /**
     *
     * @param json
     * @return
     * @throws Exception
     */
    @ApiOperation("????????????HTML")
    @MethodDefine(title = "????????????HTML", path = "/getSubTemplateHtml", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @PostMapping("/getSubTemplateHtml")
    public JsonResult getSubTemplateHtml(@RequestBody JSONObject json) throws Exception {
        String templateId = json.getString("templateId");
        String columns = json.getString("columns");
        String tableComment = json.getString("tableComment");
        String tableName = json.getString("tableName");
        Map<String, Object> model = new HashMap<String, Object>();
        List controls = JSONArray.parseArray(columns);
        model.put("controls", controls);
        model.put("tableComment", tableComment);
        model.put("tableName", tableName);
        String template1 = getTemplate(templateId);
        JsonResult result = new JsonResult(true);
        result.setData(template1);
        return result;
    }

    @MethodDefine(title = "???????????????????????????????????????", path = "/getMobileByAliasAndPermisson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "paramsList")})
    @ApiOperation("???????????????????????????????????????")
    @PostMapping("getMobileByAliasAndPermisson")
    public JsonResult getMobileByAliasAndPermisson(@RequestBody List<FormParams> paramsList) {
        JsonResult<List<BpmView>> jsonResult = JsonResult.Success();
        try {
            List<BpmView> bpmViews = new ArrayList<>();
            for (FormParams params : paramsList) {
                JsonResult<BpmView> viewJsonResult = getMobileByFormParams(params);
                //?????????????????????
                if (!viewJsonResult.isSuccess() && viewJsonResult.getCode() == 1) {
                    continue;
                }
                if (!viewJsonResult.isSuccess() && viewJsonResult.getCode() == JsonResult.FAIL_CODE) {
                    return viewJsonResult;
                }
                bpmViews.add(viewJsonResult.getData());
            }
            jsonResult.setData(bpmViews);
        } catch (Exception ex) {
            jsonResult.setShow(false);
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    @ApiOperation("?????????????????????????????????")
    @MethodDefine(title = "?????????????????????????????????", path = "/getByAliasAndPermisson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "paramsList")})
    @PostMapping("getByAliasAndPermisson")
    public JsonResult getByAliasAndPermisson(@RequestBody List<FormParams> paramsList) {
        JsonResult<List<BpmView>> jsonResult = JsonResult.Success();
        try {
            List<BpmView> bpmViews = new ArrayList<>();
            for (FormParams params : paramsList) {
                JsonResult<BpmView> viewJsonResult = getByFormParams(params);
                //?????????????????????
                if (!viewJsonResult.isSuccess() && viewJsonResult.getCode() == 1) {
                    continue;
                }
                if (!viewJsonResult.isSuccess() && viewJsonResult.getCode() == JsonResult.FAIL_CODE) {
                    return viewJsonResult;
                }
                bpmViews.add(viewJsonResult.getData());
            }
            jsonResult.setData(bpmViews);
        } catch (Exception ex) {
            jsonResult.setShow(false);
            jsonResult.setMessage(ExceptionUtil.getExceptionMessage(ex));
        }
        return jsonResult;
    }

    /**
     * ??????????????????????????????????????????
     * @param formParams
     * @return
     */
    private JsonResult<BpmView> getMobileByFormParams(FormParams formParams) {
        //???????????????????????????????????????
        Boolean readOnly = formParams.getReadOnly();
        String alias = formParams.getAlias();
        String pk = formParams.getPk();
        String permission = formParams.getPermission();
        FormMobile formMobile = null;
        //??????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(alias)) {
            formMobile = formMobileServiceImpl.getByAlias(alias);
            if (BeanUtil.isEmpty(formMobile)) {
                return JsonResult.Fail("???????????????!");
            }
        } else {
            String formId = formDefPermissionService.getFormIdByBoAlias(formParams.getBoAlias());
            if (StringUtils.isEmpty(formId)) {
                JsonResult jsonResult = JsonResult.Fail("??????????????????!");
                return jsonResult.setCode(1);
            }
            formMobile = formMobileServiceImpl.getByFormId(formId);
            //?????????????????????
            if(StringUtils.isEmpty(permission)){
                FormPermission formPermission = formPermissionServiceImpl.getByConfig(FormPermission.TYPE_FORM, formId);
                if (formPermission != null) {
                    permission = formPermission.getPermission();
                } else {
                    permission = "{}";
                }
            }
        }
        FormBoEntity boEntity = boEntityService.getWithAttrsByDefId(formMobile.getBodefId());

        BpmView bpmView = new BpmView();
        bpmView.setFormId(formMobile.getId());
        bpmView.setAlias(formMobile.getAlias());
        bpmView.setIdField(boEntity.getIdField());
        bpmView.setBoAlias(formMobile.getBodefAlias());
        bpmView.setTitle(formMobile.getName());
        bpmView.setScript(formMobile.getScript());
        bpmView.setTemplate(formMobile.getFormHtml());
        bpmView.setMetadata(formMobile.getMetadata());
        JSONObject data = getData(formMobile.getFormPcAlias(), pk);
        bpmView.setData(data);

        Map<String, String> entMap = getEntMapByBoEnt(boEntity);
        FormPc fp = formPcServiceImpl.getByAlias(formMobile.getFormPcAlias());
        JSONObject returnPermission = getPermission(permission, boEntity, fp,readOnly,formParams);
        bpmView.setPermission(returnPermission);
        bpmView.setEntMap(entMap);
        JsonResult result = JsonResult.Success();
        result.setData(bpmView);

        return result;
    }

    /**
     * ????????????????????????PC????????????
     * @param formParams
     * @return
     */
    private JsonResult<BpmView> getByFormParams(FormParams formParams) {
        //???????????????????????????????????????
        Boolean readOnly = formParams.getReadOnly();
        String alias = formParams.getAlias();
        String pk = formParams.getPk();
        String permission = formParams.getPermission();
        FormPc formPc = null;
        //??????????????????????????????????????????????????????
        if (StringUtils.isNotEmpty(alias)) {
            formPc = formPcServiceImpl.getByAlias(alias);
            if (BeanUtil.isEmpty(formPc)) {
                return JsonResult.Fail("?????????"+alias+"????????????!");
            }
        } else {
            String formId = formDefPermissionService.getFormIdByBoAlias(formParams.getBoAlias());
            if (StringUtils.isEmpty(formId)) {
                JsonResult jsonResult = JsonResult.Fail("??????????????????!");
                return jsonResult.setCode(1);
            }
            if(StringUtils.isEmpty(permission)){
                if(!formParams.getDefaultWrite()){
                    formPc = formPcServiceImpl.getByFormId(formId);
                    FormPermission formPermission = formPermissionServiceImpl.getByConfig(FormPermission.TYPE_FORM, formId);
                    if (formPermission != null) {
                        permission = formPermission.getPermission();
                    } else {
                        permission = "{}";
                    }
                }
            }
        }

        FormBoEntity boEntity = boEntityService.getWithAttrsByDefId(formPc.getBodefId());
        //????????????????????????????????????
        if(formParams.getDefaultWrite()){
            JSONObject  initPermssion =formPermissionServiceImpl.getFormInitPermission(boEntity,formPc,"",false);
            permission=formPermissionServiceImpl.getInitPermissionObject(initPermssion).toJSONString();
            readOnly=false;
        }

        //????????????
        JSONArray subtableRights = getSubTablePermission(permission, boEntity);
        //?????????????????????
        ProcessHandleHelper.clearObjectLocal();
        ProcessHandleHelper.setObjectLocal(subtableRights);

        BpmView bpmView = new BpmView();
        bpmView.setFormId(formPc.getId());
        bpmView.setAlias(formPc.getAlias());
        bpmView.setIdField(boEntity.getIdField());
        bpmView.setBoAlias(formPc.getBoDefAlias());
        bpmView.setTitle(formPc.getName());
        String script=FormPc.FORM_TYPE_EASY_DESIGN.equals( formPc.getType())?formPc.getJavascript():formPc.getJavascript();
        bpmView.setScript(script);
        bpmView.setTemplate(formSolutionService.parseCacTemplate(permission,formPc.getTemplate(),formParams));
        bpmView.setMetadata(formPc.getMetadata());
        bpmView.setWizard(formPc.getWizard());
        bpmView.setType(formPc.getType());
        JSONObject data = getData(formPc.getAlias(), pk);
        bpmView.setData(data);

        //???????????????????????????
        Map<String, String> entMap = getEntMapByBoEnt(boEntity);
        JSONObject returnPermission= getPermission(permission, boEntity, formPc,readOnly,formParams);

        bpmView.setPermission(returnPermission);
        bpmView.setEntMap(entMap);
        JsonResult result = JsonResult.Success();
        result.setData(bpmView);

        return result;
    }


    /**
     * ?????????????????????
     *
     * @param permission
     * @param boEntity
     * @return
     */
    private JSONArray getSubTablePermission(String permission, FormBoEntity boEntity) {
        //????????????
        JSONArray subtableRights = null;

        if (StringUtils.isNotEmpty(permission)) {
            JSONObject rightSetting = JSONObject.parseObject(permission);
            subtableRights = rightSetting.getJSONArray("subtableRights");
        } else {
            subtableRights = new JSONArray();
            List<FormBoEntity> boEntityList = boEntity.getBoEntityList();
            for (FormBoEntity subEnt : boEntityList) {
                JSONObject right = new JSONObject();
                right.put("alias", subEnt.getAlias());
                right.put("name", subEnt.getName());
                right.put("type", "all");
                right.put("setting", "");
                subtableRights.add(right);
            }
        }
        return subtableRights;
    }


    /**
     * ??????Bo???????????????
     * <pre>
     *     product:main
     *     user:onetoone
     *     grade:onetomany
     * </pre>
     * @param boEntity
     * @return
     */
    private Map<String, String> getEntMapByBoEnt(FormBoEntity boEntity) {
        Map<String, String> map = new HashMap<>();
        if(StringUtils.isEmpty(boEntity.getAlias())){
            return map;
        }
        map.put(boEntity.getAlias(), FormBoRelation.RELATION_MAIN);
        for (FormBoEntity subEnt : boEntity.getBoEntityList()) {
            map.put(subEnt.getAlias(), subEnt.getBoRelation().getType());
        }
        return map;
    }

    /**
     * ?????????????????????
     *
     * @param permission
     * @param boEntity
     * @param formPc
     * @param readOnly
     * @return
     */
    private JSONObject getPermission(String permission, FormBoEntity boEntity, FormPc formPc,Boolean readOnly,FormParams formParams) {
        //1. ?????????????????????????????????
        IUser user = ContextUtil.getCurrentUser();
        Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
        JSONObject rightSetting = new JSONObject();
        if(StringUtils.isNotEmpty(permission)) {
            rightSetting = JSONObject.parseObject(permission);
        }
        JSONObject jsonObject = formPermissionServiceImpl.calcRights(boEntity, rightSetting, profiles, formPc, readOnly,formParams);
        return jsonObject;
    }

    @MethodDefine(title = "????????????????????????", path = "/getByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "alias"),@ParamDefine(title = "??????", varName = "pk"),@ParamDefine(title = "??????????????????", varName = "initPermission")})
    @ApiOperation("????????????????????????")
    @GetMapping("getByAlias")
    public JsonResult<BpmView> getByAlias(@RequestParam(value = "alias") String alias,
                                          @RequestParam(value = "pk") String pk,
                                          @RequestParam(value = "initPermission") Boolean initPermission) {
        FormPc formPc = formPcServiceImpl.getByAlias(alias);
        if (BeanUtil.isEmpty(formPc)) {
            return JsonResult.Fail("???????????????!");
        }
        BpmView bpmView = new BpmView();
        bpmView.setFormId(formPc.getId());
        bpmView.setAlias(formPc.getAlias());
        bpmView.setScript(formPc.getJavascript());
        bpmView.setTemplate(formPc.getTemplate());

        JSONObject data = getData(alias, pk);
        bpmView.setData(data);

        if (initPermission) {
            FormBoEntity boEntity = boEntityService.getWithAttrsByDefId(formPc.getBodefId());
            JSONObject permission = formPermissionServiceImpl.getInitPermission(boEntity,formPc);
            bpmView.setPermission(permission);
        }

        JsonResult result = JsonResult.Success();
        result.setData(bpmView);

        return result;
    }

    /**
     * ???????????????????????????????????????????????????
     * @param formAlias
     * @param pk
     * @return
     */
    private JSONObject getData(String formAlias, String pk) {
        JSONObject data = null;
        //????????????
        if (StringUtils.isNotEmpty(pk)) {
            data = dataHandler.getById(formAlias, pk);
        } else {
            data = dataHandler.getInitData(formAlias);
        }
        return data;
    }

    @MethodDefine(title = "??????????????????????????????", path = "/getDataByPk", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "BO??????", varName = "boAlias"),@ParamDefine(title = "??????", varName = "pk")})
    @ApiOperation("??????????????????")
    @PostMapping("getDataByPk")
    public JSONObject getDataByPk(@RequestParam(name = "boAlias") String boAlias,
                                  @RequestParam(name = "pk") String pk){
        JSONObject data=dataHandler.getDataById(boAlias,pk);
        return  data;
    }

    @MethodDefine(title = "??????????????????", path = "/saveFormData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "data")})
    @ApiOperation("??????????????????")
    @AuditLog(operation = "??????????????????")
    @PostMapping("saveFormData")
    public JsonResult saveFormData(@RequestBody JSONObject data) {

        JSONObject formData=data.getJSONObject("formData");
        String formulas=data.getString("formulas");
        String op=data.getString("op");
        String opinion=data.getString("opinion");


        List<DataResult> list = formPcServiceImpl.saveData(formData,formulas,op,opinion);
        JsonResult result = JsonResult.Success();
        result.setData(list);
        return result;
    }

    @MethodDefine(title = "??????????????????(??????)", path = "/saveFormDataDirect", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "data")})
    @ApiOperation("??????????????????(??????)")
    @AuditLog(operation = "??????????????????(??????)")
    @PostMapping("saveFormDataDirect")
    public JsonResult saveFormDataDirect(@RequestBody JSONObject data) {

        JsonResult result = JsonResult.Success("??????????????????!");
        try{
            List<DataResult> list = formPcServiceImpl.saveData(data);
            result.setData(list);
            LogContext.put(Audit.DETAIL,"??????????????????");
        }
        catch (Exception ex){
            MessageUtil.triggerException("????????????????????????!",ExceptionUtil.getExceptionMessage(ex));
        }
        return result;
    }

    @MethodDefine(title = "??????????????????????????????????????????", path = "/generateByAttr", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("??????????????????????????????????????????")
    @AuditLog(operation = "??????????????????????????????????????????HTML")
    @PostMapping("/generateByAttr")
    public JsonResult generateByAttr(HttpServletRequest request) throws Exception {

        String attrId = RequestUtil.getString(request, "attrId");
        String relType = RequestUtil.getString(request, "relType");

        FormBoAttr boAttr = formBoAttrServiceImpl.getById(attrId);

        FormBoEntity boEntity = boEntityService.getById(boAttr.getEntId());
        boEntity.setBoRelation(new FormBoRelation().setType(relType));
        boEntity.getBoAttrList().add(boAttr);

        StringBuffer sb = new StringBuffer();

        String template = "<@fieldCtrl field=field type='" + relType + "' />";
        String fieldTemplate = getTemplateByAlias("fieldCtrl", "pc");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("ctxPath", request.getContextPath());
        model.put("field", boAttr);

        TemplateHashModel util = FtlUtil.generateStaticModel(StringUtils.class);
        model.put("util", util);

        sb.append(freemarkEngine.parseByStringTemplate(model, fieldTemplate + template));

        JSONObject metadataList = new JSONObject();
        formPcServiceImpl.parseMetadata(boEntity, "", sb.toString(), metadataList);
        JSONObject data = new JSONObject();
        data.put("html", sb.toString());
        data.put("metadata", metadataList);

        String detail="??????"+boAttr.getComment() +"," +boAttr.getName() +"???????????????HTML??????";
        LogContext.put(Audit.DETAIL,detail);

        return JsonResult.getSuccessResult(data).setShow(false);
    }

    @ApiOperation("????????????????????????????????????")
    @MethodDefine(title = "????????????????????????????????????", path = "/generateByBoEnt", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @AuditLog(operation = "????????????????????????????????????HTML")
    @PostMapping("/generateByBoEnt")
    public JsonResult generateByBoEnt(HttpServletRequest request) throws Exception {
        String entId = RequestUtil.getString(request, "entId");
        String template = RequestUtil.getString(request, "template");
        String relType = RequestUtil.getString(request, "relType");

        FormBoEntity boEnt = boEntityService.getById(entId);
        boEnt.setBoRelation(new FormBoRelation().setType(relType));
        boEnt.setBoAttrList(formBoAttrServiceImpl.getByEntId(entId));
        boEntityService.removeEntRefFields(boEnt);

        StringBuffer sb = new StringBuffer();

        String formTemplate = getTemplateByAlias(template, "pc");
        String fieldTemplate = getTemplateByAlias("fieldCtrl", "pc");

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("ctxPath", request.getContextPath());
        model.put("ent", boEnt);

        TemplateHashModel util = FtlUtil.generateStaticModel(StringUtils.class);
        model.put("util", util);

        sb.append(freemarkEngine.parseByStringTemplate(model, fieldTemplate + formTemplate));

        JSONObject metadataList = new JSONObject();
        formPcServiceImpl.parseMetadata(boEnt, template, sb.toString(), metadataList);
        JSONObject data = new JSONObject();
        data.put("html", sb.toString());
        data.put("metadata", metadataList);

        String detail="????????????:" + boEnt.getName() +"," +boEnt.getAlias() +",???????????????HTML";
        LogContext.put(Audit.DETAIL,detail);

        return JsonResult.getSuccessResult(data).setShow(false);
    }

    @MethodDefine(title = "????????????", path = "/deploy", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "formId"),@ParamDefine(title = "????????????????????????", varName = "genSolution")})
    @ApiOperation(value = "????????????")
    @AuditLog(operation = "????????????")
    @PostMapping("/deploy")
    public JsonResult deploy(@RequestParam(value = "formId") String formId,@RequestParam(value = "genSolution") boolean genSolution) {

        JsonResult result = formPcServiceImpl.updDeployStatus(formId,genSolution);
        return result.setShow(false);
    }

    @MethodDefine(title = "??????????????????????????????", path = "/getVersions", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "alias")})
    @ApiOperation(value = "????????????", notes = "??????????????????????????????")
    @GetMapping("/getVersions")
    public List<FormPc> getVersions(@RequestParam(value = "alias") String alias) {
        List<FormPc> list = formPcServiceImpl.getVersions(alias);
        return list;
    }

    @MethodDefine(title = "???????????????", path = "/switchMain", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "id")})
    @ApiOperation(value = "???????????????", notes = "???????????????")
    @AuditLog(operation = "???????????????")
    @GetMapping("/switchMain")
    public JsonResult switchMain(@RequestParam(value = "id") String id) {
        try {
            FormPc formPc = formPcServiceImpl.get(id);

            String detail="????????????:"+ formPc.getName() +"(" +id +")????????????";
            LogContext.put(Audit.DETAIL,detail);

            formPcServiceImpl.switchMain(formPc);
            return JsonResult.Success("?????????????????????!");
        } catch (Exception ex) {
            MessageUtil.triggerException("?????????????????????!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }

    }

    @MethodDefine(title = "???????????????", path = "/createVersion", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "id")})
    @ApiOperation(value = "???????????????", notes = "???????????????")
    @AuditLog(operation = "???????????????")
    @GetMapping("/createVersion")
    public JsonResult createVersion(@RequestParam(value = "id") String id) {
        try {
            FormPc formPc = formPcServiceImpl.get(id);

            FormPc newVer= formPcServiceImpl.createVersion(formPc);

            String detail="?????????????????????:"+ newVer.getName() +"(" +newVer.getId() +")";
            LogContext.put(Audit.DETAIL,detail);

            return JsonResult.Success("?????????????????????!");
        } catch (Exception ex) {
            MessageUtil.triggerException("?????????????????????!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    @MethodDefine(title = "????????????", path = "/copyNew", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation(value = "????????????", notes = "????????????")
    @AuditLog(operation = "???????????????")
    @PostMapping("/copyNew")
    public JsonResult copyNew(HttpServletRequest request) {
        String newForm = request.getParameter("newForm");
        JSONObject jsonObject = JSON.parseObject(newForm);
        String oldPkId = jsonObject.getString("oldPkId");
        String newName = jsonObject.getString("newName");
        String newAlias = jsonObject.getString("newAlias");
        FormPc formPc = formPcServiceImpl.get(oldPkId);
        String detai="????????????:"+formPc.getName() +"("+formPc.getName()+"),??????????????????:"+ newName + "("+newAlias+")";

        if (!formPcServiceImpl.isExist(newAlias, "")) {
            formPcServiceImpl.copyNewForm(formPc, newAlias, newName);
            LogContext.put(Audit.DETAIL,detai +",????????????!");
            return JsonResult.Success("??????????????????!");
        }
        LogContext.put(Audit.DETAIL,"??????????????????," + newAlias + "?????????!");
        LogContext.put(Audit.STATUS,Audit.STATUS_FAIL);

        return JsonResult.Success("??????????????????," + newAlias + "?????????!");
    }

    /**
     * PDF??????
     * @param request
     * @throws Exception
     */
    @MethodDefine(title = "??????PDF??????", path = "/genPdfTemplate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("??????PDF??????")
    @AuditLog(operation = "??????PDF??????")
    @PostMapping("/genPdfTemplate")
    public JsonResult genPdfTemplate(HttpServletRequest request) throws Exception {
        String boDefId = RequestUtil.getString(request, "bodefId");

        FormBoEntity boEnt=boEntityService.getByDefId(boDefId,true);

        List<FormTemplate> mainForms=formTemplateService
                .getByTypeAndCategory(FORM_PRINT,FormBoRelation.RELATION_MAIN);

        List<FormTemplate> oneToOneForms=formTemplateService
                .getByTypeAndCategory(FORM_PRINT,FormBoRelation.RELATION_ONETOONE);

        List<FormTemplate> oneToManyForms=formTemplateService
                .getByTypeAndCategory(FORM_PRINT,FormBoRelation.RELATION_ONETOMANY);

        JSONArray mainAry= getJsonByTemplates(mainForms);

        JSONArray rtnJson=new JSONArray();

        JSONObject mainJson=new JSONObject();
        mainJson.put("type", FormBoRelation.RELATION_MAIN);
        mainJson.put("key", boEnt.getAlias());
        mainJson.put("name", boEnt.getName());
        mainJson.put("template", mainAry);

        rtnJson.add(mainJson);

        if(BeanUtil.isNotEmpty( boEnt.getBoEntityList())){
            for(FormBoEntity subEnt: boEnt.getBoEntityList()){
                JSONObject subJson=new JSONObject();
                JSONArray subAry=null;
                if(subEnt.getRelationType().equals(FormBoRelation.RELATION_ONETOONE)){
                    subAry= getJsonByTemplates(oneToOneForms);
                    subJson.put("type", FormBoRelation.RELATION_ONETOONE);
                }
                else{
                    subAry= getJsonByTemplates(oneToManyForms);
                    subJson.put("type", FormBoRelation.RELATION_ONETOMANY);
                }
                subJson.put("key", subEnt.getAlias());
                subJson.put("name", subEnt.getName());
                subJson.put("template", subAry);
                rtnJson.add(subJson);
            }
        }

        String detail="??????BO????????????HTML,????????????:"+ boEnt.getName() +"("+boEnt.getAlias()+")";
        LogContext.put(Audit.DETAIL,detail);


        return new JsonResult().setData(rtnJson);
    }

    /**
     * PDF??????
     * @param request
     * @throws Exception
     */
    @MethodDefine(title = "??????PDF??????", path = "/pdfTempEdit", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "request")})
    @ApiOperation("??????PDF??????")
    @AuditLog(operation = "??????BO??????????????????HTML")
    @RequestMapping(value = "pdfTempEdit",method = {RequestMethod.GET,RequestMethod.POST})
    public JsonResult pdfTempEdit(HttpServletRequest request) throws Exception {

        String boDefId=RequestUtil.getString(request, "boDefId") ;
        String templates=RequestUtil.getString(request, "templates") ;
        String html= genTemplate(boDefId,templates);

        String detail="??????BoDefId("+boDefId+")????????????HTML";

        return new JsonResult().setData(html);
    }

    /**
     * ??????????????????
     * @param boDefId
     * @param templates
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    private String genTemplate(String boDefId,String templates) throws TemplateException, IOException{

        FormBoEntity boEnt=boEntityService.getByDefId(boDefId,true);


        JSONObject jsonObj=JSONObject.parseObject(templates);

        //???????????????
        String mainAlias=jsonObj.getJSONObject("main").getString(boEnt.getName());

        StringBuffer sb=new StringBuffer();
        String fieldTemplate= formTemplateService.getByAliasAndType("field", FORM_PRINT);
        String formTemplate= formTemplateService.getByAliasAndType(mainAlias, FORM_PRINT);

        Map<String,Object> model=new HashMap<String,Object>();

        model.put("ent", boEnt);

        sb.append(freemarkEngine.parseByStringTemplate( model,fieldTemplate+ formTemplate));

        //????????????
        JSONArray ary= jsonObj.getJSONArray("sub");

        List<FormBoEntity> entList=boEnt.getBoEntityList();
        if(BeanUtil.isNotEmpty(entList)){
            for(FormBoEntity subEnt:entList){
                String name=subEnt.getName();

                String template=getTemplate(name,ary);
                String subTemplate= formTemplateService.getByAliasAndType(template, FORM_PRINT);

                model.put("ent", subEnt);

                sb.append(freemarkEngine.parseByStringTemplate(model,fieldTemplate + subTemplate));
            }
        }


        return sb.toString();
    }

    /**
     * ?????????????????????????????????
     * @param name
     * @param ary
     * @return
     */
    private String getTemplate(String name,JSONArray ary){
        for(Object obj:ary){
            JSONObject subObj=(JSONObject)obj;
            if(subObj.containsKey(name)){
                return subObj.getString(name);
            }
        }
        return "";
    }


    /**
     * ??????????????????
     * @param forms
     * @return
     */
    private JSONArray getJsonByTemplates(List<FormTemplate> forms){
        JSONArray ary=new JSONArray();
        for(FormTemplate template:forms){
            JSONObject obj=new JSONObject();
            obj.put("alias", template.getAlias());
            obj.put("name", template.getName());
            ary.add(obj);
        }
        return ary;
    }

    @MethodDefine(title = "????????????ID????????????", path = "/updStatusByInstId", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "bpmInstDataDtos")})
    @ApiOperation("????????????ID????????????")
    @AuditLog(operation = "????????????ID????????????")
    @PostMapping("updStatusByInstId")
    public  JsonResult updStatusByInstId(@RequestBody List<BpmInstDataDto> bpmInstDataDtos){

        String detail=JSONArray.toJSONString(bpmInstDataDtos);

        LogContext.put(Audit.DETAIL,detail);


        for(BpmInstDataDto dto:bpmInstDataDtos){

            formDataService.updDataByStatus(dto.getBodefAlias(),
                    dto.getInstId(),dto.getStatus(),dto.getPk());
        }

        return JsonResult.Success();
    }

    @MethodDefine(title = "?????????????????????", path = "/getServerAddress", method = HttpMethodConstants.GET)
    @ApiOperation(value = "?????????????????????")
    @GetMapping("getServerAddress")
    public JSONObject getServerAddress(@RequestParam(value = "ctxPath",required = false) String ctxPath){
        JSONObject jsonObject=new JSONObject();
        String serverAddress =SysPropertiesUtil.getString("serverAddress");
        if(!"false".equals(ctxPath)) {
            serverAddress += SysPropertiesUtil.getString("ctxPath");
        }
        if(StringUtils.isEmpty(serverAddress)){
            serverAddress="http://localhost/jpaas";
        }
        jsonObject.put("serverAddress",serverAddress);

        IUser currentUser = ContextUtil.getCurrentUser();
        JSONObject user=new JSONObject();
        user.put("userId",currentUser.getUserId());
        user.put("userName",currentUser.getFullName());
        jsonObject.put("currentUser",user);
        return jsonObject;
    }

    @MethodDefine(title = "????????????????????????", path = "/getOpinionTemplate", method = HttpMethodConstants.GET)
    @ApiOperation(value = "????????????????????????")
    @GetMapping("getOpinionTemplate")
    public String getOpinionTemplate(){
        String template="";
        try {
            template=freemarkEngine.mergeTemplateIntoString("/view/opinionTemplate.ftl",new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return template;
    }

    @Override
    protected JsonResult beforeSave(FormPc ent) {
        boolean isExist=formPcServiceImpl.isExist(ent.getAlias(),ent.getId());
        if(isExist){
            return JsonResult.Fail("???????????????????????????,???????????????KEY!");
        }
        ent.setMain(1);
        ent.setDeployed(1);
        ent.setType(FormPc.FORM_TYPE_SEL_DEV);
        return super.beforeSave(ent);
    }

    @MethodDefine(title = "??????????????????????????????", path = "/getHistoryTemplate", method = HttpMethodConstants.GET)
    @ApiOperation(value = "??????????????????????????????")
    @GetMapping("getHistoryTemplate")
    public JsonResult getHistoryTemplate(){
        String template= formTemplateService.getByAliasAndType("approvalHistory", "print");
        return new JsonResult().setShow(false).setData(template);
    }
}
