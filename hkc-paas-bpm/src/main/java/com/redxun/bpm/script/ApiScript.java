package com.redxun.bpm.script;

import com.redxun.bpm.script.cls.ClassScriptType;
import com.redxun.bpm.script.cls.IScript;
import com.redxun.bpm.script.cls.MethodDefine;
import com.redxun.bpm.script.cls.ParamDefine;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.feign.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;

@ClassScriptType(type = "ApiScript",description = "调用服务脚本")
@Component("ApiScript")
public class ApiScript implements IScript {
    @Autowired
    OrgClient orgClient;
    @Autowired
    PortalClient portalClient;
    @Autowired
    SystemClient systemClient;
    @Autowired
    FormClient formClient;
    @Autowired
    BpmClient bpmClient;
    @Autowired
    CustomerClient customerClient;

    @MethodDefine(title="调用自定义服务接口",description = "根据服务名和接口路径调用")
    public Object executeCustomerApi(@ParamDefine(varName = "appName",description = "服务名")String appName,
                                     @ParamDefine(varName = "url",description = "接口路径")String url,
                                     @ParamDefine(varName = "type",description = "请求方式(POST/GET)")String type,
                                     @ParamDefine(varName = "params",description = "传入参数") Map<String,Object> params){
        if(HttpMethodConstants.POST.getValue().equals(type)){
            return customerClient.executePostApi(appName,url,params);
        }
        return customerClient.executeGetApi(appName,url,params);
    }
    @MethodDefine(title="调用用户服务接口",description = "根据接口路径调用")
    public Object executeUserApi(@ParamDefine(varName = "url",description = "接口路径")String url,@ParamDefine(varName = "type",description = "请求方式(POST/GET)")String type,@ParamDefine(varName = "params",description = "传入参数") Map<String,Object> params){
        if(HttpMethodConstants.POST.getValue().equals(type)){
            return orgClient.executePostApi(url,params);
        }
        return orgClient.executeGetApi(url,params);
    }
    @MethodDefine(title="调用门户服务接口",description = "根据接口路径调用")
    public Object executePortalApi(@ParamDefine(varName = "url",description = "接口路径")String url,@ParamDefine(varName = "type",description = "请求方式(POST/GET)")String type,@ParamDefine(varName = "params",description = "传入参数")Map<String,Object> params){
        if(HttpMethodConstants.POST.getValue().equals(type)){
            return portalClient.executePostApi(url,params);
        }
        return portalClient.executeGetApi(url,params);
    }
    @MethodDefine(title="调用系统服务接口",description = "根据接口路径调用")
    public Object executeSystemApi(@ParamDefine(varName = "url",description = "接口路径")String url,@ParamDefine(varName = "type",description = "请求方式(POST/GET)")String type,@ParamDefine(varName = "params",description = "传入参数")Map<String,Object> params){
        if(HttpMethodConstants.POST.getValue().equals(type)){
            return systemClient.executePostApi(url,params);
        }
        return systemClient.executeGetApi(url,params);
    }
    @MethodDefine(title="调用表单服务接口",description = "根据接口路径调用")
    public Object executeFormApi(@ParamDefine(varName = "url",description = "接口路径")String url,@ParamDefine(varName = "type",description = "请求方式(POST/GET)")String type,@ParamDefine(varName = "params",description = "传入参数")Map<String,Object> params){
        if(HttpMethodConstants.POST.getValue().equals(type)){
            return formClient.executePostApi(url,params);
        }
        return formClient.executeGetApi(url,params);
    }
    @MethodDefine(title="调用流程服务接口",description = "根据接口路径调用")
    public Object executeBpmApi(@ParamDefine(varName = "url",description = "接口路径")String url,@ParamDefine(varName = "type",description = "请求方式(POST/GET)")String type,@ParamDefine(varName = "params",description = "传入参数")Map<String,Object> params){
        if(HttpMethodConstants.POST.getValue().equals(type)){
            return bpmClient.executePostApi(url,params);
        }
        return bpmClient.executeGetApi(url,params);
    }
}
