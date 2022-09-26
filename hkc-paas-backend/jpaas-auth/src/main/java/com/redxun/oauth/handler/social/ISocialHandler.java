package com.redxun.oauth.handler.social;

import com.redxun.common.base.entity.JsonResult;

/**
 * 社会化登陆处理器接口
 */
public interface ISocialHandler {

    /**
     * 登陆类型：1微信公众号，2企业微信，3钉钉，4飞书,5钉钉-PC登陆,6企业微信-PC登陆
     * @return
     */
    String getType();

    /**
     * 获取第三方授权URL
     * @param tenantId 租户id
     * @param redirectUrl 重定向地址
     * @param state 自定义参数
     */
    JsonResult handAuthUrl(String tenantId, String redirectUrl, String state);
    /**
     * 根据授权信息获取绑定用户
     * @param tenantId 租户id
     * @param code 临时授权code
     * @return 成功返回用户信息，未绑定返回openId,其余情况返回失败信息
     */
    JsonResult handGetUser(String tenantId, String code);

    /**
     * 绑定登陆用户
     * @param openId openId
     * @param tenantId
     * @return 绑定成功，返回openId,其余情况返回失败信息
     */
    JsonResult handBindUserByOpenId(String tenantId, String openId);
    /**
     * 绑定登陆用户
     * @param code code
     * @param tenantId
     * @return 绑定成功，返回openId,其余情况返回失败信息
     */
    JsonResult handBindUserByCode(String tenantId, String code);
}
