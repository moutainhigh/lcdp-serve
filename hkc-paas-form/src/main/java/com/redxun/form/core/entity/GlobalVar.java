
/**
 * <pre>
 *
 * 描述：代码生成全局变量实体类定义
 * 表:FORM_CODEGEN_GLOBALVAR
 * 作者：ray
 * 邮箱: ray@redxun.cn
 * 日期:2021-08-06 17:52:43
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
@TableName(value = "FORM_CODEGEN_GLOBALVAR")
public class GlobalVar extends BaseExtEntity<String> {

    @JsonCreator
    public GlobalVar() {
    }

    //ID_
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //名称
    @TableField(value = "CONFIG_",jdbcType=JdbcType.VARCHAR)
    private String config;



    @Override
    public String getPkId() {
        return id;
    }

    @Override
    public void setPkId(String pkId) {
        this.id=pkId;
    }



}



