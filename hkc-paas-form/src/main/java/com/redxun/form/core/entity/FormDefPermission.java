
/**
 * <pre>
 *
 * 描述：业务模型表单权限表实体类定义
 * 表:FORM_DEF_PERMISSION
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-20 09:33:44
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

/**
 * @author hujun
 */
@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_DEF_PERMISSION")
public class FormDefPermission extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormDefPermission() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    /**
     * 表单ID
     */
    @TableField(value = "FORM_ID_", jdbcType = JdbcType.VARCHAR)
    private String formId;
    /**
     * 业务模型ID
     */
    @TableField(value = "BO_DEF_ID_", jdbcType = JdbcType.VARCHAR)
    private String boDefId;


    @TableField(value = "BO_ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String boAlias;
    /**
     * 优先级
     */
    @TableField(value = "LEVEL_", jdbcType = JdbcType.INTEGER)
    private Integer level = 1;
    /**
     * 使用权限
     */
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
        this.id = pkId;
    }
}



