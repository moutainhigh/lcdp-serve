
/**
 * <pre>
 *
 * 描述：表单变更记录实体类定义
 * 表:FORM_CHANGE_LOG
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-03-28 16:09:03
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
@TableName(value = "FORM_CHANGE_LOG")
public class FormChangeLog  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormChangeLog() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //BO实体别名
    @TableField(value = "BO_ALIAS_",jdbcType=JdbcType.VARCHAR)
    private String boAlias;
    //BO实体名称
    @TableField(value = "BO_NAME_",jdbcType=JdbcType.VARCHAR)
    private String boName;
    //序号
    @TableField(value = "SN_",jdbcType=JdbcType.VARCHAR)
    private int sn;
    //SQL语句
    @TableField(value = "SQL_",jdbcType=JdbcType.VARCHAR)
    private String sql;
    //类型
    @TableField(value = "TYPE_",jdbcType=JdbcType.VARCHAR)
    private String type;



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



