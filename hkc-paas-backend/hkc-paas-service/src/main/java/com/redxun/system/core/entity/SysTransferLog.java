
/**
 * <pre>
 *
 * 描述：权限转移日志表实体类定义
 * 表:SYS_TRANSFER_LOG
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-21 14:15:51
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

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_TRANSFER_LOG")
public class SysTransferLog extends BaseExtEntity<String> {

    @JsonCreator
    public SysTransferLog() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    /**
     * 操作描述
     */
    @FieldDef(comment = "操作描述")
    @TableField(value = "OP_DESCP_")
    private String opDescp;
    /**
     * 权限转移人
     */
    @TableField(value = "AUTHOR_PERSON_")
    private String authorPerson;
    /**
     * 目标转移人
     */
    @TableField(value = "TARGET_PERSON_")
    private String targetPerson;

    @TableField(exist = false)
    private String authorPersonName;

    @TableField(exist = false)
    private String targetPersonName;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }
}



