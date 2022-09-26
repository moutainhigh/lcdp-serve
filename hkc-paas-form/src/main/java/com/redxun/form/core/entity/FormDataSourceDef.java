
/**
 * <pre>
 *
 * 描述：数据源定义管理实体类定义
 * 表:form_datasource_def
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-05 10:49:22
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
@TableName(value = "form_datasource_def")
public class FormDataSourceDef  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormDataSourceDef() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    /**
     * 数据源名称
     */
    @FieldDef(comment = "数据源名称")
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    /**
     * 别名
     */
    @FieldDef(comment = "数据源别名")
    @TableField(value = "ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String alias;
    /**
     * 是否使用
     */
    @TableField(value = "ENABLE_", jdbcType = JdbcType.VARCHAR)
    private String enable;
    /**
     * 是否加密
     */
    @TableField(value = "ENCRYPT_",jdbcType = JdbcType.VARCHAR)
    private String encrypt;
    /**
     * 数据源设定
     */
    @TableField(value = "SETTING_", jdbcType = JdbcType.VARCHAR)
    private String setting;
    /**
     * 数据库类型
     */
    @TableField(value = "DB_TYPE_", jdbcType = JdbcType.VARCHAR)
    private String dbType;
    /**
     * 启动时初始化
     */
    @TableField(value = "INIT_ON_START_", jdbcType = JdbcType.VARCHAR)
    private String initOnStart;

    /**
     * 微服务名称
     */
    @TableField(value = "APP_NAME_", jdbcType = JdbcType.VARCHAR)
    private String appName;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
    private String appId;

    /**
     * 是否为租户使用
     */
    @TableField(value = "IS_TENANT_", jdbcType = JdbcType.VARCHAR)
    private String isTenant;

    /**
     * 变更记录序号
     */
    @TableField(value = "CHANGE_SN_", jdbcType = JdbcType.INTEGER)
    private int changeSn;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



