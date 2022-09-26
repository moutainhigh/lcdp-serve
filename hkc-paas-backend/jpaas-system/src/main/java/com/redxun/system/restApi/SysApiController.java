package com.redxun.system.restApi;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.controller.RestApiController;
import com.redxun.idempotence.IdempotenceRequired;
import com.redxun.mq.MessageModel;
import com.redxun.msgsend.util.MesAutoUtil;
import com.redxun.system.core.entity.SysInvokeScript;
import com.redxun.system.core.service.SysInterfaceApiServiceImpl;
import com.redxun.system.core.service.SysInvokeScriptServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统服务外部API
 */
@Slf4j
@RestController
@RequestMapping("/restApi/system")
@ClassDefine(title = "系统服务外部API", alias = "sysApiController",path = "/restApi/system",packageName = "系统服务接口")
public class SysApiController implements RestApiController {
    @Autowired
    SysInterfaceApiServiceImpl sysInterfaceApiService;

    @Autowired
    SysInvokeScriptServiceImpl sysInvokeScriptService;

    @Autowired
    GroovyEngine groovyEngine;

    @MethodDefine(title = "执行接口", path = "/executeApi", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "接口ID", varName = "apiId"),@ParamDefine(title = "传递参数",varName = "params")})
    @IdempotenceRequired
    @PostMapping("/executeApi")
    public JsonResult executeApi(@ApiParam @RequestParam String apiId, @ApiParam @RequestBody String params) throws Exception{
        return sysInterfaceApiService.executeApi(apiId,params, IdGenerator.getIdStr());
    }

    @MethodDefine(title = "统一消息发送接口", path = "/sendMessageApi", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "消息参数",varName = "model")})
    @IdempotenceRequired
    @PostMapping("/sendMessageApi")
    public JsonResult sendMessageApi(@RequestBody MessageModel model){
        MesAutoUtil.sendMessage(JSONObject.toJSONString(model));
        return JsonResult.getSuccessResult("发送消息成功！");
    }

    @MethodDefine(title = "调用脚本", path = "/invokeScript", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "脚本别名", varName = "alias"),@ParamDefine(title = "传递参数",varName = "params")})
    @IdempotenceRequired
    @PostMapping("/invokeScript/{alias}")
    public JsonResult invokeScript(@ApiParam @PathVariable("alias")String alias,@RequestBody JSONObject params) throws Exception{
        SysInvokeScript invokeScript=sysInvokeScriptService.getByAlias(alias);
        if(invokeScript==null){
            return new JsonResult(false,"不存在标识键为："+ alias+"脚本配置");
        }
        Map<String,Object> model=new HashMap<>(params.size());
        try{
            if(BeanUtil.isNotEmpty(params)){
                model.putAll(params);
                model.put("params",params);
            }
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
