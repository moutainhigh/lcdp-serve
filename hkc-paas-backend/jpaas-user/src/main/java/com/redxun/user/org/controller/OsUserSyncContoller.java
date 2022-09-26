package com.redxun.user.org.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.handler.platform.IPlatformHandler;
import com.redxun.user.handler.platform.PlatformHandlerContext;
import com.redxun.user.org.entity.OsDimension;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.ISyncService;
import com.redxun.user.org.service.OsGroupServiceImpl;
import com.redxun.user.org.service.OsUserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

import static com.redxun.common.utils.ContextUtil.getCurrentTenantId;

/**
 * 用户同步Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/sync")
@ClassDefine(title = "同步api", alias = "osUserSyncContoller", path = "/user/org/sync", packages = "org", packageName = "组织架构")
@Api(tags = "同步api")
@RefreshScope
public class OsUserSyncContoller {

    @Resource
    ISyncService iSyncService;
    @Autowired
    OsUserServiceImpl osUserService;
    @Autowired
    OsGroupServiceImpl osGroupService;

    //这里在nacos上加一串 redxun.initpwd=***，设置密码
    @Value("${redxun.initpwd:1234}")
    private String initPwd;

    @Value("${redxun.syncorg:dummy}")
    private String syncMode;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @ApiOperation(value = "同步信息", notes = "同步信息")
    @GetMapping(value = "/syncOrgUser")
    @AuditLog(operation = "同步组织用户信息")
    public JsonResult syncOrgUser() throws Exception {
        String tenantId= ContextUtil.getCurrentTenantId();

        JsonResult<List<OsGroup>> departMentResult = iSyncService.getDepartMent(tenantId);
        if(!departMentResult.isSuccess()){
            return departMentResult;
        }
        List<OsGroup> detpartMents=departMentResult.getData();
        handGroup(detpartMents);

        JsonResult<List<OsUser>>  usersResult = iSyncService.getUsers(tenantId, detpartMents);
        if(!usersResult.isSuccess()){
            return usersResult;
        }
        List<OsUser> users=usersResult.getData();

        handUser(users);
        String detail="组织架构同步成功";
        LogContext.put(Audit.DETAIL,detail);
        LogContext.put(Audit.OPERATION,"同步组织架构信息");
        return JsonResult.Success("同步用户组织架构成功!").setShow(false);
    }


    @ApiOperation(value = "获取同步模式", notes = "获取同步模式")
    @GetMapping(value = "/getSyncConfig")
    public String getSyncConfig(){
        return syncMode;
    }

    private void handUser(List<OsUser> users) {

        //用户数据处理
        for (OsUser user : users) {
            //查询是否在数据库存在
            String userId = user.getUserId();
            OsUser userData;
            //钉钉有id，直接用id来查用户是否存在
            if (userId != null && !("").equals(userId)) {
                userData = osUserService.get(userId);
            } else {
                //微信通讯录沒有id，所以拿用戶名查找用戶是否存在
                userData = osUserService.getByUsername(user.getUserNo());
            }
            // 更新
            if (userData != null) {
                //这个是处理微信没有id, 更新不了
                user.setUserId(userData.getUserId());
                //微信没有id，所以要拿数据库的id付给他
                osUserService.updateBySync(user);
            } else {
                //插入
                user.setEnable("0");
                user.setPwd(passwordEncoder.encode(user.getUserNo() + "_" + initPwd));
                user.setTenantId(getCurrentTenantId());
                user.setStatus(OsUser.STATUS_IN_JOB);
                osUserService.insertByUserNo(user);
            }
        }
    }


    private void handGroup(List<OsGroup> detpartMents) {
        //path处理
        Map<String, OsGroup> map = new HashMap<>();
        for (int i = 0; i < detpartMents.size(); i++) {
            OsGroup group = detpartMents.get(i);
            map.put(group.getGroupId(), group);
        }

        for (String key : map.keySet()) {
            map = osUserService.handPath(map, key);
        }
        Collection<OsGroup> valueCollection = map.values();
        detpartMents=new ArrayList<>(valueCollection);

        osGroupService.updateGroupBySync(detpartMents);

    }

    @ApiOperation(value = "推送组织架构到第三方平台", notes = "推送组织架构到第三方平台信息")
    @GetMapping(value = "/pushGroup")
    @AuditLog(operation = "推送组织架构到第三方平台")
    public JsonResult pushGroup(String tenantId,Integer platformType) throws Exception {
        tenantId = StringUtils.isEmpty(tenantId)?ContextUtil.getCurrentTenantId():tenantId;
        IPlatformHandler platformHandler = PlatformHandlerContext.getPlatformHandler(platformType.toString());
        if(platformHandler==null){
            return JsonResult.getFailResult("未知的第三方平台");
        }
        pushGroup(tenantId,platformHandler);
        pushUser(tenantId,platformHandler);
        return JsonResult.Success("同步用户组织架构成功!");
    }

    private void pushGroup(String tenantId, IPlatformHandler platformHandler){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        queryWrapper.eq("DIM_ID_", OsDimension.DIM_ADMIN_ID);
        List<OsGroup> list = osGroupService.findAll(queryWrapper);
        if(list !=null && list.size()>0){
            list.stream().forEach(osGroup -> {
                platformHandler.pushAddDepartment(osGroup);
            });
        }

    }
    private void pushUser(String tenantId,IPlatformHandler platformHandler ){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        List<OsUser> list = osUserService.findAll(queryWrapper);
        if(list!=null){
            list.stream().forEach(osUser -> {
                //设置主部门
                OsGroup mainGroup = osGroupService.getMainGroup(osUser.getUserId(),osUser.getTenantId());
                osUser.setMainDepId(mainGroup!=null?mainGroup.getGroupId():"");
                platformHandler.pushAddUser(osUser);
            });
        }
    }


}
