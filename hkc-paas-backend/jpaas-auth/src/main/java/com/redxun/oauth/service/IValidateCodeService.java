package com.redxun.oauth.service;

import com.redxun.common.base.entity.JsonResult;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yjy
 * @date 2018/12/10
 */
public interface IValidateCodeService {
    /**
     * 保存图形验证码
     * @param deviceId 前端唯一标识
     * @param imageCode 验证码
     */
    void saveImageCode(String deviceId, String imageCode);

    JsonResult sendSmsCode(String mobile);

    /**
     * 获取验证码
     * @param deviceId 前端唯一标识/手机号
     */
    String getCode(String deviceId);

    /**
     * 删除验证码
     * @param deviceId 前端唯一标识/手机号
     */
    void remove(String deviceId);

    /**
     * 验证验证码
     */
    void validate(HttpServletRequest request);

    /**
     * 验证手机短信信息
     * @param mobile    手机号码
     * @param captcha   验证码
     * @param username   用户账号
     * @return
     */
    JsonResult validateSms(String mobile, String captcha, String username);
}
