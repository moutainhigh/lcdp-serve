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
 * 机构类型
 *
 * @author yjy
 * @date 2019-10-29 17:31:21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("os_inst_type")
public class OsInstType extends BaseExtEntity<String> {
    private static final long serialVersionUID=1L;


    @TableId(value = "TYPE_ID_",type = IdType.INPUT)
    private String typeId;

    @FieldDef(comment = "机构类型编码")
    @TableField(value = "TYPE_CODE_")
    private String typeCode;

    @FieldDef(comment = "机构类型名称")
    @TableField(value = "TYPE_NAME_")
    private String typeName;

    @TableField(value = "ENABLED_")
    private String enabled;

    @TableField(value = "IS_DEFAULT_")
    private String isDefault;

    @TableField(value = "HOME_URL_")
    private String homeUrl;

    @TableField(value = "DESCP_")
    private String descp;

    @Override
    public String getPkId() {
        return typeId;
    }

    @Override
    public void setPkId(String pkId) {
        this.typeId=pkId;
    }

}
