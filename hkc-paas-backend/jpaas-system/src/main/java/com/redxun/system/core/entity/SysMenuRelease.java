
/**
 * <pre>
 *
 * 描述：发布菜单路径记录表实体类定义
 * 表:SYS_MENU_RELEASE
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-09-02 17:01:10
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
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_MENU_RELEASE")
public class SysMenuRelease  extends BaseExtEntity<String> {

    @JsonCreator
    public SysMenuRelease() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //发布ID
    @TableField(value = "RELEASE_ID_")
    private String releaseId;
    //菜单ID
    @TableField(value = "MENU_ID_")
    private String menuId;
    //菜单名称
    @FieldDef(comment = "菜单名称")
    @TableField(value = "MENU_NAME_")
    private String menuName;
    //发布路径
    @TableField(value = "RELEASE_URL_")
    private String releaseUrl;
    //当前路径
    @TableField(value = "MENU_URL_")
    private String menuUrl;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



