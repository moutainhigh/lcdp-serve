package com.redxun.util.feishu.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author weiwei.chen
 * @version V1.0
 * @since 2021-05-10 16:11
 */
@Data
public class FeiShuTokenReq implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 第三方应用的appId
     */
    @JSONField(name = "app_id")
    private String appId;

    /**
     * 第三方应用的appSecret
     */
    @JSONField(name = "app_secret")
    private String appSecret;
}
