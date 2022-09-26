
/**
 * <pre>
 *
 * 描述：租户数据源执行记录实体类定义
 * 表:FORM_EXECUTE_LOG
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-04-02 11:35:17
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
@TableName(value = "FORM_EXECUTE_LOG")
public class FormExecuteLog  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormExecuteLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //表单变更记录ID
    @TableField(value = "CHANGE_LOG_ID_",jdbcType=JdbcType.VARCHAR)
    private String changeLogId;
    //数据源别名
    @TableField(value = "DATASOURCE_",jdbcType=JdbcType.VARCHAR)
    private String datasource;
    //执行的SQL语句
    @TableField(value = "SQL_",jdbcType=JdbcType.VARCHAR)
    private String sql;
    //状态(1:成功 0:失败)
    @TableField(value = "STATUS_",jdbcType=JdbcType.VARCHAR)
    private String status;
    //记录
    @TableField(value = "RECORD_",jdbcType=JdbcType.VARCHAR)
    private String record;
    //创建人名称
    @TableField(value = "CREATE_BY_NAME_",jdbcType=JdbcType.VARCHAR)
    private String createByName;
    //批次
    @TableField(value = "BATCH_",jdbcType=JdbcType.VARCHAR)
    private String batch;

    //数据源别名
    @TableField(exist = false)
    private String boAlias;
    //数据源名称
    @TableField(exist = false)
    private String boName;

    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


    /**
    生成子表属性的Array List
    */

}



