
/**
 * <pre>
 *
 * 描述：表单日历视图实体类定义
 * 表:FORM_CALENDAR_VIEW
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-05-24 11:07:03
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
@TableName(value = "FORM_CALENDAR_VIEW")
public class FormCalendarView  extends FormCalendarViewExt<java.lang.String> {
    @JsonCreator
    public FormCalendarView() {
    }
    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;
    //应用ID
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;
    //分类
    @TableField(value = "CATEGORY_",jdbcType=JdbcType.VARCHAR)
    private String category;
    //数据源别名
    @TableField(value = "DB_ALIAS_",jdbcType=JdbcType.VARCHAR)
    private String dbAlias;
    //数据源名称
    @TableField(value = "DB_NAME_",jdbcType=JdbcType.VARCHAR)
    private String dbName;
    //别名
    @TableField(value = "KEY_",jdbcType=JdbcType.VARCHAR)
    private String key;
    //视图名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //SQL语句
    @TableField(value = "SQL_",jdbcType=JdbcType.VARCHAR)
    private String sql;
    //SQL构建方式
    @TableField(value = "USE_COND_SQL",jdbcType=JdbcType.VARCHAR)
    private String useCondSql;

    //所有字段列
    @TableField(exist = false)
    private List fieldColumns;

    //权限配置
    @TableField(exist = false)
    private String permissionConf;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }

}



