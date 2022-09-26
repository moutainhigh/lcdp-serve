
/**
 * <pre>
 *
 * 描述：布局权限设置实体类定义
 * 表:ins_portal_permission
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-26 22:42:55
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
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 门户权限
 */
@Data
@Accessors(chain = true)
@TableName(value = "ins_portal_permission")
public class InsPortalPermission  extends BaseExtEntity<java.lang.String> {

    public static final String ALL_TYPE="ALL";
    public static final String SUB_GROUP_TYPE="subGroup";
    public static final String USER_TYPE="user";
    public static final String GROUP_TYPE="group";
    @JsonCreator
    public InsPortalPermission() {
    }

    //权限ID
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //门户ID
    @TableField(value = "LAYOUT_ID_")
    private String layoutId;
    //类型
    @TableField(value = "TYPE_")
    private String type;
    //用户或组ID
    @TableField(value = "OWNER_ID_")
    private String ownerId;
    //用户或组ID
    @TableField(value = "OWNER_NAME_")
    private String ownerName;
    //菜单类型
    @TableField(value = "MENU_TYPE_")
    private String menuType;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



