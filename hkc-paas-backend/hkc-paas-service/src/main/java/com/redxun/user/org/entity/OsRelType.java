package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redxun.common.base.entity.BaseExtEntity;
import com.redxun.log.annotation.FieldDef;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *  关系类型定义对象 os_rel_type
 *
 * @author yjy
 * @date 2019-11-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_rel_type")
public class OsRelType extends BaseExtEntity<String> {
    private static final long serialVersionUID=1L;

    /**
     * 关系类型。用户关系=USER-USER；
     */
    public final static String REL_TYPE_USER_USER="USER-USER";
    /**
     * 关系类型，用户组关系=GROUP-GROUP；
     */
    public final static String REL_TYPE_GROUP_GROUP="GROUP-GROUP";
    /**
     * 关系类型，组与用户关系=GROUP-USER
     */
    public final static String REL_TYPE_GROUP_USER="GROUP-USER";
    /**
     * 关系类型，组关系与用户的关系=REL_USER
     */
    public final static String REL_TYPE_REL_USER="REL_USER";

    /**
     * 用户与组的从属关系=GROUP-USER-BELONG
     */
    public final static String REL_CAT_GROUP_USER_BELONG="GROUP-USER-BELONG";
    /**
     * OS_REL_TYPE_表中的从属关系定义主键，由初始化数据初始化 ID=1
     */
    public final static String REL_CAT_GROUP_USER_BELONG_ID="1";
    /**
     * 用户与组的组负责人关系=GROUP-USER-LEADER
     */
    public final static String REL_CAT_GROUP_USER_LEADER="GROUP-USER-LEADER";
    /**
     * OS_REL_TYPE_表中的组内负责人定义主键，由初始化数据初始化 ID=2
     */
    public final static String REL_CAT_GROUP_USER_LEADER_ID="2";
    /**
     * 用户与组的上下级关系=USER-UP-LOWER
     */
    public final static String REL_CAT_USER_UP_LOWER="USER-UP-LOWER";
    /**
     * OS_REL_TYPE_表中的用户与组的上下级关系定义主键，由初始化数据初始化 ID=3
     */
    public final static String REL_CAT_USER_UP_LOWER_ID="3";

    /**
     * 部门下的职位关系，我们称之为岗位=GROUP-DEP-POS
     */
    public final static String REL_CAT_DEP_POS="GROUP-DEP-POS";
    /**
     * OS_REL_TYPE_部门下的职位关系（岗位）定义主键，由初始化数据初始化 ID=4
     */
    public final static String REL_CAT_DEP_POS_ID="4";
    /**
     * 两个组关系下的用户关系，我们称之为组关系用户=REL-USER-BELONG
     */
    public final static String REL_CAT_REL_USER="REL-USER-BELONG";
    /**
     * OS_REL_TYPE_用户组之间关系下的用户定义主键，由初始化数据初始化 ID=5
     */
    public final static String REL_CAT_REL_USER_ID="5";

    /**
     * 关系约束-一对一
     */
    public final static String CONST_TYPE_ONE_ONE="ONE-ONE";
    /**
     * 关系约束-一对多
     */
    public final static String CONST_TYPE_ONE_MANY="ONE-MANY";
    /**
     * 关系约束-多对一
     */
    public final static String CONST_TYPE_MANY_ONE="MANY-ONE";
    /**
     * 关系约束-多对多
     */
    public final static String CONST_TYPE_MANY_MANY="MANY-MANY";

    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;

    /** 关系名 */
    @FieldDef(comment = "关系名")
    @TableField(value = "NAME_")
    private String name;

    /** 关系业务主键 */
    @FieldDef(comment = "关系编码")
    @TableField(value = "KEY_")
    private String key;

    /** 关系类型。用户关系=USER-USER；用户组关系=GROUP-GROUP；用户与组关系=USER-GROUP；组与用户关系=GROUP-USER */
    @TableField(value = "REL_TYPE_")
    private String relType;

    /** 关系约束类型。1对1=one2one；1对多=one2many；多对1=many2one；多对多=many2many */
    @TableField(value = "CONST_TYPE_")
    private String constType;

    /** 关系当前方名称 */
    @TableField(value = "PARTY1_")
    private String party1;

    /** 关系关联方名称 */
    @TableField(value = "PARTY2_")
    private String party2;

    /** 当前方维度ID（仅对用户组关系） */
    @TableField(value = "DIM_ID1_")
    private String dimId1;

    /** 关联方维度ID（用户关系忽略此值） */
    @TableField(value = "DIM_ID2_")
    private String dimId2;

    /** 等级 */
    @TableField(value = "LEVEL_")
    private Long level;

    /** 状态。actived 已激活；locked 锁定；deleted 已删除 */
    @TableField(value = "STATUS_")
    private String status;

    /** 是否系统预设 */
    @TableField(value = "IS_SYSTEM_")
    private String isSystem;

    /** 是否默认 */
    @TableField(value = "IS_DEFAULT_")
    private String isDefault;

    /** 是否是双向 */
    @TableField(value = "IS_TWO_WAY_")
    private String isTwoWay;

    /** 关系备注 */
    @TableField(value = "MEMO_")
    private String memo;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

}
