
/**
 * <pre>
 *
 * 描述：代码生成模板实体类定义
 * 表:form_codegen_template
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-08-07 21:29:48
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
@TableName(value = "form_codegen_template")
public class CodeGenTemplate  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public CodeGenTemplate() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //内容
    @TableField(value = "CONTENT_",jdbcType=JdbcType.VARCHAR)
    private String content;
    //ENABLED_
    @TableField(value = "ENABLED_",jdbcType=JdbcType.VARCHAR)
    private String enabled;
    //FILE_NAME_
    @TableField(value = "FILE_NAME_",jdbcType=JdbcType.VARCHAR)
    private String fileName;
    //名称
    @TableField(value = "NAME_",jdbcType=JdbcType.VARCHAR)
    private String name;
    //PATH_
    @TableField(value = "PATH_",jdbcType=JdbcType.VARCHAR)
    private String path;
    //生成单个文件
    @TableField(value = "SINGLE_",jdbcType=JdbcType.VARCHAR)
    private String single;



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



