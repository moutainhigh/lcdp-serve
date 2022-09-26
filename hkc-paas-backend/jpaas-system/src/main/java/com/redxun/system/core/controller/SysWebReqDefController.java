package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.RequestUtil;
import com.redxun.dto.form.AlterSql;
import com.redxun.dto.sys.SysWebReqDefDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysWebReqDef;
import com.redxun.system.core.service.SysWebReqDefServiceImpl;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/system/core/sysWebReqDef")
@ClassDefine(title = "WEB请求调用定义",alias = "sysWebReqDefController",path = "/system/core/sysWebReqDef",packages = "core",packageName = "系统管理")
@Api(tags = "WEB请求调用定义")
public class SysWebReqDefController extends BaseController<SysWebReqDef> {

    @Autowired
    SysWebReqDefServiceImpl sysWebReqDefServiceImpl;

    @Override
    public BaseService getBaseService() {
        return sysWebReqDefServiceImpl;
    }

    @Override
    public String getComment() {
        return "WEB请求调用定义";
    }

    @Override
    protected JsonResult beforeSave(SysWebReqDef ent) {
        boolean isExist = sysWebReqDefServiceImpl.isExist(ent);
        if (isExist) {
            return new JsonResult(false, "【" + ent.getAlias() + "】定义已存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "根据别名获取请求定义", path = "/getByAlias", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "别名", varName = "alias")})
    @ApiOperation(value = "根据别名获取请求定义")
    @GetMapping("getByAlias")
    public SysWebReqDefDto getByAlias(@RequestParam("alias") String alias) {
        SysWebReqDef sysWebReqDef = sysWebReqDefServiceImpl.getByAlias(alias);
        SysWebReqDefDto sysWebReqDefDto = new SysWebReqDefDto();
        if (BeanUtil.isNotEmpty(sysWebReqDef)) {
            BeanUtil.copyProperties(sysWebReqDefDto, sysWebReqDef);
        }
        return sysWebReqDefDto;
    }

    @MethodDefine(title = "执行调用", path = "/execute", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation(value = "执行调用")
    @PostMapping("execute")
    public JsonResult execute(@RequestBody JSONObject jsonObject) throws Exception {
        String mode = jsonObject.getString("mode");
        String url = jsonObject.getString("url");
        String type = jsonObject.getString("type");
        String header = jsonObject.getString("header");
        String body = jsonObject.getString("body");
        String temp = jsonObject.getString("temp");
        if ("SOAP".equals(mode)) {
            return sysWebReqDefServiceImpl.executeReq(url, "POST", sysWebReqDefServiceImpl.parseParamsMap(header), sysWebReqDefServiceImpl.parseParamsMap(body), temp,null,null);
        } else {
            return sysWebReqDefServiceImpl.executeReq(url, type, sysWebReqDefServiceImpl.parseParamsMap(header), sysWebReqDefServiceImpl.parseParamsMap(body), temp,null,null);
        }
    }

    @MethodDefine(title = "初始化脚本", path = "/initDefaultCode", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation(value = "初始化脚本")
    @PostMapping("initDefaultCode")
    public JsonResult initDefaultCode(@RequestBody JSONObject jsonObject) throws Exception {
        String alias = jsonObject.getString("alias");
        JsonResult result = new JsonResult();
        if (StringUtils.isEmpty(alias)) {
            result.setSuccess(false);
            result.setMessage("缺少WebReq请求的别名。");
            return result;
        }
        String initCode = "import com.alibaba.fastjson.JSONObject;\n" +
                "import com.redxun.common.base.entity.JsonPageResult;\n" +
                "import com.baomidou.mybatisplus.extension.plugins.pagination.Page;\n" +
                "Page page=new Page();\n" +
                "String data=sqlScript.doExecuteWebReq(\"" + alias + "\",$header,$params,$body);\n" +
                "JSONObject result = JSONObject.parseObject(data);\n" +
                "//page.setRecords();\n" +
                "//page.setTotal();\n" +
                "JsonPageResult pageResult=new JsonPageResult(page);\n" +
                "pageResult.setSuccess(true);\n" +
                "return pageResult;";

        String resultHeaderCode = initCode;

        JSONObject resJson = new JSONObject();
        resJson.put("resultHeaderCode", resultHeaderCode);

        result.setSuccess(true);
        result.setData(resJson);

        return result;
    }

    @MethodDefine(title = "执行脚本", path = "/executeScript", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation(value = "执行脚本")
    @PostMapping("executeScript")
    public JsonResult executeScript(@RequestBody JSONObject jsonObject) throws Exception {
        String scriptText = jsonObject.getString("scriptText");
        String webreqKey = jsonObject.getString("key");
        String header = jsonObject.getString("header");
        String params = jsonObject.getString("params");
        String body = jsonObject.getString("body");
        JsonResult obj = JsonResult.Success();
        try {
            Map<String, String> headerMap = new HashMap<>(SysConstant.INIT_CAPACITY_16);
            Map<String, String> paramsMap = new HashMap<>(SysConstant.INIT_CAPACITY_16);
            if (StringUtils.isNotEmpty(header)) {
                headerMap = (Map) JSON.parse(header);
            }
            if (StringUtils.isNotEmpty(params)) {
                paramsMap = (Map) JSON.parse(params);
            }
            Object result = sysWebReqDefServiceImpl.executeScript(scriptText, webreqKey, headerMap, paramsMap, body);
            obj.setData(result);
        } catch (Exception e) {
            obj.setSuccess(false);
            obj.setMessage("执行出错： " + e.getMessage());
        }
        return obj;
    }

    @MethodDefine(title = "启动脚本", path = "/start", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation(value = "启动脚本")
    @PostMapping("start")
    public JsonResult start(@RequestBody JSONObject jsonObject) throws Exception {
        String alias = jsonObject.getString("alias");
        String header = jsonObject.getString("header");
        String params = jsonObject.getString("params");
        String paramData = jsonObject.getString("paramData");
        JSONObject json = JSONObject.parseObject(paramData);

        return sysWebReqDefServiceImpl.executeStart(alias, header, params, json.getInnerMap());
    }

    @MethodDefine(title = "导出WEB请求定义", path = "/doExport", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "列表", varName = "solutionIds")})
    @ApiOperation("导出WEB请求定义")
    @AuditLog(operation = "导出WEB请求定义")
    @GetMapping("/doExport")
    public void doExport(@ApiParam @RequestParam(value = "solutionIds") String solutionIds)throws Exception{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        if(StringUtils.isEmpty(solutionIds)){
            LogContext.addError("导出失败，请选择要导出的记录。");
            return;
        }
        StringBuilder sb=new StringBuilder();
        sb.append("导出WEB请求定义:");

        String[] idArr = solutionIds.split(",");
        Map<String,String> map=new HashMap<>();
        for(String id : idArr) {
            JSONObject json = new JSONObject();
            SysWebReqDef sysWebReqDef = sysWebReqDefServiceImpl.get(id);
            json.put("sysWebReq", sysWebReqDef);
            sb.append(sysWebReqDef.getName() +"("+id+"),");

            String fileName =id+".json";
            String defStr = JSONObject.toJSONString(json);
            map.put(fileName,defStr);
        }
        LogContext.put(Audit.DETAIL,sb.toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String downFileName = "Sys-WebReqDef-" + sdf.format(new Date());
        FileUtil.downloadZip(response,downFileName,map);
    }


    @MethodDefine(title = "导入WEB请求定义", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "导入WEB请求定义")
    @ApiOperation("导入WEB请求定义")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = "Sys-WebReqDef";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String treeId=request.getParameter("treeId");
        String appId=request.getParameter("appId");
          sysWebReqDefServiceImpl.importWebReq(file,treeId,appId);
        return JsonResult.Success().setMessage("导入成功");
    }
}
