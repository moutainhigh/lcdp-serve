package com.redxun.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.oauth.service.IValidateCodeService;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码提供
 * @author yjy
 * @date 2018/12/18
 */
@Controller
public class ValidateCodeController {
    @Autowired
    private IValidateCodeService validateCodeService;

    /**
     * 创建验证码
     *
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{deviceId}")
    public void createCode(@PathVariable(name = "deviceId") String deviceId, HttpServletResponse response) throws Exception {
        Assert.notNull(deviceId, "机器码不能为空");
        // 设置请求头为输出图片类型
        CaptchaUtil.setHeader(response);

        response.setHeader("blob", "true");

        // 三个参数分别为宽、高、位数
        GifCaptcha gifCaptcha = new GifCaptcha(100, 35, 4);
        // 设置类型：字母数字混合
        gifCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        // 保存验证码
        validateCodeService.saveImageCode(deviceId, gifCaptcha.text().toLowerCase());
        // 输出图片流
        gifCaptcha.out(response.getOutputStream());
    }

    /**
     * 发送手机验证码
     * 后期要加接口限制
     *
     * @param mobile 手机号
     * @return R
     */
    @ResponseBody
    @GetMapping(SecurityConstants.MOBILE_VALIDATE_CODE_URL_PREFIX + "/{mobile}")
    public JsonResult createCode(@PathVariable(value = "mobile") String mobile) {
        Assert.notNull(mobile, "手机号不能为空");
        return validateCodeService.sendSmsCode(mobile);
    }

    /**
     * 获取验证码的配置
     * @return
     */
    @ResponseBody
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_CONFIG_PREFIX)
    public JsonResult getValidCodeConfig(){
        Boolean ingoreValideCode= SysPropertiesUtil.getBoolean("ignoreValidCode");
        return new JsonResult(true,ingoreValideCode,null);
    }

    /**
     * 获了以登录的相关配置参数
     * @return
     */
    @ResponseBody
    @GetMapping(SecurityConstants.LOGIN_CONFIG_URL)
    public JsonResult getLoginConfig(){
        //是否忽略验证码
        String ignoreValidCode= SysPropertiesUtil.getString("ignoreValidCode");
        //应用名称
        String appName = SysPropertiesUtil.getString("appName");

        JSONObject loginObj=new JSONObject();
        loginObj.put("ignoreValidCode",ignoreValidCode);
        loginObj.put("appName",appName);
        return new JsonResult(true,loginObj,null);
    }
}
