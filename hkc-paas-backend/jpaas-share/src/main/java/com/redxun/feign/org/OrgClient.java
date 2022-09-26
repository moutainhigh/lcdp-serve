package com.redxun.feign.org;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.model.JPaasUser;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组织架构的Feign客户端封装
 *
 */
@FeignClient(name = "jpaas-user")
public interface OrgClient {
    /**
     * 获取机构对应的企业微信实体代理DTO
     * @param tenantId
     * @return
     */
    @GetMapping(value="/user/org/osWxEntAgent/getDefaultAgent")
    OsWxEntAgentDto getDefaultAgent(@RequestParam(value = "tenantId") String tenantId);

    /**
     * 获取机构对应钉钉的代理配置DTO
     * @param tenantId
     * @return
     */
    @GetMapping(value="/user/org/osDdAgent/getDdAgent")
    OsDdAgentDto getDdAgent(@RequestParam(value = "tenantId") String tenantId);

    /**
     * 根据用户组ID获取用户列表信息
     * @param groupId 用户组ID
     * @return
     */
    @GetMapping("/user/org/osUser/getByGroupId")
    List<OsUserDto> getByGroupId(@RequestParam(value = "groupId") String groupId) ;

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/user/org/osUser/getById")
    OsUserDto getUserById(@RequestParam(value = "userId") String userId) ;

    /**
     * 获取所有的用户信息
     * @return
     */
    @GetMapping("/user/org/osUser/getAllUser")
    List<OsUserDto> getAllUser();

    /**
     * 根据维度编码获取组织维度DTO
     * @param code
     * @return
     */
    @GetMapping("/user/org/osDimension/getDimensionByCode")
    OsDimensionDto getDimensionByCode(@RequestParam(value = "code") String code) ;

    /**
     * 根据多个用户组ID返回用户组列表
     * @param groupIds 用户组IDs,如 1,2,3
     * @return
     */
    @GetMapping("/user/org/osGroup/getGroupByIds")
    List<OsGroupDto> getGroupByIds(@RequestParam(value = "groupIds") String groupIds) ;

    /**
     * 根据维度Key与用户组Key返回用户组Key
     * @param dimKey
     * @param groupKey
     * @return
     */
    @GetMapping("/user/org/osGroup/getByDimKeyGroupKey")
    OsGroupDto getByDimKeyGroupKey(@RequestParam(value = "dimKey") String dimKey,@RequestParam(value = "groupKey") String groupKey) ;

    /**
     * 根据用户组ID返回用户组信息
     * @param groupId
     * @return
     */
    @GetMapping("/user/org/osGroup/getById")
    OsGroupDto getGroupById(@RequestParam(value = "groupId") String groupId) ;

    /**
     * 根据用户组Key返回用户组
     * @param groupKey
     * @return
     */
    @GetMapping("/user/org/osGroup/getByKey")
    OsGroupDto getGroupByKey(@RequestParam(value="groupKey") String groupKey);

    /**
     * 根据用户组Key与租户ID返回用户组信息
     * @param groupKey
     * @param tenantId
     * @return
     */
    @GetMapping("/user/org/osGroup/getByKeyTenantId")
    OsGroupDto getGroupByKeyTenantId(@RequestParam(value="groupKey") String groupKey,@RequestParam(value="tenantId") String tenantId);

    /**
     * 根据用户账号获取用户信息
     * @param username
     * @return
     */
    @GetMapping("/user/org/osUser/getByUsername")
    JPaasUser getByUsername(@RequestParam(value = "username") String username);

    /**
     * 根据微信OpenID获取用户信息
     * @param wxOpenId
     * @return
     */
    @GetMapping("/user/org/osUser/getByWxOpenId")
    JPaasUser getByWxOpenId(@RequestParam(value = "wxOpenId") String wxOpenId);

    /**
     * 判断该账号以外的其他用户是否包括了微信OpenID
     * @param username 用户账号
     * @param wxOpenId 微信OpenID
     * @return
     */
    @GetMapping("/user/org/osUser/isOtherUserContainWxOpenId")
    String isOtherUserContainWxOpenId(@RequestParam("username")String username, @RequestParam("wxOpenId")String wxOpenId);

