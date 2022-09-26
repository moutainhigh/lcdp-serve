
/**
 * <pre>
 *
 * 描述：权限转移设置表实体类定义
 * 表:SYS_TRANSFER_SETTING
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "SYS_TRANSFER_SETTING")
public class SysTransferSetting extends BaseExtEntity<String> {

    @JsonCreator
    public SysTransferSetting() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    /**
     * 名称
     */
    @TableField(value = "NAME_")
    private String name;
    /**
     * 状态
     */
    @TableField(value = "STATUS_")
    private String status;
    /**
     * 数据源别名
     */
    @TableField(value = "DS_ALIAS_")
    private String dsAlias;
    /**
     * ID字段
     */
    @TableField(value = "ID_FIELD_")
    private String idField;
    /**
     * NAME字段
     */
    @TableField(value = "NAME_FIELD_")
    private String nameField;
    /**
     * SELECTSQL语句
     */
    @TableField(value = "SELECT_SQL_")
    private String selectSql;
    /**
     * UPDATESQL语句
     */
    @TableField(value = "UPDATE_SQL_")
    private String updateSql;
    /**
     * 日志内容模板
     */
    @TableField(value = "LOG_TEMPLET_")
    private String logTemplet;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }
}



