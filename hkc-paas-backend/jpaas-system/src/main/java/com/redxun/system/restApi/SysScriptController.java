package com.redxun.system.restApi;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.idempotence.IdempotenceRequired;
import com.redxun.log.annotation.AuditLog;
import com.redxun.system.core.entity.SysInvokeScript;
import com.redxun.system.core.service.SysInvokeScriptServiceImpl;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/restApi/system")
@ClassDefine(title = "系统服务外部API", alias = "sysApiController",path = "/restApi/system",packageName = "系统服务接口")
public class SysScriptController {

    @Resource
    SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Autowired
    GroovyEngine groovyEngine;


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
            MessageUtil.triggerException("执行脚本出错!", ExceptionUtil.getExceptionMessage(ex) );
            return null;
        }
    }
}
