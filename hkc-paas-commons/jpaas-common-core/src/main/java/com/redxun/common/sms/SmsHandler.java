package com.redxun.common.sms;

import com.redxun.common.base.entity.JsonResult;

/**
 * 短信接口
 */
public interface SmsHandler {
    /**
     * 发送手机短信
     * @param mobile
     * @return
     */
    JsonResult sendPhoneValidCode(String mobile);


    /**
     * 发送流程审批业务
     * @param mobile
     * @return
     */
    void sendBpmApproval(String mobile,String[] params);
}
