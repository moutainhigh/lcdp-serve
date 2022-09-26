
/**
 * <pre>
 *
 * 描述：SQL执行日志实体类定义
 * 表:form_sql_log
 * 作者：hj
 * 邮箱: gjh@redxun.cn
 * 日期:2020-12-15 11:02:42
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


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_sql_log")
public class FormSqlLog  extends BaseExtEntity<java.lang.String> {

    public static final  String TYPE_FORM_BO_LIST="FORM_BO_LIST";
    public static final  String TYPE_FORM_TABLE_FORMULA="FORM_TABLE_FORMULA";
    public static final  String TYPE_GRID_REPORT_DESIGN="TYPE_GRID_REPORT_DESIGN";

    @JsonCreator
    public FormSqlLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //日志类型
    @TableField(value = "TYPE_")
    private String type;
    //SQL详情
    @TableField(value = "SQL_")
    private String sql;
    //参数说明
    @TableField(value = "PARAMS_")
    private String params;
    //执行备注
    @TableField(value = "REMARK_")
    private String remark;
    //是否成功
    @TableField(value = "IS_SUCCESS_")
    private String isSuccess;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }


}



