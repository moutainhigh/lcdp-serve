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
public class FeiShuAppToken implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * app_access_token
     */
    @JSONField(name = "app_access_token")
    private String appAccessToken;
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
