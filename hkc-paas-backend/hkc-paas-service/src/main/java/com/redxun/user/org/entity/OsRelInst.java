package com.redxun.user.org.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.redxun.common.base.entity.BaseExtEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *  关系实例对象 os_rel_inst
 *
 * @author yjy
 * @date 2019-11-08
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_rel_inst")
public class OsRelInst extends BaseExtEntity<String> {
    private static final long serialVersionUID=1L;

    @TableId(value = "INST_ID_",type = IdType.INPUT)
    private String instId;

    /** 关系类型ID */
    @TableField(value = "REL_TYPE_ID_")
    private String relTypeId;

    /** 关系类型KEY_
             */
    @TableField(value = "REL_TYPE_KEY_")
    private String relTypeKey;

    /** 当前方ID */
    @TableField(value = "PARTY1_")
    private String party1;

    /** 关联方ID */
    @TableField(value = "PARTY2_")
    private String party2;

    /** 当前方维度ID */
    @TableField(value = "DIM1_")
    private String dim1;

    /** 关联方维度ID */
    @TableField(value = "DIM2_")
    private String dim2;

    /** IS_MAIN_ */
    @TableField(value = "IS_MAIN_")
    private String isMain;

    /** 状态
            ENABLED
            DISABLED */
    @TableField(value = "STATUS_")
    private String status;

    /** 别名 */
    @TableField(value = "ALIAS_")
    private String alias;

    /** 关系类型 */
    @TableField(value = "REL_TYPE_")
    private String relType;

    /** 路径 */
    @TableField(value = "PATH_")
    private String path;

    @TableField(exist = false)
    private String fullname;
    @TableField(exist = false)
    private String userNo;
    @TableField(exist = false)
    private String deptName;
    @TableField(exist = false)
    private String mobile;
    @TableField(exist = false)
    private String email;
    @TableField(exist = false)
    private List<OsRelInst> children;

    @Override
    public String getPkId() {
        return instId;
    }

    @Override
    public void setPkId(String pkId) {
        this.instId=pkId;
    }

}
