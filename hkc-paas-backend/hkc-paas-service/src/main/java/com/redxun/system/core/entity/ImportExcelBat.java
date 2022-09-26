
/**
 * <pre>
 *
 * 描述：sys_excel_batmanage实体类定义
 * 表:sys_excel_batmanage
 * 作者：Ventus
 * 邮箱: hzh@redxun.cn
 * 日期:2020-11-27 11:15:30
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
@TableName(value = "sys_excel_batmanage")
public class ImportExcelBat  extends BaseExtEntity<String> {

    @JsonCreator
    public ImportExcelBat() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //批次ID
    @TableField(value = "BAT_ID_")
    private Integer batId;
    //列名
    @TableField(value = "DS_ALIAS_")
    private String dsAlias;
    //表名
    @TableField(value = "TABLE_")
    private String table;

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



