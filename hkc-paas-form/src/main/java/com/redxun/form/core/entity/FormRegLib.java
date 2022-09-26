
/**
 * <pre>
 *
 * 描述：正则表达式实体类定义
 * 表:form_reg_lib
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-09-23 10:27:55
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
@TableName(value = "form_reg_lib")
public class FormRegLib  extends BaseExtEntity<java.lang.String> {

    /**
     * 脱敏规则
     */
    public static String MASKING="regsMasking";

    @JsonCreator
    public FormRegLib() {
    }

    //主键
    @TableId(value = "REG_ID_",type = IdType.INPUT)
	private String regId;

    //别名
    @FieldDef(comment = "别名")
    @TableField(value = "KEY_", jdbcType = JdbcType.VARCHAR)
    private String key;
    //替换公式
    @TableField(value = "MENT_TEXT_", jdbcType = JdbcType.VARCHAR)
    private String mentText;
    //名称
    @FieldDef(comment = "名称")
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //正则公式
    @TableField(value = "REG_TEXT_", jdbcType = JdbcType.VARCHAR)
    private String regText;
    //类型
    @TableField(value = "TYPE_", jdbcType = JdbcType.VARCHAR)
    private String type;
    //用户ID
    @TableField(value = "USER_ID_", jdbcType = JdbcType.VARCHAR)
    private String userId;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    @Override
    public String getPkId() {
        return regId;
    }

    @Override
    public void setPkId(String pkId) {
        this.regId=pkId;
    }
}



