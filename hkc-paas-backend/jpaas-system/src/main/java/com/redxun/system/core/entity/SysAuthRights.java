
/**
 * <pre>
 *
 * 描述：权限定义表实体类定义
 * 表:sys_auth_rights
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-14 17:01:07
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_auth_rights")
public class SysAuthRights  extends BaseExtEntity<String> {
    public static final String PERMISSION_EVERYONE="everyone";
    public static final String PERMISSION_NONE="none";
    public static final String PERMISSION_CUSTOM="custom";


    @JsonCreator
    public SysAuthRights() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //分类ID
    @TableField(value = "TREE_ID_")
    private String treeId;
    //分类名称
    @TableField(value = "TREE_NAME_")
    private String treeName;
    //权限配置ID
    @TableField(value = "SETTING_ID_")
    private String settingId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



