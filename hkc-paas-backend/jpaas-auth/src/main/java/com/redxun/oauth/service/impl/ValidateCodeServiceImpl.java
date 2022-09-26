package com.redxun.oauth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.redxun.api.org.IUserService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.redis.template.RedisRepository;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.utils.SmsVerificationUtil;
import com.redxun.oauth.exception.ValidateCodeException;
import com.redxun.oauth.service.IValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yjy
 * @date 2018/12/10
 */
@Slf4j
@Service
public class ValidateCodeServiceImpl implements IValidateCodeService {
    @Autowired
    private RedisRepository redisRepository;

    @Resource
    private IUserService userService;

    @Resource
    private SmsVerificationUtil smsVerificationUtil;

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param deviceId 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String deviceId, String imageCode) {
        redisRepository.setExpire(buildKey(deviceId), imageCode, SecurityConstants.DEFAULT_IMAGE_EXPIRE);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public JsonResult sendSmsCode(String mobile)  {
        Object tempCode = redisRepository.get(buildKey(mobile));
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return JsonResult.getFailResult("验证码未失效，请失效后再次申请");
        }

        JPaasUser user = userService.findByMobile(mobile);
        if (user == null) {
            log.error("根据用户手机号{}查询用户为空", mobile);
            return JsonResult.getFailResult("手机号不存在");
        }
        JsonResult jsonResult = null;
        try {
            jsonResult = smsVerificationUtil.getSmsByKey("qcloud").sendPhoneValidCode(mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code=(String)jsonResult.getData();
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
        redisRepository.setExpire(buildKey(mobile), code, SecurityConstants.DEFAULT_IMAGE_EXPIRE);
        return jsonResult.setData(null).setShow(false);
    }

    /**
     * 获取验证码
     * @param deviceId 前端唯一标识/手机号
     */
    @Override
    public String getCode(String deviceId) {
        return (String)redisRepository.get(buildKey(deviceId));
    }

    /**
     * 删除验证码
     * @param deviceId 前端唯一标识/手机号
     */
    @Override
    public void remove(String deviceId) {
        redisRepository.del(buildKey(deviceId));
    }

    /**
     * 验证验证码
     */
    @Override
    public void validate(HttpServletRequest request) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求参数中携带deviceId参数");
        }
        String code = this.getCode(deviceId);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request, "validCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("请填写验证码");
        }

        if (code == null) {
            throw new ValidateCodeException("验证码不存在或已过期");
        }

        if (!StringUtils.equals(code, codeInRequest.toLowerCase())) {
            throw new ValidateCodeException("验证码不正确");
        }

        this.remove(deviceId);
    }

    @Override
    public JsonResult validateSms(String mobile, String captcha, String username) {
        if(StringUtils.isBlank(username)){
            log.error("用户名{}为空", username);
            return JsonResult.getFailResult("用户名为空");
        }
        JPaasUser user = userService.findByUsername(username);
        if (user == null) {
            log.error("用户{}不存在", username);
            return JsonResult.getFailResult("用户不存在");
        }else{
            if(StringUtils.isBlank(user.getMobile())){
                log.error("用户{}绑定的手机号码为空", username);
                return JsonResult.getFailResult("用户绑定的手机号码为空");
            }else{
                if(!user.getMobile().trim().equals(mobile)){
                    log.error("用户{}绑定的手机号{}与输入的手机号不一致", username, mobile);
                    return JsonResult.getFailResult("用户绑定的手机号与输入的手机号不一致");
                }
            }

        }

        Object tempCode = redisRepository.get(buildKey(mobile));
        if (tempCode == null) {
            log.error("手机号码:{}验证码{}已失效", mobile, captcha);
            return JsonResult.getFailResult("验证码已失效，请再次申请");
        }
        if(!tempCode.toString().equals(captcha)){
            log.error("手机号码:{}验证码{}不正确", mobile, captcha);
            return JsonResult.getFailResult("验证码不正确");
        }

        redisRepository.del(buildKey(mobile));
        return JsonResult.getSuccessResult("验证码正确");
    }

    private String buildKey(String deviceId) {
        return SecurityConstants.DEFAULT_CODE_KEY + ":" + deviceId;
    }
}
