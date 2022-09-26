package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.system.ext.handler.ApiReturnDataHandlerExecutor;
import com.redxun.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 获取系统的公共服务API
 */
@Slf4j
@RestController
@RequestMapping("/system/core/public")
@Api(tags = "公共API")
public class PublicController {
    @Resource
    private ApiReturnDataHandlerExecutor apiReturnDataHandlerExecutor;

    /**
     * 获取APP的所有服务
     * @return
     */
    @ApiOperation("获取所有的服务类")
    @GetMapping("/getAppServices")
    public List<String> getAppServices(){
        return SysUtil.getAppServices();
    }

    @GetMapping("/getApiReturnDataHandler")
    public JSONArray getBpmHandlers() {
        JSONArray list = apiReturnDataHandlerExecutor.getHandlers();
        return list;
    }

    /**
     * 获取系统应用的参数配置
     * @return
     */
    @RequestMapping("/getSysAppConfigs")
    @ResponseBody
    public JSONObject getSysAppConfigs(){
        String appName= SysPropertiesUtil.getString("appName");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appName",appName);
        return jsonObject;
    }
}
