
/**
 * <pre>
 *
 * 描述：表单权限配置实体类定义
 * 表:form_permission
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-01-12 17:03:17
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
@TableName(value = "form_permission")
public class FormPermission  extends BaseExtEntity<java.lang.String> {

    public static final String PERMISSION_REQUIRED="required";
    public static final String PERMISSION_WRITE="w";
    public static final String PERMISSION_READONLY="r";
    public static final String PERMISSION_NONE="none";

    //权限计算使用
    public static final String PERMISSION_MAIN="main";
    public static final String PERMISSION_TABBTN="tabBtn";
    public static final String PERMISSION_TABLIST="tabList";
    public static final String PERMISSION_SUB="sub";

    public static final String PERMISSION_EVERYONE="everyone";
    public static final String PERMISSION_CUSTOM="custom";
    public static final String PERMISSION_DEFAULT="default";

    public static final String PERMISSION_TYPE="type";

    public static final String PERMISSION_ALIAS="alias";
    public static final String PERMISSION_REMOVE="remove";

    public static final String TYPE_FORM="form";
    public static final String TYPE_FORM_SOLUTION="formSol";

    public static final String PERMISSION_EVERYONE_NAME="所有人";
    public static final String PERMISSION_NONE_NAME="无权限";

    public static final String PERMISSION_EDIT="edit";
    public static final String PERMISSION_EDIT_NAME="edit_name";
    public static final String PERMISSION_READ="read";
    public static final String PERMISSION_READ_NAME="read_name";
    public static final String PERMISSION_REQUIRE="require";
    public static final String PERMISSION_REQUIRE_NAME="require_name";





    @JsonCreator
    public FormPermission() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //表单方案ID
    // form,formSol
    @FieldDef(comment = "类型" )
    @TableField(value = "TYPE_", jdbcType = JdbcType.VARCHAR)
    private String type;
    //流程定义ID
    @TableField(value = "CONFIG_ID_", jdbcType = JdbcType.VARCHAR)
    private String configId;

    //权限
    @TableField(value = "PERMISSION_", jdbcType = JdbcType.VARCHAR)
    private String permission;

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



