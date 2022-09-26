
/**
 * <pre>
 *
 * 描述：手机表单实体类定义
 * 表:form_mobile
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-05-18 09:44:12
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
@TableName(value = "form_mobile")
public class FormMobile  extends BaseExtEntity<java.lang.String> {

    public  static final String SCRIPT_ONLOAD="_onload=function";
    public  static final String SCRIPT_FUNCTION="_function";
    public  static final String TYPE_NORMAL="normal";
    public  static final String TYPE_EASY="easy";

    @JsonCreator
    public FormMobile() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //分类ID
    @TableField(value = "CATEGORY_ID_", jdbcType = JdbcType.VARCHAR)
    private String categoryId;
    //ALIAS_
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    //表单HTML
    @TableField(value = "FORM_HTML_", jdbcType = JdbcType.VARCHAR)
    private String formHtml;
    //表单脚本
    @TableField(value = "SCRIPT_", jdbcType = JdbcType.VARCHAR)
    private String script;
    //是否发布
    @TableField(value = "DEPLOYED_", jdbcType = JdbcType.INTEGER)
    private Integer deployed;

    @TableField(value = "METADATA_", jdbcType = JdbcType.VARCHAR)
    private String metadata;

    //业务模型ID
    @TableField(value = "BODEF_ID_", jdbcType = JdbcType.VARCHAR)
    private String bodefId;

    //业务模型ID
    @TableField(value = "BODEF_ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String bodefAlias;

    //分组权限
    @TableField(value = "GROUP_PERMISSIONS_")
    private String groupPermissions;

    //类型
    @TableField(value = "TYPE_")
    private String type=TYPE_NORMAL;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;


    @FieldDef(comment = "按钮定义")
    @TableField(value = "BUTTON_DEF_")
    private String buttonDef;

    @FieldDef(comment = "PC表单别名")
    @TableField(value = "FORM_PC_ALIAS_")
    private String formPcAlias;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



