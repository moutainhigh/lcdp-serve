package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.dto.form.FormMobileDto;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.entity.FormMobile;
import com.redxun.form.core.entity.FormRule;
import com.redxun.form.core.service.FormDataService;
import com.redxun.form.core.service.FormMobileServiceImpl;
import com.redxun.form.core.service.export.FormMobileExport;
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
@RequestMapping("/form/core/formMobile")
@ClassDefine(title = "????????????",alias = "formMobileController",path = "/form/core/formMobile",packages = "core",packageName = "????????????")
@Api(tags = "????????????")
public class FormMobileController extends BaseController<FormMobile> {

    @Autowired
    FormMobileServiceImpl formMobileServiceImpl;
    @Autowired
    private FormBoDefServiceImpl formBoDefService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    FormMobileExport formMobileExport;
    @Override
    public BaseService getBaseService() {
        return formMobileServiceImpl;
    }
    @Override
    protected IExport getExport() {
        return formMobileExport;
    }
    @Override
    public String getComment() {
        return "????????????";
    }

    @Resource
    private FormDataService formDataService;

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","FORM","read");
        super.handleFilter(filter);
    }

    @MethodDefine(title = "??????????????????", path = "/importMobileForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????", varName = "formMobileJson")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("importMobileForm")
    public JsonResult importMobileForm(@RequestBody JSONObject formMobileJson) {
        JsonResult result=JsonResult.Success("????????????????????????!");
        StringBuilder sb=new StringBuilder();
        sb.append("??????????????????,??????????????????:");
        JSONArray formMobileDtos = formMobileJson.getJSONArray("formMobileDto");
        String treeId=formMobileJson.getString("treeId");
        String appId=formDataService.getAppIdByTreeId(treeId);
        for (int i=0;i<formMobileDtos.size();i++) {
            JSONObject formMobileDtoJson = formMobileDtos.getJSONObject(i);
            String formMobileDtoStr = formMobileDtoJson.toJSONString();
            try {
                FormMobile formMobile = JSONObject.parseObject(formMobileDtoStr,FormMobile.class);
                String formMobileId = formMobile.getId();
                formMobileServiceImpl.delete(formMobileId);
                formMobile.setCategoryId(treeId);
                formMobile.setAppId(appId);
                formMobileServiceImpl.insert(formMobile);
                sb.append(formMobile.getName() +"("+formMobile.getId()+"),???????????????:" + treeId);
            }catch (Exception e){
                String message=ExceptionUtil.getExceptionMessage(e);
                sb.append("????????????,????????????:" + message );
                result=JsonResult.Fail("????????????:" + message);
            }
        }
        LogContext.put(Audit.DETAIL,sb.toString());
        return  result;
    }

    @MethodDefine(title = "??????????????????????????????", path = "/getMobleFormByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????", varName = "alias")})
    @GetMapping("getMobleFormByAlias")
    public FormMobileDto getMobleFormByAlias(@RequestParam (value="alias") String alias){
        FormMobileDto formMobileDto =new FormMobileDto();
        FormMobile formMobile = formMobileServiceImpl.getByAlias(alias);
        try {
            BeanUtil.copyNotNullProperties(formMobileDto, formMobile);
        }catch (Exception e){
            log.error("--FormMobileController.getMobleFormByAlias is error---:"+e.getMessage());
        }
        return formMobileDto;
    }

    @MethodDefine(title = "??????????????????????????????????????????", path = "/getByBoAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "??????????????????", varName = "boAlias")})
    @GetMapping("getByBoAlias")
    public List<FormMobile> getByBoAlias(@RequestParam (value="boAlias") String boAlias){
        FormBoDef formBoDef = formBoDefService.getByAlias(boAlias);
        if(BeanUtil.isEmpty(formBoDef)){
            return new ArrayList<>();
        }
        return formMobileServiceImpl.getByBoDefId(formBoDef.getId(),"");
    }

    @MethodDefine(title = "??????????????????", path = "/saveForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????", varName = "json")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("saveForm")
    public JsonResult saveForm(@RequestBody JSONObject json) throws Exception{
        StringBuilder sb=new StringBuilder();
        sb.append("??????????????????:");
        FormMobile formMobile= json.toJavaObject(FormMobile.class);
        sb.append(formMobile.getName() +"("+formMobile.getId()+")");

        String pkId=formMobile.getId();
        String appId=formDataService.getAppIdByTreeId(formMobile.getCategoryId());
        formMobile.setAppId(appId);

        JsonResult jsonResult=new JsonResult();

        boolean isExist=formMobileServiceImpl.isExist(formMobile.getAlias(),pkId);
        if(isExist){
            sb.append(",?????????????????????");
            return JsonResult.Fail("?????????????????????!").setSuccess(false);
        }
        if(StringUtils.isEmpty(pkId)) {
            String id = IdGenerator.getIdStr();
            formMobile.setId(id);
            formMobileServiceImpl.insert(formMobile);
        }
        else{
            formMobileServiceImpl.update(formMobile);
        }
        sb.append(",????????????!");

        LogContext.put(Audit.DETAIL,sb.toString());

        return JsonResult.Success("??????????????????!").setData(formMobile.getId());
    }


    @MethodDefine(title = "??????????????????", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID", varName = "request")})
    @AuditLog(operation = "??????????????????")
    @PostMapping("/doImport")
    @ApiOperation("????????????")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {

        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("???????????????????????????");
        }
        String treeId=request.getParameter("treeId");
        formMobileServiceImpl.importMobileForm(file,treeId);
        return JsonResult.Success().setMessage("????????????");
    }



}
