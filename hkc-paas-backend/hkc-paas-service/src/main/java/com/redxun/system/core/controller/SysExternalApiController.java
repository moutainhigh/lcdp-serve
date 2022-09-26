
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.feign.form.FormClient;
import com.redxun.system.core.entity.SysExternalApi;
import com.redxun.system.core.service.SysExternalApiServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/core/sysExternalApi")
@Api(tags = "对外API接口管理")
@ClassDefine(title = "对外API接口管理", alias = "SysExternalApiController", path = "/system/core/sysExternalApi", packages = "core", packageName = "子系统名称")

public class SysExternalApiController extends BaseController<SysExternalApi> {

    @Autowired
    SysExternalApiServiceImpl sysExternalApiService;

    @Autowired
    FormClient formClient;


    @Override
    public BaseService getBaseService() {
        return sysExternalApiService;
    }

    @Override
    public String getComment() {
        return "对外API接口管理";
    }


    /**
     * 生成接口
     * @param type 类型 form（表单）、sql(自定义查询)
     * @param alias
     * @return
     */
    @MethodDefine(title = "生成接口", path = "/genExternalApi", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "类型", varName = "type"),@ParamDefine(title = "别名", varName = "alias")})
    @ApiOperation("生成接口")
    @PostMapping("/genExternalApi")
    public JsonResult genExternalApi(@ApiParam @RequestParam(value = "type") String type,@ApiParam @RequestParam(value = "alias") String alias){
        JsonResult jsonResult = new JsonResult(true,"生成接口成功!");
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(alias)) {
            return new JsonResult(false,"生成接口失败,参数不全!");
        }
        try {
            String[] aryAlias= alias.split(",");
            for (int i = 0; i < aryAlias.length; i++) {
                sysExternalApiService.genExternalApi(type,aryAlias[i]);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new JsonResult(false,"生成接口失败");
        }
        return jsonResult;
    }
}

