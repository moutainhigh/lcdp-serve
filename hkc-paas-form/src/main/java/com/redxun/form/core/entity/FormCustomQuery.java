
/**
 * <pre>
 *
 * 描述：自定查询实体类定义
 * 表:form_custom_query
 * 作者：csx
 * 邮箱: csx@redxun.cn
 * 日期:2020-02-04 17:53:40
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
@TableName(value = "form_custom_query")
public class FormCustomQuery  extends BaseExtEntity<java.lang.String> {


    public final static String QUERY_SQL="sql";

    public final static String QUERY_TABLE="table";

    public final static String QUERY_FREEMARK_SQL="freeMarkerSql";

    @JsonCreator
    public FormCustomQuery() {
    }

    /**
     * 主键
     */
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    /**
     * 名称
     */
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    /**
     * 标识名 租户中唯一
     */
    @TableField(value = "KEY_", jdbcType = JdbcType.VARCHAR)
    private String key;
    /**
     * 对象名称(表名或视图名)
     */
    @TableField(value = "TABLE_NAME_", jdbcType = JdbcType.VARCHAR)
    private String tableName;
    /**
     * 支持分页(1,支持,0不支持)
     */
    @TableField(value = "IS_PAGE_", jdbcType = JdbcType.INTEGER)
    private Integer isPage;
    /**
     * 分页大小
     */
    @TableField(value = "PAGE_SIZE_", jdbcType = JdbcType.INTEGER)
    private Integer pageSize;
    /**
     * 条件字段定义
     */
    @TableField(value = "WHERE_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String whereField;
    /**
     * 结果字段定义
     */
    @TableField(value = "RESULT_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String resultField;
    /**
     * 排序字段
     */
    @TableField(value = "ORDER_FIELD_", jdbcType = JdbcType.VARCHAR)
    private String orderField;
    /**
     * 数据源名称
     */
    @TableField(value = "DS_ALIAS_", jdbcType = JdbcType.VARCHAR)
    private String dsAlias;
    /**
     * 是否为表(1,表,0视图)
     */
    @TableField(value = "TABLE_", jdbcType = JdbcType.INTEGER)
    private Integer table;
    /**
     * 自定sql
     */
    @TableField(value = "SQL_DIY_", jdbcType = JdbcType.VARCHAR)
    private String sqlDiy;
    /**
     * SQL
     */
    @TableField(value = "SQL_", jdbcType = JdbcType.VARCHAR)
    private String sql;
    /**
     * SQL构建类型
     */
    @TableField(value = "SQL_BUILD_TYPE_", jdbcType = JdbcType.VARCHAR)
    private String sqlBuildType;
    /**
     * 分类ID
     */
    @TableField(value = "TREE_ID_", jdbcType = JdbcType.VARCHAR)
    private String treeId;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_", jdbcType = JdbcType.VARCHAR)
    private String appId;

    //是否为租户使用
    @TableField(value = "IS_TENANT_",jdbcType = JdbcType.VARCHAR)
    private String isTenant;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }
}



