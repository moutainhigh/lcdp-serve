package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 飞书用户信息参数
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuUserAccessTokenInfoReq {
    /**
     * 授权类型，如："authorization_code"
     */
    @JSONField(name = "grant_type")
    private String grantType = "authorization_code";
    /**
     * 来自请求身份验证流程，用户扫码登录后会自动302到redirect_uri并带上此参数
     */
    private String code;

}