    /**
     * 根据微信OpenID更新用户信息
     * @param username
     * @param wxOpenId
     */
    @GetMapping("/user/org/osUser/updateByWxOpenId")
    void updateByWxOpenId(@RequestParam("username")String username, @RequestParam("wxOpenId")String wxOpenId);

    /**
     * 获取某个用户组（多个）下的多个角色（只需要包括其中一个）的用户Id列表
     * @param groupIds
     * @param roleIds
     * @return
     */
    @GetMapping("/user/org/osUser/getUserIdsByGroupIdsRoleIds")
    List<String> getUserIdsByGroupIdsRoleIds(@RequestParam("groupIds") String groupIds,@RequestParam("roleIds") String roleIds);

    /**
     * 获取同步属于两个组下的用户
     * @param groupId1
     * @param groupId2
     * @return
     */
    @GetMapping("/user/org/osUser/getUserIdsByGroupId1GroupId2")
    List<String> getUserIdsByGroupId1GroupId2(@RequestParam("groupId1") String groupId1,@RequestParam("groupId2") String groupId2);

    /**
     * 按照用户组与关系类型进行用户查找
     * @param groupId
     * @param relTypeKey
     * @return
     */
    @GetMapping("/user/org/osUser/getUserIdsByGroupIdRelTypeKey")
    List<String> getUserIdsByGroupIdRelTypeKey(@RequestParam("groupId") String groupId,@RequestParam("relTypeKey") String relTypeKey);

    /**
     * 按照用户Id与关系类型进行用户查找
     * @param userId
     * @param relTypeKey
     * @return
     */
    @GetMapping("/user/org/osUser/getUserIdsByGroupIdRelTypeKey")
    List<String> getUserIdsByUserIdRelTypeKey(@RequestParam("userId") String userId,@RequestParam("relTypeKey") String relTypeKey);

    /**
     * 获取用户的主部门。
     * @param userId
     * @param tenantId
     * @return
     */
    @GetMapping("/user/org/osGroup/getMainDeps")
    OsGroupDto getMainDeps(@RequestParam("userId")String userId, @RequestParam("tenantId")String tenantId);

    /**
     * 获取用户所属的组。
     * @param userId
     * @return
     */
    @GetMapping("/user/org/osGroup/getBelongGroups")
    List<OsGroupDto>  getBelongGroups(@RequestParam("userId") String userId);

    /**
     * 获取用户所属的所有的部门
     * @param userId
     * @return
     */
    @GetMapping("/user/org/osGroup/getBelongDeps")
    List<OsGroupDto>  getBelongDeps(@RequestParam("userId") String userId);

    /**
     * 获取上级用户组
     * @param groupId
     * @return
     */
    @GetMapping("/user/org/osGroup/getUpGroup")
    OsGroupDto  getUpGroup(@RequestParam("groupId") String groupId);

    /**
     * 根据当前用户组（往上查找符合等级的）的上级用户组
     * @param groupId
     * @param rankLevel
     * @return
     */
    @GetMapping("/user/org/osGroup/getUpRankLevelGroup")
    OsGroupDto  getUpRankLevelGroup(@RequestParam("groupId") String groupId, @RequestParam("rankLevel") Integer rankLevel);

    /**
     * 获取用户组织信息
     * @return
     */
    @GetMapping("/user/org/osGroup/getCurrentProfile")
    Map<String, Set<String>> getCurrentProfile();

    /**
     * 获取处理器列表
     * @return
     */
    @GetMapping("/user/org/osGroup/getOsUserGroupHanderList")
    JSONArray getOsUserGroupHanderList();

    /**
     * 返回流程节点执行者列表中的所有的用户DTO
     * @param executors
     * @return
     */
    @PostMapping("/user/org/osUser/getUsersByTaskExecutor")
    List<OsUserDto> getUsersByTaskExecutor(@RequestBody List<TaskExecutor> executors);

    /**
     * 根据用户Ids返回用户（DTO实例）列表
     * @param userIds
     * @return
     */
    @PostMapping("/user/org/osUser/getUsersByIds")
    List<OsUserDto> getUsersByIds(@RequestParam("userIds") String userIds);

