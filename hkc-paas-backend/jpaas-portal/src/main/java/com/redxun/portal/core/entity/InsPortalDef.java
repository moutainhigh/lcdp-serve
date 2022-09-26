
/**
 * <pre>
 *
 * 描述：门户定义实体类定义
 * 表:ins_portal_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-26 22:42:38
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
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门户定义
 */
@Data
@Accessors(chain = true)
@TableName(value = "ins_portal_def")
public class InsPortalDef  extends BaseExtEntity<java.lang.String> {

    public static final String IS_NO_MOBILE="NO";
    public static final String IS_MOBILE="YES";
    public static final String IS_DEFAULT_="1";
    public static final String IS_NOT_DEFAULT_="0";

    @JsonCreator
    public InsPortalDef() {
    }

    //门户ID
    @TableId(value = "PORT_ID_",type = IdType.INPUT)
	private String portId;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "KEY_")
    private String key;
    //是否默认
    @TableField(value = "IS_DEFAULT_")
    private String isDefault;
    //布局HTML
    @TableField(value = "LAYOUT_HTML_")
    private String layoutHtml;
    //优先级
    @TableField(value = "PRIORITY_")
    private Integer priority;
    //是否默认
    @TableField(value = "IS_MOBILE_")
    private String isMobile;
    //门户布局
    @TableField(value = "LAYOUT_JSON_")
    private String layoutJson;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @Override
    public String getPkId() {
        return portId;
    }

    @Override
    public void setPkId(String pkId) {
        this.portId=pkId;
    }
}



