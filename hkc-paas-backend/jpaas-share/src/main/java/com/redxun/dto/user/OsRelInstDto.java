package com.redxun.dto.user;

import com.redxun.common.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 关系实例DTO
 * 参才jpaas-user中的OsRelInst类
 */
@Getter
@Setter
public class OsRelInstDto extends BaseDto {
    private String instId;

    /** 关系类型ID */
    private String relTypeId;

    /** 关系类型KEY_
     */
    private String relTypeKey;

    /** 当前方ID */
    private String party1;

    /** 关联方ID */
    private String party2;

    /** 当前方维度ID */
    private String dim1;

    /** 关联方维度ID */
    private String dim2;

    /** IS_MAIN_ */
    private String isMain;

    /** 状态
     ENABLED
     DISABLED */
    private String status;

    /** 别名 */
    private String alias;

    /** 关系类型 */
    private String relType;

    /** 路径 */
    private String path;
}
