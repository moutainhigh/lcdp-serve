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
 * 用户组等级分类
 *
 * @author yjy
 * @date 2019-10-29 17:31:18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_rank_type")
public class OsRankType extends BaseExtEntity<String> {
    private static final long serialVersionUID=1L;

    @TableId(value = "RK_ID_",type = IdType.INPUT)
    private String rkId;

    @TableField(value = "DIM_ID_")
    private String dimId;

    @FieldDef(comment = "等级类型")
    @TableField(value = "NAME_")
    private String name;

    @TableField(value = "KEY_")
    private String key;

    @TableField(value = "LEVEL_")
    private Integer level;


    @Override
    public String getPkId() {
        return rkId;
    }

    @Override
    public void setPkId(String pkId) {
        this.rkId=pkId;
    }

}
