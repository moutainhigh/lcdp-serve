package com.redxun.api.org.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.common.base.entity.IUser;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.*;
import com.redxun.feign.org.OrgClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组织服务类
 */
@Component
@Primary
public class OrgServiceImpl implements IOrgService {

    @Resource
    @Lazy
    OrgClient orgClient;

    @Override
    public OsDimensionDto getDimensionByCode(String dimensionCode) {
        return orgClient.getDimensionByCode(dimensionCode);
    }

    @Override
    public List<OsUserDto> getByGroupId(String groupId) {
        return orgClient.getByGroupId(groupId);
    }

    @Override
    public OsUserDto getUserById(String userId) {
        return orgClient.getUserById(userId);
    }

    @Override
    public List<OsUserDto> getUsersByIds(String userIds) {
        return orgClient.getUsersByIds(userIds);
    }

    @Override
    public IUser getByUsername(String username) {
        return orgClient.getByUsername(username);
    }

    @Override
    public OsGroupDto getGroupById(String groupId) {
        return orgClient.getGroupById(groupId);
    }

    @Override
    public OsGroupDto getGroupByKey(String groupKey) {
        return orgClient.getGroupByKey(groupKey);
    }

    @Override
    public OsGroupDto getMainDeps(String userId, String tenantId) {
        return orgClient.getMainDeps(userId,tenantId);
    }

    @Override
    public List<OsGroupDto> getBelongGroups(String userId) {
        return orgClient.getBelongGroups(userId);
    }

    @Override
    public List<OsGroupDto> getBelongDeps(String userId) {
        return orgClient.getBelongDeps(userId);
    }

    @Override
    public Map<String, Set<String>> getCurrentProfile() {
        return orgClient.getCurrentProfile();
    }

    @Override
    public JSONArray getOsUserGroupHanderList() {
        return orgClient.getOsUserGroupHanderList();
    }

    @Override
    public List<OsUserDto> getUsersByTaskExecutor(List<TaskExecutor> executors) {
        return orgClient.getUsersByTaskExecutor(executors);
    }

    @Override
    public OsWxEntAgentDto getDefaultAgent(String tenantId){
        return orgClient.getDefaultAgent(tenantId);
    }

    @Override
    public OsDdAgentDto getDdAgent(String tenantId) {
        return orgClient.getDdAgent(tenantId);
    }

    @Override
    public IUser getByWxOpenId(String wxOpenId) {
        return orgClient.getByWxOpenId(wxOpenId);
    }

    @Override
    public String isOtherUserContainWxOpenId(String username,String wxOpenId) {
        return orgClient.isOtherUserContainWxOpenId(username,wxOpenId);
    }

    @Override
    public void updateByWxOpenId(String username,String wxOpenId) {
        orgClient.updateByWxOpenId(username,wxOpenId);
    }

    @Override
    public JSONObject getAccountByDdcode(String ddCode) {
        return orgClient.getAccountByDdcode(ddCode);
    }

    @Override
    public void updateByDdId(String username,String ddId) {
        orgClient.updateByDdId(username,ddId);
    }

    @Override
    public OsUserDto getByAccount(String account) {
        return orgClient.getByAccount(account);
    }

    @Override
    public List getGroupByUserId(String userId,String tenantId) {
        return orgClient.getGroupByUserId(userId,tenantId);
    }

    @Override
    public List getGrantMenusByGroupId( String groupId) {
        return orgClient.getGrantMenusByGroupId(groupId);
    }

    @Override
    public List<OsGroupDto> getCompanysByAdminId(String tenantId, String userId) {
        return orgClient.getCompanysByAdminId(tenantId, userId);
    }

    @Override
    public OsGroupDto getCompanyByUserId(String tenantId, String userId) {
        return orgClient.getCompanyByUserId(tenantId, userId);
    }

    @Override
    public OsFsAgentDto getFsDefaultAgent(String tenantId){
        return orgClient.getFsDefaultAgent(tenantId);
    }


    /**
     * 获取当前人的公司数据。
     * {
     *     supportGrade:true,
     *     companyId:"",
     *     originCompanyId:"",
     *     companys:[{groupId:"",name:""}]
     * }
     * @return
     */
    @Override
    public JSONObject getCompanys(){
        return orgClient.getCompanys();
    }

}
