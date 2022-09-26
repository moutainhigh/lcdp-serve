
/**
 * <pre>
 *
 * 描述：SYS_OFFICE_TEMPLATE实体类定义
 * 表:SYS_OFFICE_TEMPLATE
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-07-16 13:51:54
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
@TableName(value = "SYS_OFFICE_TEMPLATE")
public class SysOfficeTemplate  extends BaseExtEntity<String> {

    @JsonCreator
    public SysOfficeTemplate() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //类型(normal,red)
    @FieldDef(comment = "类型")
    @TableField(value = "TYPE_")
    private String type;
    //文档ID
    @TableField(value = "DOC_ID_")
    private String docId;
    //文件名
    @TableField(value = "DOC_NAME_")
    private String docName;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



