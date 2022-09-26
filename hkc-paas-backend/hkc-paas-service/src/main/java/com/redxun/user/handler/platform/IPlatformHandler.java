package com.redxun.user.handler.platform;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;

/**
 * 第三方平台处理器接口
 */
public interface IPlatformHandler {

    /**Platform
     * 登陆类型：1微信公众号，2企业微信，3钉钉，4飞书
     * @return
     */
    String getPlatformType();

    /**
     * 推送新增部门至第三方平台
     * @param osGroup
     */
    JsonResult pushAddDepartment(OsGroup osGroup);
    /**
     * 推送删除部门至第三方平台
     * @param osGroup 部门
     * @return
     */
    JsonResult pushDelDepartment(OsGroup osGroup);

    /**
     * 推送新增用户至第三方平台
     * @param osUser
     */
    JsonResult pushAddUser(OsUser osUser);
    /**
     * 推送删除用户至第三方平台
     * @param osUser 用户
     * @return
     */
    JsonResult pushDelUser(OsUser osUser);


}
