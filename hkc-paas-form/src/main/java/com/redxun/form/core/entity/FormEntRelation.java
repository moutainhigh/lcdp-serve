
/**
 * <pre>
 *
 * 描述：数据关联删除约束实体类定义
 * 表:form_ent_relation
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-04-20 16:48:37
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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_ent_relation")
public class FormEntRelation extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormEntRelation() {
    }

    //主键
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;
    //实体ID
    @TableField(value = "ENT_ID_", jdbcType = JdbcType.VARCHAR)
    private String entId;
    //实体名称
    @TableField(value = "ENT_NAME_", jdbcType = JdbcType.VARCHAR)
    private String entName;
    //关联配置
    @TableField(value = "RELATION_CONFIG_", jdbcType = JdbcType.VARCHAR)
    private String relationConfig;
    //表名
    @TableField(value = "TABLE_NAME_", jdbcType = JdbcType.VARCHAR)
    private String tableName;
    //表名
    @TableField(value = "PROMPT_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String promptField;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id = pkId;
    }


    /**
     生成子表属性的Array List
     */

}



