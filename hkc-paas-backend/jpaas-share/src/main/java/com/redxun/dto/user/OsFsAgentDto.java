
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
 * 飞书应用配置实体DTO
 */
@Data
public class OsFsAgentDto extends BaseDto {
    public static final String SAVE_DEPLOY = "saveDeploy";
    //主键
    private String id;
    //应用名称
    private String name;
    //应用ID
    private String appId;
    //密钥
    private String secret;
    //PC主页
    private String pcHomepage;
    //后台主页
    private String adminPage;
    //H5主页
    private String h5Homepage;
    //是否默认
    private Integer isDefault;
    //是否推送部门和用户到飞书
    private Integer isPush;


}



