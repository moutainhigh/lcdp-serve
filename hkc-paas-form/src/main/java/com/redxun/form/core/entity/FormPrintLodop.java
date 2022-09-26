
/**
 * <pre>
 *
 * 描述：表单套打模板实体类定义
 * 表:form_print_lodop
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-21 10:03:33
 * 版权：广州红迅软件
 * </pre>
 */
package com.redxun.form.core.entity;

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
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_print_lodop")
public class FormPrintLodop  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormPrintLodop() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //模板名称
    @FieldDef(comment = "模板名称")
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //背景图
    @TableField(value = "BACK_IMG_", jdbcType = JdbcType.VARCHAR)
    private String backImg;
    //表单ID
    @TableField(value = "FORM_ID_", jdbcType = JdbcType.VARCHAR)
    private String formId;
    //表单名称
    @TableField(value = "FORM_NAME_", jdbcType = JdbcType.VARCHAR)
    private String formName;
    //套打模板
    @TableField(value = "TEMPLATE_", jdbcType = JdbcType.VARCHAR)
    private String template;

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



