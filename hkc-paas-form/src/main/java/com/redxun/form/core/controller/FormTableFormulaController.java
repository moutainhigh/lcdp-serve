package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.feign.sys.SystemClient;
import com.redxun.form.bo.entity.AlterSql;
import com.redxun.form.bo.entity.FormBoDef;
import com.redxun.form.bo.service.FormBoDefServiceImpl;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.entity.FormTableFormula;
import com.redxun.form.core.service.FormTableFormulaServiceImpl;
import com.redxun.form.core.service.export.FormTableExport;
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

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/form/core/formTableFormula")
@ClassDefine(title = "表间公式",alias = "formTableFormulaController",path = "/form/core/formTableFormula",packages = "core",packageName = "表单管理")
@Api(tags = "表间公式")
public class FormTableFormulaController extends BaseController<FormTableFormula> {

    @Autowired
    FormTableFormulaServiceImpl formTableFormulaService;
    @Autowired
    FormBoDefServiceImpl formBoDefService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    FormTableExport formTableExport;

    @Override
    protected IExport getExport() {
        return formTableExport;
    }

    @Override
    public BaseService getBaseService() {
        return formTableFormulaService;
    }

    @Override
    public String getComment() {
        return "表间公式";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        Map<String,Object> map = filter.getParams();
        if(map.containsKey("boAlias")){
            String boAlias=(String)map.get("boAlias");
            String[] boAry = boAlias.split(",");
            List<String> boIdAry=new ArrayList<>();
            for(int i=0;i<boAry.length;i++) {
                FormBoDef formBoDef=formBoDefService.getByAlias(boAry[i]);
                boIdAry.add(formBoDef.getId());
            }
            filter.addQueryParam(new QueryParam("BO_DEF_ID_",QueryParam.OP_IN, StringUtils.join(boIdAry,",")));
        }
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");

        super.handleFilter(filter);
    }


    /**
     * 获取表单公式字段处理器
     * @param json
     * @return
     */
    @ApiOperation("获取表单公式字段处理器")
    @PostMapping(value="getTableFieldValueHandler")
    public JSONObject getTableFieldValueHandler(@RequestBody JSONObject json){
        JSONObject formData=json.getJSONObject("formData");
        JSONObject setting=json.getJSONObject("setting");
        JSONObject conf=setting.getJSONObject("conf");
        DataHolder dataHolder=new DataHolder();
        JSONObject curMain=new JSONObject();
        String boDefs=conf.getString("boDefs");
        for(String boDef:boDefs.split(",")){
            curMain.put(boDef,formData.getJSONObject(boDef));
        }
        dataHolder.setCurMain(curMain);
        return formTableFormulaService.getTableFieldValueHandler(dataHolder,conf);
    }


    @MethodDefine(title = "导入表间公式", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "导入表间公式")
    @PostMapping("/doImport")
    @ApiOperation("批量导入")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if(!checkName.contains(formsolution)){
            LogContext.addError("请选择正确的压缩包!");
            return JsonResult.Fail("请选择正确的压缩包!");
        }
        String treeId=request.getParameter("treeId");
        formTableFormulaService.importFormula(file,treeId);
        return JsonResult.Success().setMessage("导入成功");
    }

}