    /**
     * 根据关系类型Key返回关系类型
     * @param relTypeKey 关系类型Key
     * @return
     */
    @GetMapping("/user/org/osRelType/getRelTypeByKey")
    OsRelTypeDto getRelTypeByKey(@RequestParam("relTypeKey")String relTypeKey);

    /**
     * 根据关系Key与PART1返回关系实例列表
     * @param relTypeKey 关系类型Key
     * @param party1 关系1方
     * @return
     */
    @GetMapping("/user/org/osRelInst/getByRelTypeKeyParty1")
    List<OsRelInstDto> getByRelTypeKeyParty1(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1);

    /**
     * 根据关系Key与PART2返回关系实例列表
     * @param relTypeKey 关系类型Key
     * @param party2 关系2方
     * @return
     */
    @GetMapping("/user/org/osRelInst/getByRelTypeKeyParty2")
    List<OsRelInstDto> getByRelTypeKeyParty2(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party2")String party2);

    /**
     * 根据关系类型Key 与Party1与关系维度1返回关系实例列表
     * @param relTypeKey  关系类型Key
     * @param party1 关系1
     * @param dimId 用户维度ID
     * @return
     */
    @GetMapping("/user/org/osRelInst/getByRelTypeKeyParty1AndDim1")
    List<OsRelInstDto> getByRelTypeKeyParty1AndDim1(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1,
                                                    @RequestParam("dimId") String dimId);

    /**
     * 根据关系类型Key 与Party2与关系维度1返回关系实例列表
     * @param relTypeKey 关系类型Key
     * @param party2 关系2
     * @param dimId 用户维度ID
     * @return
     */
    @GetMapping("/user/org/osRelInst/getByRelTypeKeyParty2AndDim1")
    List<OsRelInstDto> getByRelTypeKeyParty2AndDim1(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party2")String party2,
                                                    @RequestParam("dimId") String dimId);

    /**
     * 根据属性条件值返回其所属用户Ids
     * @param dimId 维度ID
     * @param condition 条件
     * @return
     */
    @PostMapping("/user/org/osPropertiesVal/getOwnerIdsByCondition")
    List<String> getOwnerIdsByCondition(@RequestParam("dimId")String dimId,@RequestParam("condition")String condition);

    /**
     * 根据钉钉编码返回用户账号
     * @param ddCode
     * @return
     */
    @PostMapping("/user/org/osUser/users-anon/getAccountByDdcode")
    JSONObject getAccountByDdcode(@RequestParam("ddCode")String ddCode);

    /**
     * 根据钉钉ID更新用户信息
     * @param username 用户名
     * @param ddId 钉钉ID
     */
    @PostMapping("/user/org/osUser/users-anon/updateByDdId")
    void updateByDdId(@RequestParam("username")String username,@RequestParam("ddId")String ddId);

    /**
     * 根据用户账号获取用户信息
     * @param account
     * @return
     */
    @GetMapping("/user/org/osUser/getByAccount")
    OsUserDto getByAccount(@RequestParam(value = "account") String account);

    /**
     * 获取用户在某个租户下的所有用户组
     * @param userId
     * @param tenantId
     * @return
     */
    @GetMapping("/user/org/osGradeRole/getGroupByUserId")
    List getGroupByUserId(@RequestParam(value = "userId") String userId,@RequestParam(value = "tenantId") String tenantId);

    /**
     * 根据用户组获取其授权的菜单
     * @param groupId
     * @return
     */
    @GetMapping("/user/org/osGroupMenu/getGrantMenusByGroupId")
    List getGrantMenusByGroupId(@RequestParam(value = "groupId") String groupId);

    /**
     * 根据组获取下级的组。
     * @param groupId
     * @return
     */
    @GetMapping("/user/org/osGroup/getDownDeps")
    List getDownDeps(@RequestParam(value = "groupId") String groupId);


    @GetMapping("/user/org/osGroup/getGroupsByUser")
    List getGroupsByUser( @RequestParam("userId")String userId,
                          @RequestParam("tenantId")String tenantId);

    /**
     * 根据用户组关系获取用户。
     * @param relTypeKey
     * @param party1
     * @return
     */
    @GetMapping("/user/org/osUser/getUserByRelTypeKeyParty1")
    List<OsUserDto> getUserByRelTypeKeyParty1( @RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1);

    /**
     * 根据用组关系获取用户。
     * @param relTypeKey
     * @param party1
     * @return
     */
    @GetMapping("/user/org/osUser/getUserByRelTypePart12")
    List<OsUserDto> getUserByRelTypePart12(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1,@RequestParam("party2")String party2);


    /**
     * 根据公司管理员用户id获取其管理的所有公司列表
     *
     * @param tenantId    租户id
     * @param userId    公司管理员用户id
     * @return
     */
    @GetMapping("/user/org/osGradeAdmin/getCompanysByAdminId")
    List<OsGroupDto> getCompanysByAdminId(@RequestParam("tenantId")String tenantId, @RequestParam("userId")String userId);

    /**
     * 根据用户id获取其所在公司
     *
     * @param tenantId    租户id
     * @param userId    用户id
     * @return
     */
    @GetMapping("/user/org/osGroup/getCompanyByUserId")
    OsGroupDto getCompanyByUserId(@RequestParam("tenantId")String tenantId, @RequestParam("userId")String userId);

    /**
     * 获取所有分公司的ids
     *
     * @param companyId   管理员所属的公司id
     * @return
     */
    @GetMapping("/user/org/osGroup/getAllSubCompanyIds")
    List<String> getAllSubCompanyIds(@RequestParam("companyId")String companyId);

    /**
     * 根据公司id获取公司dto
     * @param companyId 公司id
     * @return
     */
    @GetMapping("/user/org/osGroup/getGroupById")
    OsGroupDto getCompanyById(@RequestParam("groupId")String companyId);

    /**
     * 根据公司id获取所有子公司dto
     * @param companyId 公司id
     * @return
     */
    @GetMapping("/user/org/osGroup/getAllSubCompanyByCompnayId")
    List<OsGroupDto> getAllSubCompanyByCompnayId(@RequestParam("groupId")String companyId);


    /**
     * 根据关系Key与PART1返回关系实例列表
     * @param relTypeKey 关系类型Key
     * @param party1 关系1方
     * @return
     */
    @GetMapping("/user/org/osRelInst/getGroupRelExecutorCalc")
    List<OsUserDto> getGroupRelExecutorCalc(@RequestParam("relTypeKey")String relTypeKey,@RequestParam("party1")String party1,@RequestParam("dimId")String dimId);

    /**
     * 根据关系类型ID和组ID查找用户数据
     * @param groupId 部门ID
     * @param relTypeId 关联维度ID
     * @return
     */
    @GetMapping("/user/org/osRelInst/getUserListByGroupIdAndRelTypeIdAndDimId")
    List<OsUserDto> getUserListByGroupIdAndRelTypeIdAndDimId(@RequestParam("dimId")String dimId,@RequestParam("groupId")String groupId,@RequestParam("relTypeId")String relTypeId);

    /**
     * 组织部门ID，关联维度ID，用户组关系类型ID获取用户
     * @param groupId 组织部门ID
     * @param relTypeId 关联维度ID
     * @param party2 用户组关系类型ID
     * @param dimId 组织维度ID
     * @return
     */
    @GetMapping("/user/org/osRelInst/getUserByGroupIdAndRelTypeIdAndParty2")
    List<OsUserDto> getUserByGroupIdAndRelTypeIdAndParty2(@RequestParam("groupId")String groupId,@RequestParam("relTypeId")String relTypeId,@RequestParam("party2")String party2,@RequestParam("dimId")String dimId);

    /**
     * 获取机构对应的飞书应用管理配置
     * @param tenantId
     * @return
     */
    @GetMapping(value="/user/org/osFsAgent/getDefaultAgent")
    OsFsAgentDto getFsDefaultAgent(@RequestParam(value = "tenantId") String tenantId);

    /**
     * 根据维度Id与用户组Key或者用户组名称返回用户组Id
     * @param groupJson
     * @return
     */
    @PostMapping("/user/org/osGroup/getByDimIdAndKeyOrName")
    List<OsGroupDto> getByDimIdAndKeyOrName(@RequestBody JSONObject groupJson);



    @PostMapping("/user/org/osGroup/getCompanys")
    JSONObject getCompanys();
}
