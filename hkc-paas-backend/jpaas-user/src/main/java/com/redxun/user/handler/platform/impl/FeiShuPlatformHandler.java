package com.redxun.user.handler.platform.impl;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.user.handler.platform.IPlatformHandler;
import com.redxun.user.org.entity.OsFsAgent;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.OsFsAgentServiceImpl;
import com.redxun.util.BeanCopyUtils;
import com.redxun.util.feishu.FeiShuClient;
import com.redxun.util.feishu.entity.FeiShuDepartment;
import com.redxun.util.feishu.entity.FeiShuDepartmentsEditReq;
import com.redxun.util.feishu.entity.FeiShuUser;
import com.redxun.util.feishu.entity.FeiShuUserEditReq;
import com.redxun.util.feishu.enums.DepartmentIdTypeEnum;
import com.redxun.util.feishu.enums.EmployeeTypeEnum;
import com.redxun.util.feishu.enums.UserIdTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 飞书第三方平台处理器
 * @author ycs
 */
@Service
@Slf4j
public class FeiShuPlatformHandler implements IPlatformHandler {

    @Autowired
    private FeiShuClient feiShuClient;
    @Autowired
    private OsFsAgentServiceImpl  osFsAgentService;

    @Override
    public String getPlatformType() {
        return "4";
    }

    @Override
    public JsonResult pushAddDepartment(OsGroup osGroup) {
        JsonResult result = JsonResult.getFailResult("推送部门失败！");
        try {
            OsFsAgent defaultAgent = osFsAgentService.getDefaultAgent(osGroup.getTenantId());
            if(defaultAgent==null){
                return JsonResult.getFailResult("未配置应用信息");
            }
            if(defaultAgent.getIsPush()==0){
                return JsonResult.getFailResult("配置未启用同步状态");
            }
            String tenantAccessToken = feiShuClient.getTenantAccessToken(defaultAgent.getAppId(), defaultAgent.getSecret());
            FeiShuDepartment department = feiShuClient.getDepartment(tenantAccessToken, osGroup.getGroupId(), UserIdTypeEnum.USER_ID, DepartmentIdTypeEnum.DEPARTMENT_ID);
            if(department!=null){
                FeiShuDepartmentsEditReq req = BeanCopyUtils.map(department, FeiShuDepartmentsEditReq.class);
                req.setName(osGroup.getName());
                req.setParentDepartmentId(osGroup.getParentId());
                FeiShuDepartment feiShuDepartment = feiShuClient.updateDepartment(tenantAccessToken, req, UserIdTypeEnum.USER_ID, DepartmentIdTypeEnum.DEPARTMENT_ID);
                if(feiShuDepartment!=null){
                    result = JsonResult.getSuccessResult("推送部门成功！");
                }
            }else{
                FeiShuDepartmentsEditReq req = new FeiShuDepartmentsEditReq();
                req.setDepartmentId(osGroup.getGroupId());
                req.setParentDepartmentId(osGroup.getParentId());
                req.setName(osGroup.getName());
                FeiShuDepartment feiShuDepartment = feiShuClient.createDepartment(tenantAccessToken, req, UserIdTypeEnum.USER_ID, DepartmentIdTypeEnum.DEPARTMENT_ID);
                if(feiShuDepartment!=null){
                    result = JsonResult.getSuccessResult("推送部门成功！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FeiShuSocialHandler.pushAddDepartment,groupId:{}",osGroup.getGroupId(),e);
            return result;
        }
        return result;
    }

    @Override
    public JsonResult pushDelDepartment(OsGroup osGroup) {
        JsonResult result = JsonResult.getFailResult("推送部门失败！");
        try {
            OsFsAgent defaultAgent = osFsAgentService.getDefaultAgent(osGroup.getTenantId());
            if(defaultAgent==null){
                return JsonResult.getFailResult("未配置应用信息");
            }
            String tenantAccessToken = feiShuClient.getTenantAccessToken(defaultAgent.getAppId(), defaultAgent.getSecret());
            Boolean aBoolean = feiShuClient.delDepartment(tenantAccessToken, osGroup.getGroupId(), DepartmentIdTypeEnum.DEPARTMENT_ID);
            if(aBoolean){
                result = JsonResult.getSuccessResult("删除部门成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FeiShuSocialHandler.pushDelDepartment,groupId:{}",osGroup.getGroupId(),e);
            return result;
        }
        return result;
    }

    @Override
    public JsonResult pushAddUser(OsUser osUser) {
        JsonResult result = JsonResult.getFailResult("推送用户失败！");
        try {
            OsFsAgent defaultAgent = osFsAgentService.getDefaultAgent(osUser.getTenantId());
            if(defaultAgent==null){
                return JsonResult.getFailResult("未配置应用信息");
            }
            if(defaultAgent.getIsPush()==0){
                return JsonResult.getFailResult("配置未启用同步状态");
            }
            String tenantAccessToken = feiShuClient.getTenantAccessToken(defaultAgent.getAppId(), defaultAgent.getSecret());
            FeiShuUser user = feiShuClient.getUser(tenantAccessToken, osUser.getUserId(), UserIdTypeEnum.USER_ID, DepartmentIdTypeEnum.DEPARTMENT_ID);
            if(user!=null){
                FeiShuUserEditReq req = BeanCopyUtils.map(user, FeiShuUserEditReq.class);
                req.setMobile(osUser.getMobile());
                req.setName(osUser.getFullName());
                List<String> list = new ArrayList<>();
                list.add(osUser.getMainDepId());
                req.setDepartmentIds(list);
                FeiShuUser  feiShuUser = feiShuClient.updateUser(tenantAccessToken, req, UserIdTypeEnum.USER_ID, DepartmentIdTypeEnum.DEPARTMENT_ID);
                if(feiShuUser!=null){
                    result = JsonResult.getSuccessResult("推送用户成功！");
                }
            }else{
                FeiShuUserEditReq req = new FeiShuUserEditReq();
                req.setUserId(osUser.getUserId());
                req.setMobile(osUser.getMobile());
                req.setName(osUser.getFullName());
                List<String> list = new ArrayList<>();
                list.add(osUser.getMainDepId());
                req.setDepartmentIds(list);
                req.setEmployeeType(EmployeeTypeEnum.REGULAR.getValue());
                FeiShuUser feiShuUser = feiShuClient.createUser(tenantAccessToken, req, UserIdTypeEnum.USER_ID, DepartmentIdTypeEnum.DEPARTMENT_ID);
                if(feiShuUser!=null){
                    result = JsonResult.getSuccessResult("推送用户成功！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FeiShuSocialHandler.pushAddUser,userId:{}",osUser.getUserId(),e);
        }
        return result;
    }

    @Override
    public JsonResult pushDelUser(OsUser osUser) {
        JsonResult result = JsonResult.getFailResult("推送用户失败！");
        try {
            OsFsAgent defaultAgent = osFsAgentService.getDefaultAgent(osUser.getTenantId());
            if(defaultAgent==null){
                return JsonResult.getFailResult("未配置应用信息");
            }
            String tenantAccessToken = feiShuClient.getTenantAccessToken(defaultAgent.getAppId(), defaultAgent.getSecret());
            Boolean aBoolean = feiShuClient.delUser(tenantAccessToken, osUser.getUserId(), UserIdTypeEnum.USER_ID);
            if(aBoolean){
                result = JsonResult.getSuccessResult("删除部门成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("FeiShuSocialHandler.pushDelUser,userId:{}",osUser.getUserId(),e);
        }
        return result;
    }
}
