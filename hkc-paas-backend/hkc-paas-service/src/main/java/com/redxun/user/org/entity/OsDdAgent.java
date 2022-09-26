package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <pre>
 *
 * 描述：os_dd_agent实体类定义
 * 表:os_dd_agent
 * 作者：gjh
 * 邮箱: zyg@redxun.cn
 * 日期:2021-02-07 11:15:52
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_dd_agent")
public class OsDdAgent  extends BaseExtEntity<String> {

    @JsonCreator
    public OsDdAgent() {
    }

    //ID
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    /**
     * 应用名称
     */
    @TableField(value = "NAME_")
    private String name;

    /**
     * 企业ID
     */
    @TableField(value = "CORP_ID_")
    private String corpId;

    /**
     * 应用ID
     */
    @TableField(value = "AGENT_ID_")
    private String agentId;

    @TableField(value = "APP_KEY_")
    private String appKey;
    /**
     * 默认应用
     */
    @TableField(value = "IS_DEFAULT_")
    private Integer isDefault;

    /**
     * 应用密钥
     */
    @TableField(value = "SECRET_")
    private String secret;
    /**
     * 应用类型
     */
    @TableField(value = "TYPE_")
    private String type;


    @TableField(value = "PC_HOMEPAGE_")
    private String pcHomePage;

    @TableField(value = "H5_HOMEPAGE_")
    private String h5HomePage;

    @TableField(value = "ADMIN_PAGE_")
    private String adminHomePage;




    @Override
    public String getPkId() {
        return this.id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }




}



