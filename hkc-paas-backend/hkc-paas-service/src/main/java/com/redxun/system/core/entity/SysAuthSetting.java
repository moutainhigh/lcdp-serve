
/**
 * <pre>
 *
 * 描述：权限配置表实体类定义
 * 表:sys_auth_setting
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-14 17:03:25
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

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_auth_setting")
public class SysAuthSetting  extends BaseExtEntity<String> {

    @JsonCreator
    public SysAuthSetting() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //权限名称
    @FieldDef(comment = "权限名称")
    @TableField(value = "NAME_")
    private String name;
    //是否启用
    @TableField(value = "ENABLE_")
    private String enable;
    //类型
    @TableField(value = "TYPE_")
    private String type;
    //权限JSON
    @TableField(value = "RIGHT_JSON_")
    private String rightJson;

    @TableField(exist = false)
    private List<SysAuthRights> sysAuthRights;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



