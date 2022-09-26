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

import java.util.List;


/**
 * <pre>
 *
 * 描述：分级管理员实体类定义
 * 表:os_grade_admin
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-06-22 10:42:51
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "os_grade_admin")
public class OsGradeAdmin  extends BaseExtEntity<String> {

    @JsonCreator
    public OsGradeAdmin() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //分组ID
    @TableField(value = "GROUP_ID_")
    private String groupId;
    //管理员ID
    @TableField(value = "USER_ID_")
    private String userId;
    //管理员名称
    @TableField(value = "FULLNAME_")
    private String fullname;
    //父ID
    @TableField(value = "PARENT_ID_")
    private String parentId;
    //层次
    @TableField(value = "DEPTH_")
    private Integer depth;
    //路径
    @TableField(value = "PATH_")
    private String path;
    //序号
    @TableField(value = "SN_")
    private Integer sn;
    //子节点数
    @TableField(value = "CHILDS_")
    private Integer childs;

    @TableField(exist = false)
    private String roleName;

    @TableField(exist = false)
    private List<OsGradeRole> osGradeRoles;

    @TableField(exist = false)
    private String userNo;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



