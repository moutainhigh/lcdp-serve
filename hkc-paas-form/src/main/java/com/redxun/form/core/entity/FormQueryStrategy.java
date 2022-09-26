
/**
 * <pre>
 *
 * 描述：查询策略表实体类定义
 * 表:FORM_QUERY_STRATEGY
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-24 17:52:37
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
@TableName(value = "FORM_QUERY_STRATEGY")
public class FormQueryStrategy extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormQueryStrategy() {
    }

    //主键
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    //策略名称
    @FieldDef(comment ="策略名称" )
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //视图类型
    @TableField(value = "VIEW_TYPE_",jdbcType = JdbcType.VARCHAR)
    private String viewType;
    //是否默认
    @TableField(value = "DEFAULT_VIEW_",jdbcType = JdbcType.VARCHAR)
    private String defaultView;
    //查询条件
    @TableField(value = "QUERY_CONDITION_", jdbcType = JdbcType.VARCHAR)
    private String queryCondition;
    //列表ID
    @FieldDef(comment ="列表ID" )
    @TableField(value = "LIST_ID_", jdbcType = JdbcType.VARCHAR)
    private String listId;

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



