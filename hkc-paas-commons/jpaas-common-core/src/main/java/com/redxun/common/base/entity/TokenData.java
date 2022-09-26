package com.redxun.common.base.entity;

import lombok.Data;

/**
 * 获取Token的信息对象
 */
@Data
public class TokenData {
    /**
     * 应用ID
     */
    private String clientId;

    /**
     * 应用密钥
     */
    private String clientSecret;

    /**
     * TOKEN
     */
    private String token;
}
