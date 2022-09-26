
/**
 * <pre>
 *
 * 描述：表单业务数据实体类定义
 * 表:FORM_BUS_INST_DATA
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-01-06 10:27:47
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
@TableName(value = "FORM_BUS_INST_DATA")
public class FormBusInstData  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormBusInstData() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //表单业务方案主键
    @TableField(value = "BUS_SOL_ID_",jdbcType=JdbcType.VARCHAR)
    private String busSolId;
    //主表单主键
    @TableField(value = "MAIN_PK_",jdbcType=JdbcType.VARCHAR)
    private String mainPk;
    //表单方案主键
    @TableField(value = "REL_FORMSOL_ID_",jdbcType=JdbcType.VARCHAR)
    private String relFormsolId;
    //关联主键
    @TableField(value = "REL_PK_",jdbcType=JdbcType.VARCHAR)
    private String relPk;
    //状态(1：生效 0：草稿)
    @TableField(value = "STATUS_",jdbcType=JdbcType.VARCHAR)
    private String status;
    //表单方案标识
    @TableField(exist = false)
    private String relFormsolAlias;

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



