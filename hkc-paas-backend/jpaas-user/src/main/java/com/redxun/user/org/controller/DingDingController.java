package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsDdAgent;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsDdAgentServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.redxun.util.dd.DingDingUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 钉钉应用相关操作
 * @Author: Elwin ZHANG
 * @Date: 2021/4/15 15:46
 **/
@Api(tags = "钉钉应用相关操作")
@ClassDefine(title = "钉钉应用相关操作",packageName = "认证中心",alias = "DingDingAuth",path = "")
@Slf4j
@RestController
@RequestMapping("/dd")
public class DingDingController {
    @Autowired
    OsUserServiceImpl osUserServiceImpl;
    @Autowired
    OsDdAgentServiceImpl osDdAgentService;
    @Autowired
    DingDingUtil ddUtil;

    @ApiOperation(value = "根据钉钉code，获取钉钉用户Id")
    @AuditLog(operation = "根据钉钉code，获取钉钉用户Id")
    @PostMapping("/getDdIdByCode")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult getDdIdByCode(@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId
            , @ApiParam(required = true, name = "code", value = "钉钉code")String code,
                                      @ApiParam(required = true, name = "autoBind", value = "是否自动绑定")Integer autoBind){
        JsonResult result=new JsonResult(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
       if (code==null ||code.isEmpty()){
           result.setMessage("参数不正确，Code无效!");
           return result;
        }
        //根据租户ID获取钉钉的配置信息
        OsDdAgent osDdAgent= osDdAgentService.getDefault(tenantId);
        if(osDdAgent==null){
            result.setMessage("没有默认的钉钉应用配置，请先在后台配置!");
            return result;
        }

        //获取Token
        String token= ddUtil.getToken(osDdAgent.getAppKey(),osDdAgent.getSecret(),tenantId);
        if(StringUtils.isEmpty(token)){
            result.setMessage("获取Token失败，请检查网格连接或安全设置!");
            return result;
        }

        //获取ddID
        String ddID=ddUtil.getUserId(token,code);
        if(StringUtils.isEmpty(ddID)){
            result.setMessage("获取ddID失败，请检查网格连接或安全设置!!");
            return result;
        }
        //自动绑定
        if(autoBind!=null && autoBind==1 ){
            LogContext.put(Audit.DETAIL, "并绑定到系统登录账号");
            return bind(ddID);
        }
        result.setData(ddID);
        result.setSuccess(true);
        result.setMessage("获取钉钉用户ID成功！");
        return result;
    }


    @ApiOperation(value = "PC扫描登录返回的code，获取钉钉用户Id")
    @AuditLog(operation = "PC扫描登录返回的code，获取钉钉用户Id")
    @PostMapping("/getDdIdQRCode")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult getDdIdQRCode(@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId
            , @ApiParam(required = true, name = "code", value = "钉钉code")String code){
        JsonResult result=new JsonResult(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        if (code==null ||code.isEmpty()){
            result.setMessage("参数不正确，Code无效!");
            return result;
        }

        //根据租户ID获取钉钉的配置信息
        OsDdAgent osDdAgent= osDdAgentService.getDefault(tenantId);
        if(osDdAgent==null){
            result.setMessage("没有默认的钉钉应用配置，请先在后台配置!");
            return result;
        }
        String unoinId=ddUtil.getUnionId(osDdAgent.getAppKey(),osDdAgent.getSecret(),code);
        if(StringUtils.isEmpty(unoinId)){
            result.setMessage("调用接口获取unoinId失败!");
            return result;
        }
        //获取Token
        String token= ddUtil.getToken(osDdAgent.getAppKey(),osDdAgent.getSecret(),tenantId);
        if(StringUtils.isEmpty(token)){
            result.setMessage("获取Token失败，请检查网格连接或安全设置!");
            return result;
        }
        String userId=ddUtil.getUserIdByUnoinId(token,unoinId);
        if(StringUtils.isEmpty(userId)){
            result.setMessage("获取用户ID失败，请检查应用的【成员信息读权限】以及出口IP白名单配置！");
            return result;
        }
        result.setData(userId);
        result.setSuccess(true);
        return result;
    }

    @ApiOperation(value = "绑定钉钉用户ID到系统登录账号")
    @AuditLog(operation = "绑定钉钉用户ID到系统登录账号")
    @PostMapping("/bind")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult bind(@ApiParam(required = true, name = "ddId", value = "钉钉用户ID") String ddId){
        JsonResult result=new JsonResult(false);
        if (ddId==null ||ddId.isEmpty()){
            result.setMessage("参数不正确，请检查ddId!");
            return result;
        }
        IUser user = ContextUtil.getCurrentUser();
        if (user==null){
            result.setMessage("请先登录再绑定!");
            return result;
        }
        //更新用户信息
        OsUser osUser=osUserServiceImpl.getById(user.getUserId());
        osUser.setDdId(ddId);
        osUserServiceImpl.updateUser(osUser);
        //成功返回
        result.setSuccess(true);
        result.setData(ddId);
        result.setMessage("绑定成功！");
        return result;
    }

    @ApiOperation(value = "解绑钉钉用户到系统登录账号")
    @AuditLog(operation = "解绑钉钉用户到系统登录账号")
    @PostMapping("/unbind")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult unBind (@ApiParam(required = false, name = "userId", value = "用户的ID")String userId){
        JsonResult result=JsonResult.Fail();
        IUser  user =ContextUtil.getCurrentUser();
        if (user==null){
            result.setMessage("请先登录再解绑!");
            return result;
        }
        //更新用户信息
        OsUser osUser=osUserServiceImpl.getById(user.getUserId());
        String ddId=osUser.getDdId();
        if(StringUtils.isEmpty(ddId)){
            result.setMessage("当前还没有绑定钉钉!");
            //没有绑定的情况下也算解绑成功，可能在别处删除了
            result.setSuccess(true);
            return result;
        }
        osUser.setDdId(null);
        osUserServiceImpl.updateById(osUser);
        //成功返回
        result.setSuccess(true);
        result.setMessage("解绑成功！");
        return result;
    }

    @ApiOperation(value = "根据租户ID，获取钉钉的企业Id")
    @AuditLog(operation = "根据租户ID，获取钉钉的企业Id")
    @PostMapping("/getDdCorpId")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult getDdCorpId(@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId){
        JsonResult result=JsonResult.Fail();
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        //根据租户ID获取钉钉的配置信息
        OsDdAgent osDdAgent= osDdAgentService.getDefault(tenantId);
        if(osDdAgent==null){
            result.setMessage("没有默认的钉钉应用配置，请先在后台配置!");
            return result;
        }
        //成功返回
        result.setSuccess(true);
        result.setData(osDdAgent.getCorpId());
        result.setMessage("操作成功！");
        return result;
    }

    @ApiOperation(value = "根据钉钉用户ID，获取用户信息")
    @AuditLog(operation = "根据钉钉用户ID，获取用户信息")
    @GetMapping("/getuser")
    public JPaasUser getUserByOpenIdAndTenantId(@ApiParam(required = true, name = "tenantId", value = "租户ID")
                                                    @RequestParam("tenantId") String tenantId
            , @ApiParam(required = true, name = "ddId", value = "钉钉用户ID") @RequestParam("ddId")String ddId){
        if (tenantId==null ||tenantId.isEmpty()){
            return null;
        }
        if (ddId==null ||ddId.isEmpty()){
            return null;
        }
        return osUserServiceImpl.selectByDDidAndTenantId(ddId,tenantId);
    }

    @ApiOperation(value = "获取企业微信的扫码登录URL")
    @AuditLog(operation = "获取企业微信的扫码登录URL")
    @PostMapping("/getQRCodeUrl")
    public JsonResult getQRCodeUrl (@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId,
                                    @ApiParam(required = true, name = "redirectUrl", value = "回调地址") String redirectUrl) {
        JsonResult result=new JsonResult(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        //根据租户ID获取钉钉的配置信息
        OsDdAgent osDdAgent= osDdAgentService.getDefault(tenantId);
        if(osDdAgent==null){
            result.setMessage("没有默认的钉钉应用配置，请先在后台配置!");
            return result;
        }
        String serverAddress= SysPropertiesUtil.getString("serverAddress");
        String jpaas= SysPropertiesUtil.getString("ctxPath");
        String newUrl=serverAddress +  jpaas  + redirectUrl;

        //构造前端要调用的URL
        String url=ddUtil.getQRConnectUrl(osDdAgent.getAppKey(),newUrl);
        result.setSuccess(true);
        result.setData(url);
        return result;
    }
}
