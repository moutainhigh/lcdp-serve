
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.log.annotation.AuditLog;
import com.redxun.system.core.entity.SysFile;
import com.redxun.system.core.service.SysFileServiceImpl;
import com.redxun.system.operator.SysFileStoreHandlerContext;
import com.redxun.system.util.SysFileUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/system/core/sysFileStore")
@Api(tags = "系统附件")
public class SysFileStoreController extends BaseController<SysFile> {

    @Autowired
    SysFileServiceImpl sysFileServiceImpl;


    @Override
    public BaseService getBaseService() {
        return sysFileServiceImpl;
    }

    @Override
    public String getComment() {
        return "系统附件";
    }

    @Autowired
    private ConfigService configService;

    /**
     * 获取栏目模板类型
     */
    @MethodDefine(title = "获取文件存储类型", path = "/getSysFileStoreTypeList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取文件存储类型")
    @GetMapping("/getSysFileStoreTypeList")
    public JsonResult getSysFileStoreTypeList() {
        Map<String, String> map= SysFileStoreHandlerContext.getSysFileStoreMapMap();
        return JsonResult.Success().setData(map);
    }

    /**
     * 获取栏目模板类型
     */
    @MethodDefine(title = "获取nacos文件存储类型", path = "/getSysFileStoreNacosConfig", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取nacos文件存储类型")
    @GetMapping("/getSysFileStoreNacosConfig")
    public JsonResult getSysFileStoreNacosConfig() throws Exception{

        String config = configService.getConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, 0L);
        return JsonResult.Success().setData(config);
    }

    @MethodDefine(title = "将文件上传配置保存到NACOS", path = "/saveStore", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体数据JSON", varName = "entity")})
    @ApiOperation(value="将文件上传配置保存到NACOS", notes="根据提交的业务实体JSON保存实体数据")
    @AuditLog(operation = "将文件上传配置保存到NACOS")
    @PostMapping("/saveStore")
    public JsonResult saveStore(@Validated @ApiParam @RequestBody JSONObject entity, BindingResult validResult) throws Exception{
        JsonResult result=null;

        configService.publishConfig(SysFileUtil.FILE_CONFIG, SysFileUtil.DEFAULT_GROUP, entity.toJSONString());

        String str="保存成功";
        result=JsonResult.getSuccessResult(str);
        result.setData(entity);
        return result;
    }
}

