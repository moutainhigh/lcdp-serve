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
 * 描述：租户关联用户表实体类定义
 * 表:OS_INST_USERS
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-10-23 11:36:05
 * 版权：广州红迅软件
 * </pre>
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "OS_INST_USERS")
public class OsInstUsers  extends BaseExtEntity<String> {

    @JsonCreator
    public OsInstUsers() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //用户ID
    @TableField(value = "USER_ID_")
    private String userId;
    //是否超管
    @TableField(value = "IS_ADMIN_")
    private Integer isAdmin;
    //用户类型
    @TableField(value = "USER_TYPE_")
    private String userType;
    //状态
    @TableField(value = "STATUS_")
    private String status;
    //审批用户
    @TableField(value = "APPROVE_USER_")
    private String approveUser;
    //创建类型
    @TableField(value = "CREATE_TYPE_")
    private String createType;
    //申请状态
    @TableField(value = "APPLY_STATUS_")
    private String applyStatus;
    //申请理由
    @TableField(value = "APPLY_NOTE_")
    private String applyNote;

    //申请人姓名
    @TableField(exist = false)
    private String usFullName;
    //审批结果：1：同意 0：拒绝
    @TableField(exist = false)
    private String isAgree;
    //用户主部门
    @TableField(exist = false)
    private String mainDepId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



