package com.redxun.feign.org;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.ServiceNameConstants;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.model.JPaasUser;
import com.redxun.dto.user.OsUserDto;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户的Feign客户端
 */
@FeignClient(name = ServiceNameConstants.USER_SERVICE)
public interface UserClient {
    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    @GetMapping(value = "user/org/osUser/findByUsername", params = "username")
    JPaasUser findByUsername(@RequestParam("username") String username);

    /**
     * 根据用户名跟租户ID获取用户信息
     * @param username
     * @param tenantId
     * @return
     */
    @GetMapping(value = "user/org/osUser/findByUsernameAndTenantId", params={"username","tenantId"})
    JPaasUser findByUsernameAndTenantId(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);

    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    @GetMapping(value = "user/org/osUser/getUserById", params = "userId")
    JPaasUser getUserById(@RequestParam("userId") String userId);

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "user/org/osUser/users-anon/mobile", params = "mobile")
    JPaasUser findByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据用户Id获取用户信息
     * @param userId
     * @return
     */
    @GetMapping(value="/user/org/osUser/getById",params="userId")
    OsUserDto findByUserId(@RequestParam("userId") String userId);

    /**
     * 根据OpenID获取用户信息
     * @param tenantId
     * @param openId
     * @return
     */
    @GetMapping(value="wxent/getuser",params={"tenantId","openId"})
    @Deprecated
    JPaasUser findByOpenId( @RequestParam("tenantId") String tenantId, @RequestParam("openId")String openId);

    /**
     * 根据租户ID与钉钉ID获取用户信息
     * @param tenantId
     * @param ddId
     * @return
     */
    @GetMapping(value="dd/getuser",params={"tenantId","ddId"})
    @Deprecated
    JPaasUser findByDdId( @RequestParam("tenantId") String tenantId, @RequestParam("ddId")String ddId);

    /**
     * 重置用户密码
     * @param username  用户名
     * @param password  需要重置的密码
     * @return
     */
    @GetMapping(value="/user/org/osUser/resetPassword",params="userId")
    JsonResult resetPassword( @RequestParam("username")String username,  @RequestParam("password")String password);

    /**
     * 按机构域名查找租户Id
     * @param domain
     * @return
     */
    @GetMapping(value = "user/org/osInst/findInstIdByDomain", params = "domain")
    String findTenanIdByDomain(@RequestParam("domain") String domain);

    /**
     * 按机构ID及机构编码查找机构ID
     * @param instNo
     * @return
     */
    @GetMapping(value = "user/org/osInst/findInstIdByInstNo", params = "instNo")
    String findTenanIdByInstNo(@RequestParam("instNo") String instNo);

    /**
     * 根据应用ID获取应用的所有菜单
     * @param appId
     * @return
     */
    @GetMapping(value = "user/org/osUser/getMenusByAppId")
    List<SysMenuDto> getMenusByAppId(@RequestParam("appId")String appId);

    /**
     * 密码多次错误输入导致账号被锁定
     * @param username
     * @param tenantId
     * @return
     */
    @GetMapping(value = "user/org/osPasswordInputError/isLockedByPasswordInputError")
    Boolean isLockedByPasswordInputError(@RequestParam("username")String username, @RequestParam("tenantId") String tenantId);

    /**
     * 密码错误输入次数递增
     * @param username
     * @param tenantId
     */
    @GetMapping(value = "user/org/osPasswordInputError/saveInputErrorIncrement")
    void saveInputErrorIncrement(@RequestParam("username")String username, @RequestParam("tenantId") String tenantId);

    /**
     * 密码过期导致账号被锁定
     * @param username
     * @param tenantId
     * @return
     */
    @GetMapping(value = "user/org/osPasswordPolicy/isLockedByPasswordExpire")
    Boolean isLockedByPasswordExpire(@RequestParam("username")String username, @RequestParam("tenantId") String tenantId);

    /**
     * 获取用户是否首次登录
     * @param isFirstLogin
     * @param tenantId
     * @return
     */
    @GetMapping(value = "user/org/osPasswordPolicy/getIsFirstLogin")
    String getIsFirstLogin(@RequestParam("isFirstLogin")String isFirstLogin, @RequestParam("tenantId")String tenantId);

    @GetMapping(value="/user/org/osUser/changePassword")
    JsonResult changePassword(@RequestParam("userId") String userId, @RequestParam("password") String password);

    /**
     * 根据OpenID获取用户信息
     * @param tenantId
     * @param openId
     * @return
     */
    @GetMapping(value="/user/org/osUser/findByOpenId",params={"tenantId","openId","platformType"})
    JPaasUser findByOpenId( @RequestParam("tenantId") String tenantId, @RequestParam("openId")String openId,@RequestParam("platformType")Integer platformType);

    /**
     * 第三方平台登陆绑定
     * @param openId
     * @param platformType
     * @param tenantId
     * @return
     */
    @PostMapping(value="/user/org/osUserPlatform/bind")
    JsonResult bind( @RequestParam("openId")String openId,  @RequestParam("platformType")Integer platformType,@RequestParam("tenantId")String tenantId);
}
