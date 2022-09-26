
/**
 * <pre>
 *
 * 描述：sys_app实体类定义
 * 表:sys_app
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2019-12-22 20:41:51
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.system.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.common.constant.MBoolean;
import com.redxun.log.annotation.FieldDef;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

/**
 * @Description 应用授权菜单实体
 * @Author xtk
 * @Date 2021/12/3 10:51
 */
@Data
@TableName(value = "sys_app_auth_menu")
public class SysAppAuthMenu extends BaseExtEntity<String> {

    @JsonCreator
    public SysAppAuthMenu() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    /**
     * 应用ID
     */
    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_",jdbcType = JdbcType.VARCHAR)
    private String appId;

    /**
     * 用户ID
     */
    @FieldDef(comment = "应用ID")
    @TableField(value = "USER_ID_",jdbcType = JdbcType.VARCHAR)
    private String userId;

    /**
     * 菜单ID
     */
    @FieldDef(comment = "菜单ID")
    @TableField(value = "MENU_ID_",jdbcType = JdbcType.VARCHAR)
    private String menuId;

    /**
     * 是否启用
     */
    @FieldDef(comment = "是否启用：Y: 是, N: 否")
    @TableField(value = "ENABLE_",jdbcType = JdbcType.VARCHAR)
    private String enable = MBoolean.Y.val;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }
}



