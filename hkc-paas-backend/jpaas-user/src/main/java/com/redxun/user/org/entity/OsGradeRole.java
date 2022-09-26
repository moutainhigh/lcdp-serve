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
 * 描述：分级管理员角色实体类定义
 * 表:os_grade_role
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-06-22 10:42:51
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_grade_role")
public class OsGradeRole  extends BaseExtEntity<String> {

    @JsonCreator
    public OsGradeRole() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //管理ID
    @TableField(value = "ADMIN_ID_")
    private String adminId;
    //角色ID
    @TableField(value = "GROUP_ID_")
    private String groupId;
    //角色名
    @TableField(value = "NAME_")
    private String name;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



