
/**
 * <pre>
 *
 * 描述：表单校验配置实体类定义
 * 表:form_rule
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-06-23 17:36:58
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
@TableName(value = "form_rule")
public class FormRule  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormRule() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //提示语
    @TableField(value = "PROMPT_", jdbcType = JdbcType.VARCHAR)
    private String prompt;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //正则表达式
    @TableField(value = "REGULAR_", jdbcType = JdbcType.VARCHAR)
    private String regular;

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



