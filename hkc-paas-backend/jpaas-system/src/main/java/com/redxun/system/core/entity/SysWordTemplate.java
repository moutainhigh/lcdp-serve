
/**
 * <pre>
 *
 * 描述：文档模板编辑实体类定义
 * 表:sys_word_template
 * 作者：hj
 * 邮箱: hj@redxun.cn
 * 日期:2020-11-17 16:43:16
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
@TableName(value = "sys_word_template")
public class SysWordTemplate  extends BaseExtEntity<String> {

    @JsonCreator
    public SysWordTemplate() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //业务对象ID
    @TableField(value = "BO_DEF_ID_")
    private String boDefId;
    //BO定义名称
    @TableField(value = "BO_DEF_NAME_")
    private String boDefName;
    //描述
    @TableField(value = "DESCRIPTION_")
    private String description;
    //数据源别名
    @TableField(value = "DS_ALIAS_")
    private String dsAlias;
    //数据源名称
    @TableField(value = "DS_NAME_")
    private String dsName;
    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_")
    private String name;
    //设定SQL语句，用于自定义数据源操作表单
    @TableField(value = "SETTING_")
    private String setting;
    //模板ID
    @TableField(value = "TEMPLATE_ID_")
    private String templateId;
    //模板名称
    @TableField(value = "TEMPLATE_NAME_")
    private String templateName;
    //数据源(SQL/自定义)
    @TableField(value = "TYPE_")
    private String type;



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



