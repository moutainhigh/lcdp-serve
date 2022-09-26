package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 飞书用户信息
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
@Data
public class FeiShuUserAccessTokenInfo {
    /**
     * user_access_token，用于获取用户资源
     */
    @JSONField(name = "access_token")
    private String accessToken;
    /**
     * 用户头像
     */
    @JSONField(name = "avatar_url")
    private String avatarUrl;
    /**
     * 用户头像 72x72
     */
    @JSONField(name = "avatar_thumb")
    private String avatarThumb;
    /**
     * 用户头像 240x240
     */
    @JSONField(name = "avatar_middle")
    private String avatarMiddle;
    /**
     * 用户头像 640x640
     */
    @JSONField(name = "avatar_big")
    private String avatarBig;
    /**
     * access_token 的有效期，单位: 秒
     */
    @JSONField(name = "expires_in")
    private String expiresIn;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户英文名称
     */
    @JSONField(name = "en_name")
    private String enName;
    /**
     * 用户在应用内的唯一标识
     */
    @JSONField(name = "open_id")
    private String openId;
    /**
     * 用户统一ID
     */
    @JSONField(name = "union_id")
    private String unionId;
    /**
     * 申请了"获取用户邮箱"权限的应用返回该字段
     */
    private String email;
    /**
     * 申请了"获取用户 user_id"权限的应用返回该字段
     */
    @JSONField(name = "user_id")
    private String userId;
    /**
     * 申请了"获取用户手机号"权限的应用返回该字段
     */
    private String mobile;
    /**
     * 当前企业标识
     */
    @JSONField(name = "tenant_key")
    private String tenantKey;
    /**
     * refresh_token 的有效期，单位: 秒
     */
    @JSONField(name = "refresh_expires_in")
    private String refreshExpiresIn;
    /**
     * 刷新用户 access_token 时使用的 token
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;
    /**
     * 此处为 Bearer
     */
    @JSONField(name = "token_type")
    private String tokenType;
}
