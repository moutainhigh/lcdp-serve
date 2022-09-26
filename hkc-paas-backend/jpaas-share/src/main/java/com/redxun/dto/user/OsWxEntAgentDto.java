
/**
 * <pre>
 *
 * 描述：企业微信应用表实体类定义
 * 表:os_wx_ent_agent
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-22 11:32:25
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.dto.user;

import com.redxun.common.dto.BaseDto;
import lombok.Data;

/**
 * 企业微信实体DTO
 */
@Data
public class OsWxEntAgentDto extends BaseDto {
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
    //企业ID
    private String corpId;
    //应用ID
    private String agentId;
    //密钥
    private String secret;
    //是否默认
    private Integer defaultAgent;


}



