
/**
 * <pre>
 *
 * 描述：ins_app_collect实体类定义
 * 表:ins_app_collect
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-29 16:45:37
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.portal.core.entity;

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
 * 常用应用
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "ins_app_collect")
public class InsAppCollect  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public InsAppCollect() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //应用名称
    @FieldDef(comment = "应用名称")
    @TableField(value = "NAME_")
    private String name;
    //应用链接地址
    @TableField(value = "URL_")
    private String url;
    //类型 内部：interior 外部：outside
    @TableField(value = "TYPE_")
    private String type;
    //用户或组ID
    @TableField(value = "OWNER_ID_")
    private String ownerId;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;
    //序号
    @TableField(value = "SN_")
    private Integer sn;
    //图标
    @TableField(value = "ICON_")
    private String icon;
    //绑定的内部应用
    @TableField(value = "APP_")
    private String app;
    //应用 default custom
    @TableField(value = "APP_TYPE_")
    private String appType;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



