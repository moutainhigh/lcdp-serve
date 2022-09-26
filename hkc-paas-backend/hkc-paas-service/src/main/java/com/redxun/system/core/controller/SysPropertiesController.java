package com.redxun.system.core.controller;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.utils.OtherConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/system/core/sysProperties")
@ClassDefine(title = "系统参数",alias = "sysPropertiesController",path = "/system/core/sysProperties",packages = "core",packageName = "系统管理")
@Api(tags = "系统参数")
public class SysPropertiesController {

    private final String JPAAS_CONFIG = "nacos-config.properties";
    private final String DEFAULT_GROUP = "DEFAULT_GROUP";
//    @Autowired
//    private ConfigService configService;

    @ApiOperation("获取系统参数")
    @MethodDefine(title = "获取系统参数", path = "/getConfig", method = HttpMethodConstants.GET)
    @GetMapping(value = "getConfig")
    public JsonResult getConfig() {
        String config = OtherConfigUtils.getFileContent(JPAAS_CONFIG);
        JsonResult jsonResult = JsonResult.Success(config);
        jsonResult.setShow(false);
        return jsonResult;
    }

    @ApiOperation("获取系统参数映射")
    @MethodDefine(title = "获取系统参数", path = "/getConfigMap", method = HttpMethodConstants.GET)
    @GetMapping(value = "getConfigMap")
    public JsonResult getConfigMap() {
        String config = OtherConfigUtils.getFileContent(JPAAS_CONFIG);
        Properties props=new Properties();
        Map<String,String> configMap=new LinkedHashMap<>();
        if(StringUtils.isNotEmpty(config)){
            StringReader reader=new StringReader(config);
            try {
                props.load(reader);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            Enumeration names=  props.propertyNames();
            while(names.hasMoreElements()){
                String name=(String) names.nextElement();
                String val=props.getProperty(name);
                name=name.replace("props.","");
                configMap.put(name,val);
            }
        }
        JsonResult jsonResult = JsonResult.getSuccessResult(configMap);
        jsonResult.setShow(false);
        return jsonResult;
    }

    @ApiOperation("保存系统配置")
    @MethodDefine(title = "保存系统配置", path = "/saveConfigMap", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置数据", varName = "configs")})
    @AuditLog(operation = "保存系统配置")
    @PostMapping(value = "saveConfigMap")
    public JsonResult saveConfigMap(@RequestBody JSONObject config) throws NacosException {
        //原配置参数
        String orgConfig = OtherConfigUtils.getFileContent(JPAAS_CONFIG);
        Properties orgProps=new Properties();
        StringReader reader =null;
        try {
            reader = new StringReader(orgConfig);
            orgProps.load(reader);
        }catch (Exception e){
            e.printStackTrace();
        }

        Iterator<String> it=config.keySet().iterator();
        while(it.hasNext()){
            String key=it.next();
            String val=config.getString(key);
            orgProps.put("props."+key,val);
        }

        StringWriter writer=new StringWriter();
        try {
            orgProps.store(writer, "");
            log.info(writer.toString());
//            configService.publishConfig(JPAAS_CONFIG, DEFAULT_GROUP, writer.toString());
            LogContext.put(Audit.DETAIL,"将系统配置参数发布到NACOS");
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return JsonResult.Success("成功保存系统配置!");
    }

    @ApiOperation("发布系统参数")
    @MethodDefine(title = "发布系统参数", path = "/publish", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置数据", varName = "config")})
    @AuditLog(operation = "发布系统参数")
    @PostMapping(value = "publish")
    public JsonResult publish(@RequestBody JSONObject config) throws NacosException {
        String str=config.getString("config");
//        configService.publishConfig(JPAAS_CONFIG, DEFAULT_GROUP, str);
        LogContext.put(Audit.DETAIL,"将系统配置发布到NACOS");
        return JsonResult.Success("发布配置成功!");
    }





    @MethodDefine(title = "获取审批类型", path = "/getCheckTypes", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取审批类型")
    @GetMapping("getCheckTypes")
    public JsonResult getCheckTypes(){
        String checkType = SysPropertiesUtil.getString("checkType");
        if(StringUtils.isEmpty(checkType)){
            return JsonResult.Fail("获取审批类型失败!");
        }
        JSONObject jsonObject=JSONObject.parseObject(checkType);
        return JsonResult.Success("获取审批类型成功!").setData(jsonObject).setShow(false);
    }




}
