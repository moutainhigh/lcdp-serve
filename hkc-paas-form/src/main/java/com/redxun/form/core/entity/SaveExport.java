
/**
 * <pre>
 *
 * 描述：form_save_export实体类定义
 * 表:form_save_export
 * 作者：huangzihao
 * 邮箱: csx@redxun.cn
 * 日期:2020-10-12 17:03:57
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

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
@TableName(value = "form_save_export")
public class SaveExport  extends BaseExtEntity<java.lang.String> {

    @JsonCreator
    public SaveExport() {
    }

    //主键
    @TableId(value = "ID_",type = IdType.INPUT)
	private String id;

    //数据列表KEY
    @TableField(value = "DATA_LIST_", jdbcType = JdbcType.VARCHAR)
    private String dataList;
    //设置
    @TableField(value = "SETTING_", jdbcType = JdbcType.VARCHAR)
    private String setting;

    //配置名称
    @TableField(value = "NAME_", jdbcType = JdbcType.VARCHAR)
    private String name;
    //是否为公共
    @TableField(value = "IS_PUBLIC_", jdbcType = JdbcType.VARCHAR)
    private Integer isPublic;


    @FieldDef(comment = "应用ID")
    @TableField(value = "APP_ID_")
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



