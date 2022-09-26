package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_wx_ent_agent")
public class OsWxEntAgent extends BaseExtEntity<String> {
    public static final String SAVE_DEPLOY = "saveDeploy";

    @JsonCreator
    public OsWxEntAgent() {
    }

    //主键
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //备注
    @TableField(value = "DESCRIPTION_")
    private String description;
    //信任域名
    @TableField(value = "DOMAIN_")
    private String domain;
    //主页地址
    @TableField(value = "HOME_URL_")
    private String homeUrl;
    //企业ID
    @TableField(value = "CORP_ID_")
    private String corpId;
    //应用ID
    @TableField(value = "AGENT_ID_")
    private String agentId;
    //密钥
    @TableField(value = "SECRET_")
    private String secret;
    //是否默认
    @TableField(value = "DEFAULT_AGENT_")
    private Integer defaultAgent;

    /**
     * 保存，保存发布
     */
    @TableField(exist = false)
    private String action="";

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }
}



