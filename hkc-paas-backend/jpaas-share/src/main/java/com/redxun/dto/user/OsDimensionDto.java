package com.redxun.dto.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.redxun.common.dto.BaseDto;
import lombok.Data;

import java.util.Objects;

/**
 * 系统用户维度DTO
 */
@Data
public class OsDimensionDto extends BaseDto{

    private String dimId;

    /** 维度名称 */
    private String name;

    /** 维度业务主键 */
    private String code;

    /** 是否系统预设维度 */
    private String isSystem;

    /** 状态 */
    private String status;

    /** 排序号 */
    private Long sn;

    /** 数据展示类型 */
    private String showType;

    /** 是否参与授权 */
    private String isGrant;

    /** 描述 */
    private String desc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OsDimensionDto dto = (OsDimensionDto) o;
        return Objects.equals(dimId, dto.dimId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dimId);
    }
}
