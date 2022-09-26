
/**
 * <pre>
 *
 * 描述：ureport_file实体类定义
 * 表:ureport_file
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-08-12 09:30:12
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.ureport.core.entity;

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
@TableName(value = "ureport_file")
public class UreportFile  extends BaseExtEntity<String> {

    @JsonCreator
    public UreportFile() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //报表名称
    @TableField(value = "NAME_")
    private String name;
    //报表内容
    @TableField(value = "CONTENT_")
    private byte[] content;
    //分类ID
    @TableField(value = "CATEGORY_ID_")
    private String categoryId;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



