
/**
 * <pre>
 *
 * 描述：表单模版实体类定义
 * 表:form_template
 * 作者：gjh
 * 邮箱: gjh@redxun.cn
 * 日期:2020-06-24 15:46:03
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
@TableName(value = "form_template")
public class FormTemplate  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormTemplate() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //模版名称
    @FieldDef(comment = "模版名称")
    @TableField(value = "NAME_")
    private String name;
    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "ALIAS_")
    private String alias;
    //模版
    @TableField(value = "TEMPLATE_")
    private String template;
    //模版类型 (pc,mobile)
    @TableField(value = "TYPE_")
    private String type;
    //类别
    @TableField(value = "CATEGORY_")
    private String category;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    //文件名称
    @TableField(value = "FILE_NAME_",jdbcType= JdbcType.VARCHAR)
    private String fileName;
    //文件路径
    @TableField(value = "PATH_",jdbcType=JdbcType.VARCHAR)
    private String path;
    //生成单个文件
    @TableField(value = "SINGLE_",jdbcType=JdbcType.VARCHAR)
    private String single;
    //模式
    @TableField(value = "MAIN_SUB_TYPE_",jdbcType=JdbcType.VARCHAR)
    private String mainSubType;
    //实体生成类型
    @TableField(value = "GEN_MODE_",jdbcType=JdbcType.VARCHAR)
    private String genMode;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



