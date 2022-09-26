
/**
 * <pre>
 *
 * 描述：sys_excel实体类定义
 * 表:sys_excel
 * 作者：Ventus
 * 邮箱: hzh@redxun.cn
 * 日期:2020-11-27 11:14:29
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
import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "sys_excel")
public class ImportExcel  extends BaseExtEntity<String> {

    @JsonCreator
    public ImportExcel() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //备注
    @TableField(value = "COMMENT_")
    private String comment;

    //映射表内容
    @TableField(value = "GRID_DATA_")
    private String gridData;
    //Excel表内容
    @TableField(value = "FIELD_")
    private String field;
    //模板别名
    @TableField(value = "KEY_")
    private String key;
    //模板名称
    @TableField(value = "NAME_")
    private String name;

    //模板ID
    @TableField(value = "TEMPLATE_ID_")
    private String templateId;




    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



