
package com.redxun.system.core.controller;

import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppFavorites;
import com.redxun.system.core.service.SysAppFavoritesServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysAppFavorites")
@Api(tags = "平台开发应用收藏")
@ClassDefine(title = "平台开发应用收藏",alias = "SysAppFavoritesController",path = "/system/core/sysAppFavorites",packages = "core",packageName = "平台开发应用收藏")
public class SysAppFavoritesController extends BaseController<SysAppFavorites> {

@Autowired
SysAppFavoritesServiceImpl sysAppFavoritesService;


@Override
public BaseService getBaseService() {
return sysAppFavoritesService;
}

@Override
public String getComment() {
return "平台开发应用收藏";
}

    @MethodDefine(title = "获取当前用户最近使用的应用", path = "/getLastUseApps", method = HttpMethodConstants.GET)
    @ApiOperation("获取当前用户最近使用的应用")
    @GetMapping("/getLastUseApps")
    public JsonResult  getLastUseApps() {
        JsonResult result= JsonResult.Success();
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return JsonResult.Fail("请先登录！");
        }
        Object obj=sysAppFavoritesService.getLastUse(userId);
        result.setData(obj);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "获取当前用户收藏的应用", path = "/getFavoritesApps", method = HttpMethodConstants.GET)
    @ApiOperation("获取当前用户收藏的应用")
    @GetMapping("/getFavoritesApps")
    public JsonResult  getFavoritesApps() {
        JsonResult result= JsonResult.Success();
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return JsonResult.Fail("请先登录！");
        }
        Object obj=sysAppFavoritesService.getFavorites(userId);
        result.setData(obj);
        result.setShow(false);
        return result;
    }

    @MethodDefine(title = "收藏应用", path = "/addFavorite", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation("收藏应用")
    @PostMapping ("/addFavorite")
    public  JsonResult addFavorite(@ApiParam @RequestParam("appId") String appId){
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return JsonResult.Fail("请先登录！");
        }
        sysAppFavoritesService.addFavorite(userId,appId);
        JsonResult result=JsonResult.Success("收藏成功！");
        return  result;
    }

    @MethodDefine(title = "取消收藏", path = "/cancelFavorite", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation("取消收藏")
    @PostMapping ("/cancelFavorite")
    public  JsonResult cancelFavorite( @ApiParam @RequestParam("appId") String appId){
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return JsonResult.Fail("请先登录！");
        }
        sysAppFavoritesService.cancelFavorite(userId,appId);
        JsonResult result=JsonResult.Success("已取消收藏！");
        return  result;
    }

    @MethodDefine(title = "保存最近使用应用", path = "/saveLastUse", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用ID", varName = "appId") ,@ParamDefine(title = "是否增加（1增加，其他为移除）", varName = "isAdd")})
    @ApiOperation("保存最近使用应用")
    @PostMapping ("/saveLastUse")
    public  JsonResult saveLastUse(@ApiParam @RequestParam("appId") String appId,
                                   @ApiParam @RequestParam(value = "isAdd",required = false,defaultValue = "1") String isAdd){
        Date lastUseTime=null;
        String userId= ContextUtil.getCurrentUserId();
        if(StringUtils.isEmpty(userId)){
            return JsonResult.Fail("请先登录！");
        }
        if("1".equals(isAdd)){
            lastUseTime=new Date();
        }
        sysAppFavoritesService.saveLastUse(userId,appId,lastUseTime);
        JsonResult result=JsonResult.Success("操作成功！").setShow(false);
        return  result;
    }
}

