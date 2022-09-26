package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.util.OpenOfficeUtil;
import com.redxun.utils.OtherConfigUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * openoffice服务。
 * @author ray
 */
@Slf4j
@RestController
@RequestMapping("/system/core/openOffice")
@ClassDefine(title = "OpenOffice服务",alias = "openOfficeContoller",path = "/system/core/openOffice",packages = "core",packageName = "系统管理")
@Api(tags = "OpenOffice服务")
public class OpenOfficeContoller {

    private final String OFFICE_CONFIG = "openOfficeConfig";
    private final String DEFAULT_GROUP = "DEFAULT_GROUP";

//    @Autowired
//    private ConfigService configService;

    @MethodDefine(title = "查看OPENOFFICE启动状态", path = "/getStatus", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "端口", varName = "port")})
    @ApiOperation("查看OPENOFFICE启动状态")
    @GetMapping(value = "getStatus")
    public JsonResult openOfficeStatus(@RequestParam("port") String port) throws NacosException, IOException {
        LogContext.put(Audit.DETAIL,"查看OPENOFFICE启动状态");
        JsonResult<String> status = OpenOfficeUtil.getStatus(port);
        status.setShow(true);
        return status;
    }

    @MethodDefine(title = "结束OPENOFFICE服务", path = "/stopOpenOffice", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "端口", varName = "port")})
    @ApiOperation("结束OPENOFFICE服务")
    @GetMapping(value = "stopOpenOffice")
    public JsonResult startOpenOffice(@RequestParam("port") String port) throws NacosException {
        LogContext.put(Audit.DETAIL, "结束OPENOFFICE服务");
        JsonResult<String> jsonResult = OpenOfficeUtil.stopService(port);
        jsonResult.setShow(true);
        return jsonResult;
    }

    @MethodDefine(title = "启动OPENOFFICE服务", path = "/startOpenOffice", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置数据", varName = "data")})
    @ApiOperation("启动OPENOFFICE服务")
    @PostMapping(value = "startOpenOffice")
    public JsonResult startOpenOffice(@RequestBody JSONObject data) throws NacosException {
        JsonResult jsonResult = JsonResult.Success();
        LogContext.put(Audit.DETAIL,"启动OPENOFFICE服务");
        if(data!=null){
            JSONObject config = data.getJSONObject("config");
            String installPath=config.getString(OpenOfficeUtil.INSTALL_PATH);
            String ip=config.getString(OpenOfficeUtil.SERVICE_IP);
            String port=config.getString(OpenOfficeUtil.SERVICE_PORT);
            JsonResult<String> result = OpenOfficeUtil.startService(installPath,ip,port);
            jsonResult.setMessage(result.getMessage());
        }
        jsonResult.setShow(true);
        return jsonResult;
    }

    @MethodDefine(title = "保存OPENOFFICE配置并重启服务", path = "/reStartOpenOffice", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置数据", varName = "data")})
    @ApiOperation("保存OPENOFFICE配置并重启服务")
    @PostMapping(value = "reStartOpenOffice")
    public JsonResult reStartOpenOffice(@RequestBody JSONObject data) throws NacosException, IOException {
        JsonResult jsonResult = JsonResult.Success();

        LogContext.put(Audit.DETAIL,"保存OPENOFFICE配置并重启服务");
        String config=data.getString("config");
        String officeConfig=data.getString("officeConfig");
//        configService.publishConfig(OFFICE_CONFIG, DEFAULT_GROUP, config);
        if(StringUtils.isNotEmpty(officeConfig)){
            JSONObject jsonConfig = JSONObject.parseObject(config);
            JsonResult<String> result = OpenOfficeUtil.startService(jsonConfig);
            jsonResult.setMessage(result.getMessage());
        }

        jsonResult.setShow(true);
        return jsonResult;
    }


    @ApiOperation("获取系统参数")
    @MethodDefine(title = "获取系统参数", path = "/getConfig", method = HttpMethodConstants.GET)
    @GetMapping(value = "getConfig")
    public JsonResult getConfig() {
        String config = OtherConfigUtils.getFileContent(OFFICE_CONFIG);
        JsonResult jsonResult = JsonResult.Success(config);
        jsonResult.setShow(false);
        return jsonResult;
    }

    @ApiOperation("发布系统参数")
    @MethodDefine(title = "发布系统参数", path = "/publish", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置数据", varName = "data")})
    @AuditLog(operation = "发布系统参数")
    @PostMapping(value = "publish")
    public JsonResult publish(@RequestBody JSONObject data) throws NacosException {
        JSONObject config = data.getJSONObject("config");
//        configService.publishConfig(OFFICE_CONFIG, DEFAULT_GROUP, config.toJSONString());
        LogContext.put(Audit.DETAIL,"将系统配置发布到NACOS");
        return JsonResult.Success("发布配置成功!");
    }
}
