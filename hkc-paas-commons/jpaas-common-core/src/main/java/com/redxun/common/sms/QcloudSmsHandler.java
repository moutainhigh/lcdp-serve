package com.redxun.common.sms;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.config.SysConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.Random;

/**
 * 腾讯云短信接口
 */
@Component(value = "qcloud")
public class QcloudSmsHandler implements SmsHandler {
    @Resource
    SysConfig sysConfig;

    /**
     * 验证码发送
     *
     * @param mobile
     * @return
     */
    @Override
    public JsonResult sendPhoneValidCode(String mobile) {
        //验证码
        String str = "";
        //随机生成6位的验证码
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str += random.nextInt(10);
        }
        String[] params = {str, "2"};
        JsonResult jsonResult = sendSms(mobile, params);
        jsonResult.setData(str);
        return jsonResult;
    }


    /**
     * 发送流程审批业务
     *
     * @param mobile
     * @param params
     */
    @Override
    public void sendBpmApproval(String mobile, String[] params) {
        sendSms(mobile, params);
    }


    private JsonResult sendSms(String mobile, String[] params) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        String message = "";
        //腾讯短信应用的 SDK AppID
        int appid = 0;
        String appkey = null;
        int templateId = 0;
        String smsSign = null;
        try {
            //短信应用SDK AppID
            appid = Integer.valueOf(sysConfig.getVal("smsAppId"));
            //短信应用SDK AppKey
            appkey = sysConfig.getVal("smsAppKey");
            //短信模板ID，需要在短信应用中申请
            templateId = Integer.valueOf(sysConfig.getVal("smsTemplateId"));
            //真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID
            smsSign = sysConfig.getVal("smsSign");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            // 签名参数未提供或者为空时，会使用默认签名发送短信，这里的13800138000是为用户输入的手机号码
            SmsSingleSenderResult result = ssender.sendWithParam("86", mobile, templateId, params, smsSign, "", "");
            if (result.result == 0) {
                jsonResult.setSuccess(true);
            }
        } catch (HTTPException e) {
            message = e.getMessage();
        } catch (JSONException e) {
            message = e.getMessage();
        } catch (IOException e) {
            message = e.getMessage();
        } catch (com.github.qcloudsms.httpclient.HTTPException e) {
            message = e.getMessage();
        }
        jsonResult.setMessage(message);

        return jsonResult;
    }
}
