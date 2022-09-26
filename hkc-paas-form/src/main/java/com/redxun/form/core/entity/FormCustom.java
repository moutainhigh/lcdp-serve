
/**
 * <pre>
 *
 * 描述：表单定制实体类定义
 * 表:FORM_CUSTOM
 * 作者：zyg
 * 邮箱: zyg@redxun.cn
 * 日期:2020-08-29 17:24:04
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
@TableName(value = "FORM_CUSTOM")
public class FormCustom  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormCustom() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;//主键

    //分类ID
    @FieldDef(comment = "分类ID")
    @TableField(value = "CATEGORY_ID_", jdbcType = JdbcType.VARCHAR)
    private String categoryId;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //类型(form,表单,other,其他)
    @TableField(value = "TYPE_", jdbcType = JdbcType.VARCHAR)
    private String type;
    //布局定义
    @TableField(value = "JSON_", jdbcType = JdbcType.VARCHAR)
    private String json;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_", jdbcType = JdbcType.VARCHAR)
    private String appId;

    @FieldDef(comment = "参数配置")
    @TableField(value = "PARAMS_", jdbcType = JdbcType.VARCHAR)
    private String params;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



