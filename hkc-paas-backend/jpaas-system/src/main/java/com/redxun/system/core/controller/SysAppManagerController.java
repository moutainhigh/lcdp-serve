
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysAppManager;
import com.redxun.system.core.service.SysAppManagerServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysAppManager")
@Api(tags = "平台开发应用授权管理")
@ClassDefine(title = "平台开发应用授权管理",alias = "SysAppManagerController",path = "/system/core/sysAppManager",packages = "core",packageName = "应用开发")

public class SysAppManagerController extends BaseController<SysAppManager> {

@Autowired
SysAppManagerServiceImpl sysAppManagerService;

    @MethodDefine(title = "保存授权", path = "/batchSave", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title ="授权信息",varName = "data")})
    @ApiOperation("batchSave")
    @PostMapping("/batchSave")
    public JsonResult batchSave(@ApiParam @RequestBody JSONObject data){
        JsonResult result=JsonResult.Success("应用授权保存成功");
        if(data==null ){
            return JsonResult.Fail("缺少请求参数！");
        }
        String appId=data.getString("appId");
        JSONArray rows=data.getJSONArray("rows");
        sysAppManagerService.batchSave(appId,rows);
        return result;
    }

@MethodDefine(title = "获取某应用的所有授权", path = "/getByAppId", method = HttpMethodConstants.GET,
        params = {@ParamDefine(title ="应用ID",varName = "appId")})
@ApiOperation("获取某应用的所有授权")
@GetMapping("/getByAppId")
public JsonResult getByAppId(@ApiParam @RequestParam (value = "appId") String appId){
    JsonResult result=JsonResult.Success("请求成功");
    if(StringUtils.isEmpty(appId)){
        return JsonResult.Fail("缺少请求参数！");
    }
    Object data=sysAppManagerService.getByAppId(appId);
    result.setData(data);
    result.setShow(false);
    return result;
}

    @MethodDefine(title = "获取当前用户的所有应用管理授权", path = "/getManagerOfCurUser", method = HttpMethodConstants.GET)
    @ApiOperation("获取当前用户的所有应用管理授权")
    @GetMapping("/getManagerOfCurUser")
    public JsonResult getManagerOfCurUser(){
        JsonResult result=JsonResult.Success("请求成功");
        result.setShow(false);
        JPaasUser curUser=(JPaasUser)ContextUtil.getCurrentUser();
        if(curUser==null || curUser.getUserId()==null){
            return JsonResult.Fail("请先登录系统！");
        }
        //管理员
        if(curUser.isAdmin()){
            result.setMessage("admin");
            return  result;
        }
        List<SysAppManager> data=sysAppManagerService.getManagerAuth(curUser.getUserId(),curUser.getRoles());
        result.setData(data);
        return result;
    }

    @MethodDefine(title = "获取当前用户的所有应用授权", path = "/getAllOfCurUser", method = HttpMethodConstants.GET)
    @ApiOperation("获取当前用户的所有应用授权")
    @GetMapping("/getAllOfCurUser")
    public JsonResult getAllOfCurUser(){
        JsonResult result=JsonResult.Success("请求成功");
        result.setShow(false);
        JPaasUser curUser=(JPaasUser)ContextUtil.getCurrentUser();
        if(curUser==null || curUser.getUserId()==null){
            return JsonResult.Fail("请先登录系统！");
        }
        //管理员
        if(curUser.isAdmin()){
            result.setMessage("admin");
            return  result;
        }
        List<SysAppManager> data=sysAppManagerService.getByUserAndAppId(null,curUser.getUserId(),curUser.getRoles());
        result.setData(data);
        return result;
    }

    @MethodDefine(title = "获取当前用户关于某应用的授权", path = "/getCurUserAuthByAppId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title ="应用ID",varName = "appId")})
    @ApiOperation("获取当前用户关于某应用的授权")
    @GetMapping("/getCurUserAuthByAppId")
    public JsonResult getCurUserAuthByAppId(@ApiParam @RequestParam (value = "appId") String appId){
        JsonResult result=JsonResult.Success("请求成功");
        result.setShow(false);
        if(StringUtils.isEmpty(appId)){
            return JsonResult.Fail("缺少请求参数！");
        }
        JPaasUser curUser=(JPaasUser)ContextUtil.getCurrentUser();
        if(curUser==null || curUser.getUserId()==null){
            return JsonResult.Fail("请先登录系统！");
        }
        //管理员
        if(curUser.isAdmin()){
            result.setMessage("admin");
            return  result;
        }
        List<SysAppManager> data=sysAppManagerService.getByUserAndAppId(appId,curUser.getUserId(),curUser.getRoles());
        if(data!=null && data.size()>0){
            result.setData(data.get(0));
        }
        return result;
    }

@Override
public BaseService getBaseService() {
return sysAppManagerService;
}

@Override
public String getComment() {
return "平台开发应用授权管理";
}

}

