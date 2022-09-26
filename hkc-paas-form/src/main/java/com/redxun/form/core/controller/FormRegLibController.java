package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.cache.CacheUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.core.entity.FormRegLib;
import com.redxun.form.core.service.FormRegLibServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
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

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/form/core/formRegLib")
@ClassDefine(title = "正则表达式",alias = "formRegLibController",path = "/form/core/formRegLib",packages = "core",packageName = "表单管理")
@Api(tags = "正则表达式")
@ContextQuerySupport(company = ContextQuerySupport.BOTH,tenant = ContextQuerySupport.BOTH)
public class FormRegLibController extends BaseController<FormRegLib> {

    @Autowired
    FormRegLibServiceImpl formRegLibService;

    @Override
    public BaseService getBaseService() {
        return formRegLibService;
    }

    @Override
    public String getComment() {
        return "正则表达式";
    }

    @Override
    protected JsonResult beforeRemove(List<String> list) {
        for(String id:list){
            FormRegLib formRegLib=formRegLibService.get(id);
            CacheUtil.remove(FormRegLib.MASKING,formRegLib.getKey());
        }
        return super.beforeRemove(list);
    }

    @Override
    protected JsonResult beforeSave(FormRegLib ent) {
        boolean isExist= formRegLibService.isExist(ent);
        if(isExist){
            return  JsonResult.Fail("数据校验别名已存在!");
        }
        if(ent!=null) {
            CacheUtil.set(FormRegLib.MASKING, ent.getKey(), ent);
        }
        return  JsonResult.Success();
    }

    @MethodDefine(title = "导出", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出")
    @AuditLog(operation = "导出正则表达式规则")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "solutionIds") String solutionIds)throws Exception{


        StringBuilder sb=new StringBuilder();
        sb.append("导出正则表达式规则,");

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            sb.append("导出失败，请选择要导出的记录。");
            LogContext.addError(sb.toString());
            return;
        }

        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {
            JSONObject json = new JSONObject();
            FormRegLib formRegLib = formRegLibService.get(id);
            json.put("formRegLib", formRegLib);

            sb.append(formRegLib.getName()+"("+id+"),");

            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Form-Rule-" + sdf.format(new Date());


        LogContext.put(Audit.DETAIL,sb.toString());


        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "导入正则表达式", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "导入正则表达式")
    @PostMapping("/doImport")
    @ApiOperation("批量导入")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Form-Rule";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        String appId=request.getParameter("appId");
        formRegLibService.importRegLib(file,treeId,appId);
        return JsonResult.Success().setMessage("导入成功");
    }
}
