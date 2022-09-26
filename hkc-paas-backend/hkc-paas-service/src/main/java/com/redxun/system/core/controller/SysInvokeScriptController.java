package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.system.core.entity.SysInvokeScript;
import com.redxun.system.core.export.SysInvokeExport;
import com.redxun.system.core.service.SysInvokeScriptServiceImpl;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.controller.IExport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/system/core/sysInvokeScript")
@ClassDefine(title = "脚本调用",alias = "sysInvokeScriptController",path = "/system/core/sysInvokeScript",packages = "core",packageName = "系统管理")
@Api(tags = "脚本调用")
public class SysInvokeScriptController extends BaseController<SysInvokeScript> {

    @Autowired
    SysInvokeScriptServiceImpl sysInvokeScriptService;
    @Autowired
    GroovyEngine groovyEngine;
    @Autowired
    SystemClient systemClient;
    @Autowired
    SysInvokeExport sysInvokeExport;
    @Override
    protected IExport getExport() {
        return sysInvokeExport;
    }
    @Override
    public BaseService getBaseService() {
        return sysInvokeScriptService;
    }

    @Override
    public String getComment() {
        return "脚本调用";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","FORM","read");
        super.handleFilter(filter);
    }



    @Override
    protected JsonResult beforeSave(SysInvokeScript ent) {
        boolean isExist = sysInvokeScriptService.isExist(ent);
        if(isExist){
            return JsonResult.Fail("调用脚本别名已存在!");
        }
        return super.beforeSave(ent);
    }

    @MethodDefine(title = "执行脚本", path = "/invoke/*", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "脚本别名", varName = "alias"),@ParamDefine(title = "执行参数", varName = "params")})
    @AuditLog(operation = "执行脚本")
    @PostMapping("/invoke/{alias}")
    public JsonResult invoke(@PathVariable("alias")String alias, @RequestBody JSONObject params) throws Exception{
        SysInvokeScript invokeScript=sysInvokeScriptService.getByAlias(alias);
        if(invokeScript==null){
            return new JsonResult(false,"不存在标识键为："+ alias+"脚本配置");
        }
        Map<String,Object> model=new HashMap<>(params.size()+1);
        try{
            Map<String,Object> variables = params.getInnerMap();
            model.putAll(variables);
            model.put("params",variables);


            Object obj=groovyEngine.executeScripts(invokeScript.getContent(), model);
            JsonResult result= new JsonResult(true,"成功执行!");
            result.setData(obj).setShow(false);
            return result;
        }
        catch(Exception ex){
            MessageUtil.triggerException("执行脚本出错!",ExceptionUtil.getExceptionMessage(ex) );
            return null;
        }
    }


    @MethodDefine(title = "测试脚本", path = "/test", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "执行参数", varName = "params")})
    @AuditLog(operation = "测试脚本")
    @PostMapping("/test")
    public JsonResult test(@RequestBody  JSONObject json) throws Exception{

        Map<String,Object> model=new HashMap<>();
        try{
            String script= json.getString("script");
            JSONArray paramAry=json.getJSONArray("params");

            Map<String,Object> params =new HashMap<>();

            if(paramAry!=null && paramAry.size()>0){
                for(int i=0;i<paramAry.size();i++){
                    JSONObject paramJson=paramAry.getJSONObject(i);
                    String paramName=paramJson.getString("paramName");
                    String paramType=paramJson.getString("paramType");
                    String value=paramJson.getString("value");

                    Object val=handType(value,paramType);
                    params.put(paramName,val);
                }
            }
            model.put("params",params);
            Object obj=groovyEngine.executeScripts(script, model);
            return JsonResult.getSuccessResult(handOutput(obj));
        }
        catch(Exception ex){
            String message=ExceptionUtil.getExceptionMessage(ex);
            return JsonResult.getFailResult(message);
        }
    }

    private Object handOutput(Object obj){
        if(obj instanceof  JSONObject  ){
            JSONObject tmp=(JSONObject)obj;
            return  tmp.toJSONString();
        }
        else if(obj instanceof JSONArray){
            JSONArray tmp=(JSONArray)obj;
            return  tmp.toJSONString();
        }

        return  obj;
    }

    /**
     * 处理参数值类型。
     * @param value
     * @param type string, int,date,boolean,obj,string,long,double
     * @return
     */
    private Object handType(String value,String type){
        if(type.equals("string")){
            return value;
        }
        else if(type.equals("int") ){
            return  Integer.parseInt(value);
        }
        else if(type.equals("long") ){
            return  Long.parseLong(value);
        }
        else if(type.equals("double") ){
            return  Double.parseDouble(value);
        }
        else if(type.equals("boolean") ){
            return  Boolean.parseBoolean(value);
        }
        else if(type.equals("date") ){
            return DateUtils.parseDate(value);
        }
        else if(type.equals("obj") ){
            return JSONObject.parseObject(value);
        }


        return  value;
    }




    @MethodDefine(title = "导入调用脚本", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "导入调用脚本")
    @ApiOperation("导入调用脚本")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String treeId=request.getParameter("treeId");
        String appId=request.getParameter("appId");
        sysInvokeScriptService.importInvokeScript(file,treeId,appId);
        return JsonResult.Success().setMessage("导入成功");
    }
}
