package com.redxun.system.core.controller;

import com.redxun.cache.CacheUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.SysAppAuth;
import com.redxun.system.core.service.SysAppAuthServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/core/sysAppAuth")
@ClassDefine(title = "客户接口授权管理",alias = "sysAppAuthController",path = "/system/core/sysAppAuth",packages = "core",packageName = "系统管理")
@Api(tags = "客户接口授权管理")
public class SysAppAuthController extends BaseController<SysAppAuth> {

    @Autowired
    SysAppAuthServiceImpl sysAppAuthService;

    //缓存外部API接口授权记录的Key前缀
    private static final  String APP_AUTH_KEY_PREFIX="sys_app_auth_";
    private  static final  String APP_AUTH_REGION="api_auth";

    @Override
    public BaseService getBaseService() {
        return sysAppAuthService;
    }

    @Override
    public String getComment() {
        return "应用授权表";
    }

    /**
     * 批量保存授权
     */
    @MethodDefine(title = "保存应用授权", path = "/saveSysAppAuth", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "授权数据", varName = "sysAppAuths")})
    @AuditLog(operation = "保存应用授权")
    @ApiOperation("保存应用授权")
    @PostMapping("/saveSysAppAuth")
    public JsonResult saveSysAppAuth(@RequestBody List<SysAppAuth> sysAppAuths) {
        if(BeanUtil.isEmpty(sysAppAuths)){
            return JsonResult.getSuccessResult("没有选择授权接口!");
        }

        String appId=sysAppAuths.get(0).getAppId();
        sysAppAuthService.removeByAppId(appId);
        for(SysAppAuth sysAppAuth:sysAppAuths){
            sysAppAuthService.insert(sysAppAuth);
        }
        String detail="接口授权保存，客户端ID为:" + appId;
        LogContext.put(Audit.DETAIL,detail);
        //清除缓存
        String key=APP_AUTH_KEY_PREFIX + appId;
         CacheUtil.remove(APP_AUTH_REGION,key);
        return JsonResult.getSuccessResult("保存成功");
    }

    @MethodDefine(title = "根据应用ID查找授权数据", path = "/findListByAppId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation("根据应用ID查找授权数据")
    @GetMapping("/findListByAppId")
    public List<SysAppAuth> findListByAppId(@RequestParam("appId")String appId){
        return sysAppAuthService.findListByAppId(appId);
    }

}
