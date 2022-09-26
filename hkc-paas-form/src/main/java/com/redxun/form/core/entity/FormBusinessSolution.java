
/**
 * <pre>
 *
 * 描述：表单业务方案实体类定义
 * 表:FORM_BUSINESS_SOLUTION
 * 作者：gjh
 * 邮箱: gaojiahao@redxun.cn
 * 日期:2022-01-07 09:35:26
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
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.type.JdbcType;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "FORM_BUSINESS_SOLUTION")
public class FormBusinessSolution  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public FormBusinessSolution() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //标识
    @TableField(value = "ALIAS_",jdbcType=JdbcType.VARCHAR)
    private String alias;
    //导航栏位置
    @TableField(value = "NAVIGATION_POSITION_",jdbcType=JdbcType.VARCHAR)
    private String navigationPosition;
    //主表单方案
    @TableField(value = "MAIN_FORM_SOLUTION_",jdbcType=JdbcType.VARCHAR)
    private String mainFormSolution;
    //表单方案配置
    @TableField(value = "FORM_SOLUTIONS_",jdbcType=JdbcType.VARCHAR)
    private String formSolutions;
    //分类
    @TableField(value = "CATEGORY_",jdbcType=JdbcType.VARCHAR)
    private String category;

    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_",jdbcType=JdbcType.VARCHAR)
    private String appId;


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



