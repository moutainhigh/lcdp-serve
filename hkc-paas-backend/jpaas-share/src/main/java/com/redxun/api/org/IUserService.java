package com.redxun.api.org;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import org.springframework.web.bind.annotation.RequestParam;

public interface IUserService {

    /**
     * 根据username获取用户
     * @param username
     * @return
     */
    JPaasUser findByUsername(@RequestParam("username") String username);

    /**
     * 根据用户名与租户ID获取
     * @param username
     * @param tenantId
     * @return
     */
    JPaasUser findByUsernameAndTenantId(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */

    JPaasUser findByMobile(@RequestParam("mobile") String mobile);





    /**
     * 重置用户密码
     * @param username
     * @param password
     * @return
     */
    JsonResult resetPassword(@RequestParam("username") String username,
                             @RequestParam("password")String password);

    /**
     * 按机构域名查找机构
     * @param domain
     * @return
     */
    String findTenanIdByDomain(@RequestParam("domain") String domain);

    /**
     *  按机构编码查找机构
     * @param instNo 机构编码
     * @return
     */
    String findTenantIdByInstNo(@RequestParam("instNo") String instNo);


    /**
     * 密码多次错误输入导致账号被锁定
     * @param username
     * @param tenantId
     * @return
     */
    Boolean isLockedByPasswordInputError(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);

    /**
     * 存密码错误输入次数递增保
     * @param username
     * @param tenantId
     * @return
     */
    void saveInputErrorIncrement(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);

    /**
     * 密码过期导致账号被锁定
     * @param username
     * @param tenantId
     * @return
     */
    Boolean isLockedByPasswordExpire(@RequestParam("username") String username, @RequestParam("tenantId") String tenantId);


    /**
     * 获取用户是否首次登录
     * @param isFirstLogin
     * @param tenantId
     * @return
     */
    String getIsFirstLogin(@RequestParam("isFirstLogin") String isFirstLogin, @RequestParam("tenantId") String tenantId);


    /**
     * 用户首次登录修改密码
     * @param userId
     * @param password
     * @return
     */
    JsonResult changePassword(@RequestParam("userId") String userId,
                             @RequestParam("password")String password);

    /**
     * 按租户ID与OpenID查找用户
     * @param tenantId
     * @param openId
     * @return
     */
    JPaasUser findByOpenId(@RequestParam("tenantId") String tenantId,
                           @RequestParam("openId")String openId,@RequestParam("platformType")Integer platformType);

    /**
     * 第三方平台绑定
     * @param openId
     * @param platformType
     * @param tenantId
     * @return
     */
    JsonResult platformBind(@RequestParam("openId") String openId,
                            @RequestParam("platformType") Integer platformType,
                            @RequestParam("tenantId") String tenantId);

}
