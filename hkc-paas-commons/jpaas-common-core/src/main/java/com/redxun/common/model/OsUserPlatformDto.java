package com.redxun.common.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 功能: 用户平台DTO映射
 *
 * @author ray
 * @date 2022/7/4 14:09
 */
@Setter
@Getter
public class OsUserPlatformDto {

    /**
     * 公众号
     */
    public static Integer TYPE_WECHAT=1;

    /**
     * 企业微信
     */
    public static Integer TYPE_WEIXIN=2;

    /**
     * 钉钉
     */
    public static Integer TYPE_DD=3;

    /**
     * 飞书
     */
    public static Integer TYPE_FEISHU=4;

    public OsUserPlatformDto(){
    }

    public OsUserPlatformDto(Integer platformType, String openId){
    }

    /**
     * 平台类型。
     * 第三方平台类型：1微信公众号，2企业微信，3钉钉，4飞书
     */
    private Integer platformType;
    /**
     * 第三方平台唯一id
     */
    private String openId;

}
