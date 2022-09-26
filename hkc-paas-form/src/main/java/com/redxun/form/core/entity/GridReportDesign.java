
/**
 * <pre>
 *
 * 描述：demo实体类定义
 * 表:demo
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-05-20 14:18:28
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
import org.apache.ibatis.type.JdbcType;

@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "grid_report_design")
public class GridReportDesign extends BaseExtEntity<String> {

    @JsonCreator
    public GridReportDesign() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
    private String id;

    //数据源
    @TableField(value = "DB_AS_")
    private String dbAs;

    //类别
    @TableField(value = "TREE_ID_",jdbcType=JdbcType.VARCHAR)
    private String treeId;

    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;

    //标识
    @TableField(value = "KEY_",jdbcType=JdbcType.VARCHAR)
    private String key;

    //模板文件
    @TableField(value = "GRF_",jdbcType=JdbcType.VARCHAR)
    private String grf;

    //文件ID
    @TableField(value = "DOC_ID_",jdbcType=JdbcType.VARCHAR)
    private String docId;

    @TableField(value = "USE_COND_SQL_TYPE_")
    private String useCondSqlType;

    @TableField(value = "USE_COND_SQL_")
    private String useCondSql;

    @TableField(value = "COND_SQLS_")
    private String condSqls;

    //查询sql
    @TableField(value = "SQL_",jdbcType=JdbcType.VARCHAR)
    private String sql;

    //查询条件配置(json)
    @TableField(value = "QUERY_CONFIG_",jdbcType=JdbcType.VARCHAR)
    private String queryConfig;

    @TableField(value = "WEB_REQ_SCRIPT_",jdbcType=JdbcType.VARCHAR)
    private String webReqScript;

    @TableField(value = "WEB_REQ_MAPPING_JSON_",jdbcType=JdbcType.VARCHAR)
    private String webReqMappingJson;

    @TableField(value = "WEB_REQ_KEY_",jdbcType=JdbcType.VARCHAR)
    private String webReqKey;

    @TableField(value = "INTERFACE_KEY_",jdbcType=JdbcType.VARCHAR)
    private String interfaceKey;

    @TableField(value = "INTERFACE_MAPPING_JSON_",jdbcType=JdbcType.VARCHAR)
    private String interfaceMappingJson;

    @TableField(value = "COLS_JSON_")
    private String colsJson;

    @TableField(value = "FIELDS_JSON_")
    private String fieldsJson;


    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }



}



