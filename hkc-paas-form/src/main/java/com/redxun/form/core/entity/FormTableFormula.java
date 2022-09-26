
/**
 * <pre>
 *
 * 描述：表间公式实体类定义
 * 表:FORM_TABLE_FORMULA
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-04-13 14:07:26
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
@TableName(value = "FORM_TABLE_FORMULA")
public class FormTableFormula extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormTableFormula() {
    }

    /**
     * ID_
     */
    @TableId(value = "ID_", type = IdType.INPUT)
    private String id;

    /**
     * 公式名称
     */
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    /**
     * 公式描述
     */
    @TableField(value = "DESCP_", jdbcType = JdbcType.VARCHAR)
    private String descp;
    /**
     * 分类ID
     */
    @TableField(value = "TREE_ID_", jdbcType = JdbcType.VARCHAR)
    private String treeId;
    /**
     * 数据填充配置
     */
    @TableField(value = "FILL_CONF_", jdbcType = JdbcType.VARCHAR)
    private String fillConf;
    /**
     * 数据源
     */
    @TableField(value = "DS_NAME_", jdbcType = JdbcType.VARCHAR)
    private String dsName;
    /**
     * 数据模板ID
     */
    @TableField(value = "BO_DEF_ID_", jdbcType = JdbcType.VARCHAR)
    private String boDefId;
    /**
     * 数据模板名称
     */
    @TableField(value = "BO_DEF_NAME_", jdbcType = JdbcType.VARCHAR)
    private String boDefName;
    /**
     * 表单触发时机
     */
    @TableField(value = "ACTION_", jdbcType = JdbcType.VARCHAR)
    private String action;
    /**
     * SYS_ID_
     */
    @TableField(value = "SYS_ID_", jdbcType = JdbcType.VARCHAR)
    private String sysId;
    /**
     * 是否开启调试模式
     */
    @TableField(value = "IS_TEST_", jdbcType = JdbcType.VARCHAR)
    private String isTest;

    /**
     * 状态 启动：YES 禁用：NO
     */
    @TableField(value = "ENABLED_", jdbcType = JdbcType.VARCHAR)
    private String enabled;

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



