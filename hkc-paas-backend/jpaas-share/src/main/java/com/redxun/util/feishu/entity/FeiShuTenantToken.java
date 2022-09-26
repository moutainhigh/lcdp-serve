package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ycs
 * @version V1.0
 * @since 2022-06-10 16:13
 */
@Data
public class FeiShuTenantToken implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * tenant_access_token
     */
    @JSONField(name = "tenant_access_token")
    private String tenantAccessToken;
    /**
     * 过期时间
     */
    private Integer expire;
    /**
     * code
     */
    private Integer code;
    /**
     * msg
     */
    private String msg;
}
