package com.redxun.api.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IOrgService {

    /**
     * 根据维度Code查找维度
     * @param dimensionCode
     * @return
     */
    OsDimensionDto getDimensionByCode(String dimensionCode);

    /**
     * 根据用户组ID获取组中得从属人员。
     * @param groupId
     * @return
     */
    List<OsUserDto> getByGroupId(String groupId);

    /**
     * 根据用户ID 获取用户信息。
     * @param userId
     * @return
     */
    OsUserDto getUserById(String userId);


    /**
     * 根据用户IDS 获取用户列表。
     * @param userIds
     * @return
     */
    List<OsUserDto>  getUsersByIds(String userIds);

    /**
     * 根据用户账号获取用户信息。
     * @param username
     * @return
     */
    IUser getByUsername( String username);

    /**
     * 根据用户组ID获取用户组信息。
     * @param groupId
     * @return
     */
    OsGroupDto getGroupById(String  groupId);

    /**
     * 通过用户组KEY获取用户组信息。
     * @param groupKey
     * @return
     */
    OsGroupDto getGroupByKey(String groupKey);

    /**
     * 获取用户的主部门。
     * @param userId
     * @param tenantId
     * @return
     */
    OsGroupDto getMainDeps(String userId,String tenantId);

    /**
     * 根据用户获取所属的用户组。
     * @param userId
     * @return
     */
    List<OsGroupDto>  getBelongGroups( String userId );

    /**
     * 根据用户获取所属的部门。
     * @param userId
     * @return
     */
    List<OsGroupDto>  getBelongDeps( String userId);


    /**
     * 获取当前人的身份信息。
     * @return
     */
    Map<String, Set<String>> getCurrentProfile();


    /**
     * 获取当前人的身份信息。
     * @return
     */
    JSONArray getOsUserGroupHanderList();

    /**
     * 根据执行人获取人员数据。
     * @param executors
     * @return
     */
    List<OsUserDto> getUsersByTaskExecutor(List<TaskExecutor> executors);

    /**
     * 获取默认的企业微信应用
     * @return
     */
    OsWxEntAgentDto getDefaultAgent(String tenantId);

    /**
    * @Description:  获取钉钉应用的配器信息
    * @param tenantId 当前租户ID
    * @return com.redxun.dto.user.OsDdAgentDto
    * @Author: Elwin ZHANG  @Date: 2021/4/23 14:28
    **/
    OsDdAgentDto getDdAgent(String tenantId);

    /**
     * 根据企业微信账号获取用户
     * @return
     */
    IUser getByWxOpenId(String wxOpenId);

    /**
     * 检验其他用户是否已经存在当前企业微信账号
     * @return
     */
    String isOtherUserContainWxOpenId(String username,String wxOpenId);


    /**
     * 绑定企业微信账号
     * @return
     */
    void updateByWxOpenId(String username,String wxOpenId);

    /*
     *根据钉钉code获取账号
     */
    JSONObject getAccountByDdcode(String ddCode);

    /**
     * 绑定钉钉账号
     * @return
     */
    void updateByDdId(String username,String ddId);

    /**
     * 根据用户账号获取用户信息。
     * @param account
     * @return
     */
    OsUserDto getByAccount( String account);

    /**
     * 根据用户获取角色列表。
     * @param userId
     * @param tenantId
     * @return
     */
    List getGroupByUserId( String userId,String tenantId);

    /**
     * 根据组Id获取列表。
     * @param groupId
     * @return
     */
    List getGrantMenusByGroupId( String groupId);

    /**
     * 根据公司管理员用户id获取其管理的所有公司k列表
     *
     * @param tenantId    租户id
     * @param userId    公司管理员用户id
     * @return
     */
    List<OsGroupDto> getCompanysByAdminId(String tenantId, String userId);

    /**
     * 根据用户id获取其所在公司
     *
     * @param tenantId    租户id
     * @param userId    用户id
     * @return
     */
    OsGroupDto getCompanyByUserId(String tenantId, String userId);

    /**
     * 获取默认的飞书应用
     * @return
     */
    OsFsAgentDto getFsDefaultAgent(String tenantId);

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
    JSONObject getCompanys();
}
