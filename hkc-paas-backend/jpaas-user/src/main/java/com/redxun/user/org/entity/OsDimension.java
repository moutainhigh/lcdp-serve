package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *  用户组维度对象 os_dimension
 *
 * @author yjy
 * @date 2019-11-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_dimension")
public class OsDimension extends BaseExtEntity<String> {

    private static final long serialVersionUID=1L;

    /**
     * 岗位维度=_POS
     */
    public static final String DIM_POS="_POS";
    /**
     * 职务维度=_JOB
     */
    public static final String DIM_JOB="_JOB";

    /**
     * 职务维度ID=3
     */
    public static final String DIM_JOB_ID="3";
    /**
     * 行政维度=_ADMIN
     */
    public static final String DIM_ADMIN="_ADMIN";
    /**
     * 行政维度ID=1
     */
    public static final String DIM_ADMIN_ID="1";

    /**
     * 角色维度=_ROLE
     */
    public static final String DIM_ROLE="_ROLE";

    /**
     * 角色维度ID=2
     */
    public static final String DIM_ROLE_ID="2";

    /**
     * 维度下的用户组数据为树型结构=TREE
     */
    public static final String SHOW_TYPE_TREE="TREE";
    /**
     * 维度下的用户组数据为平铺结构=FLAT
     */
    public static final String SHOW_TYPE_FLAT="FLAT";

    @TableId(value = "DIM_ID_",type = IdType.INPUT)
    private String dimId;

    /** 维度名称 */
    @TableField(value = "NAME_")
    private String name;

    /** 维度业务主键 */
    @TableField(value = "CODE_")
    private String code;

    /** 是否系统预设维度 */
    @TableField(value = "IS_SYSTEM_")
    private String isSystem;

    /** 状态 */
    @TableField(value = "STATUS_")
    private String status;

    /** 排序号 */
    @TableField(value = "SN_")
    private Long sn;

    /** 数据展示类型 */
    @TableField(value = "SHOW_TYPE_")
    private String showType;

    /** 是否参与授权 */
    @TableField(value = "IS_GRANT_")
    private String isGrant;

    /** 描述 */
    @TableField(value = "DESC_")
    private String desc;





    @TableField(exist = false)
    private Long parentId=0L;


    @Override
    public String getPkId() {
        return dimId;
    }

    @Override
    public void setPkId(String pkId) {
        this.dimId=pkId;
    }

}
