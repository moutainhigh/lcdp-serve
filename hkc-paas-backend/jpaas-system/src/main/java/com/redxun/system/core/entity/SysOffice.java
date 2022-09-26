
/**
 * <pre>
 *
 * 描述：SYS_OFFICE实体类定义
 * 表:SYS_OFFICE
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
@TableName(value = "SYS_OFFICE")
public class SysOffice  extends BaseExtEntity<String> {
    public static String SUPPORT_VER="YES";

    public static String NOT_SUPPORT_VER="NO";

    @JsonCreator
    public SysOffice() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_")
    private String name;
    //支持版本
    @TableField(value = "SUPPORT_VERSION_")
    private String supportVersion;
    //版本
    @TableField(value = "VERSION_")
    private Integer version;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



