
/**
 * <pre>
 *
 * 描述：SYS_OFFICE_VER实体类定义
 * 表:SYS_OFFICE_VER
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-07 11:10:40
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
@TableName(value = "SYS_OFFICE_VER")
public class SysOfficeVer  extends BaseExtEntity<String> {

    @JsonCreator
    public SysOfficeVer() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //OFFICE主键
    @TableField(value = "OFFICE_ID_")
    private String officeId;
    //版本
    @TableField(value = "VERSION_")
    private Integer version;
    //附件ID
    @TableField(value = "FILE_ID_")
    private String fileId;
    //文件名
    @TableField(value = "FILE_NAME_")
    private String fileName;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



