package com.redxun.dto.user;

/**
 * @Description:钉钉应用配置信息
 * @Author: Elwin ZHANG
 * @Date: 2021/4/23 14:18
 **/
import com.baomidou.mybatisplus.annotation.TableField;
import com.redxun.common.dto.BaseDto;
import lombok.Data;

@Data
public class OsDdAgentDto extends BaseDto{
    public static final String SAVE_DEPLOY = "saveDeploy";
    private String id;

    //名称
    private String name;
    //备注
    private String description;
    //信任域名
    private String domain;
    //主页地址
    private String homeUrl;
    //钉钉企业ID
    private String corpId;
    //应用ID
    private String agentId;

    private String appKey;
    //密钥
    private String secret;
    //是否默认
    private Integer isDefault;

    /**
     * 应用类型
     */
    private String type;

    private String pcHomePage;

    private String h5HomePage;

}
