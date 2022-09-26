
package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.user.org.entity.OsUserPlatform;
import com.redxun.user.org.service.OsUserPlatformServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/org/osUserPlatform")
@Api(tags = "第三方平台登陆绑定")
@ClassDefine(title = "第三方平台登陆绑定", alias = "OsUserPlatformController", path = "/user/platform", packages = "org", packageName = "子系统名称")

public class OsUserPlatformController extends BaseController<OsUserPlatform> {

    @Autowired
    OsUserPlatformServiceImpl osUserPlatformService;


    @Override
    public BaseService getBaseService() {
        return osUserPlatformService;
    }

    @Override
    public String getComment() {
        return "第三方平台登陆绑定";
    }

    /**
     * @Description: 绑定第三方平台openId到系统账号
     * @param openId 第三方平台用户用ID或OpenID
     * @return com.redxun.common.base.entity.JsonResult
     * @Author: Elwin ZHANG  @Date: 2022/6/14 18:02
     **/
    @ApiOperation(value = "绑定第三方平台openId到系统账号")
    @AuditLog(operation = "绑定第三方平台openId到系统账号")
    @PostMapping("/bind")
    public JsonResult bind2CurUser(@ApiParam(required = true, name = "openId", value = "企业微信用户用ID或OpenID") String openId,
                                   @ApiParam(required = true, name = "platformType", value = "平台类型") Integer platformType,
                                   @ApiParam(required = true, name = "tenantId", value = "租户id") String tenantId) {
        JsonResult result=new JsonResult(false);
        if (StringUtils.isEmpty(openId) || platformType ==null){
            result.setMessage("参数不正确，请检查OpenId!");
            return result;
        }
        IUser user = ContextUtil.getCurrentUser();
        if (user==null){
            result.setMessage("请先登录再绑定!");
            return result;
        }
        if(!user.getTenantId().equals(tenantId)){
            result.setMessage("授权平台与登陆不在同一个租户");
            return result;
        }
        //查询openId是否被绑定
        OsUserPlatform openIdUserPlatform = osUserPlatformService.getOsUserPlatform(tenantId, openId, platformType);
        //查询userId是否有绑定过
        OsUserPlatform userIdUserPlatform = osUserPlatformService.getOsUserPlatformByUserId(user.getUserId(), platformType);
        if(openIdUserPlatform!=null){
            //如果存在则删除,再新增
            osUserPlatformService.delete(openIdUserPlatform.getId());
        }
        if(userIdUserPlatform!=null){
            //如果存在则删除,再新增
            osUserPlatformService.delete(userIdUserPlatform.getId());
        }
        //新增绑定关系
        OsUserPlatform osUserPlatform = new OsUserPlatform();
        osUserPlatform.setUserId(user.getUserId());
        osUserPlatform.setPlatformType(platformType);
        osUserPlatform.setOpenId(openId);
        osUserPlatform.setTenantId(tenantId);
        osUserPlatformService.insert(osUserPlatform);
        //成功返回
        result.setSuccess(true);
        result.setData(osUserPlatform);
        result.setMessage("绑定成功！");
        return result;
    }

    /**
     * @Description: 解绑第三方平台登陆
     * @return com.redxun.common.base.entity.JsonResult
     * @Author: Elwin ZHANG  @Date: 2021/4/14 18:02
     **/
    @ApiOperation(value = "解绑第三方平台登陆")
    @AuditLog(operation = "解绑第三方平台登陆")
    @PostMapping("/unbind")
    public JsonResult unBind (@ApiParam(required = false, name = "platformType", value = "第三方平台类型：1微信公众号，2企业微信，3钉钉，4飞书")Integer platformType){
        JsonResult result= JsonResult.Fail();
        IUser user = ContextUtil.getCurrentUser();
        if (user==null){
            result.setMessage("请先登录再解绑!");
            return result;
        }
        //查询userId是否有绑定过
        OsUserPlatform userIdUserPlatform = osUserPlatformService.getOsUserPlatformByUserId(user.getUserId(), platformType);
        if(userIdUserPlatform!=null){
            //如果存在则删除
            osUserPlatformService.delete(userIdUserPlatform.getId());
        }
        //成功返回
        result.setSuccess(true);
        result.setMessage("解绑成功！");
        return result;
    }


}

